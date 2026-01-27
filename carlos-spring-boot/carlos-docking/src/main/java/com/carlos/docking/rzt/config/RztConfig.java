package com.carlos.docking.rzt.config;

import com.carlos.docking.config.FeignClientCustomBuilder;
import com.carlos.docking.rzt.FeignRzt;
import com.carlos.docking.rzt.RztService;
import com.carlos.docking.rzt.RztUtil;
import com.carlos.docking.rzt.organization.FeignRztOrganization;
import com.carlos.docking.rzt.organization.RztOrganizationManager;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationRunner;
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
@EnableConfigurationProperties(RztProperties.class)
@ConditionalOnProperty(prefix = "yunjin.docking.rzt", name = "enabled", havingValue = "true")
@RequiredArgsConstructor
public class RztConfig {

    private final RztProperties properties;


    /**
     * 接口连接工具配置
     */
    @Bean
    public FeignRzt feignRzt() {
        return FeignClientCustomBuilder.getFeignClient(FeignRzt.class, properties.getApi());
    }

    /**
     * token管理器
     */
    @Bean
    public RztAccessTokenManager rztAccessTokenManager(RztProperties properties, FeignRzt feignRzt) {
        return new RztAccessTokenManager(properties, feignRzt);
    }

    /**
     * 接口连接工具配置
     */
    @Bean
    public FeignRztOrganization feignRztOrganization() {
        return FeignClientCustomBuilder.getFeignClient(FeignRztOrganization.class, properties.getOrganization());
    }
    /**
     * org管理器
     */
    @Bean
    public RztOrganizationManager rztOrganizationManager(RztProperties properties, FeignRztOrganization feignRztOrganization) {
        return new RztOrganizationManager(properties, feignRztOrganization);
    }

    @Bean
    public RztService rztService(FeignRzt feignRzt, FeignRztOrganization feignRztOrganization, RztAccessTokenManager tokenManager, RztOrganizationManager organizationManager, RztProperties rztProperties) {
        return new RztService(feignRzt, feignRztOrganization, tokenManager, organizationManager, rztProperties);
    }

    @Bean
    public RztUtil rztUtil(RztService rztService) {
        return new RztUtil(rztService);
    }


    /**
     * token初始化进程
     */
    @Bean
    public ApplicationRunner rztTokenInitWork(RztAccessTokenManager tokenManager, RztOrganizationManager organizationManager) {
        return new RztInitWorker(tokenManager, organizationManager, properties.getAccessTokenRefreshCorn(), properties.getUserCacheRefreshCorn());
    }


}
