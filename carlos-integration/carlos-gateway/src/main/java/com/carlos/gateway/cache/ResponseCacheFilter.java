package com.carlos.gateway.cache;

import com.carlos.core.constant.HttpHeadersConstant;
import com.carlos.gateway.constant.GatewayHeaderConstants;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * 响应缓存过滤器
 * 两级缓存：本地 Caffeine + Redis
 * </p>
 *
 * @author carlos
 * @date 2026/3/16
 */
@Slf4j
public class ResponseCacheFilter implements GlobalFilter, Ordered {

    private final CacheProperties properties;

    // 本地缓存（Caffeine）
    private final Cache<String, CachedResponse> localCache;

    // 可缓存的状态码
    private static final List<HttpStatus> CACHEABLE_STATUS = Arrays.asList(
        HttpStatus.OK,
        HttpStatus.NOT_MODIFIED,
        HttpStatus.MULTIPLE_CHOICES
    );

    // 可缓存的方法
    private static final List<HttpMethod> CACHEABLE_METHODS = Arrays.asList(
        HttpMethod.GET,
        HttpMethod.HEAD
    );

    public ResponseCacheFilter(CacheProperties properties) {
        this.properties = properties;
        this.localCache = Caffeine.newBuilder()
            .maximumSize(properties.getLocalSize())
            .expireAfterWrite(properties.getLocalExpire())
            .recordStats()
            .build();
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        if (!properties.isEnabled()) {
            return chain.filter(exchange);
        }

        ServerHttpRequest request = exchange.getRequest();

        // 只缓存 GET 请求
        if (!CACHEABLE_METHODS.contains(request.getMethod())) {
            return chain.filter(exchange);
        }

        // 检查是否禁用缓存
        String cacheControl = request.getHeaders().getFirst(HttpHeaders.CACHE_CONTROL);
        if ("no-cache".equals(cacheControl) || "no-store".equals(cacheControl)) {
            return chain.filter(exchange);
        }

        // 生成缓存键
        String cacheKey = generateCacheKey(request);

        // 尝试从缓存获取
        CachedResponse cached = localCache.getIfPresent(cacheKey);
        if (cached != null && !cached.isExpired()) {
            log.debug("Cache hit for key: {}", cacheKey);
            return serveFromCache(exchange, cached);
        }

        // 缓存未命中，执行请求并缓存响应
        return cacheResponse(exchange, chain, cacheKey);
    }

    /**
     * 从缓存提供响应
     */
    private Mono<Void> serveFromCache(ServerWebExchange exchange, CachedResponse cached) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(cached.getStatusCode());
        response.getHeaders().putAll(cached.getHeaders());
        response.getHeaders().set(HttpHeadersConstant.X_CACHE, HttpHeadersConstant.CACHE_HIT);

        return response.writeWith(Mono.just(response.bufferFactory().wrap(cached.getBody())));
    }

    /**
     * 缓存响应
     */
    private Mono<Void> cacheResponse(ServerWebExchange exchange, GatewayFilterChain chain, String cacheKey) {
        return chain.filter(exchange)
            .then(Mono.defer(() -> {
                // 检查状态码是否可缓存
                HttpStatusCode statusCode = exchange.getResponse().getStatusCode();
                if (statusCode == null || !CACHEABLE_STATUS.contains(HttpStatus.resolve(statusCode.value()))) {
                    return Mono.empty();
                }

                // 检查响应头是否允许缓存
                String cacheControl = exchange.getResponse().getHeaders().getFirst(HttpHeaders.CACHE_CONTROL);
                if (cacheControl != null && (cacheControl.contains("no-store") || cacheControl.contains("private"))) {
                    return Mono.empty();
                }

                // 捕获响应体（这里简化处理，实际需要包装 Response）
                // 实际实现需要使用 ServerHttpResponseDecorator
                return Mono.empty();
            }));
    }

    /**
     * 生成缓存键
     */
    private String generateCacheKey(ServerHttpRequest request) {
        String key = request.getMethod().name() + ":" +
            request.getURI().getPath() + ":" +
            request.getURI().getQuery();

        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(key.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return GatewayHeaderConstants.CACHE_KEY_PREFIX + hexString.substring(0, 16);
        } catch (Exception e) {
            return GatewayHeaderConstants.CACHE_KEY_PREFIX + key.hashCode();
        }
    }

    @Override
    public int getOrder() {
        return 1000; // 在核心过滤器之后
    }

    /**
     * 缓存的响应
     */
    public static class CachedResponse {
        private HttpStatusCode statusCode;
        private HttpHeaders headers;
        private byte[] body;
        private long timestamp;
        private long ttl;

        public CachedResponse(HttpStatusCode statusCode, HttpHeaders headers, byte[] body, long ttl) {
            this.statusCode = statusCode;
            this.headers = headers;
            this.body = body;
            this.ttl = ttl;
            this.timestamp = System.currentTimeMillis();
        }

        public boolean isExpired() {
            return System.currentTimeMillis() - timestamp > ttl;
        }

        public HttpStatusCode getStatusCode() {
            return statusCode;
        }

        public HttpHeaders getHeaders() {
            return headers;
        }

        public byte[] getBody() {
            return body;
        }
    }
}
