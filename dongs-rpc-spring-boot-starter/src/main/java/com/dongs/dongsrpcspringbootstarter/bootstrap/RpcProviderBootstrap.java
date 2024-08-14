package com.dongs.dongsrpcspringbootstarter.bootstrap;

import com.dongs.dongsrpcspringbootstarter.annotation.RpcService;
import com.dongs.drpc.RpcApplication;
import com.dongs.drpc.config.RegistryConfig;
import com.dongs.drpc.config.RpcConfig;
import com.dongs.drpc.model.ServiceMetaInfo;
import com.dongs.drpc.registry.LocalRegistry;
import com.dongs.drpc.registry.Registry;
import com.dongs.drpc.registry.RegistryFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

/**
 * rpc服务提供者启动
 *
 * @author dongs
 */

@Slf4j
public class RpcProviderBootstrap implements BeanPostProcessor {


    /**
     * Bean初始化后执行，注册服务
     * @param bean
     * @param beanName
     * @return
     * @throws BeansException
     */
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Class<?> beanClass = bean.getClass();
        RpcService rpcService = beanClass.getAnnotation(RpcService.class);
        if (rpcService!= null){
            // 需要注册服务
            // 1、获取服务基本信息
            Class<?> interfaceClass = rpcService.interfaceClass();
            // 默认值处理
            if (interfaceClass == void.class){
                interfaceClass = beanClass.getInterfaces()[0];
            }
            String serviceName = interfaceClass.getName();
            String serviceVersion = rpcService.serviceVersion();

            // 2、注册服务
            // 本地注册
            LocalRegistry.registry(serviceName,beanClass);

            // 全局配置
            final RpcConfig rpcConfig = RpcApplication.getRpcConfig();
            // 注册服务到注册中心
            RegistryConfig registryConfig = rpcConfig.getRegistryConfig();
            Registry registry = RegistryFactory.getInstance(registryConfig.getRegistryType());
            ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
            serviceMetaInfo.setServiceHost(rpcConfig.getServerHost());
            serviceMetaInfo.setServiceName(serviceName);
            serviceMetaInfo.setServicePort(rpcConfig.getServerPort());
            serviceMetaInfo.setServiceVersion(serviceVersion);
            try {
                registry.registry(serviceMetaInfo);
            } catch (Exception e) {
                log.error("服务注册失败",e);
                throw new RuntimeException(serviceName + "注册失败",e);
            }
        }
        return BeanPostProcessor.super.postProcessAfterInitialization(bean, beanName);
    }
}
