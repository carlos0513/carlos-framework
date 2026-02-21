package com.carlos.msg.api.pojo.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.carlos.core.enums.AppEnum;
import com.carlos.core.enums.BaseEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * <p>
 * 渠道类型
 * </p>
 *
 * @author Carlos
 * @date 2025-05-06 23:42
 */
@Getter
@AllArgsConstructor
@AppEnum(code = "ChannelType")
public enum ChannelType implements BaseEnum {
    /**
     * 渠道类型
     */
    SMS(1, "sms", "短信"),
    DING_TALK(2, "dingtalk", "钉钉"),
    WX(3, "wx", "微信"),
    RZT(4, "rzt", "蓉政通"),
    SOCKET(5, "socket", "站内信"),

    ;


    /**
     * code值
     */
    private final Integer code;

    /**
     * key
     */
    @EnumValue
    private final String key;
    /**
     * 描述
     */
    private final String desc;


}
