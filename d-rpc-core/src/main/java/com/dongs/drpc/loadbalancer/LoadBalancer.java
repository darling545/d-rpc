package com.dongs.drpc.loadbalancer;

import com.dongs.drpc.model.ServiceMetaInfo;

import java.util.List;
import java.util.Map;

/**
 * 负载均衡器接口（消费端）
 *
 * @author dongs
 */
public interface LoadBalancer {

    /**
     * 选择服务节点
     * @param requestParams 请求参数
     * @param serviceMetaInfoList 服务节点列表
     * @return 服务节点
     */
    ServiceMetaInfo selectService(Map<String,Object> requestParams, List<ServiceMetaInfo> serviceMetaInfoList);
}
