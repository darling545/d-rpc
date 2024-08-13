package com.dongs.drpc.loadbalancer;

import com.dongs.drpc.spi.SpiLoader;

/**
 * 负载均衡器工厂
 *
 * @author dongs
 */
public class LoadBalancerFactory {

    static {
        SpiLoader.load(LoadBalancer.class);
    }

    private static final LoadBalancer DEFAULT_LOAD_BALANCER = new RoundRobinLoadBalancer();


    public static LoadBalancer getLoadBalancer(String key) {
        return SpiLoader.getInstance(LoadBalancer.class, key);
    }
}
