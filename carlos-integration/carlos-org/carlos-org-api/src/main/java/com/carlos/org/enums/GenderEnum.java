package com.carlos.org.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.carlos.core.enums.AppEnum;
import com.carlos.core.enums.BaseEnum;
import com.carlos.core.exception.ServiceException;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 用户性别
 *
 * @author carlos
 * @date 2021/11/17 23:54
 */
@AppEnum(code = "UserGender")
@Getter
@AllArgsConstructor
public enum GenderEnum implements BaseEnum {

    /**
     * 报表类型
     */
    UNKNOWN(0, "保密"),
    MALE(1, "男"),
    FEMALE(2, "女"),
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

    public static GenderEnum codeOf(final Integer code) {
        final GenderEnum[] values = GenderEnum.values();
        for (final GenderEnum value : values) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        throw new ServiceException("性别不合法");
    }
}
