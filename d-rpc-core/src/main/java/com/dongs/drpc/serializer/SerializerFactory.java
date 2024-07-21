package com.dongs.drpc.serializer;


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
    private static final Map<String,Serializer> KEY_SERIALIZER_MAP = new HashMap<String, Serializer>(){{
        put(SerializerKeys.JDK,new JdkSerializer());
        put(SerializerKeys.JSON,new JsonSerializer());
        put(SerializerKeys.KRYO,new KryoSerializer());
        put(SerializerKeys.HESSIAN,new HessianSerializer());
    }};

    /**
     * 默认序列化器
     */
    private static final Serializer DEFAULT_SERIALIZER = KEY_SERIALIZER_MAP.get("jdk");

    /**
     * 获取实例
     * @param key
     * @return
     */
    public static Serializer getInstance(String key){
        return KEY_SERIALIZER_MAP.getOrDefault(key,DEFAULT_SERIALIZER);
    }


}
