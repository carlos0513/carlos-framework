package com.carlos.boot.response;


import com.carlos.boot.GlobalExceptionHandler;
import com.carlos.boot.interceptors.ApplicationInterceptorProperties;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * WebMvc配置
 *
 * @author carlos
 * @date 2018-11-08
 */
@Slf4j
@Configuration
@EnableConfigurationProperties(ResponseProperties.class)
@AllArgsConstructor
public class ResponseConfig implements WebMvcConfigurer {

    private final ResponseProperties responseProperties;

    private final ApplicationInterceptorProperties interceptorProperties;

    /**
     * 响应包装Advice
     */
    @Bean
    @ConditionalOnProperty(prefix = "carlos.boot.response.wrap", name = "enable", havingValue = "true")
    public ResponseWrapperAdvice responseWrapperAdvice() {
        return new ResponseWrapperAdvice(responseProperties, interceptorProperties);
    }

    /**
     * 全局异常处理器
     */
    @Bean
    public GlobalExceptionHandler globalExceptionHandler() {
        return new GlobalExceptionHandler();
    }

    // ----------------------   转换器配置 end   ------------------------

}
