package com.carlos.oauth.support.thirdparty;

import lombok.Getter;

/**
 * 第三方登录平台类型
 *
 * <p>定义支持的第三方登录平台及其配置。</p>
 *
 * @author carlos
 * @version 3.0.0
 * @since 2026-02-22
 */
@Getter
public enum ThirdPartyType {

    /**
     * 微信登录
     *
     * <p>支持微信公众号、微信小程序、微信开放平台等多种方式。</p>
     * <p><strong>授权模式：</strong>authorization_code</p>
     * <p><strong>必需参数：</strong>code（授权码）</p>
     */
    WECHAT("wechat", "微信登录", "wx_code"),

    /**
     * 微信小程序登录
     *
     * <p>小程序特有的登录方式，使用 wx.login 获取 code。</p>
     * <p><strong>必需参数：</strong>code（登录凭证）</p>
     */
    WECHAT_MINI("wechat_mini", "微信小程序登录", "wx_mini_code"),

    /**
     * 钉钉登录
     *
     * <p>支持钉钉扫码登录和钉钉内免登。</p>
     * <p><strong>授权模式：</strong>authorization_code</p>
     * <p><strong>必需参数：</strong>code（授权码）或 access_token</p>
     */
    DINGTALK("dingtalk", "钉钉登录", "dt_code"),

    /**
     * 短信验证码登录
     *
     * <p>通过手机短信验证码登录。</p>
     * <p><strong>必需参数：</strong>mobile:code（手机号和验证码，用冒号分隔）</p>
     */
    SMS("sms", "短信验证码登录", "sms_code"),

    /**
     * 企业微信登录
     *
     * <p>企业微信独立的登录方式。</p>
     * <p><strong>必需参数：</strong>code（授权码）</p>
     */
    WECHAT_WORK("wechat_work", "企业微信登录", "wx_work_code"),

    /**
     * 飞书登录
     *
     * <p>飞书开放平台登录。</p>
     * <p><strong>必需参数：</strong>code（授权码）</p>
     */
    FEISHU("feishu", "飞书登录", "feishu_code");

    /**
     * 类型编码（用于数据库存储和接口传输）
     */
    private final String code;

    /**
     * 类型描述
     */
    private final String description;

    /**
     * OAuth2 授权类型（grant_type）
     */
    private final String grantType;

    ThirdPartyType(String code, String description, String grantType) {
        this.code = code;
        this.description = description;
        this.grantType = grantType;
    }

    /**
     * 根据编码获取类型
     *
     * @param code 类型编码
     * @return ThirdPartyType 类型枚举，不存在返回 null
     */
    public static ThirdPartyType getByCode(String code) {
        for (ThirdPartyType type : values()) {
            if (type.getCode().equalsIgnoreCase(code)) {
                return type;
            }
        }
        return null;
    }

    /**
     * 根据 grant_type 获取类型
     *
     * @param grantType 授权类型
     * @return ThirdPartyType 类型枚举，不存在返回 null
     */
    public static ThirdPartyType getByGrantType(String grantType) {
        for (ThirdPartyType type : values()) {
            if (type.getGrantType().equalsIgnoreCase(grantType)) {
                return type;
            }
        }
        return null;
    }
}
