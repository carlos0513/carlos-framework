package com.carlos.org.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.carlos.core.enums.AppEnum;
import com.carlos.core.enums.BaseEnum;
import com.carlos.core.exception.ServiceException;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 资源类型
 *
 * @author carlos
 * @date 2021/11/17 23:54
 */
@AppEnum(code = "ResourceTypeEnum")
@Getter
@AllArgsConstructor
public enum ResourceTypeEnum implements BaseEnum {

    // 资源类型(可扩展):0按钮, 1指标
    BUTTON(0, "按钮"),

    METRICS(1, "指标");

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

    public static String getDescByCode(final Integer code) {

        for (final ResourceTypeEnum value : ResourceTypeEnum.values()) {
            if (value.getCode().equals(code)) {
                return value.getDesc();
            }
        }
        return "";
    }

    public static ResourceTypeEnum codeOf(final Integer code) {
        final ResourceTypeEnum[] values = ResourceTypeEnum.values();
        for (final ResourceTypeEnum value : values) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        throw new ServiceException("资源类型不合法");
    }
}
