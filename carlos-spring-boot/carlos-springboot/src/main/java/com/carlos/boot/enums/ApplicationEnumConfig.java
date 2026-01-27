package com.carlos.boot.enums;

import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <p>
 * 枚举配置类
 * </p>
 *
 * @author yunjin
 * @date 2020/9/23 23:39
 */
@Configuration
@ConditionalOnProperty(prefix = "carlos.boot.enums", name = "enabled", havingValue = "true")
@EnableConfigurationProperties(ApplicationEnumProperties.class)
@AllArgsConstructor
public class ApplicationEnumConfig {

    private final ApplicationEnumProperties enumProperties;


    @Bean
    public EnumService enumService() {
        return new EnumService(enumProperties);
    }

}
