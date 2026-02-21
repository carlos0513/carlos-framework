package com.yunjin.org.config;

import com.yunjin.core.interfaces.ApplicationExtend;
import com.yunjin.org.api.ApiUser;
import com.yunjin.system.api.ApiRegion;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * <p>
 * 用户模块配置
 * </p>
 *
 * @author Carlos
 * @date 2023/10/31 21:53
 */
@Configuration
@EnableConfigurationProperties({OrgProperties.class, MenuApiMappingProperties.class})
public class OrgConfig {

    @Bean
    @Primary
//    @ConditionalOnMissingBean
    public ApplicationExtend applicationExtend(ApiUser apiUser, ApiRegion apiRegion) {
        return new OrgApplicationExtendImpl(apiUser, apiRegion);
    }

}
