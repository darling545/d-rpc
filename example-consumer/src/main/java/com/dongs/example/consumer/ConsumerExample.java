package com.dongs.example.consumer;

import com.dongs.drpc.config.RpcConfig;
import com.dongs.drpc.utils.ConfigUtils;

public class ConsumerExample {

    public static void main(String[] args) {
        RpcConfig rpcConfig = ConfigUtils.loadConfig(RpcConfig.class,"rpc");
        System.out.println(rpcConfig);
    }
}
