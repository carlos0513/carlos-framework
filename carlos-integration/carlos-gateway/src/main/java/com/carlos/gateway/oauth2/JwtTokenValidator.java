package com.carlos.gateway.oauth2;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTUtil;
import cn.hutool.jwt.signers.JWTSigner;
import cn.hutool.jwt.signers.JWTSignerUtil;
import com.carlos.core.auth.UserContext;
import com.carlos.core.exception.ServiceException;
import com.carlos.core.response.StatusCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import reactor.core.publisher.Mono;

import java.io.Serializable;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 * JWT Token 验证器
 * 使用 Hutool JWT 实现
 * </p>
 *
 * @author carlos
 * @date 2026/3/16
 */
@Slf4j
public class JwtTokenValidator implements TokenValidator {

    private final OAuth2GatewayProperties properties;
    private final ReactiveStringRedisTemplate redisTemplate;
    private volatile JWTSigner signer;

    public JwtTokenValidator(OAuth2GatewayProperties properties,
                             ReactiveStringRedisTemplate redisTemplate) {
        this.properties = properties;
        this.redisTemplate = redisTemplate;
        initSigner();
    }

    private void initSigner() {
        try {
            if (StrUtil.isNotBlank(properties.getJwtPublicKey())) {
                String publicKeyBase64 = properties.getJwtPublicKey()
                    .replace("-----BEGIN PUBLIC KEY-----", "")
                    .replace("-----END PUBLIC KEY-----", "")
                    .replaceAll("\\s", "");
                byte[] keyBytes = Base64.getDecoder().decode(publicKeyBase64);
                X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
                KeyFactory keyFactory = KeyFactory.getInstance("RSA");
                PublicKey publicKey = keyFactory.generatePublic(spec);
                this.signer = JWTSignerUtil.rs256(publicKey);
            }
        } catch (Exception e) {
            log.error("Failed to initialize JWT signer", e);
            throw new ServiceException("JWT signer initialization failed");
        }
    }

    @Override
    public Mono<UserContext> validate(String token) {
        if (StrUtil.isBlank(token)) {
            return Mono.error(new ServiceException(StatusCode.UNAUTHORIZED, "Token is empty"));
        }

        // 检查 Token 是否在黑名单中（用于实现登出功能）
        return isTokenBlacklisted(token)
            .flatMap(isBlacklisted -> {
                if (isBlacklisted) {
                    return Mono.error(new ServiceException(StatusCode.UNAUTHORIZED, "Token has been revoked"));
                }
                return validateJwt(token);
            });
    }

    private Mono<Boolean> isTokenBlacklisted(String token) {
        String jti = extractJti(token);
        if (StrUtil.isBlank(jti)) {
            return Mono.just(false);
        }
        return redisTemplate.hasKey("token:blacklist:" + jti);
    }

    private String extractJti(String token) {
        try {
            JWT jwt = JWTUtil.parseToken(token);
            return (String) jwt.getPayload("jti");
        } catch (Exception e) {
            return null;
        }
    }

    private Mono<UserContext> validateJwt(String token) {
        return Mono.fromCallable(() -> {
            try {
                JWT jwt = JWTUtil.parseToken(token);

                // 验证签名
                if (signer != null && !jwt.verify(signer)) {
                    throw new ServiceException(StatusCode.UNAUTHORIZED, "Invalid token signature");
                }

                // 验证发行者
                String issuer = (String) jwt.getPayload("iss");
                if (!properties.getJwtIssuer().equals(issuer)) {
                    throw new ServiceException(StatusCode.UNAUTHORIZED, "Invalid token issuer");
                }

                // 验证受众
                Object audience = jwt.getPayload("aud");
                if (audience instanceof String) {
                    if (!properties.getJwtAudience().equals(audience)) {
                        throw new ServiceException(StatusCode.UNAUTHORIZED, "Invalid token audience");
                    }
                } else if (audience instanceof List) {
                    if (!((List<?>) audience).contains(properties.getJwtAudience())) {
                        throw new ServiceException(StatusCode.UNAUTHORIZED, "Invalid token audience");
                    }
                }

                // 构建用户上下文
                return buildUserContext(jwt);
            } catch (Exception e) {
                log.debug("JWT validation failed: {}", e.getMessage());
                throw new ServiceException(StatusCode.UNAUTHORIZED, "Invalid token");
            }
        });
    }

    private UserContext buildUserContext(JWT jwt) {
        JSONObject payload = JSONUtil.parseObj(jwt.getPayload());

        UserContext context = new UserContext();
        context.setToken((String) payload.get("jti"));
        context.setUserId(payload.getStr("sub"));
        context.setAccount(payload.getStr("preferred_username"));
        context.setRoleId(payload.getLong("role_id"));
        context.setTenantId(payload.getLong("tenant_id"));
        context.setDepartmentId(payload.getLong("dept_id"));
        context.setPhone(payload.getStr("phone"));

        // 解析角色列表
        Object roles = payload.get("roles");
        if (roles instanceof List) {
            Set<Serializable> roleIds = ((List<?>) roles).stream()
                .map(Object::toString)
                .map(Long::parseLong)
                .collect(Collectors.toSet());
            context.setRoleIds(roleIds);
        }

        // 解析部门列表
        Object depts = payload.get("depts");
        if (depts instanceof List) {
            Set<Serializable> deptIds = ((List<?>) depts).stream()
                .map(Object::toString)
                .map(Long::parseLong)
                .collect(Collectors.toSet());
            context.setDepartmentIds(deptIds);
        }

        return context;
    }

    @Override
    public OAuth2GatewayProperties.TokenType getType() {
        return OAuth2GatewayProperties.TokenType.JWT;
    }
}
