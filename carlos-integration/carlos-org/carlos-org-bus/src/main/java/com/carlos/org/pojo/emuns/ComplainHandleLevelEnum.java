
package com.carlos.org.pojo.emuns;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.carlos.core.enums.AppEnum;
import com.carlos.core.enums.BaseEnum;
import com.carlos.core.exception.ServiceException;
import lombok.AllArgsConstructor;
import lombok.Getter;


@AppEnum(code = "ComplainHandleLevelEnum")
@Getter
@AllArgsConstructor
public enum ComplainHandleLevelEnum implements BaseEnum {


    REGION(1, "区级"),

    CITY(2, "市级");

    @EnumValue
    private final Integer code;

    private final String desc;

    public static String getDescByCode(final Integer code) {

        for (final ComplainHandleLevelEnum value : ComplainHandleLevelEnum.values()) {
            if (value.getCode().equals(code)) {
                return value.getDesc();
            }
        }
        return "";
    }

    public static ComplainHandleLevelEnum codeOf(final Integer code) {
        final ComplainHandleLevelEnum[] values = ComplainHandleLevelEnum.values();
        for (final ComplainHandleLevelEnum value : values) {
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
