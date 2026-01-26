package com.yunjin.boot.resource;

import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <p>
 * 资源配置
 * </p>
 *
 * @author yunjin
 * @date 2020/9/23 23:39
 */
@Configuration
@EnableConfigurationProperties(ApplicationResourceProperties.class)
@AllArgsConstructor
public class ApplicationResourceConfig {

    private final ApplicationResourceProperties resourceProperties;

    @Bean
    public ResourceService resourceService(ResourceStore resourceStore) {
        return new ResourceService(resourceProperties, resourceStore);
    }

    @Bean
    @ConditionalOnMissingBean(ResourceStore.class)
    public ResourceStore resourceStore() {
        return new DefaultResourceStore();
    }

}
