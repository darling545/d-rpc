package com.dongs.drpc.protocol;

import lombok.Getter;

/**
 * 协议消息的类型枚举类
 *
 * @author dongs
 */
@Getter
public enum ProtocolMessageTypeEnum {

    REQUEST(0),
    RESPONSE(1),
    HEART_BEAT(2),
    OTHER(3);

    private final int key;

    ProtocolMessageTypeEnum(int key) {
        this.key = key;
    }

    /**
     * 根据key获取枚举值
     */
    public static ProtocolMessageTypeEnum getEnumByKey(int key) {
        for (ProtocolMessageTypeEnum value : ProtocolMessageTypeEnum.values()) {
            if (value.key == key) {
                return value;
            }
        }
        return null;
    }
}
