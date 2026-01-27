package com.carlos.oauth2.config;

import com.carlos.oauth2.constant.OAuth2Constant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * OAuth2资源服务器配置
 *
 * @author yunjin
 * @date 2026-01-25
 */
@Slf4j
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
@EnableConfigurationProperties(OAuth2Properties.class)
@ConditionalOnProperty(prefix = "carlos.oauth2.resource-server", name = "enabled", havingValue = "true")
public class OAuth2ResourceServerConfig {

    private final OAuth2Properties oAuth2Properties;

    /**
     * 资源服务器安全过滤器链
     */
    @Bean
    public SecurityFilterChain resourceServerSecurityFilterChain(HttpSecurity http) throws Exception {
        OAuth2Properties.ResourceServer resourceServer = oAuth2Properties.getResourceServer();

        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(authorize -> {
                    // 配置不需要认证的路径
                    if (resourceServer.getPermitAllPaths() != null && !resourceServer.getPermitAllPaths().isEmpty()) {
                        authorize.requestMatchers(resourceServer.getPermitAllPaths().toArray(new String[0])).permitAll();
                    }

                    // 默认放行的路径
                    authorize
                            .requestMatchers(
                                    "/error",
                                    "/actuator/**",
                                    "/v3/api-docs/**",
                                    "/swagger-ui/**",
                                    "/swagger-ui.html",
                                    "/doc.html",
                                    "/webjars/**"
                            ).permitAll()
                            .anyRequest().authenticated();
                })
                .oauth2ResourceServer(oauth2 ->
                        oauth2.jwt(jwt ->
                                jwt.jwtAuthenticationConverter(jwtAuthenticationConverter())
                        )
                );

        return http.build();
    }

    /**
     * JWT解码器（资源服务器）
     */
    @Bean
    public JwtDecoder jwtDecoder() {
        OAuth2Properties.ResourceServer resourceServer = oAuth2Properties.getResourceServer();

        if (resourceServer.getJwkSetUri() != null) {
            log.info("Configuring JWT decoder with JWK Set URI: {}", resourceServer.getJwkSetUri());
            return NimbusJwtDecoder.withJwkSetUri(resourceServer.getJwkSetUri()).build();
        } else if (resourceServer.getIssuerUri() != null) {
            log.info("Configuring JWT decoder with Issuer URI: {}", resourceServer.getIssuerUri());
            return NimbusJwtDecoder.withIssuerLocation(resourceServer.getIssuerUri()).build();
        } else {
            throw new IllegalStateException(
                    "Either yunjin.oauth2.resource-server.jwk-set-uri or " +
                            "yunjin.oauth2.resource-server.issuer-uri must be configured"
            );
        }
    }

    /**
     * JWT认证转换器
     * 从JWT中提取权限信息
     */
    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter());
        return converter;
    }

    /**
     * JWT权限转换器
     * 从JWT Claims中提取权限和角色信息
     */
    private Converter<Jwt, Collection<GrantedAuthority>> jwtGrantedAuthoritiesConverter() {
        return jwt -> {
            Collection<GrantedAuthority> authorities = new ArrayList<>();

            // 从scope中提取权限
            JwtGrantedAuthoritiesConverter scopeConverter = new JwtGrantedAuthoritiesConverter();
            authorities.addAll(scopeConverter.convert(jwt));

            // 从自定义的authorities字段中提取权限
            String authoritiesStr = jwt.getClaimAsString(OAuth2Constant.Claims.AUTHORITIES);
            if (authoritiesStr != null && !authoritiesStr.isEmpty()) {
                String[] authArray = authoritiesStr.split(",");
                for (String auth : authArray) {
                    if (auth != null && !auth.trim().isEmpty()) {
                        authorities.add(new SimpleGrantedAuthority(auth.trim()));
                    }
                }
            }

            // 从role_ids中提取角色权限
            List<Long> roleIds = jwt.getClaim(OAuth2Constant.Claims.ROLE_IDS);
            if (roleIds != null && !roleIds.isEmpty()) {
                for (Long roleId : roleIds) {
                    authorities.add(new SimpleGrantedAuthority("ROLE_" + roleId));
                }
            }

            log.debug("Extracted authorities from JWT: {}", authorities);
            return authorities;
        };
    }
}
