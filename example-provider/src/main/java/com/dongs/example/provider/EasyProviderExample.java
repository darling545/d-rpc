package com.dongs.example.provider;

import com.dongs.drpc.RpcApplication;
import com.dongs.drpc.config.RegistryConfig;
import com.dongs.drpc.config.RpcConfig;
import com.dongs.drpc.model.ServiceMetaInfo;
import com.dongs.drpc.registry.LocalRegistry;
import com.dongs.drpc.registry.Registry;
import com.dongs.drpc.registry.RegistryFactory;
import com.dongs.drpc.server.HttpServer;
import com.dongs.drpc.server.VertxHttpServer;
import com.dongs.drpc.server.tcp.VertxTcpServer;
import com.dongs.example.common.service.UserService;

public class EasyProviderExample {

    public static void main(String[] args) {

        RpcApplication.init();

        // 注册服务
        String serviceName = UserService.class.getName();
        LocalRegistry.registry(serviceName, UserServiceImpl.class);

        // 注册服务到注册中心
        RpcConfig rpcConfig = RpcApplication.getRpcConfig();
        RegistryConfig registryConfig = rpcConfig.getRegistryConfig();
        Registry registry = RegistryFactory.getInstance(registryConfig.getRegistryType());
        ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
        serviceMetaInfo.setServiceHost(rpcConfig.getServerHost());
        serviceMetaInfo.setServiceName(serviceName);
        serviceMetaInfo.setServicePort(rpcConfig.getServerPort());
        try {
            registry.registry(serviceMetaInfo);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 启动TCP服务
        VertxTcpServer vertxTcpServer = new VertxTcpServer();

        vertxTcpServer.doStart(8080);





    }
}
