package com.dongs.drpc.loadbalancer;

import com.dongs.drpc.model.ServiceMetaInfo;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * 一致性hash负载均衡器
 * 每次调用会重新组环
 *
 * @author dongs
 */
public class ConsistentHashLoadBalancer implements LoadBalancer{

    /**
     * 一致性hash环，添加虚拟节点
     */
    private final TreeMap<Integer,ServiceMetaInfo> virtualNodeRing = new TreeMap<>();


    /**
     * 虚拟节点的个数
     */
    private static final int VIRTUAL_NODE_NUM = 100;




    /**
     * @param requestParams       请求参数
     * @param serviceMetaInfoList 服务节点列表
     * @return
     */
    @Override
    public ServiceMetaInfo selectService(Map<String, Object> requestParams, List<ServiceMetaInfo> serviceMetaInfoList) {
        if (serviceMetaInfoList.isEmpty()){
            return null;
        }
         // 构建虚拟结点环
        for (ServiceMetaInfo serviceMetaInfo : serviceMetaInfoList){
            for (int i = 0; i < VIRTUAL_NODE_NUM; i++){
                int hash = getHash(serviceMetaInfo.getServiceAddress() + "#" + i);
                virtualNodeRing.put(hash,serviceMetaInfo);
            }
        }

        int hash = getHash(requestParams);
        // 找到大于等于hash的第一个虚拟结点
        Map.Entry<Integer,ServiceMetaInfo> entry = virtualNodeRing.ceilingEntry(hash);
        if (entry == null){
            // 如果没有大于等于的，取第一个
            entry = virtualNodeRing.firstEntry();
        }
        return entry.getValue();
    }


    /**
     * Hash算法
     */
    private int getHash(Object key){
        return key.hashCode();
    }
}
