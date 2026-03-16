package com.carlos.gateway.ratelimit;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.server.ServerWebExchange;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * 分布式限流过滤器（基于 Redis + Lua）
 * 使用令牌桶算法实现平滑限流
 * </p>
 *
 * @author carlos
 * @date 2026/3/16
 */
@Slf4j
@Component
public class RedisRateLimiter extends AbstractGatewayFilterFactory<RedisRateLimiter.Config> {

    /**
     * 令牌桶限流 Lua 脚本
     * KEYS[1]: 限流键
     * ARGV[1]: 最大令牌数
     * ARGV[2]: 每秒产生的令牌数
     * ARGV[3]: 当前时间戳（毫秒）
     */
    private static final String RATE_LIMITER_SCRIPT =
        "local key = KEYS[1]\n" +
            "local maxTokens = tonumber(ARGV[1])\n" +
            "local refillRate = tonumber(ARGV[2])\n" +
            "local now = tonumber(ARGV[3])\n" +
            "\n" +
            "local bucket = redis.call('HMGET', key, 'tokens', 'lastRefill')\n" +
            "local tokens = tonumber(bucket[1]) or maxTokens\n" +
            "local lastRefill = tonumber(bucket[2]) or now\n" +
            "\n" +
            "-- 计算新令牌数\n" +
            "local elapsed = now - lastRefill\n" +
            "local newTokens = math.min(maxTokens, tokens + (elapsed / 1000) * refillRate)\n" +
            "\n" +
            "local allowed = 0\n" +
            "if newTokens >= 1 then\n" +
            "    newTokens = newTokens - 1\n" +
            "    allowed = 1\n" +
            "end\n" +
            "\n" +
            "redis.call('HMSET', key, 'tokens', newTokens, 'lastRefill', now)\n" +
            "redis.call('PEXPIRE', key, 60000)\n" +
            "\n" +
            "return allowed\n";

    private final ReactiveStringRedisTemplate redisTemplate;
    private final RedisScript<Long> rateLimiterScript;

    public RedisRateLimiter(ReactiveStringRedisTemplate redisTemplate) {
        super(Config.class);
        this.redisTemplate = redisTemplate;
        this.rateLimiterScript = RedisScript.of(RATE_LIMITER_SCRIPT, Long.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            String key = resolveKey(exchange, config);
            long now = Instant.now().toEpochMilli();

            List<String> keys = List.of("ratelimit:" + key);
            List<String> args = Arrays.asList(
                String.valueOf(config.getBurstCapacity()),
                String.valueOf(config.getReplenishRate()),
                String.valueOf(now)
            );

            return redisTemplate.execute(rateLimiterScript, keys, args)
                .single()
                .flatMap(allowed -> {
                    if (allowed == 1L) {
                        // 添加限流响应头
                        exchange.getResponse().getHeaders().add("X-RateLimit-Limit",
                            String.valueOf(config.getBurstCapacity()));
                        return chain.filter(exchange);
                    } else {
                        log.warn("Rate limit exceeded for key: {}", key);
                        exchange.getResponse().setStatusCode(
                            org.springframework.http.HttpStatus.TOO_MANY_REQUESTS);
                        return exchange.getResponse().setComplete();
                    }
                });
        };
    }

    /**
     * 解析限流键
     */
    private String resolveKey(ServerWebExchange exchange, Config config) {
        String key = switch (config.getKeyResolver()) {
            case IP -> exchange.getRequest().getRemoteAddress() != null
                ? exchange.getRequest().getRemoteAddress().getAddress().getHostAddress()
                : "unknown";
            case USER -> exchange.getRequest().getHeaders().getFirst("X-User-Id");
            case API -> exchange.getRequest().getURI().getPath();
            case COMBINED -> {
                String ip = exchange.getRequest().getRemoteAddress() != null
                    ? exchange.getRequest().getRemoteAddress().getAddress().getHostAddress()
                    : "unknown";
                String path = exchange.getRequest().getURI().getPath();
                yield ip + ":" + path;
            }
        };
        return key != null ? key : "default";
    }

    @Override
    public List<String> shortcutFieldOrder() {
        return Arrays.asList("replenishRate", "burstCapacity", "keyResolver");
    }

    @Validated
    public static class Config {

        @Min(1)
        private int replenishRate = 10;  // 每秒产生的令牌数

        @Min(1)
        private int burstCapacity = 20;  // 令牌桶容量

        @NotNull
        private KeyResolver keyResolver = KeyResolver.IP;  // 限流维度

        public enum KeyResolver {
            IP,        // 基于 IP 限流
            USER,      // 基于用户限流
            API,       // 基于 API 限流
            COMBINED   // 组合限流（IP + API）
        }

        public int getReplenishRate() {
            return replenishRate;
        }

        public void setReplenishRate(int replenishRate) {
            this.replenishRate = replenishRate;
        }

        public int getBurstCapacity() {
            return burstCapacity;
        }

        public void setBurstCapacity(int burstCapacity) {
            this.burstCapacity = burstCapacity;
        }

        public KeyResolver getKeyResolver() {
            return keyResolver;
        }

        public void setKeyResolver(KeyResolver keyResolver) {
            this.keyResolver = keyResolver;
        }
    }
}
