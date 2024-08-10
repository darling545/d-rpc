package com.dongs.drpc.serializer;


import com.dongs.drpc.spi.SpiLoader;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

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


//    private static Map<String,Class<?>> serializerMap = new HashMap<>();
//
//    private static Lock lock = new ReentrantLock();
//
//    public static Map<String,Class<?>> getSerializerMap(){
//        if (serializerMap.isEmpty()){
//            lock.lock();
//            try{
//                if (serializerMap.isEmpty()){
//                    serializerMap = SpiLoader.load(Serializer.class);
//                }
//            }finally {
//                lock.unlock();
//            }
//        }
//        return serializerMap;
//    }


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
