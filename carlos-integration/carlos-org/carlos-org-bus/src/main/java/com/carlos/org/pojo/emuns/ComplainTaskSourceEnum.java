
package com.carlos.org.pojo.emuns;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.carlos.core.enums.AppEnum;
import com.carlos.core.enums.BaseEnum;
import com.carlos.core.exception.ServiceException;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AppEnum(code = "ComplainTaskSourceEnum")
@Getter
@AllArgsConstructor
public enum ComplainTaskSourceEnum implements BaseEnum {


    OTHER_SYS(21, "其他系统任务"),

    OFFLINE_TASK(22, "线下任务"),

    ;

    @EnumValue
    private final Integer code;

    private final String desc;

    public static String getDescByCode(final Integer code) {

        for (final ComplainTaskSourceEnum value : ComplainTaskSourceEnum.values()) {
            if (value.getCode().equals(code)) {
                return value.getDesc();
            }
        }
        return "";
    }

    public static ComplainTaskSourceEnum codeOf(final Integer code) {
        final ComplainTaskSourceEnum[] values = ComplainTaskSourceEnum.values();
        for (final ComplainTaskSourceEnum value : values) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        throw new ServiceException("数据类型不合法");
    }

    @Override
    public Integer getCode() {
        return this.code;
    }

    @Override
    public String getDesc() {
        return this.desc;
    }
}
