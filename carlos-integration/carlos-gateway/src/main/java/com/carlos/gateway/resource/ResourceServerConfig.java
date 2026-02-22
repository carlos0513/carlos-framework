// package com.carlos.gateway.resource;
//
// import com.carlos.core.auth.AuthConstant;
// import com.carlos.gateway.auth.GatewayAuthConfig;
// import com.carlos.gateway.auth.GatewayAuthProperties;
// import com.carlos.gateway.auth.RemoveJwtFilter;
// import lombok.AllArgsConstructor;
// import org.springframework.boot.autoconfigure.AutoConfigureAfter;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.core.convert.converter.Converter;
// import org.springframework.security.authentication.AbstractAuthenticationToken;
// import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
// import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
// import org.springframework.security.config.web.server.ServerHttpSecurity;
// import org.springframework.security.oauth2.jwt.Jwt;
// import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
// import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
// import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverterAdapter;
// import org.springframework.security.web.server.SecurityWebFilterChain;
// import reactor.core.publisher.Mono;
//
// /**
//  * <p>
//  * 资源服务器配置
//  * </p>
//  *
//  * @author yunjin
//  * @date 2021/11/3 18:10
//  */
// @Configuration
// @EnableWebFluxSecurity
// @AutoConfigureAfter(GatewayAuthConfig.class)
// @AllArgsConstructor
// public class ResourceServerConfig {
//
//     private final RemoveJwtFilter removeJwtFilter;
//
//     private final GatewayAuthProperties authProperties;
//
//
//     // 客户端认证通道
//     // @Order(1)
//     // @Bean
//     // SecurityWebFilterChain clientFilterChain(ServerHttpSecurity http) {
//     //     http.securityMatcher(pathMatchers("/internal/**"))
//     //             .authorizeExchange(ex -> ex.anyExchange().hasAuthority("SERVICE_ACCESS"));
//     //     return http.build();
//     // }
//     //
//     // // 用户认证通道
//     // @Order(2)
//     // @Bean
//     // SecurityWebFilterChain userFilterChain(ServerHttpSecurity http) {
//     //     http.authorizeExchange(ex -> ex.anyExchange().authenticated());
//     //     return http.build();
//     // }
//
//     /**
//      * 用户认证过滤器配置
//      */
//     @Bean
//     public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
//         http.oauth2ResourceServer().jwt().jwtAuthenticationConverter(jwtAuthenticationConverter());
//         //自定义处理JWT请求头过期或签名错误的结果
//         http.oauth2ResourceServer().authenticationEntryPoint(authenticationEntryPoint());
//         //对白名单路径，直接移除JWT请求头
//         http.addFilterBefore(removeJwtFilter, SecurityWebFiltersOrder.AUTHENTICATION);
//         http.authorizeExchange()
//                 //白名单配置
//                 .pathMatchers(authProperties.getWhitelist().toArray(new String[0])).permitAll()
//
//                 //鉴权管理器配置
//                 .anyExchange().access(authorizationManager())
//                 .and()
//                 .exceptionHandling()
//                 //处理未授权
//                 .accessDeniedHandler(accessDeniedHandler())
//                 //处理未认证
//                 .authenticationEntryPoint(authenticationEntryPoint())
//                 .and().csrf().disable();
//         // http.oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()));
//         return http.build();
//     }
//
//
//     /**
//      * 当token采用jwt时，需要将jwt转换为spring security的认证信息
//      * */
//     @Bean
//     public Converter<Jwt, ? extends Mono<? extends AbstractAuthenticationToken>> jwtAuthenticationConverter() {
//         JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
//         jwtGrantedAuthoritiesConverter.setAuthorityPrefix(AuthConstant.AUTHORITY_PREFIX);
//         jwtGrantedAuthoritiesConverter.setAuthoritiesClaimName(AuthConstant.AUTHORITY_CLAIM_NAME);
//         JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
//         jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);
//         return new ReactiveJwtAuthenticationConverterAdapter(jwtAuthenticationConverter);
//     }
//
//
//     /**
//      * 注册鉴权管理器
//      */
//     @Bean
//     public AuthorizationManager authorizationManager() {
//         return new AuthorizationManager(authProperties);
//     }
//
//     /**
//      * 认证失败处理器
//      */
//     @Bean
//     public RestAuthenticationEntryPoint authenticationEntryPoint() {
//         return new RestAuthenticationEntryPoint();
//     }
//
//     /**
//      * 鉴权失败处理器
//      */
//     @Bean
//     public RestfulAccessDeniedHandler accessDeniedHandler() {
//         return new RestfulAccessDeniedHandler();
//     }
//
// }
