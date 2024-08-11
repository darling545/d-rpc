package com.dongs.drpc.registry;

import com.dongs.drpc.config.RegistryConfig;
import com.dongs.drpc.model.ServiceMetaInfo;

import java.util.List;

/**
 * 注册中心
 *
 * @author dongs
 */
public interface Registry {


    /**
     * 初始化注册中心
     * @param registryConfig
     */
    void init(RegistryConfig registryConfig);


    /**
     * 注册服务（服务端）
     *
     * @param serviceMetaInfo 服务元信息
     */
    void registry(ServiceMetaInfo serviceMetaInfo) throws Exception;

    /**
     * 注销服务（服务端）
     * @param serviceMetaInfo
     */
    void unRegistry(ServiceMetaInfo serviceMetaInfo);

    /**
     * 服务发现
     * @param serviceKey
     * @return
     */
    List<ServiceMetaInfo> serviceDiscovery(String serviceKey);


    /**
     * 心跳检测（服务端）
     */
    void heartBeat();

    /**
     * 监听（消费端）
     */
    void watch(String serviceNodeKey);


    /**
     * 服务销毁
     */
    void destroy();
}
