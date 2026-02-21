
package com.yunjin.org.pojo.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.yunjin.core.enums.AppEnum;
import com.yunjin.core.enums.BaseEnum;
import com.yunjin.core.exception.ServiceException;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AppEnum(code = "ComplainSourceEnum")
@Getter
@AllArgsConstructor
public enum ComplainSourceEnum implements BaseEnum {


    OUT_TASK(11, "报表通外任务"),

    BBT_TASK(12, "报表通任务"),

    ;

    @EnumValue
    private final Integer code;

    private final String desc;

    public static String getDescByCode(final Integer code) {

        for (final ComplainSourceEnum value : ComplainSourceEnum.values()) {
            if (value.getCode().equals(code)) {
                return value.getDesc();
            }
        }
        return "";
    }

    public static ComplainSourceEnum codeOf(final Integer code) {
        final ComplainSourceEnum[] values = ComplainSourceEnum.values();
        for (final ComplainSourceEnum value : values) {
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
