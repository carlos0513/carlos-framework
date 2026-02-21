package com.yunjin.org.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.yunjin.core.enums.AppEnum;
import com.yunjin.core.enums.BaseEnum;
import com.yunjin.core.exception.ServiceException;
import lombok.Getter;

@AppEnum(code = "LabelTypeEnum")
@Getter
public enum LabelTypeEnum implements BaseEnum {
    SYSTEM(1, "系统标签"),
    CUSTOM(2, "自定义标签"),
    BUSINESS_STANDARD(3, "业务标准标签"),
    ;

    @EnumValue
    private final Integer code;
    private final String desc;

    LabelTypeEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    @Override
    public Integer getCode() {
        return this.code;
    }

    @Override
    public String getDesc() {
        return this.desc;
    }

    public static String getDescByCode(final Integer code) {

        for (final LabelTypeEnum value : LabelTypeEnum.values()) {
            if (value.getCode().equals(code)) {
                return value.getDesc();
            }
        }
        return "";
    }

    public static LabelTypeEnum codeOf(final Integer code) {
        final LabelTypeEnum[] values = LabelTypeEnum.values();
        for (final LabelTypeEnum value : values) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        throw new ServiceException("数据类型不合法");
    }

    public static LabelTypeEnum getEnum(final String desc) {
        final LabelTypeEnum[] values = LabelTypeEnum.values();
        for (final LabelTypeEnum value : values) {
            if (value.getDesc().equals(desc)) {
                return value;
            }
        }
        throw new ServiceException("数据类型不合法");
    }


}
