
package com.yunjin.org.pojo.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.yunjin.core.enums.AppEnum;
import com.yunjin.core.enums.BaseEnum;
import com.yunjin.core.exception.ServiceException;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 用户消息类型数据类型
 *
 * @author yunjin
 * @date 2021/11/17 23:54
 */
@AppEnum(code = "UserMessageStatusEnum")
@Getter
@AllArgsConstructor
public enum UserMessageStatus implements BaseEnum {
    READ(1, "已读"),
    UNREAD(0, "未读"),
    ;

    @EnumValue
    private final Integer code;

    private final String desc;

    @Override
    public Integer getCode() {
        return this.code;
    }

    @Override
    public String getDesc() {
        return this.desc;
    }


    public static UserMessageStatus codeOf(final Integer code) {
        final UserMessageStatus[] values = UserMessageStatus.values();
        for (final UserMessageStatus value : values) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        throw new ServiceException("数据类型不合法");
    }
}
