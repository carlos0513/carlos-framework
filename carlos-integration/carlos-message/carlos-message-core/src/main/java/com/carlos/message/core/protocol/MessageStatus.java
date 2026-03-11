package com.carlos.message.core.protocol;

import com.carlos.core.enums.AppEnum;
import com.carlos.core.enums.BaseEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * <p>
 * 消息状态枚举
 * </p>
 *
 * @author Carlos
 * @date 2026/3/11
 */
@AppEnum(code = "MessageStatus")
@Getter
@AllArgsConstructor
public enum MessageStatus implements BaseEnum {

    /**
     * 待发送
     */
    PENDING(0, "待发送"),

    /**
     * 发送中
     */
    SENDING(1, "发送中"),

    /**
     * 已发送
     */
    SENT(2, "已发送"),

    /**
     * 送达
     */
    DELIVERED(3, "送达"),

    /**
     * 已读
     */
    READ(4, "已读"),

    /**
     * 失败
     */
    FAILED(5, "失败"),

    /**
     * 撤回
     */
    REVOKED(6, "撤回");

    private final Integer code;
    private final String desc;

    @Override
    public Integer getCode() {
        return code;
    }

    @Override
    public String getDesc() {
        return desc;
    }

    /**
     * 根据code获取枚举
     *
     * @param code code
     * @return 枚举
     */
    public static MessageStatus of(Integer code) {
        for (MessageStatus status : values()) {
            if (status.getCode().equals(code)) {
                return status;
            }
        }
        return null;
    }
}
