package com.carlos.minio.web;


import com.carlos.minio.config.MinioConfig;
import com.carlos.minio.config.MinioProperties;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * <p>
 * 接口操作minio时对象处理
 * </p>
 *
 * @author carlos
 * @date 2021/6/10 14:39
 */
@Configuration
@ConditionalOnProperty(prefix = "carlos.minio", name = "enabled", havingValue = "true", matchIfMissing = true)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@AutoConfigureAfter(MinioConfig.class)
public class MinioWebAutoConfig implements WebMvcConfigurer {

    /**
     * Minio控制器
     */
    @Bean
    public MinioController minioController(MinioProperties properties) {
        return new MinioController(properties);
    }


    @Override
    public void addReturnValueHandlers(List<HandlerMethodReturnValueHandler> handlers) {
        handlers.add(new MinioObjectHandlerMethodReturnValueHandler());
    }

}
