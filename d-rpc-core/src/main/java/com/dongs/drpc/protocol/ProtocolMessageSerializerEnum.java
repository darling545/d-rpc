package com.dongs.drpc.protocol;


import cn.hutool.core.util.ObjectUtil;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 协议消息的序列化器枚举类
 *
 * @author dongs
 */
@Getter
public enum ProtocolMessageSerializerEnum {


    JDK(0, "jdk"),
    HESSIAN(3, "hessian"),
    KRYO(2, "kryo"),
    JSON(1, "json");

    private final int key;

    private final String value;

    ProtocolMessageSerializerEnum(int key, String value) {
        this.key = key;
        this.value = value;
    }

    /**
     * 获取值列表
     */
    public static List<String> getValues() {
        return Arrays.stream(values()).map(item -> item.value).collect(Collectors.toList());
    }

    /**
     * 根据key获取枚举
     */
    public static ProtocolMessageSerializerEnum getEnumByKey(int key) {
        for (ProtocolMessageSerializerEnum value : ProtocolMessageSerializerEnum.values()) {
            if (value.key == key) {
                return value;
            }
        }
        return null;
    }

    /**
     * 根据value获取枚举
     */
    public static ProtocolMessageSerializerEnum getEnumByValue(String value) {
        if (ObjectUtil.isEmpty(value)) {
            return null;
        }
        for (ProtocolMessageSerializerEnum item : ProtocolMessageSerializerEnum.values()) {
            if (item.value.equals(value)) {
                return item;
            }
        }
        return null;
    }

}
