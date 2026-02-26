package com.carlos.auth.config;

import com.carlos.auth.service.DefaultExtendUserDetailsService;
import com.carlos.auth.service.ExtendUserDetailsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * OAuth2 服务自动配置
 *
 * <p>配置 OAuth2 相关的服务层组件，包括用户详情服务等。</p>
 *
 * <h3>配置的 Bean：</h3>
 * <ul>
 *   <li>{@link ExtendUserDetailsService} - 用户详情服务</li>
 * </ul>
 *
 * <h3>扩展点：</h3>
 * <p>应用可以实现 {@link ExtendUserDetailsService} 接口并注册为 Bean，
 * 自动替换默认实现。</p>
 *
 * @author carlos
 * @version 3.0.0
 * @since 2026-02-22
 */
@Slf4j
@Configuration
@ConditionalOnProperty(
        prefix = "carlos.auth",
        name = "enabled",
        havingValue = "true",
        matchIfMissing = true
)
public class OAuth2ServiceConfig {

    /**
     * 用户详情服务
     *
     * <p>提供用户认证和业务信息加载功能。</p>
     *
     * <h3>默认实现：</h3>
     * <p>使用 {@link DefaultExtendUserDetailsService}，仅包含测试用户。</p>
     *
     * <h3>自定义实现：</h3>
     * <pre>{@code
     * @Service
     * @Primary
     * public class MyUserDetailsService implements ExtendUserDetailsService {
     *     // 自定义实现...
     * }
     * }</pre>
     *
     * @param passwordEncoder 密码编码器
     * @return ExtendUserDetailsService 用户详情服务
     */
    @Bean
    @ConditionalOnMissingBean
    public ExtendUserDetailsService extendUserDetailsService(PasswordEncoder passwordEncoder) {
        log.info("Configuring default ExtendUserDetailsService (for development only)");
        return new DefaultExtendUserDetailsService(passwordEncoder);
    }
}
