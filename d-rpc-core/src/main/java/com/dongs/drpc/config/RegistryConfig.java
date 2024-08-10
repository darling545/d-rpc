package com.dongs.drpc.config;


import lombok.Data;

/**
 * RPC框架配置类
 *
 * @author dongs
 */
@Data
public class RegistryConfig {

    /**
     * 注册中心地址
     */
    private String address = "http://localhost:2380";

    /**
     * 注册中心类别
     */
    private String registryType = "etcd";

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 超时时间
     */
    private Long timeout = 10000L;
}
