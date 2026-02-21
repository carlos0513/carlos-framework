// package com.carlos.gateway.resource;
//
// import cn.hutool.json.JSONUtil;
// import com.carlos.core.response.Result;
// import com.carlos.core.response.StatusCode;
// import lombok.extern.slf4j.Slf4j;
// import org.springframework.core.io.buffer.DataBuffer;
// import org.springframework.http.HttpHeaders;
// import org.springframework.http.HttpStatus;
// import org.springframework.http.MediaType;
// import org.springframework.http.server.reactive.ServerHttpResponse;
// import org.springframework.security.core.AuthenticationException;
// import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
// import org.springframework.web.server.ServerWebExchange;
// import reactor.core.publisher.Mono;
//
// import java.nio.charset.StandardCharsets;
//
// /**
//  * <p>
//  * 自定义返回结果：没有登录或token过期时
//  * </p>
//  *
//  * @author yunjin
//  * @date 2021/11/4 10:04
//  */
// @Slf4j
// public class RestAuthenticationEntryPoint implements ServerAuthenticationEntryPoint {
//
//
//     @Override
//     public Mono<Void> commence(ServerWebExchange exchange, AuthenticationException e) {
//         ServerHttpResponse response = exchange.getResponse();
//         response.setStatusCode(HttpStatus.OK);
//         response.getHeaders().set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
//         response.getHeaders().set("Access-Control-Allow-Origin", "*");
//         response.getHeaders().set("Cache-Control", "no-cache");
//         String body = JSONUtil.toJsonStr(Result.fail(StatusCode.UNAUTHORIZED, e.getMessage()));
//         DataBuffer buffer = response.bufferFactory().wrap(body.getBytes(StandardCharsets.UTF_8));
//         if (log.isDebugEnabled()) {
//             log.debug("请求认证不通过:{}", exchange.getRequest().getURI());
//         }
//         return response.writeWith(Mono.just(buffer));
//     }
// }
