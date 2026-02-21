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
     * 云政通
     */
    YZT(12, "yzt", YztLogin.class),
    /**
     * 蓉政通
     */
    RZT(0, "rzt", RztLogin.class),
    /**
     * 大联动
     */
    BIG_LINKAGE(1, "bigLinkage", BigLinkAgeLogin.class),
    /**
     * 事件中枢
     */
    EVENT(2, "event", EventLogin.class),
    /**
     * 钉钉
     */
    DINGTALK(3, "dingtalk", DingtalkLogin.class),
    /**
     * 账号密码
     */
    PASSWORD(4, "password", PasswordLogin.class),

    /**
     * 二维码填报登录
     */
    QR_FILL(5, "qrFill", GridQrLogin.class),

    /**
     * 数据共享登录
     */
    QR_DATA_SHARE(6, "qrDataShare", GridQrLogin.class),

    /**
     * 天府统一认证登录
     */
    TF_OAUTH(7, "tfoauth", TfOauthLogin.class),

    /**
     * 天府统一APP登录
     */
    TF_APP(8, "tfapp", TfAppLogin.class),

    /**
     * 遂宁APP登录
     */
    SUINING_APP(9, "snapp", SuiningAppLogin.class),
    /**
     * 黑龙江一体化平台
     */
    LJ_APP(10, "lja", LJALogin.class),
    /**
     * 报表通token登录
     */
    BBT(11, "bbt", BbtLogin.class),

    /**
     * 市级系统单点登录
     */
    CITY_SSO(12, "citySso", CitySSOLogin.class),
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
