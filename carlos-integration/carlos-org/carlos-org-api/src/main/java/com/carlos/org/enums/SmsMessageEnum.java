
package com.carlos.org.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.carlos.core.enums.AppEnum;
import com.carlos.core.enums.BaseEnum;
import com.carlos.core.exception.ServiceException;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 用户菜单收藏数据类型
 *
 * @author yunjin
 * @date 2021/11/17 23:54
 */
@AppEnum(code = "SmsMessageEnum")
@Getter
@AllArgsConstructor
public enum SmsMessageEnum implements BaseEnum {

    FEEDBACK_COMMIT(1,"feedback_commit"),
    FEEDBACK_RESULT(2,"feedback_result"),
    TASK_CORRECTION(3,"task_correction"),
    TASK_RESULT(4,"task_result"),
    TASK_OPERATE(5,"task_operate"),
    TASK_TODO(6,"task_todo"),
    APPLY_STATE(7,"apply_state"),
    TASK_REMIND(8,"task_remind"),
    TASK_EXPIRE(9,"task_expire"),
    CREATE_USER(10,"create_user");

    @EnumValue
    private final int code;
    private final String templateCode;
    @Override
    public Integer getCode() {
        return this.code;
    }
    @Override
    public String getDesc() {
        return "";
    }
    public static SmsMessageEnum codeOf(final Integer code) {
        final SmsMessageEnum[] values = SmsMessageEnum.values();
        for (final SmsMessageEnum value : values) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        throw new ServiceException("数据类型不合法");
    }
}
