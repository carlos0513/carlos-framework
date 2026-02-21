package com.yunjin.org.pojo.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.yunjin.core.enums.AppEnum;
import com.yunjin.core.enums.BaseEnum;
import com.yunjin.core.exception.ServiceException;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 用户性别
 *
 * @author yunjin
 * @date 2021/11/17 23:54
 */
@AppEnum(code = "UserGender")
@Getter
@AllArgsConstructor
public enum UserGenderEnum implements BaseEnum {

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

    public static UserGenderEnum codeOf(final Integer code) {
        final UserGenderEnum[] values = UserGenderEnum.values();
        for (final UserGenderEnum value : values) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        throw new ServiceException("性别不合法");
    }
}
