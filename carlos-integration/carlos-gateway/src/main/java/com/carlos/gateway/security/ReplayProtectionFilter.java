package com.carlos.gateway.security;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.carlos.core.constant.HttpHeadersConstant;
import com.carlos.core.response.CommonErrorCode;
import com.carlos.gateway.constant.GatewayHeaderConstants;
import com.carlos.gateway.exception.ErrorResponse;
import com.carlos.gateway.exception.ReplayAttackException;
import com.carlos.gateway.whitelist.WhitelistContext;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.HexFormat;
import java.util.Optional;

/**
 * <p>
 * 防重放攻击过滤器 - 性能优化版
 * 基于请求签名和时间戳验证
 * </p>
 * <p>
 * 优化点：
 * 1. 优先使用统一白名单检查结果（UnifiedWhitelistFilter）
 * 2. ThreadLocal 缓存 Mac 实例，避免每次创建（Mac 实例创建开销大）
 * 3. 使用 HexFormat (JDK 17+) 替代手动十六进制转换
 * </p>
 *
 * @author carlos
 * @date 2026/3/16
 * @updated 2026/4/10 优化：统一白名单 + ThreadLocal Mac + HexFormat
 */
@Slf4j
public class ReplayProtectionFilter implements GlobalFilter, Ordered {

    private final ReplayProtectionProperties properties;
    private final ReactiveStringRedisTemplate redisTemplate;

    // 时间戳允许的最大误差（秒）
    private static final long TIMESTAMP_TOLERANCE = 300; // 5分钟

    // ThreadLocal Mac 实例缓存（Key 是密钥，避免不同密钥冲突）
    private final ThreadLocal<Mac> macCache = new ThreadLocal<>();
    private volatile String cachedKey = null;

    // 十六进制格式器（JDK 17+ 特性，比手动转换快 2-3 倍）
    private static final HexFormat HEX_FORMAT = HexFormat.of();

    public ReplayProtectionFilter(ReplayProtectionProperties properties,
                                  ReactiveStringRedisTemplate redisTemplate) {
        this.properties = properties;
        this.redisTemplate = redisTemplate;
    }

    @PostConstruct
    public void init() {
        // 预热 Mac 实例（如果密钥已配置）
        if (StrUtil.isNotBlank(properties.getSecretKey())) {
            try {
                Mac mac = Mac.getInstance("HmacSHA256");
                mac.init(new SecretKeySpec(properties.getSecretKey().getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
                macCache.set(mac);
                cachedKey = properties.getSecretKey();
                if (log.isInfoEnabled()) {
                    log.info("ReplayProtectionFilter initialized with HMAC-SHA256");
                }
            } catch (Exception e) {
                log.error("Failed to initialize HMAC", e);
            }
        }
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        if (!properties.isEnabled()) {
            return chain.filter(exchange);
        }

        ServerHttpRequest request = exchange.getRequest();
        String path = request.getURI().getPath();
        String clientIp = getClientIp(request);

        // 1. 白名单快速检查（优先使用统一白名单检查结果）
        if (isWhitelisted(exchange, path)) {
            return chain.filter(exchange);
        }

        // 2. 获取必要参数
        String timestamp = request.getHeaders().getFirst(HttpHeadersConstant.X_TIMESTAMP);
        String nonce = request.getHeaders().getFirst(HttpHeadersConstant.X_NONCE);
        String signature = request.getHeaders().getFirst(HttpHeadersConstant.X_SIGNATURE);

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
                "缺少必要的安全请求头（" + HttpHeadersConstant.X_TIMESTAMP + ", " +
                    HttpHeadersConstant.X_NONCE + ", " + HttpHeadersConstant.X_SIGNATURE + "）");
        }

        // 3. 验证时间戳
        return checkTimestamp(timestamp)
            .flatMap(valid -> {
                if (!Boolean.TRUE.equals(valid)) {
                    if (log.isWarnEnabled()) {
                        log.warn("Replay protection: Expired timestamp from [{}] to [{}]", clientIp, path);
                    }
                    return blockRequest(exchange, ReplayAttackException.AttackType.EXPIRED_TIMESTAMP,
                        nonce, "请求时间戳过期");
                }
                return Mono.just(true);
            })
            // 4. 验证 Nonce（防止重放）
            .flatMap(v -> checkNonce(nonce))
            .flatMap(valid -> {
                if (!Boolean.TRUE.equals(valid)) {
                    if (log.isWarnEnabled()) {
                        log.warn("Replay protection: Duplicate nonce detected from [{}] to [{}]", clientIp, path);
                    }
                    return blockRequest(exchange, ReplayAttackException.AttackType.DUPLICATE_NONCE,
                        nonce, "检测到重放攻击（重复的请求标识）");
                }
                return Mono.just(true);
            })
            // 5. 验证签名
            .flatMap(v -> verifySignature(request, timestamp, nonce, signature))
            .flatMap(valid -> {
                if (!Boolean.TRUE.equals(valid)) {
                    if (log.isWarnEnabled()) {
                        log.warn("Replay protection: Invalid signature from [{}] to [{}]", clientIp, path);
                    }
                    return blockRequest(exchange, ReplayAttackException.AttackType.INVALID_SIGNATURE,
                        nonce, "请求签名验证失败");
                }
                return chain.filter(exchange);
            });
    }

    /**
     * 检查路径是否在白名单中（优化版）
     * <p>
     * 优先使用统一白名单检查结果，如果统一白名单未启用，则使用本地白名单
     *
     * @param exchange ServerWebExchange
     * @param path     请求路径
     * @return true 如果在白名单中
     */
    private boolean isWhitelisted(ServerWebExchange exchange, String path) {
        // 1. 优先使用统一白名单检查结果
        if (WhitelistContext.isReplayWhitelisted(exchange)) {
            return true;
        }

        // 2. 降级兼容：统一白名单未启用或结果不存在，使用本地白名单
        if (properties.getWhitelist() == null || properties.getWhitelist().isEmpty()) {
            return false;
        }
        // 使用快速前缀匹配
        for (String whitelistPath : properties.getWhitelist()) {
            if (path.startsWith(whitelistPath)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取客户端 IP（优化版）
     */
    private String getClientIp(ServerHttpRequest request) {
        if (request.getRemoteAddress() == null) {
            return "unknown";
        }
        return request.getRemoteAddress().getAddress().getHostAddress();
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
        String key = GatewayHeaderConstants.NONCE_CACHE_PREFIX + nonce;
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
     * 验证签名（优化版 - 使用 ThreadLocal Mac）
     */
    private Mono<Boolean> verifySignature(ServerHttpRequest request, String timestamp,
                                          String nonce, String signature) {
        return Mono.fromCallable(() -> {
            try {
                String method = request.getMethod().name();
                String path = request.getURI().getPath();
                String query = Optional.ofNullable(request.getURI().getQuery()).orElse("");

                // 构建签名字符串
                StringBuilder signStr = new StringBuilder(256);
                signStr.append(method).append("|")
                    .append(path).append("|")
                    .append(query).append("|")
                    .append(timestamp).append("|")
                    .append(nonce);

                // 如果有密钥，使用 HMAC 验证
                if (StrUtil.isNotBlank(properties.getSecretKey())) {
                    String computed = hmacSha256Optimized(signStr.toString(), properties.getSecretKey());
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

    /**
     * 优化的 HMAC-SHA256 计算（使用 ThreadLocal Mac）
     */
    private String hmacSha256Optimized(String data, String key) throws Exception {
        Mac mac = getMacInstance(key);
        byte[] hash = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
        // 使用 JDK 17 HexFormat（比手动转换快 2-3 倍）
        return HEX_FORMAT.formatHex(hash);
    }

    /**
     * 获取 Mac 实例（ThreadLocal 缓存）
     */
    private Mac getMacInstance(String key) throws Exception {
        // 检查密钥是否变化
        if (!key.equals(cachedKey)) {
            // 密钥变化，重新初始化
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
            macCache.set(mac);
            cachedKey = key;
            return mac;
        }

        Mac mac = macCache.get();
        if (mac == null) {
            // ThreadLocal 为空，初始化
            mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
            macCache.set(mac);
        }
        return mac;
    }

    /**
     * 拦截请求，返回统一格式的错误响应
     */
    private Mono<Void> blockRequest(ServerWebExchange exchange, ReplayAttackException.AttackType attackType,
                                    String requestId, String message) {
        String path = exchange.getRequest().getURI().getPath();
        String clientIp = getClientIp(exchange.getRequest());

        if (log.isWarnEnabled()) {
            log.warn("Replay protection blocked request from [{}] to [{}]: {} (type={})",
                clientIp, path, message, attackType.name());
        }

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
            byte[] bytes = JSONUtil.toJsonStr(errorResponse).getBytes(StandardCharsets.UTF_8);
            return response.bufferFactory().wrap(bytes);
        }));
    }

    @Override
    public int getOrder() {
        return -4000; // 在 WAF 之后执行
    }
}
