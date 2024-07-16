package com.dongs.drpc.registry;


import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 本地注册中心
 *
 * @author dongs
 */
public class LocalRegistry {


    /**
     * 注册信息存储
     */
    private static final Map<String,Class<?>> map = new ConcurrentHashMap<String, Class<?>>();


    /**
     * 注册服务
     * @param serviceName 服务名称
     * @param implClass 实现类
     */
    public static void registry(String serviceName,Class<?> implClass){
        map.put(serviceName,implClass);
    }


    /**
     * 获取服务
     * @param serviceName 服务名称
     * @return 实现类
     */
    public static Class<?> get(String serviceName){
        return map.get(serviceName);
    }

    /**
     * 删除服务
     * @param serviceName 服务名称
     */
    public static void remove(String serviceName){
        map.remove(serviceName);
    }

}
