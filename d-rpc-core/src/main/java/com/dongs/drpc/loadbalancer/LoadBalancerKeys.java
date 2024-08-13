package com.dongs.drpc.loadbalancer;


/**
 * 负载均衡器的key
 *
 * @author dongs
 */
public interface LoadBalancerKeys {

    /**
     * 轮询负载均衡器
     */
    String ROUND_ROBIN = "roundRobin";

    /**
     * 随机负载均衡器
     */
    String RANDOM = "random";

    /**
     * 一致性哈希负载均衡器
     */
    String CONSISTENT_HASH = "consistentHash";


}
