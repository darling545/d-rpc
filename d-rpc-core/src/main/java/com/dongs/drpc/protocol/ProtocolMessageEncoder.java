package com.dongs.drpc.protocol;


import com.dongs.drpc.serializer.Serializer;
import com.dongs.drpc.serializer.SerializerFactory;
import io.vertx.core.buffer.Buffer;
import java.io.IOException;


/**
 * 协议消息编码器
 *
 * @author dongs
 */
public class ProtocolMessageEncoder {

    /**
     * 编码
     * @param protocolMessage
     * @return
     * @throws IOException
     */
    public static Buffer encode(ProtocolMessage<?> protocolMessage) throws IOException {
        if (protocolMessage == null || protocolMessage.getHeader() == null){
            return Buffer.buffer();
        }

        ProtocolMessage.Header header = protocolMessage.getHeader();
        // 依次向缓冲区写入字节
        Buffer buffer = Buffer.buffer();
        buffer.appendByte(header.getMagic());
        buffer.appendByte(header.getVersion());
        buffer.appendByte(header.getSerializer());
        buffer.appendByte(header.getType());
        buffer.appendByte(header.getStatus());
        buffer.appendLong(header.getRequestId());
        // 获取序列化器
        ProtocolMessageSerializerEnum serializerEnum = ProtocolMessageSerializerEnum.getEnumByKey(header.getSerializer());
        if (serializerEnum == null){
            throw new RuntimeException("序列化器不存在");
        }
        Serializer serializer = SerializerFactory.getInstance(serializerEnum.getValue());
        byte[] bytesBody = serializer.serialize(protocolMessage.getBody());
        // 写入消息体长度和数据
        buffer.appendInt(bytesBody.length);
        buffer.appendBytes(bytesBody);
        return buffer;
    }

}
