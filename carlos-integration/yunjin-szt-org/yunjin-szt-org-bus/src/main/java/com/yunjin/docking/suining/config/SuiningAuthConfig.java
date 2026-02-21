package com.yunjin.docking.suining.config;

import com.yunjin.docking.suining.SuiningAuthService;
import com.yunjin.docking.suining.SuiningAuthUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <p>
 * 蓉政通配置
 * </p>
 *
 * @author Carlos
 * @date 2022/4/22 10:24
 */
@Configuration
@EnableConfigurationProperties(SuiningAuthProperties.class)
@ConditionalOnProperty(prefix = "yunjin.docking.suining", name = "enabled", havingValue = "true")
@RequiredArgsConstructor
public class SuiningAuthConfig {

    private final SuiningAuthProperties properties;


    @Bean
    public SuiningAuthService SuiningAuthService() {
        return new SuiningAuthService(properties);
    }


    @Bean
    public SuiningAuthUtil SuiningAuthUtil(SuiningAuthService SuiningAuthService) {
        return new SuiningAuthUtil(SuiningAuthService);
    }
}
