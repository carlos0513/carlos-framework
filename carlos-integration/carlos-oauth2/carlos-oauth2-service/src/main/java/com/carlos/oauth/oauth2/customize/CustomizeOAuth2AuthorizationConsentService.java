package com.carlos.oauth.oauth2.customize;

import com.carlos.redis.util.RedisUtil;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsent;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsentService;
import org.springframework.util.Assert;

import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 自定义授权确认信息处理服务，管理用户对客户端的授权同意记录 一、OAuth2AuthorizationConsentService 核心作用 OAuth2AuthorizationConsentService 是 Spring
 * Security OAuth2 授权服务器中用于 管理用户对客户端的授权同意记录 的核心组件，其核心功能包括：
 * <p>
 * 持久化授权同意信息 存储用户对客户端（Client）的授权范围（Scope）和权限的明确同意记录。例如，当用户首次通过授权码模式同意客户端访问 read 和 write 权限后，后续请求可跳过重复授权步骤4。 支持授权流程优化 在
 * OAuth2 授权码模式中，若用户已对某客户端的特定 Scope 进行过授权，授权服务器可通过查询此服务判断是否需要重新请求用户确认，提升用户体验2。 确保安全性 通过记录授权行为，防止未经同意的 Scope 变更或客户端滥用权限。
 * 二、需自定义实现类的场景 默认实现（如 InMemoryOAuth2AuthorizationConsentService）适用于简单场景，但在以下情况需重写：
 * <p>
 * 1. 持久化存储需求 问题：内存存储重启后丢失数据，导致用户需重复授权。 解决方案：继承 OAuth2AuthorizationConsentService 并实现数据库（如 MySQL）或 Redis 存储。 示例：参考
 * JdbcClientDetailsService 的实现方式，将数据写入 oauth2_authorization_consent 表4。 2. 分布式系统集成 问题：微服务架构中多个授权服务器实例需共享授权记录。 解决方案：通过
 * Redis 或分布式数据库实现跨实例数据同步，类似令牌持久化方案。 生效条件： 1.客户端未声明作用域 在 RegisteredClient 配置中，需通过 .scopes()
 * 方法明确定义客户端请求的作用域，否则授权同意信息不会被记录。
 * <p>
 * 2.资源服务器未校验权限 资源服务器需通过 .hasAuthority() 或 .hasRole() 方法声明所需的权限，否则即使授权同意信息存在也不会触发校验。
 * </p>
 *
 * @author carlos
 * @date 2022/11/4 13:19
 */
public class CustomizeOAuth2AuthorizationConsentService implements OAuth2AuthorizationConsentService {

    private final static Long TIMEOUT = 10L;

    @Override
    public void save(OAuth2AuthorizationConsent authorizationConsent) {
        Assert.notNull(authorizationConsent, "authorizationConsent cannot be null");
        RedisUtil.setValue(buildKey(authorizationConsent), authorizationConsent, TIMEOUT, TimeUnit.MINUTES);
    }

    @Override
    public void remove(OAuth2AuthorizationConsent authorizationConsent) {
        Assert.notNull(authorizationConsent, "authorizationConsent cannot be null");
        RedisUtil.delete(buildKey(authorizationConsent));
    }

    @Override
    public OAuth2AuthorizationConsent findById(String registeredClientId, String principalName) {
        Assert.hasText(registeredClientId, "registeredClientId cannot be empty");
        Assert.hasText(principalName, "principalName cannot be empty");
        return RedisUtil.getValue(buildKey(registeredClientId, principalName));
    }

    private static String buildKey(String registeredClientId, String principalName) {
        return "oauth2:consent:" + registeredClientId + ":" + principalName;
    }

    private static String buildKey(OAuth2AuthorizationConsent authorizationConsent) {
        return buildKey(authorizationConsent.getRegisteredClientId(), authorizationConsent.getPrincipalName());
    }
}
