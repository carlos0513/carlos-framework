package com.carlos.gateway.oauth2;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.carlos.core.auth.UserContext;
import com.carlos.core.exception.BusinessException;
import com.carlos.core.response.CommonErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Base64;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 * Opaque Token 验证器（参考微信/钉钉设计）
 * 通过调用认证服务器 introspection 端点验证 Token
 * </p>
 *
 * @author carlos
 * @date 2026/3/16
 */
@Slf4j
public class OpaqueTokenValidator implements TokenValidator {

    private final OAuth2GatewayProperties properties;
    private final WebClient webClient;
    private final ReactiveStringRedisTemplate redisTemplate;

    public OpaqueTokenValidator(OAuth2GatewayProperties properties,
                                WebClient.Builder webClientBuilder,
                                ReactiveStringRedisTemplate redisTemplate) {
        this.properties = properties;
        this.webClient = webClientBuilder.build();
        this.redisTemplate = redisTemplate;
    }

    @Override
    public Mono<UserContext> validate(String token) {
        if (StrUtil.isBlank(token)) {
            return Mono.error(new BusinessException(CommonErrorCode.UNAUTHORIZED, "Token is empty"));
        }

        // 先从缓存中获取
        String cacheKey = "token:introspection:" + token;
        return redisTemplate.opsForValue()
            .get(cacheKey)
            .flatMap(cached -> {
                log.debug("Token introspection cache hit");
                return Mono.just(parseCachedContext(cached));
            })
            .switchIfEmpty(Mono.defer(() -> introspectToken(token, cacheKey)));
    }

    private Mono<UserContext> introspectToken(String token, String cacheKey) {
        log.debug("Introspecting token at: {}", properties.getIntrospectionUri());

        String basicAuth = Base64.getEncoder().encodeToString(
            (properties.getClientId() + ":" + properties.getClientSecret())
                .getBytes(StandardCharsets.UTF_8));

        return webClient.post()
            .uri(properties.getIntrospectionUri())
            .header(HttpHeaders.AUTHORIZATION, "Basic " + basicAuth)
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .body(BodyInserters.fromFormData("token", token))
            .retrieve()
            .bodyToMono(String.class)
            .flatMap(response -> {
                JSONObject json = JSONUtil.parseObj(response);

                // 检查 Token 是否有效
                Boolean active = json.getBool("active");
                if (active == null || !active) {
                    return Mono.error(new BusinessException(CommonErrorCode.UNAUTHORIZED, "Token is not active"));
                }

                UserContext context = buildUserContext(json);

                // 缓存验证结果
                return cacheIntrospectionResult(cacheKey, json, context)
                    .thenReturn(context);
            })
            .onErrorMap(e -> {
                if (e instanceof BusinessException) {
                    return e;
                }
                log.error("Token introspection failed", e);
                return new BusinessException(CommonErrorCode.UNAUTHORIZED, "Token validation failed");
            });
    }

    private Mono<Void> cacheIntrospectionResult(String cacheKey, JSONObject json, UserContext context) {
        // 计算缓存时间（取 expires_in 和配置的最小值）
        Long expiresIn = json.getLong("exp");
        Duration cacheDuration = properties.getIntrospectionCacheDuration();

        if (expiresIn != null) {
            long ttl = expiresIn - System.currentTimeMillis() / 1000;
            if (ttl > 0 && ttl < cacheDuration.getSeconds()) {
                cacheDuration = Duration.ofSeconds(ttl);
            }
        }

        return redisTemplate.opsForValue()
            .set(cacheKey, JSONUtil.toJsonStr(context), cacheDuration)
            .then();
    }

    private UserContext buildUserContext(JSONObject json) {
        UserContext context = new UserContext();
        context.setToken(json.getStr("jti"));
        context.setUserId(json.getStr("sub"));
        context.setAccount(json.getStr("username"));
        context.setRoleId(json.getLong("role_id"));
        context.setTenantId(json.getLong("tenant_id"));
        context.setDepartmentId(json.getLong("dept_id"));
        context.setPhone(json.getStr("phone"));

        // 解析角色列表
        Object roles = json.get("roles");
        if (roles instanceof List) {
            Set<Serializable> roleIds = ((List<?>) roles).stream()
                .map(Object::toString)
                .map(Long::parseLong)
                .collect(Collectors.toSet());
            context.setRoleIds(roleIds);
        }

        // 解析部门列表
        Object depts = json.get("depts");
        if (depts instanceof List) {
            Set<Serializable> deptIds = ((List<?>) depts).stream()
                .map(Object::toString)
                .map(Long::parseLong)
                .collect(Collectors.toSet());
            context.setDepartmentIds(deptIds);
        }

        return context;
    }

    private UserContext parseCachedContext(String cached) {
        return JSONUtil.toBean(cached, UserContext.class);
    }

    @Override
    public OAuth2GatewayProperties.TokenType getType() {
        return OAuth2GatewayProperties.TokenType.OPAQUE;
    }
}
