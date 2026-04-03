package com.carlos.auth.security;

import com.carlos.auth.provider.UserInfo;
import com.carlos.core.auth.LoginUserInfo;
import com.carlos.auth.service.ExtendUserDetailsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * JWT 令牌提供者
 *
 * <p>负责生成访问令牌和刷新令牌。</p>
 *
 * @author Carlos
 * @version 1.0.0
 * @since 2026-04-03
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    /**
     * JWT 编码器
     */
    private final JwtEncoder jwtEncoder;

    /**
     * 用户详情服务
     */
    private final ExtendUserDetailsService userDetailsService;

    /**
     * 访问令牌有效期（秒）- 2小时
     */
    private static final long ACCESS_TOKEN_EXPIRY_SECONDS = 7200;

    /**
     * 刷新令牌有效期（秒）- 7天
     */
    private static final long REFRESH_TOKEN_EXPIRY_SECONDS = 604800;

    /**
     * 令牌发行者
     */
    private static final String ISSUER = "carlos-auth-server";

    /**
     * 生成访问令牌
     *
     * @param authentication 认证信息
     * @param userInfo 用户信息
     * @return JWT 访问令牌
     */
    public String generateAccessToken(Authentication authentication, UserInfo userInfo) {
        Instant now = Instant.now();
        String username = authentication.getName();

        // 加载用户详细信息
        LoginUserInfo loginUserInfo = userDetailsService.loadLoginUserInfo(username);

        // 构建 JWT Claims
        JwtClaimsSet.Builder claimsBuilder = JwtClaimsSet.builder()
            .issuer(ISSUER)
            .issuedAt(now)
            .expiresAt(now.plus(ACCESS_TOKEN_EXPIRY_SECONDS, ChronoUnit.SECONDS))
            .subject(username)
            .id(UUID.randomUUID().toString())
            .claim("token_type", "access_token");

        // 添加用户信息
        if (userInfo != null) {
            claimsBuilder.claim("user_id", userInfo.getUserId());
            claimsBuilder.claim("username", userInfo.getUsername());
            
            if (userInfo.getTenantId() != null) {
                claimsBuilder.claim("tenant_id", userInfo.getTenantId());
            }
            if (userInfo.getDeptId() != null) {
                claimsBuilder.claim("dept_id", userInfo.getDeptId());
            }
        }

        // 添加 LoginUserInfo 中的详细信息
        if (loginUserInfo != null) {
            if (loginUserInfo.getId() != null) {
                claimsBuilder.claim("user_id", loginUserInfo.getId());
            }
            if (loginUserInfo.getClientId() != null) {
                claimsBuilder.claim("tenant_id", loginUserInfo.getClientId());
            }
            if (loginUserInfo.getDepartmentId() != null) {
                claimsBuilder.claim("dept_id", loginUserInfo.getDepartmentId());
            }
            if (loginUserInfo.getRoleIds() != null && !loginUserInfo.getRoleIds().isEmpty()) {
                claimsBuilder.claim("role_ids", loginUserInfo.getRoleIds());
            }
        }

        // 添加权限信息
        Set<String> authorities = authentication.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.toSet());
        
        if (!authorities.isEmpty()) {
            claimsBuilder.claim("authorities", String.join(",", authorities));
            claimsBuilder.claim("scope", String.join(" ", authorities));
        }

        JwtClaimsSet claims = claimsBuilder.build();

        // 编码生成 JWT
        String token = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
        
        log.debug("Generated access token for user: {} (expires in {} seconds)", 
            username, ACCESS_TOKEN_EXPIRY_SECONDS);

        return token;
    }

    /**
     * 生成刷新令牌
     *
     * @param authentication 认证信息
     * @return JWT 刷新令牌
     */
    public String generateRefreshToken(Authentication authentication) {
        Instant now = Instant.now();
        String username = authentication.getName();

        JwtClaimsSet claims = JwtClaimsSet.builder()
            .issuer(ISSUER)
            .issuedAt(now)
            .expiresAt(now.plus(REFRESH_TOKEN_EXPIRY_SECONDS, ChronoUnit.SECONDS))
            .subject(username)
            .id(UUID.randomUUID().toString())
            .claim("token_type", "refresh_token")
            .build();

        String token = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
        
        log.debug("Generated refresh token for user: {} (expires in {} seconds)", 
            username, REFRESH_TOKEN_EXPIRY_SECONDS);

        return token;
    }

    /**
     * 生成完整的令牌响应
     *
     * @param authentication 认证信息
     * @param userInfo 用户信息
     * @return 包含访问令牌和刷新令牌的响应
     */
    public TokenResponse generateTokens(Authentication authentication, UserInfo userInfo) {
        String accessToken = generateAccessToken(authentication, userInfo);
        String refreshToken = generateRefreshToken(authentication);

        return TokenResponse.builder()
            .accessToken(accessToken)
            .refreshToken(refreshToken)
            .tokenType("Bearer")
            .expiresIn(ACCESS_TOKEN_EXPIRY_SECONDS)
            .build();
    }

    /**
     * 令牌响应内部类
     */
    @lombok.Data
    @lombok.Builder
    @lombok.NoArgsConstructor
    @lombok.AllArgsConstructor
    public static class TokenResponse {
        private String accessToken;
        private String refreshToken;
        private String tokenType;
        private Long expiresIn;
    }
}
