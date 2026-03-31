package com.carlos.boot.translation.config;

import com.carlos.boot.translation.service.CachedTranslationService;
import com.carlos.boot.translation.service.TranslationService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * <p>
 * 翻译模块自动配置
 * </p>
 *
 * @author carlos
 * @date 2025/01/20
 */
@Configuration
@EnableConfigurationProperties(TranslationProperties.class)
@ComponentScan("com.carlos.boot.translation")
@ConditionalOnProperty(prefix = "carlos.translation", name = "enabled", havingValue = "true", matchIfMissing = true)
public class TranslationAutoConfiguration {

    /**
     * 翻译服务
     *
     * @return TranslationService
     */
    @Bean
    @ConditionalOnMissingBean
    public TranslationService translationService() {
        return new CachedTranslationService();
    }
}
