package com.carlos.message.core.channel;

import com.carlos.core.enums.AppEnum;
import com.carlos.core.enums.BaseEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * <p>
 * 渠道类型枚举
 * </p>
 *
 * @author Carlos
 * @date 2026/3/11
 */
@AppEnum(code = "ChannelType")
@Getter
@AllArgsConstructor
public enum ChannelType implements BaseEnum {

    /**
     * 短信
     */
    SMS(1, "短信"),

    /**
     * 邮件
     */
    EMAIL(2, "邮件"),

    /**
     * 钉钉
     */
    DINGTALK(3, "钉钉"),

    /**
     * 企业微信
     */
    WECHAT_WORK(4, "企业微信"),

    /**
     * 站内信
     */
    WEBSOCKET(5, "站内信");

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
    public static ChannelType of(Integer code) {
        for (ChannelType type : values()) {
            if (type.getCode().equals(code)) {
                return type;
            }
        }
        return null;
    }
}
