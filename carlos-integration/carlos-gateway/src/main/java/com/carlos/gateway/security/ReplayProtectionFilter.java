package com.carlos.gateway.security;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Optional;

/**
 * <p>
 * 防重放攻击过滤器
 * 基于请求签名和时间戳验证
 * </p>
 *
 * @author carlos
 * @date 2026/3/16
 */
@Slf4j
@Component
public class ReplayProtectionFilter implements GlobalFilter, Ordered {

    private final ReplayProtectionProperties properties;
    private final ReactiveStringRedisTemplate redisTemplate;

    // 时间戳允许的最大误差（秒）
    private static final long TIMESTAMP_TOLERANCE = 300; // 5分钟

    public ReplayProtectionFilter(ReplayProtectionProperties properties,
                                  ReactiveStringRedisTemplate redisTemplate) {
        this.properties = properties;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        if (!properties.isEnabled()) {
            return chain.filter(exchange);
        }

        ServerHttpRequest request = exchange.getRequest();

        // 1. 白名单检查
        String path = request.getURI().getPath();
        if (properties.getWhitelist() != null &&
            properties.getWhitelist().stream().anyMatch(path::startsWith)) {
            return chain.filter(exchange);
        }

        // 2. 获取必要参数
        String timestamp = request.getHeaders().getFirst("X-Timestamp");
        String nonce = request.getHeaders().getFirst("X-Nonce");
        String signature = request.getHeaders().getFirst("X-Signature");

        // 如果是 GET 请求且不需要签名验证，可以只检查时间戳
        if (request.getMethod() == org.springframework.http.HttpMethod.GET &&
            !properties.isStrictMode()) {
            if (timestamp == null) {
                return chain.filter(exchange);
            }
            return checkTimestamp(timestamp)
                .flatMap(valid -> {
                    if (Boolean.TRUE.equals(valid)) {
                        return chain.filter(exchange);
                    }
                    return blockRequest(exchange, "Request expired or timestamp invalid");
                });
        }

        // 严格模式：必须包含所有参数
        if (StrUtil.isBlank(timestamp) || StrUtil.isBlank(nonce) || StrUtil.isBlank(signature)) {
            return blockRequest(exchange, "Missing required security headers");
        }

        // 3. 验证时间戳
        return checkTimestamp(timestamp)
            .flatMap(valid -> {
                if (!Boolean.TRUE.equals(valid)) {
                    return blockRequest(exchange, "Request expired or timestamp invalid");
                }
                return Mono.just(true);
            })
            // 4. 验证 Nonce（防止重放）
            .flatMap(v -> checkNonce(nonce))
            .flatMap(valid -> {
                if (!Boolean.TRUE.equals(valid)) {
                    return blockRequest(exchange, "Request replay detected");
                }
                return Mono.just(true);
            })
            // 5. 验证签名
            .flatMap(v -> verifySignature(request, timestamp, nonce, signature))
            .flatMap(valid -> {
                if (!Boolean.TRUE.equals(valid)) {
                    return blockRequest(exchange, "Invalid signature");
                }
                return chain.filter(exchange);
            });
    }

    /**
     * 检查时间戳
     */
    private Mono<Boolean> checkTimestamp(String timestamp) {
        return Mono.fromCallable(() -> {
            try {
                long ts = Long.parseLong(timestamp);
                long now = System.currentTimeMillis() / 1000;
                return Math.abs(now - ts) <= TIMESTAMP_TOLERANCE;
            } catch (NumberFormatException e) {
                return false;
            }
        });
    }

    /**
     * 检查 Nonce（确保唯一性）
     */
    private Mono<Boolean> checkNonce(String nonce) {
        String key = "nonce:" + nonce;
        return redisTemplate.hasKey(key)
            .flatMap(exists -> {
                if (Boolean.TRUE.equals(exists)) {
                    // Nonce 已存在，可能是重放攻击
                    return Mono.just(false);
                }
                // 存储 Nonce，设置过期时间
                return redisTemplate.opsForValue()
                    .set(key, "1", Duration.ofSeconds(TIMESTAMP_TOLERANCE * 2))
                    .thenReturn(true);
            });
    }

    /**
     * 验证签名
     * 签名算法：HMAC_SHA256(method + path + timestamp + nonce + body)
     */
    private Mono<Boolean> verifySignature(ServerHttpRequest request, String timestamp,
                                          String nonce, String signature) {
        return Mono.fromCallable(() -> {
            try {
                String method = request.getMethod().name();
                String path = request.getURI().getPath();
                String query = Optional.ofNullable(request.getURI().getQuery()).orElse("");

                // 构建签名字符串
                StringBuilder signStr = new StringBuilder();
                signStr.append(method).append("|")
                    .append(path).append("|")
                    .append(query).append("|")
                    .append(timestamp).append("|")
                    .append(nonce);

                // 如果有密钥，使用 HMAC 验证
                if (StrUtil.isNotBlank(properties.getSecretKey())) {
                    String computed = hmacSha256(signStr.toString(), properties.getSecretKey());
                    return computed.equalsIgnoreCase(signature);
                }

                // 否则只检查时间戳和 Nonce（弱验证）
                return true;
            } catch (Exception e) {
                log.error("Signature verification failed", e);
                return false;
            }
        });
    }

    private String hmacSha256(String data, String key) throws Exception {
        javax.crypto.Mac mac = javax.crypto.Mac.getInstance("HmacSHA256");
        mac.init(new javax.crypto.spec.SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
        byte[] hash = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

    /**
     * 拦截请求
     */
    private Mono<Void> blockRequest(ServerWebExchange exchange, String message) {
        log.warn("Replay protection blocked request: {}", message);
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.FORBIDDEN);
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

        String body = String.format("{\"code\":403,\"message\":\"%s\"}", message);
        return response.writeWith(Mono.just(response.bufferFactory().wrap(body.getBytes())));
    }

    @Override
    public int getOrder() {
        return -4000; // 在 WAF 之后执行
    }
}
