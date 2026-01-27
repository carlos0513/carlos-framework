package com.carlos.boot.application;


import cn.hutool.core.map.MapUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.HashMap;
import java.util.Map;

/**
 * WebMvc配置
 *
 * @author yunjin
 */
@Slf4j
@Configuration(proxyBeanMethods = false)
@RequiredArgsConstructor
public class ApplicationWebMvcConfig implements WebMvcConfigurer {

    private final ApplicationProperties applicationProperties;


    @Override
    public void addResourceHandlers(@NonNull final ResourceHandlerRegistry registry) {
        // 设置项目静态资源访问
        Map<String, String> resourceHandlers = applicationProperties.getResourceHandlers();
        if (MapUtil.isEmpty(resourceHandlers)) {
            resourceHandlers = new HashMap<>();
        }
        resourceHandlers.put("/static/**", "classpath:/static/");
        resourceHandlers.forEach((key, value) -> {
            if (!registry.hasMappingForPattern(key)) {
                registry.addResourceHandler(key).addResourceLocations(value);
            }
        });
    }
}
