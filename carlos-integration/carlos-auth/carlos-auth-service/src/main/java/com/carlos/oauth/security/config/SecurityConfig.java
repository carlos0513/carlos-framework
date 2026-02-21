package com.carlos.oauth.security.config;

import lombok.AllArgsConstructor;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

/**
 * <p>
 * 对认证服务的授权配置
 * </p>
 *
 * @author carlos
 * @date 2022/11/4 12:51
 */
@EnableWebSecurity(debug = true)
@AllArgsConstructor
public class SecurityConfig {

    /**
     * Spring Security的过滤器链，用于Spring Security的身份认证。 配置哪些地址不需要过滤 暴露静态资源
     */
    // @Bean
    // @Order(2)
    // SecurityFilterChain resources(HttpSecurity http) throws Exception {
    //     http
    //             .requestMatchers(matcher ->
    //                     matcher
    //                             .antMatchers(HttpMethod.OPTIONS, "/**")
    //                             .antMatchers(
    //                                     "/actuator/**",
    //                                     "/error",
    //                                     "/assets/**",
    //                                     "/swagger-ui.html",
    //                                     "/doc.html",
    //                                     "/favicon.ico",
    //                                     "/docs",
    //                                     "/swagger-resources/**",
    //                                     "/webjars/**",
    //                                     "/v2/api-docs-ext",
    //                                     "/v3/api-docs-ext",
    //                                     "/v2/api-docs",
    //                                     "/v3/api-docs",
    //                                     "/rsa/public",
    //                                     "/oauth2/token",
    //                                     "/oauth2/introspect",
    //                                     "/pwd/**",
    //                                     "/**"
    //                             ))
    //             .authorizeHttpRequests((authorize) -> authorize.anyRequest().permitAll())
    //             // 禁用 CSRF
    //             .csrf().disable()
    //             .httpBasic().disable()
    //             .requestCache().disable()
    //             .securityContext().disable()
    //             .sessionManagement().disable()
    //             .logout().disable();
    //     return http.build();
    // }


}
