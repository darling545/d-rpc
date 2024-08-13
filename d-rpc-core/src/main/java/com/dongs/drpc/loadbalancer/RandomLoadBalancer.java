package com.dongs.drpc.loadbalancer;


import com.dongs.drpc.model.ServiceMetaInfo;

import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * 随机轮询负载均衡器
 *
 * @author dongs
 */
public class RandomLoadBalancer implements LoadBalancer{


    private final Random random = new Random();

    /**
     * @param requestParams       请求参数
     * @param serviceMetaInfoList 服务节点列表
     * @return
     */
    @Override
    public ServiceMetaInfo selectService(Map<String, Object> requestParams, List<ServiceMetaInfo> serviceMetaInfoList) {
        int size = serviceMetaInfoList.size();
        if (size == 0){
            return null;
        }
        // 如果只有一个，无需轮询
        if (size == 1){
            return serviceMetaInfoList.get(0);
        }
        // 随机取一个服务
        int index = random.nextInt(size);
        return serviceMetaInfoList.get(index);
    }
}
