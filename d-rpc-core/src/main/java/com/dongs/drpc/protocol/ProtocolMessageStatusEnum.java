package com.dongs.drpc.protocol;


import lombok.Getter;

/**
 * 协议消息的状态枚举值
 *
 * @author dongs
 */
@Getter
public enum ProtocolMessageStatusEnum {


    OK("ok",20),
    BAD_REQUEST("bad request",40),
    BAD_RESPONSE("bad response",50);

    private final String text;

    private final int value;

    ProtocolMessageStatusEnum(String text, int value) {
        this.text = text;
        this.value = value;
    }

    /**
     * 根据value值获取枚举
     */
    public static ProtocolMessageStatusEnum getEnumByValue(int value) {
        for (ProtocolMessageStatusEnum status : ProtocolMessageStatusEnum.values()) {
            if (status.value == value) {
                return status;
            }
        }
        return null;
    }
}
