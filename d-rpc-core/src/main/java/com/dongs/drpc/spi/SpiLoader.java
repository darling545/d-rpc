package com.dongs.drpc.spi;


import cn.hutool.core.io.resource.ResourceUtil;
import com.dongs.drpc.serializer.Serializer;
import lombok.extern.slf4j.Slf4j;

import javax.lang.model.element.NestingKind;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * SPI加载器
 */
@Slf4j
public class SpiLoader {


    /**
     * 存储已经加载的类（接口名=>（Key => 实现类））
     */
    private static Map<String,Map<String,Class<?>>> loaderMap = new ConcurrentHashMap<>();

    /**
     * 对象实例化缓存（避免重复new），类路径 => 实例对象，单例模式
     */
    private static Map<String,Object> instanceCache = new ConcurrentHashMap<>();

    /**
     * 系统SPI目录
     */
    private static final String RPC_SYSTEM_SPI_DIR = "META-INF/rpc/system/";

    /**
     * 用户自定义SPI目录
     */
    private static final String RPC_CUSTOM_SPI_DIR = "META-INF/rpc/custom/";

    /**
     * 扫描路径
     */
    private static final String[] SCAN_DIRS = new String[]{RPC_CUSTOM_SPI_DIR,RPC_SYSTEM_SPI_DIR};


    /**
     * 动态加载类列表
     */
    private static final List<Class<?>> LOAD_CLASS_LIST = Arrays.asList(Serializer.class);

    public static void loadAll(){
        log.info("加载所有的SPI服务");
        for (Class<?> aClass : LOAD_CLASS_LIST) {
            load(aClass);
        }
    }


    /**
     * 获取某一个接口的实例
     * @param aClass
     * @param key
     * @return
     * @param <T>
     */
    public static <T> T getInstance(Class<T> aClass,String key){
        String className = aClass.getName();
        Map<String,Class<?>> spiMap = loaderMap.get(className);
        if (spiMap == null){
            throw new RuntimeException(String.format("SpiLoader 未加载 %s 类型",className));
        }
        if (!spiMap.containsKey(key)){
            throw new RuntimeException(String.format("SpiLoader 的 %s 中不存在 key=%s 的类型",className,key));
        }
        Class<?> implClass = spiMap.get(key);
        String implClassName = implClass.getName();
        if (!instanceCache.containsKey(implClassName)){
            try {
                instanceCache.put(implClassName,implClass.newInstance());
            } catch (InstantiationException | IllegalAccessException e) {
                String errorMessage = String.format("%s 类型实例化失败", implClassName);
                throw new RuntimeException(errorMessage,e);
            }
        }
        return (T) instanceCache.get(implClassName);
    }

    /**
     * 加载某一个类型
     *
     * @param aClass
     * @return
     */
    public static Map<String,Class<?>> load(Class<?> aClass) {
        log.info("加载类型为{}的SPI", aClass.getName());
        // 扫描路径，用户定义的SPI优先于系统的SPI
        Map<String,Class<?>> spiMap = new HashMap<>();
        for (String scanDir : SCAN_DIRS){
            List<URL> resources = ResourceUtil.getResources(scanDir + aClass.getName());
            log.info("路径是 {}",scanDir + aClass.getName());
            // 读取每个资源文件
            for (URL resource : resources){
                try {
                    InputStreamReader inputStreamReader = new InputStreamReader(resource.openStream());
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                    String line;
                    while((line = bufferedReader.readLine()) != null){
                        String[] strArray = line.split("=");
                        if (strArray.length > 1){
                            String key = strArray[0];
                            String value = strArray[1];
                            spiMap.put(key,Class.forName(value));
                        }
                    }
                } catch (Exception e) {
                    log.error("spi resource load error",e);
                }
            }
        }
        loaderMap.put(aClass.getName(),spiMap);
        return spiMap;
    }


}
