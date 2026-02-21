// package com.carlos.gateway.resource;
//
//
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Service;
// import org.springframework.web.reactive.function.client.WebClient;
// import reactor.core.publisher.Mono;
//
// import java.time.Duration;
//
// // 示例实现（基于WebClient）
// @Service
// public class RemoteAuthServiceImpl implements AuthService {
//     @Autowired
//     private WebClient.Builder webClientBuilder;
//
//     @Override
//     public Mono<ClientInfo> validateToken(String token) {
//         return webClientBuilder.build()
//                 .post()
//                 .uri("http://auth-server/oauth/check_token")
//                 .header("Authorization", "Bearer " + token)
//                 .retrieve()
//                 .bodyToMono(ClientInfo.class)
//                 .cache(
//                         info -> Duration.ofMinutes(30),
//                         error -> Duration.ZERO,
//                         () -> Duration.ZERO
//                 ); // 启用响应缓存
//     }
// }