package com.carlos.auth.fallback;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * feign配置类
 *
 * @author Carlos
 * @date 2022/11/16 10:24
 */
@Configuration
public class AuthFeignConfig {

    @Bean
    public FeignAuthFallbackFactory authFallbackFactory() {
        return new FeignAuthFallbackFactory();
    }
}
