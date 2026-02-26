package com.carlos.auth.oauth2.customize;

import cn.hutool.extra.spring.SpringUtil;
import com.carlos.redis.util.RedisUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.lang.Nullable;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationCode;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.util.Assert;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;


/**
 * <p>
 * 自定义授权服务 自定义token存储 一、核心作用 OAuth2AuthorizationService 是 Spring Security OAuth2 授权服务器的核心接口，用于管理 OAuth2 授权的全生命周期，主要功能包括：
 * 授权信息存储与检索 保存和获取 OAuth2 授权相关数据，例如： 授权码（Authorization Code） 访问令牌（Access Token） 刷新令牌（Refresh Token） 关联的权限范围（Scope）和用户信息
 * 15。 令牌生命周期管理 跟踪令牌状态（如是否已过期、是否被撤销），并支持动态更新或删除令牌 15。 分布式一致性支持 在微服务场景下，确保多个授权服务器实例间的令牌状态同步（需配合 Redis 或数据库实现）2。
 * </p>
 *
 * @author carlos
 * @date 2022/11/4 14:35
 */
public class CustomizeOAuth2AuthorizationService implements OAuth2AuthorizationService {

    private final static Long TIMEOUT = 10L;

    private static final String AUTHORIZATION = "oauth2";


    @Override
    public void save(OAuth2Authorization authorization) {
        Assert.notNull(authorization, "authorization cannot be null");
        ObjectMapper mapper = SpringUtil.getBean(ObjectMapper.class);
        mapper.addMixIn(AuthorizationGrantType.class, AuthorizationGrantTypeMixin.class);
        String json = null;
        try {
            json = mapper.writeValueAsString(authorization);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }


        if (isState(authorization)) {
            String token = authorization.getAttribute("state");
            RedisUtil.setValue(buildKey(OAuth2ParameterNames.STATE, token), json, TIMEOUT,
                    TimeUnit.MINUTES);
        }

        if (isCode(authorization)) {
            OAuth2Authorization.Token<OAuth2AuthorizationCode> authorizationCode = authorization
                    .getToken(OAuth2AuthorizationCode.class);
            OAuth2AuthorizationCode authorizationCodeToken = authorizationCode.getToken();
            long between = ChronoUnit.MINUTES.between(authorizationCodeToken.getIssuedAt(),
                    authorizationCodeToken.getExpiresAt());
            RedisUtil.setValue(buildKey(OAuth2ParameterNames.CODE, authorizationCodeToken.getTokenValue()),
                    json, between, TimeUnit.MINUTES);
        }

        if (isRefreshToken(authorization)) {
            OAuth2RefreshToken refreshToken = authorization.getRefreshToken().getToken();
            long between = ChronoUnit.SECONDS.between(refreshToken.getIssuedAt(), refreshToken.getExpiresAt());
            RedisUtil.setValue(buildKey(OAuth2ParameterNames.REFRESH_TOKEN, refreshToken.getTokenValue()),
                    json, between, TimeUnit.SECONDS);
        }

        if (isAccessToken(authorization)) {
            OAuth2AccessToken accessToken = authorization.getAccessToken().getToken();
            long between = ChronoUnit.SECONDS.between(accessToken.getIssuedAt(), accessToken.getExpiresAt());
            RedisUtil.setValue(buildKey(OAuth2ParameterNames.ACCESS_TOKEN, accessToken.getTokenValue()),
                    json, between, TimeUnit.SECONDS);
        }
    }

    @Override
    public void remove(OAuth2Authorization authorization) {
        Assert.notNull(authorization, "authorization cannot be null");

        List<String> keys = new ArrayList<>();
        if (isState(authorization)) {
            String token = authorization.getAttribute("state");
            keys.add(buildKey(OAuth2ParameterNames.STATE, token));
        }

        if (isCode(authorization)) {
            OAuth2Authorization.Token<OAuth2AuthorizationCode> authorizationCode = authorization
                    .getToken(OAuth2AuthorizationCode.class);
            OAuth2AuthorizationCode authorizationCodeToken = authorizationCode.getToken();
            keys.add(buildKey(OAuth2ParameterNames.CODE, authorizationCodeToken.getTokenValue()));
        }

        if (isRefreshToken(authorization)) {
            OAuth2RefreshToken refreshToken = authorization.getRefreshToken().getToken();
            keys.add(buildKey(OAuth2ParameterNames.REFRESH_TOKEN, refreshToken.getTokenValue()));
        }

        if (isAccessToken(authorization)) {
            OAuth2AccessToken accessToken = authorization.getAccessToken().getToken();
            keys.add(buildKey(OAuth2ParameterNames.ACCESS_TOKEN, accessToken.getTokenValue()));
        }
        RedisUtil.delete(keys);
    }

    @Override
    @Nullable
    public OAuth2Authorization findById(String id) {
        throw new UnsupportedOperationException();
    }

    @Override
    @Nullable
    public OAuth2Authorization findByToken(String token, @Nullable OAuth2TokenType tokenType) {
        Assert.hasText(token, "token cannot be empty");
        Assert.notNull(tokenType, "tokenType cannot be empty");
        String jsonToken = RedisUtil.getValue(buildKey(tokenType.getValue(), token));
        ObjectMapper objectMapper = SpringUtil.getBean(ObjectMapper.class);
        objectMapper.addMixIn(AuthorizationGrantType.class, AuthorizationGrantTypeMixin.class);
        try {
            return objectMapper.readValue(jsonToken, OAuth2Authorization.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private String buildKey(String type, String id) {
        return String.format("%s:%s:%s", AUTHORIZATION, type, id);
    }

    private static boolean isState(OAuth2Authorization authorization) {
        return Objects.nonNull(authorization.getAttribute("state"));
    }

    private static boolean isCode(OAuth2Authorization authorization) {
        OAuth2Authorization.Token<OAuth2AuthorizationCode> authorizationCode = authorization
                .getToken(OAuth2AuthorizationCode.class);
        return Objects.nonNull(authorizationCode);
    }

    private static boolean isRefreshToken(OAuth2Authorization authorization) {
        return Objects.nonNull(authorization.getRefreshToken());
    }

    private static boolean isAccessToken(OAuth2Authorization authorization) {
        return Objects.nonNull(authorization.getAccessToken());
    }
}
