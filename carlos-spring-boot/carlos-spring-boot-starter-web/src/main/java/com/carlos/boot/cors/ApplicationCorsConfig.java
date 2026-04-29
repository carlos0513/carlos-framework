package com.carlos.boot.cors;


import cn.hutool.core.collection.CollUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.List;

/**
 * 跨域配置 默认开启
 *
 * @author carlos
 * @date 2019/10/14
 */
@Slf4j
@Configuration(proxyBeanMethods = false)
@RequiredArgsConstructor
@EnableConfigurationProperties(ApplicationCorsProperties.class)
@ConditionalOnProperty(value = {"carlos.boot.cors.enable"}, havingValue = "true", matchIfMissing = true)
public class ApplicationCorsConfig {

    private final ApplicationCorsProperties corsProperties;

    @Bean
    public FilterRegistrationBean<CorsFilter> corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        // 跨域配置
        config.setAllowCredentials(corsProperties.isAllowCredentials());
        // Spring Boot 3.x 中 allowedOriginPatterns 不能为空，需设置默认值
        List<String> originPatterns = corsProperties.getAllowedOriginsPattens();
        if (CollUtil.isEmpty(originPatterns)) {
            originPatterns = List.of("*");
        }
        config.setAllowedOriginPatterns(originPatterns);
        config.setAllowedHeaders(corsProperties.getAllowedHeaders());
        config.setAllowedMethods(corsProperties.getAllowedMethods());
        // config.setExposedHeaders(corsProperties.getExposedHeaders());
        config.setMaxAge(corsProperties.getMaxAge());

        // CORS过滤的路径，默认：/**
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration(corsProperties.getPath(), config);
        FilterRegistrationBean<CorsFilter> bean = new FilterRegistrationBean<>(new CorsFilter(source));
        bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return bean;
    }


}
