package com.dongs.drpc.serializer;


import com.dongs.drpc.spi.SpiLoader;

import java.util.HashMap;
import java.util.Map;

/**
 * 序列化器工厂
 *
 * @author dongs
 */
public class SerializerFactory {

    /**
     * 序列化映射（实现单例模式）
     */
    static {
        SpiLoader.load(Serializer.class);
    }

    /**
     * 默认序列化器
     */
    private static final Serializer DEFAULT_SERIALIZER = new JdkSerializer();

    /**
     * 获取实例
     * @param key
     * @return
     */
    public static Serializer getInstance(String key){
        return SpiLoader.getInstance(Serializer.class,key);
    }


}
