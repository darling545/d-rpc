package com.dongs.drpc.protocol;

/**
 * 协议常量
 *
 * @author dongs
 */
public interface ProtocolConstant {


    /**
     * 消息头长度
     */
    int MESSAGE_HEADER_LENGTH = 17;

    /**
     * 魔数
     */
    byte PROTOCOL_MAGIC = 0x1;

    /**
     * 协议版本号
     */
    byte PROTOCOL_VERSION = 0x1;

}
