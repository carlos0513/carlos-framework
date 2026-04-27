package com.carlos.boot.application;

import cn.hutool.extra.spring.EnableSpringUtil;
import com.carlos.boot.enums.EnumService;
import com.carlos.boot.resource.ResourceService;
import com.carlos.boot.util.ExtendInfoUtil;
import com.carlos.core.interfaces.ApplicationExtend;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <p>
 * 应用配置
 * </p>
 *
 * @author carlos
 * @date 2020/9/23 23:39
 */
@Slf4j
@Configuration(proxyBeanMethods = false)
@RequiredArgsConstructor
@EnableSpringUtil
@EnableConfigurationProperties(ApplicationProperties.class)
public class ApplicationConfig {

    private final ObjectProvider<ApplicationExtend> applicationExtendProvider;

    /**
     * 初始化 ExtendInfoUtil 静态字段
     */
    @PostConstruct
    public void init() {
        ExtendInfoUtil.init(applicationExtendProvider.getIfAvailable());
        log.debug("ExtendInfoUtil initialized");
    }

    /**
     * 应用信息控制器
     */
    @Bean
    @ConditionalOnProperty(prefix = "carlos.boot.application", name = "enabled", havingValue = "true", matchIfMissing = true)
    public ApplicationController applicationController(EnumService enumService, ResourceService resourceService) {
        return new ApplicationController(enumService, resourceService);
    }
}
