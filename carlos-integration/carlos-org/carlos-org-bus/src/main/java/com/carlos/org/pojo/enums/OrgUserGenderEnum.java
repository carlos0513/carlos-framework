package com.carlos.org.pojo.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.carlos.core.enums.AppEnum;
import com.carlos.core.enums.BaseEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * <p>
 * 用户性别枚举
 * </p>
 *
 * @author carlos
 * @date 2026-03-03
 */
@AppEnum(code = "OrgUserGender")
@Getter
@AllArgsConstructor
public enum OrgUserGenderEnum implements BaseEnum {

    /**
     * 保密
     */
    UNKNOWN(0, "保密"),

    /**
     * 男
     */
    MALE(1, "男"),

    /**
     * 女
     */
    FEMALE(2, "女");

    @EnumValue
    private final Integer code;

    private final String desc;

    @Override
    public Integer getCode() {
        return code;
    }

    @Override
    public String getDesc() {
        return desc;
    }

}
