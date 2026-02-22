package com.carlos.oauth.config;

import com.carlos.oauth.support.thirdparty.*;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * 第三方登录配置类
 *
 * <p>配置微信、钉钉、短信等第三方登录方式。</p>
 *
 * <h3>使用方式：</h3>
 *
 * <h4>1. 启用第三方登录</h4>
 * <pre>{@code
 * carlos:
 *   oauth2:
 *     third-party:
 *       enabled: true
 *       wechat: true      # 启用微信登录
 *       dingtalk: true    # 启用钉钉登录
 *       sms: true         # 启用短信登录
 * }</pre>
 *
 * <h4>2. 实现登录服务（必需）</h4>
 * <pre>{@code
 * @Service
 * @Primary
 * public class MyWechatLoginService implements ThirdPartyLoginService {
 *
 *     @Override
 *     public LoginUserInfo login(String authCode) {
 *         // 调用微信 API 获取用户信息
 *         // 查找或创建本地用户
 *         // 返回 LoginUserInfo
 *     }
 *
 *     @Override
 *     public ThirdPartyType getType() {
 *         return ThirdPartyType.WECHAT;
 *     }
 * }
 * }</pre>
 *
 * @author carlos
 * @version 3.0.0
 * @since 2026-02-22
 */
@Configuration
@ConditionalOnProperty(
        prefix = "carlos.oauth2.third-party",
        name = "enabled",
        havingValue = "true"
)
public class ThirdPartyLoginConfig {

    /**
     * 微信登录服务
     *
     * <p>默认实现仅用于测试，生产环境需要覆盖。</p>
     */
    @Bean
    @ConditionalOnMissingBean(name = "wechatLoginService")
    @ConditionalOnProperty(prefix = "carlos.oauth2.third-party", name = "wechat", havingValue = "true")
    public ThirdPartyLoginService wechatLoginService() {
        return new DefaultThirdPartyLoginService(ThirdPartyType.WECHAT);
    }

    /**
     * 钉钉登录服务
     */
    @Bean
    @ConditionalOnMissingBean(name = "dingtalkLoginService")
    @ConditionalOnProperty(prefix = "carlos.oauth2.third-party", name = "dingtalk", havingValue = "true")
    public ThirdPartyLoginService dingtalkLoginService() {
        return new DefaultThirdPartyLoginService(ThirdPartyType.DINGTALK);
    }

    /**
     * 短信登录服务
     */
    @Bean
    @ConditionalOnMissingBean(name = "smsLoginService")
    @ConditionalOnProperty(prefix = "carlos.oauth2.third-party", name = "sms", havingValue = "true")
    public ThirdPartyLoginService smsLoginService() {
        return new DefaultThirdPartyLoginService(ThirdPartyType.SMS);
    }

    /**
     * 微信登录认证转换器
     */
    @Bean
    @ConditionalOnProperty(prefix = "carlos.oauth2.third-party", name = "wechat", havingValue = "true")
    public WechatAuthenticationConverter wechatAuthenticationConverter() {
        return new WechatAuthenticationConverter();
    }

    /**
     * 钉钉登录认证转换器
     */
    @Bean
    @ConditionalOnProperty(prefix = "carlos.oauth2.third-party", name = "dingtalk", havingValue = "true")
    public DingtalkAuthenticationConverter dingtalkAuthenticationConverter() {
        return new DingtalkAuthenticationConverter();
    }

    /**
     * 短信登录认证转换器
     */
    @Bean
    @ConditionalOnProperty(prefix = "carlos.oauth2.third-party", name = "sms", havingValue = "true")
    public SmsAuthenticationConverter smsAuthenticationConverter() {
        return new SmsAuthenticationConverter();
    }

    /**
     * 第三方登录认证转换器列表
     *
     * <p>用于在授权服务器配置中统一注册。</p>
     */
    @Bean
    public List<ThirdPartyAuthenticationConverter> thirdPartyConverters(
            WechatAuthenticationConverter wechatConverter,
            DingtalkAuthenticationConverter dingtalkConverter,
            SmsAuthenticationConverter smsConverter) {

        List<ThirdPartyAuthenticationConverter> converters = new ArrayList<>();
        converters.add(wechatConverter);
        converters.add(dingtalkConverter);
        converters.add(smsConverter);
        return converters;
    }

    /**
     * 统一的第三方认证转换器接口
     *
     * <p>用于类型安全的转换器列表注入。</p>
     */
    public interface ThirdPartyAuthenticationConverter
            extends org.springframework.security.web.authentication.AuthenticationConverter {
    }
}
