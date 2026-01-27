package com.carlos.boot.interceptors;


import com.carlos.boot.BootConstant;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * <p>
 * 拦截器配置
 * </p>
 *
 * @author yunjin
 * @date 2021/10/9 11:04
 */
@Slf4j
@Configuration
@EnableConfigurationProperties(ApplicationInterceptorProperties.class)
@RequiredArgsConstructor
public class ApplicationInterceptorConfig implements WebMvcConfigurer {

    private final ApplicationInterceptorProperties interceptorProperties;


    @Override
    public void addInterceptors(final InterceptorRegistry registry) {
        final GlobalInterceptorProperties properties = interceptorProperties.getGlobalInterceptor();
        properties.getExcludePaths().addAll(BootConstant.COMMON_EXCLUDE_PATH);
        final Set<String> includePaths = properties.getIncludePaths();
        if (CollectionUtils.isEmpty(includePaths)) {
            includePaths.add(BootConstant.ALL_PATH);
        }
        registry.addInterceptor(new GlobalInterceptor(properties))
                .excludePathPatterns(properties.getExcludePaths().toArray(new String[]{}))
                .addPathPatterns(includePaths.toArray(new String[]{}));


    }

}
