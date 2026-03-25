package com.carlos.gateway.security;

import cn.hutool.core.util.StrUtil;
import com.carlos.core.response.CommonErrorCode;
import com.carlos.gateway.exception.ErrorResponse;
import com.carlos.gateway.exception.ReplayAttackException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
 * @updated 2026/3/24 优化异常处理，统一响应格式
 */
@Slf4j
@Component
public class ReplayProtectionFilter implements GlobalFilter, Ordered {

    private final ReplayProtectionProperties properties;
    private final ReactiveStringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;

    // 时间戳允许的最大误差（秒）
    private static final long TIMESTAMP_TOLERANCE = 300; // 5分钟

    public ReplayProtectionFilter(ReplayProtectionProperties properties,
                                  ReactiveStringRedisTemplate redisTemplate) {
        this.properties = properties;
        this.redisTemplate = redisTemplate;
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        if (!properties.isEnabled()) {
            return chain.filter(exchange);
        }

        ServerHttpRequest request = exchange.getRequest();
        String path = request.getURI().getPath();
        String clientIp = request.getRemoteAddress() != null
            ? request.getRemoteAddress().getAddress().getHostAddress()
            : "unknown";

        // 1. 白名单检查
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
                    return blockRequest(exchange, ReplayAttackException.AttackType.EXPIRED_TIMESTAMP,
                        nonce, "请求时间戳过期或无效");
                });
        }

        // 严格模式：必须包含所有参数
        if (StrUtil.isBlank(timestamp) || StrUtil.isBlank(nonce) || StrUtil.isBlank(signature)) {
            return blockRequest(exchange, ReplayAttackException.AttackType.UNKNOWN, nonce,
                "缺少必要的安全请求头（X-Timestamp, X-Nonce, X-Signature）");
        }

        // 3. 验证时间戳
        return checkTimestamp(timestamp)
            .flatMap(valid -> {
                if (!Boolean.TRUE.equals(valid)) {
                    log.warn("Replay protection: Expired timestamp from [{}] to [{}]", clientIp, path);
                    return blockRequest(exchange, ReplayAttackException.AttackType.EXPIRED_TIMESTAMP,
                        nonce, "请求时间戳过期");
                }
                return Mono.just(true);
            })
            // 4. 验证 Nonce（防止重放）
            .flatMap(v -> checkNonce(nonce))
            .flatMap(valid -> {
                if (!Boolean.TRUE.equals(valid)) {
                    log.warn("Replay protection: Duplicate nonce detected from [{}] to [{}]", clientIp, path);
                    return blockRequest(exchange, ReplayAttackException.AttackType.DUPLICATE_NONCE,
                        nonce, "检测到重放攻击（重复的请求标识）");
                }
                return Mono.just(true);
            })
            // 5. 验证签名
            .flatMap(v -> verifySignature(request, timestamp, nonce, signature))
            .flatMap(valid -> {
                if (!Boolean.TRUE.equals(valid)) {
                    log.warn("Replay protection: Invalid signature from [{}] to [{}]", clientIp, path);
                    return blockRequest(exchange, ReplayAttackException.AttackType.INVALID_SIGNATURE,
                        nonce, "请求签名验证失败");
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
                // 存储 Nonce，设置过期时间（2倍时间戳容差）
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
     * 拦截请求，返回统一格式的错误响应
     */
    private Mono<Void> blockRequest(ServerWebExchange exchange, ReplayAttackException.AttackType attackType,
                                    String requestId, String message) {
        String path = exchange.getRequest().getURI().getPath();
        String clientIp = exchange.getRequest().getRemoteAddress() != null
            ? exchange.getRequest().getRemoteAddress().getAddress().getHostAddress()
            : "unknown";

        log.warn("Replay protection blocked request from [{}] to [{}]: {} (type={})",
            clientIp, path, message, attackType.name());

        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.FORBIDDEN);
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

        // 构建标准化的错误响应
        ErrorResponse errorResponse = ErrorResponse.builder()
            .status(HttpStatus.FORBIDDEN.value())
            .code(CommonErrorCode.FORBIDDEN.getCode())
            .msg(message)
            .path(path)
            .method(exchange.getRequest().getMethod() != null ? exchange.getRequest().getMethod().name() : "UNKNOWN")
            .extra(java.util.Map.of(
                "attackType", attackType.name(),
                "requestId", requestId != null ? requestId : "unknown",
                "clientIp", clientIp
            ))
            .build();

        return response.writeWith(Mono.fromSupplier(() -> {
            try {
                byte[] bytes = objectMapper.writeValueAsBytes(errorResponse);
                return response.bufferFactory().wrap(bytes);
            } catch (Exception e) {
                log.error("Failed to serialize replay protection response", e);
                String fallback = String.format(
                    "{\"success\":false,\"status\":403,\"code\":54031,\"message\":\"%s\"}",
                    message);
                return response.bufferFactory().wrap(fallback.getBytes(StandardCharsets.UTF_8));
            }
        }));
    }

    @Override
    public int getOrder() {
        return -4000; // 在 WAF 之后执行
    }
}
