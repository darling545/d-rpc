package com.dongs.drpc.proxy;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.dongs.drpc.RpcApplication;
import com.dongs.drpc.config.RpcConfig;
import com.dongs.drpc.constant.RpcConstant;
import com.dongs.drpc.model.RpcRequest;
import com.dongs.drpc.model.RpcResponse;
import com.dongs.drpc.model.ServiceMetaInfo;
import com.dongs.drpc.registry.Registry;
import com.dongs.drpc.registry.RegistryFactory;
import com.dongs.drpc.serializer.JdkSerializer;
import com.dongs.drpc.serializer.Serializer;
import com.dongs.drpc.serializer.SerializerFactory;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.List;

/**
 * 动态代理
 *
 * @author dongs
 */
@Slf4j
public class ServiceProxy implements InvocationHandler {
    /**
     * @param proxy  the proxy instance that the method was invoked on
     * @param method the {@code Method} instance corresponding to
     *               the interface method invoked on the proxy instance.  The declaring
     *               class of the {@code Method} object will be the interface that
     *               the method was declared in, which may be a superinterface of the
     *               proxy interface that the proxy class inherits the method through.
     * @param args   an array of objects containing the values of the
     *               arguments passed in the method invocation on the proxy instance,
     *               or {@code null} if interface method takes no arguments.
     *               Arguments of primitive types are wrapped in instances of the
     *               appropriate primitive wrapper class, such as
     *               {@code java.lang.Integer} or {@code java.lang.Boolean}.
     * @return
     * @throws Throwable
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

//        SerializerFactory.getSerializerMap();

        final Serializer serializer = SerializerFactory.getInstance(RpcApplication.getRpcConfig().getSerializer());

        String serviceName = method.getDeclaringClass().getName();

        RpcRequest rpcRequest = RpcRequest.builder()
                .serviceName(method.getDeclaringClass().getName())
                .methodName(method.getName())
                .args(args)
                .parameterTypes(method.getParameterTypes())
                .build();


        try{
            byte[] bytes = serializer.serialize(rpcRequest);
            // 从注册中心获取服务提供者的请求地址
            RpcConfig rpcConfig = RpcApplication.getRpcConfig();
            Registry registry = RegistryFactory.getInstance(rpcConfig.getRegistryConfig().getRegistryType());
            ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
            serviceMetaInfo.setServiceName(serviceName);
            serviceMetaInfo.setServiceVersion(RpcConstant.DEFAULT_SERVICE_VERSION);
            List<ServiceMetaInfo> serviceMetaInfoList = registry.serviceDiscovery(serviceMetaInfo.getServiceKey());
            if (CollUtil.isEmpty(serviceMetaInfoList)){
                throw new RuntimeException("服务不存在");
            }
            ServiceMetaInfo selectServiceMetaInfo = serviceMetaInfoList.get(0);
            // 需要后期进行使用注册中心和服务发现机制解决

            try(HttpResponse httpResponse = HttpRequest.post(selectServiceMetaInfo.getServiceAddress())
                    .body(bytes)
                    .execute()){
                byte[] result = httpResponse.bodyBytes();
                RpcResponse rpcResponse = serializer.deserialize(result,RpcResponse.class);
                return rpcResponse.getData();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
