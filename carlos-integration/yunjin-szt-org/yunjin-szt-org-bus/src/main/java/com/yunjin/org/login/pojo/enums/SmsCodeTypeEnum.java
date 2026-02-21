package com.yunjin.org.login.pojo.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.yunjin.core.enums.AppEnum;
import com.yunjin.core.enums.BaseEnum;
import com.yunjin.core.exception.ServiceException;
import com.yunjin.org.login.*;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * <p>
 * 短信验证码类型
 * </p>
 *
 */
@AppEnum(code = "SmsCodeTypeEnum")
@Getter
@AllArgsConstructor
public enum SmsCodeTypeEnum implements BaseEnum {

    /**
     * 登录
     */
    LOGIN(1, "登录"),
    /**
     * 修改密码
     */
    UPDATE_PWD(2, "修改密码");




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
