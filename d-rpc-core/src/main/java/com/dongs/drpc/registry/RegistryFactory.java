package com.dongs.drpc.registry;


import com.dongs.drpc.spi.SpiLoader;

/**
 * 注册中心工厂类
 *
 * @author dongs
 */
public class RegistryFactory {

    /**
     * 初始化注册中心
     */
    static {
        SpiLoader.load(Registry.class);
    }


    /**
     * 默认的注册中心
     */
    private static final Registry DEFAULT_REGISTRY = new EtcdRegistry();

    /**
     * 获取实例
     * @param key
     * @return
     */
    public static Registry getInstance(String key) {
        return SpiLoader.getInstance(Registry.class,key);
    }
}
