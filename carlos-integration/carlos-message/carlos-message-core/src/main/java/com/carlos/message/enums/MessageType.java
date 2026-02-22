package com.carlos.message.enums;

import com.carlos.core.enums.AppEnum;
import com.carlos.core.enums.BaseEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * <p>
 * 消息类型
 * </p>
 *
 * @author Carlos
 * @date 2022/1/11
 */
@Getter
@AllArgsConstructor
@AppEnum(code = "MessageType")
public enum MessageType implements BaseEnum {
    /**
     * 系统消息
     */
    SYS_MESSAGE(1, "系统消息"),

    ;


    /**
     * code值
     */
    private final Integer code;
    /**
     * 描述
     */
    private final String desc;


    @Override
    public Integer getCode() {
        return code;
    }

    @Override
    public String getDesc() {
        return desc;
    }
}
