package com.dongs.drpc.protocol;

import com.dongs.drpc.model.RpcRequest;
import com.dongs.drpc.model.RpcResponse;
import com.dongs.drpc.serializer.Serializer;
import com.dongs.drpc.serializer.SerializerFactory;
import io.vertx.core.buffer.Buffer;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * 协议消息解码器
 *
 * @author dongs
 */
@Slf4j
public class ProtocolMessageDecoder {


    public static ProtocolMessage<?> decode(Buffer buffer) throws IOException {
        log.info("解码的信息" + buffer.toString());
        // 从指定位置读取buffer
        ProtocolMessage.Header header = new ProtocolMessage.Header();
        byte magic = buffer.getByte(0);
        // 校验魔数
        if (magic != ProtocolConstant.PROTOCOL_MAGIC) {
            throw new IOException("Invalid magic number: " + magic);
        }
        header.setMagic(magic);
        header.setVersion(buffer.getByte(1));
        header.setSerializer(buffer.getByte(2));
        header.setType(buffer.getByte(3));
        header.setStatus(buffer.getByte(4));
        header.setRequestId(buffer.getLong(5));
        header.setBodyLength(buffer.getInt(13));
        // 解决粘包问题，只读取指定长度的数据
        byte[] bodyBytes = buffer.getBytes(17,17 + header.getBodyLength());
        // 解析消息体
        ProtocolMessageSerializerEnum serializerEnum = ProtocolMessageSerializerEnum.getEnumByKey(header.getSerializer());
        if (serializerEnum == null){
            throw new IOException("Invalid serializer(noHave): " + header.getSerializer());
        }
        Serializer serializerUse = SerializerFactory.getInstance(serializerEnum.getValue());
        ProtocolMessageTypeEnum typeEnum = ProtocolMessageTypeEnum.getEnumByKey(header.getType());
        if (typeEnum == null){
            throw new IOException("Invalid type(noHave): " + header.getType());
        }
        switch (typeEnum){
            case REQUEST:
                RpcRequest request = serializerUse.deserialize(bodyBytes, RpcRequest.class);
                return new ProtocolMessage<>(header, request);
            case RESPONSE:
                RpcResponse response = serializerUse.deserialize(bodyBytes, RpcResponse.class);
                return new ProtocolMessage<>(header, response);
            case HEART_BEAT:
            case OTHER:
            default:
                throw new IOException("Invalid type: " + header.getType());
        }
    }
}
