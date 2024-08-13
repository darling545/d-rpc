package com.dongs.drpc.bootstrap;


import com.dongs.drpc.RpcApplication;
import com.dongs.drpc.config.RegistryConfig;
import com.dongs.drpc.config.RpcConfig;
import com.dongs.drpc.model.ServiceMetaInfo;
import com.dongs.drpc.model.ServiceRegisterInfo;
import com.dongs.drpc.registry.LocalRegistry;
import com.dongs.drpc.registry.Registry;
import com.dongs.drpc.registry.RegistryFactory;
import com.dongs.drpc.server.tcp.VertxTcpServer;

import java.util.List;

/**
 * 服务提供者启动类（初始化）
 *
 * @author dongs
 */
public class ProviderBootstrap {

    public static void init(List<ServiceRegisterInfo<?>> serviceRegisterInfoList) {
        RpcApplication.init();

        // 注册服务
        for (ServiceRegisterInfo<?> serviceRegisterInfo : serviceRegisterInfoList) {
            String serviceName = serviceRegisterInfo.getServiceName();
            LocalRegistry.registry(serviceName, serviceRegisterInfo.getImplClass());

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
                throw new RuntimeException(serviceName + "注册失败",e);
            }
        }

        // 启动TCP服务
        VertxTcpServer vertxTcpServer = new VertxTcpServer();

        vertxTcpServer.doStart(8080);
    }
}
