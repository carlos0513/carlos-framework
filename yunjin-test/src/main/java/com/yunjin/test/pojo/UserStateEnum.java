package com.yunjin.test.pojo;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.yunjin.core.enums.AppEnum;
import com.yunjin.core.enums.BaseEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 用户性别
 *
 * @author yunjin
 * @date 2021/11/17 23:54
 */
@AppEnum(code = "UserState")
@Getter
@AllArgsConstructor
public enum UserStateEnum implements BaseEnum {

    /**
     * 用户状态
     */
    // DISABLE(0, "禁用"),
    ENABLE(1, "启用"),
    LOCK(2, "锁定"),
    DELETE(9, "注销"),
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

}
