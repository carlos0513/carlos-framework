// package com.carlos.gateway.resource;
//
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.cache.CacheManager;
// import org.springframework.cloud.gateway.filter.GatewayFilterChain;
// import org.springframework.cloud.gateway.filter.GlobalFilter;
// import org.springframework.http.HttpStatus;
// import org.springframework.stereotype.Component;
// import org.springframework.web.server.ServerWebExchange;
// import reactor.core.publisher.Mono;
//
// @Component
// public class CustomTokenFilter implements GlobalFilter {
//     @Autowired
//     private AuthService authService; // 认证服务接口
//     @Autowired
//     private CacheManager cacheManager; // 缓存管理器
//
//     @Override
//     public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
//         // 1. 从Header提取Token
//         String token = exchange.getRequest().getHeaders().getFirst("Authorization");
//         if (StringUtils.isEmpty(token)) {
//             return unauthorizedResponse(exchange, "Missing token");
//         }
//
//         // 2. 校验Token有效性
//         return cacheManager.getCache("tokenCache").get(token, String.class)
//                 .switchIfEmpty(Mono.defer(() -> authService.validateToken(token)))
//                 .flatMap(clientInfo -> {
//                     // 3. 注入客户端信息到下游
//                     exchange.getRequest().mutate()
//                             .header("X-Client-ID", clientInfo.getId())
//                             .header("X-Client-Scope", clientInfo.getScopes());
//                     return chain.filter(exchange);
//                 })
//                 .onErrorResume(e -> unauthorizedResponse(exchange, "Invalid token"));
//     }
//
//     private Mono<Void> unauthorizedResponse(ServerWebExchange exchange, String msg) {
//         exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
//         return exchange.getResponse().writeWith(Mono.just(
//                 exchange.getResponse().bufferFactory().wrap(msg.getBytes())));
//     }
// }