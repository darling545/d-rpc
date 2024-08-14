package com.dongs.drpc.server.tcp;

import cn.hutool.core.util.IdUtil;
import com.dongs.drpc.RpcApplication;
import com.dongs.drpc.model.RpcRequest;
import com.dongs.drpc.model.RpcResponse;
import com.dongs.drpc.model.ServiceMetaInfo;
import com.dongs.drpc.protocol.*;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetClient;
import io.vertx.core.net.NetSocket;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

/**
 * Vertx TCP请求客户端
 */
@Slf4j
public class VertxTcpClient {


    public static RpcResponse doRequest(RpcRequest rpcRequest, ServiceMetaInfo serviceMetaInfo) throws Exception {
        Vertx vertx = Vertx.vertx();
        NetClient netClient = vertx.createNetClient();
        // 转为同步获取结果 TODO 需要优化
        CompletableFuture<RpcResponse> responseFuture = new CompletableFuture<>();
        netClient.connect(serviceMetaInfo.getServicePort(),serviceMetaInfo.getServiceHost(),
                result -> {
                    if (result.succeeded()){
                        System.out.println("连接成功");
                        NetSocket socket = result.result();
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
//                        socket.handler(buffer -> {
//                            log.info("解码的信息" + buffer.toString());
//                            ProtocolMessage<RpcResponse> responseProtocolMessage = null;
//                            try {
//                                responseProtocolMessage = (ProtocolMessage<RpcResponse>) ProtocolMessageDecoder.decode(buffer);
//                                responseFuture.complete(responseProtocolMessage.getBody());
//                            } catch (IOException ex) {
//                                throw new RuntimeException("协议消息解码失败");
//                            }
//                        });
                        TcpBufferHandlerWrapper bufferHandlerWrapper = new TcpBufferHandlerWrapper(
                                buffer -> {
                                    try{
                                        ProtocolMessage<RpcResponse> responseProtocolMessage =
                                                (ProtocolMessage<RpcResponse>) ProtocolMessageDecoder.decode(buffer);
                                        responseFuture.complete(responseProtocolMessage.getBody());
                                    }catch (IOException e){
                                        throw new RuntimeException("协议消息解码失败");
                                    }
                                }
                        );
                        socket.handler(bufferHandlerWrapper);
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
        return rpcResponse;
    }

}
