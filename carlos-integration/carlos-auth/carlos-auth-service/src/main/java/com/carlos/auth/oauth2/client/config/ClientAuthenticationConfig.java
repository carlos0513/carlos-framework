package com.carlos.auth.oauth2.client.config;

import com.carlos.auth.app.service.AppClientService;
import com.carlos.auth.oauth2.client.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;

/**
 * OAuth2 客户端认证配置类
 *
 * <p>配置客户端认证相关的组件，包括：</p>
 * <ul>
 *   <li>客户端凭证转换器（支持 JSON 格式的 POST body）</li>
 *   <li>客户端认证成功/失败处理器</li>
 *   <li>客户端凭证模式的 Token 生成器</li>
 *   <li>客户端 Token 增强器</li>
 *   <li>数据库客户端仓库（可选）</li>
 * </ul>
 *
 * <h3>使用说明：</h3>
 * <p>默认情况下，客户端认证使用内存存储（通过 application.yml 配置）。
 * 如果需要使用数据库存储客户端信息，需要启用 {@code carlos.oauth2.client.db-repository=true}。</p>
 *
 * <h3>配置示例：</h3>
 * <pre>{@code
 * carlos:
 *   oauth2:
 *     client:
 *       # 启用数据库客户端仓库
 *       db-repository: true
 *       # 启用客户端凭证模式的自定义 Token 生成
 *       custom-token-generator: true
 * }</pre>
 *
 * @author Carlos
 * @version 3.0.0
 * @since 2026-04-08
 * @see ClientSecretPostBodyAuthenticationConverter
 * @see CustomizeRegisteredClientRepository
 * @see CustomizeClientOAuth2AccessTokenGenerator
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
@ConditionalOnProperty(
    prefix = "carlos.oauth2.authorization-server",
    name = "enabled",
    havingValue = "true",
    matchIfMissing = true
)
public class ClientAuthenticationConfig {

    /**
     * 客户端凭证 POST Body 转换器
     *
     * <p>支持从 JSON 格式的 POST 请求体中提取 client_id 和 client_secret。
     * 适用于 Content-Type: application/json 的客户端认证请求。</p>
     *
     * @return ClientSecretPostBodyAuthenticationConverter 实例
     */
    @Bean
    @ConditionalOnMissingBean
    public ClientSecretPostBodyAuthenticationConverter clientSecretPostBodyAuthenticationConverter() {
        log.info("Configuring ClientSecretPostBodyAuthenticationConverter");
        return new ClientSecretPostBodyAuthenticationConverter();
    }

    /**
     * 客户端认证失败处理器
     *
     * @return CustomClientAuthenticationFailureHandler 实例
     */
    @Bean
    @ConditionalOnMissingBean
    public CustomClientAuthenticationFailureHandler customClientAuthenticationFailureHandler() {
        log.info("Configuring CustomClientAuthenticationFailureHandler");
        return new CustomClientAuthenticationFailureHandler();
    }

    /**
     * 客户端认证成功处理器
     *
     * @return CustomClientAuthenticationSuccessHandler 实例
     */
    @Bean
    @ConditionalOnMissingBean
    public CustomClientAuthenticationSuccessHandler customClientAuthenticationSuccessHandler() {
        log.info("Configuring CustomClientAuthenticationSuccessHandler");
        return new CustomClientAuthenticationSuccessHandler();
    }

    /**
     * 客户端 Token 增强器
     *
     * <p>为客户端凭证模式（CLIENT_CREDENTIALS）生成的 Token 添加自定义声明，
     * 如 client_id、token_type、issued_at 等。</p>
     *
     * @return CustomizeClientOAuth2TokenCustomizer 实例
     */
    @Bean
    @ConditionalOnMissingBean(name = "clientOAuth2TokenCustomizer")
    public CustomizeClientOAuth2TokenCustomizer clientOAuth2TokenCustomizer() {
        log.info("Configuring CustomizeClientOAuth2TokenCustomizer");
        return new CustomizeClientOAuth2TokenCustomizer();
    }

    /**
     * 数据库客户端仓库
     *
     * <p>从数据库加载 OAuth2 客户端配置，替代内存存储。
     * 需要启用 {@code carlos.oauth2.client.db-repository=true}。</p>
     *
     * @param appClientService 应用客户端服务
     * @return CustomizeRegisteredClientRepository 实例
     */
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnBean(AppClientService.class)
    @ConditionalOnProperty(
        prefix = "carlos.oauth2.client",
        name = "db-repository",
        havingValue = "true",
        matchIfMissing = true
    )
    public RegisteredClientRepository registeredClientRepository(AppClientService appClientService) {
        log.info("Configuring CustomizeRegisteredClientRepository (database-based)");
        return new CustomizeRegisteredClientRepository(appClientService);
    }




}
