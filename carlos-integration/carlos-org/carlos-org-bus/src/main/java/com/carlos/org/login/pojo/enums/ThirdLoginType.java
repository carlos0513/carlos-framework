package com.carlos.org.login.pojo.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.carlos.core.enums.AppEnum;
import com.carlos.core.enums.BaseEnum;
import com.carlos.core.exception.ServiceException;
import com.carlos.org.login.BbtLogin;
import com.carlos.org.login.PasswordLogin;
import com.carlos.org.login.Sm4PasswordLogin;
import com.carlos.org.login.ThridLogin;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * <p>
 * 第三方登录类型
 * </p>
 *
 * @author Carlos
 * @date 2022/11/11 13:31
 */
@AppEnum(code = "ThirdLoginType")
@Getter
@AllArgsConstructor
public enum ThirdLoginType implements BaseEnum {


    /**
     * 账号密码
     */
    PASSWORD(4, "password", PasswordLogin.class),

    /**
     * 报表通token登录
     */
    BBT(11, "bbt", BbtLogin.class),
    /**
     * 密钥密码登录
     */
    SM4_PWD(13, "sm4pwd", Sm4PasswordLogin.class);


    @EnumValue
    private final Integer code;

    private final String desc;

    private final Class<? extends ThridLogin> loginClass;

    public static ThirdLoginType getByDesc(final String desc) {
        final ThirdLoginType[] values = ThirdLoginType.values();
        for (final ThirdLoginType value : values) {
            if (value.getDesc().equals(desc)) {
                return value;
            }
        }
        throw new ServiceException("登录类型不合法");
    }

    public static ThridLogin getLogin(String desc) {
        ThirdLoginType[] values = ThirdLoginType.values();
        for (ThirdLoginType value : values) {
            if (value.getDesc().equalsIgnoreCase(desc)) {
                Class<? extends ThridLogin> c = value.getLoginClass();
                try {
                    return c.newInstance();
                } catch (Exception e) {
                    throw new ServiceException("获取第三方登录实例失败！", e);
                }
            }
        }
        return null;
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
