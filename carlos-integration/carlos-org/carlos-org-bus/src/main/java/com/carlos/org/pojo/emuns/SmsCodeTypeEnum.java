package com.carlos.org.pojo.emuns;

import com.carlos.core.enums.AppEnum;
import com.carlos.core.enums.BaseEnum;
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
