package com.carlos.boot.filters;


import com.carlos.boot.filters.xss.XssFilter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 过滤器配置
 *
 * @author yunjin
 * @date 2018-11-08
 */
@Slf4j
@Configuration
@EnableConfigurationProperties(ApplicationFilterProperties.class)
@RequiredArgsConstructor
public class ApplicationFilterConfig implements WebMvcConfigurer {

    private final ApplicationFilterProperties filterProperties;

    /**
     * 请求封装过滤器，解决请求的参数多次读取的问题
     *
     * @author yunjin
     * @date 2020/5/19 15:40
     */
    @Bean
    @Order(1)
    public FilterRegistrationBean<RequestWrapperFilter> requestDetailFilter() {
        ApplicationFilterProperties.FilterProperties properties = filterProperties.getRequestWrapper();
        FilterRegistrationBean<RequestWrapperFilter> filter = new FilterRegistrationBean<>(new RequestWrapperFilter());
        return properties.initFilterRegistrationBean(filter);
    }

    @Bean
    @Order(2)
    @ConditionalOnProperty(value = {"yunjin.boot.filters.xss.enable"})
    public FilterRegistrationBean<XssFilter> xssFilter() {
        ApplicationFilterProperties.FilterProperties properties = filterProperties.getXss();
        FilterRegistrationBean<XssFilter> filter = new FilterRegistrationBean<>(new XssFilter());
        return properties.initFilterRegistrationBean(filter);
    }
}
