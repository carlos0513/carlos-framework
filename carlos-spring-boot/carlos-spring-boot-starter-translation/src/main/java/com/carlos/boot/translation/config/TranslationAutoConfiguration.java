package com.carlos.boot.translation.config;

import com.carlos.boot.translation.aop.TranslationAspect;
import com.carlos.boot.translation.cache.TranslationCacheManager;
import com.carlos.boot.translation.service.CachedTranslationService;
import com.carlos.boot.translation.service.TranslationService;
import com.carlos.core.interfaces.ApplicationExtend;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
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
@ConditionalOnProperty(prefix = "carlos.translation", name = "enabled", havingValue = "true", matchIfMissing = true)
public class TranslationAutoConfiguration {


    /**
     * 缓存管理器
     *
     * @param properties 配置属性
     * @return TranslationCacheManager
     */
    @Bean
    @ConditionalOnMissingBean
    public TranslationCacheManager cacheManager(TranslationProperties properties) {
        return new TranslationCacheManager(properties);
    }

    /**
     * 翻译服务
     *
     * @return TranslationService
     */
    @Bean
    @ConditionalOnMissingBean
    public TranslationService translationService(ApplicationExtend applicationExtend, TranslationCacheManager cacheManager) {
        return new CachedTranslationService(applicationExtend, cacheManager);
    }

    /**
     * 翻译切面
     *
     * @param translationService 翻译服务
     * @return TranslationAspect
     */
    @Bean
    @ConditionalOnMissingBean
    public TranslationAspect translationAspect(TranslationService translationService) {
        return new TranslationAspect(translationService);
    }
}
