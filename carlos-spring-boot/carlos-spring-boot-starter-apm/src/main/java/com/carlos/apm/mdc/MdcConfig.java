package com.carlos.apm.mdc;

import com.carlos.apm.config.ApmProperties;
import com.carlos.apm.mdc.filter.MdcFilter;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

/**
 * MDC 配置类
 * <p>
 * 配置 MDC 过滤器，自动将 Trace ID 注入到日志上下文中
 *
 * @author Carlos
 * @date 2024-12-09
 */
@Slf4j
@Configuration(proxyBeanMethods = false)
@ConditionalOnWebApplication
@ConditionalOnProperty(prefix = "carlos.apm.mdc", name = "enabled", havingValue = "true", matchIfMissing = true)
@EnableConfigurationProperties(ApmProperties.class)
public class MdcConfig {

    @PostConstruct
    public void init() {
        log.info("[Carlos APM] MDC 上下文模块已启用");
    }

    /**
     * 注册 MDC 过滤器
     * <p>
     * 优先级设置为最高，确保在请求处理开始时 Trace ID 就已经设置到 MDC 中
     */
    @Bean
    public FilterRegistrationBean<MdcFilter> mdcFilterRegistration(ApmProperties apmProperties) {
        FilterRegistrationBean<MdcFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new MdcFilter(apmProperties));
        registration.addUrlPatterns("/*");
        registration.setName("mdcFilter");
        registration.setOrder(Ordered.HIGHEST_PRECEDENCE);
        log.debug("[Carlos APM] MDC Filter 已注册");
        return registration;
    }
}
