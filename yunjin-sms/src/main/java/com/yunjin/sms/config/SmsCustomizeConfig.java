package com.yunjin.sms.config;

import com.yunjin.sms.SmsUtil;
import com.yunjin.sms.postal.config.PostalFactory;
import com.yunjin.sms.template.DefaultSmsTemplateManager;
import com.yunjin.sms.template.SmsTemplateManager;
import com.yunjin.sms.ums.config.UmsFactory;
import com.yunjin.sms.wocloud.config.WoCloudFactory;
import com.yunjin.sms.ynchina.config.YnChinaFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <p>
 * 短信Factory配置
 * </p>
 *
 * @author Carlos
 * @date 2023/11/20 18:47
 */
@Configuration
@EnableConfigurationProperties(SmsProperties.class)
@RequiredArgsConstructor
public class SmsCustomizeConfig {

    private final SmsProperties smsProperties;

    /**
     * 邮政云短信注册
     */
    @Bean
    @ConditionalOnProperty(prefix = "sms.blends.postal", name = "supplier", havingValue = "postal")
    public PostalFactory postalFactory() {
        return PostalFactory.instance();
    }

    /**
     * 企信短信注册
     */
    @Bean
    @ConditionalOnProperty(prefix = "sms.blends.wocloud", name = "supplier", havingValue = "wocloud")
    public WoCloudFactory woCloudFactory() {
        return WoCloudFactory.instance();
    }

    /**
     * 一信通
     */
    @Bean
    @ConditionalOnProperty(prefix = "sms.blends.ums", name = "supplier", havingValue = "ums")
    public UmsFactory umsFactory() {
        return UmsFactory.instance();
    }

    /**
     * 云南
     */
    @Bean
    @ConditionalOnProperty(prefix = "sms.blends.ynchina", name = "supplier", havingValue = "ynchina")
    public YnChinaFactory ynChina() {
        return YnChinaFactory.instance();
    }

    /**
     * 注册短信模板管理器
     */
    @Bean
    @ConditionalOnMissingBean
    public SmsTemplateManager smsTemplateManager() {
        return new DefaultSmsTemplateManager(smsProperties.getTemplates());
    }

    @Bean
    public SmsUtil smsUtil(SmsTemplateManager templateManager) {
        return new SmsUtil(templateManager);
    }

}
