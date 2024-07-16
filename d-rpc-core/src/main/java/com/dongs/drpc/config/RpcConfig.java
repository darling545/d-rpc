package com.dongs.drpc.config;


import lombok.Data;

/**
 * RPC配置类
 */
@Data
public class RpcConfig {

    /**
     * rpc名称
     */
    private String name = "d-rpc";


    /**
     * 主机地址
     */
    private String serverHost = "localhost";

    /**
     * 版本
     */
    private String version = "1.0";

    /**
     * 端口号
     */
    private Integer serverPort = 8080;

    /**
     * 模拟调用
     */
    private boolean mock = false;
}
