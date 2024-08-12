package com.dongs.drpc.proxy;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.dongs.drpc.RpcApplication;
import com.dongs.drpc.config.RpcConfig;
import com.dongs.drpc.constant.RpcConstant;
import com.dongs.drpc.model.RpcRequest;
import com.dongs.drpc.model.RpcResponse;
import com.dongs.drpc.model.ServiceMetaInfo;
import com.dongs.drpc.protocol.*;
import com.dongs.drpc.registry.Registry;
import com.dongs.drpc.registry.RegistryFactory;
import com.dongs.drpc.serializer.JdkSerializer;
import com.dongs.drpc.serializer.Serializer;
import com.dongs.drpc.serializer.SerializerFactory;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetClient;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.List;
import java.util.concurrent.CompletableFuture;

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
            // 发送TCP请求
            Vertx vertx = Vertx.vertx();
            NetClient netClient = vertx.createNetClient();
            // 转为同步获取结果 TODO 需要优化
            CompletableFuture<RpcResponse> responseFuture = new CompletableFuture<>();
            netClient.connect(selectServiceMetaInfo.getServicePort(),selectServiceMetaInfo.getServiceHost(),
                    result -> {
                        if (result.succeeded()){
                            System.out.println("连接成功");
                            io.vertx.core.net.NetSocket socket = result.result();
                            // 发送消息
                            // 构造消息
                            ProtocolMessage<RpcRequest> protocolMessage = new ProtocolMessage<>();
                            ProtocolMessage.Header header = new ProtocolMessage.Header();
                            header.setMagic(ProtocolConstant.PROTOCOL_MAGIC);
                            header.setVersion(ProtocolConstant.PROTOCOL_VERSION);
                            log.info("----------------------------------------" + RpcApplication.getRpcConfig().getSerializer());
                            header.setSerializer((byte) ProtocolMessageSerializerEnum.getEnumByValue(RpcApplication.getRpcConfig().getSerializer()).getKey());
                            header.setType((byte) ProtocolMessageTypeEnum.REQUEST.getKey());
                            header.setRequestId(IdUtil.getSnowflakeNextId());
                            protocolMessage.setHeader(header);
                            protocolMessage.setBody(rpcRequest);
                            // 编码
                            Buffer encode = null;
                            try {
                                encode = ProtocolMessageEncoder.encode(protocolMessage);
                                socket.write(encode);
                            } catch (IOException e) {
                                throw new RuntimeException("协议消息编码失败");
                            }
                            // 读取响应
                            socket.handler(buffer -> {
                                log.info("解码的信息" + buffer.toString());
                                ProtocolMessage<RpcResponse> responseProtocolMessage = null;
                                try {
                                    responseProtocolMessage = (ProtocolMessage<RpcResponse>) ProtocolMessageDecoder.decode(buffer);
                                    responseFuture.complete(responseProtocolMessage.getBody());
                                } catch (IOException ex) {
                                    throw new RuntimeException("协议消息解码失败");
                                }
                            });
                        }else {
                            log.info(String.valueOf(result.cause()));
                            System.out.println("连接失败");
                        }
                    });
//            try(HttpResponse httpResponse = HttpRequest.post(selectServiceMetaInfo.getServiceAddress())
//                    .body(bytes)
//                    .execute()){
//                byte[] result = httpResponse.bodyBytes();
//                RpcResponse rpcResponse = serializer.deserialize(result,RpcResponse.class);
//                return rpcResponse.getData();
//            }
            RpcResponse rpcResponse = responseFuture.get();
            // 关闭连接
            netClient.close();
            return rpcResponse.getData();
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
