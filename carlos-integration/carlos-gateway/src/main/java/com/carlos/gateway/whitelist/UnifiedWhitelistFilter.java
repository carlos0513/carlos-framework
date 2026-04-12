package com.carlos.gateway.whitelist;

import com.carlos.core.util.PathMatchUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <p>
 * 统一白名单检查过滤器
 * 最先执行，统一检查所有类型的白名单，将结果存入 Exchange Attributes
 * 后续过滤器直接从 Attributes 读取，避免重复检查
 * </p>
 * <p>
 * 优化效果：
 * 1. 避免多个过滤器重复进行路径匹配（Ant 匹配开销大）
 * 2. 一次匹配，多处使用
 * 3. 支持分层白名单（全局、认证、WAF、限流等）
 * </p>
 *
 * @author carlos
 * @date 2026/4/10
 */
@Slf4j
public class UnifiedWhitelistFilter implements GlobalFilter, Ordered {

    private final UnifiedWhitelistProperties properties;

    // 白名单匹配结果缓存（短期缓存，减少重复计算）
    // Key: path:type, Value: 是否匹配
    private final ConcurrentHashMap<String, Boolean> matchCache = new ConcurrentHashMap<>();

    // 缓存大小限制，防止内存泄漏
    private static final int MAX_CACHE_SIZE = 10000;

    public UnifiedWhitelistFilter(UnifiedWhitelistProperties properties) {
        this.properties = properties;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        if (!properties.isEnabled()) {
            return chain.filter(exchange);
        }

        String path = exchange.getRequest().getURI().getPath();

        // 执行统一白名单检查
        WhitelistCheckResult result = performWhitelistCheck(path);

        // 将结果存入 Exchange Attributes
        WhitelistContext.setCheckResult(exchange, result);

        if (log.isDebugEnabled()) {
            log.debug("Unified whitelist check for [{}]: global={}, auth={}, waf={}, replay={}, rateLimit={}, prefixStrip={}",
                path, result.isGlobalWhitelisted(), result.isAuthWhitelisted(),
                result.isWafWhitelisted(), result.isReplayWhitelisted(),
                result.isRateLimitWhitelisted(), result.isPrefixStripWhitelisted());
        }

        return chain.filter(exchange);
    }

    /**
     * 执行白名单检查
     */
    private WhitelistCheckResult performWhitelistCheck(String path) {
        // 检查各类白名单（使用缓存）
        boolean globalMatch = isPathWhitelisted(path, WhitelistType.GLOBAL, properties.getGlobalPaths());
        boolean authMatch = isPathWhitelisted(path, WhitelistType.AUTH, properties.getAuthPaths());
        boolean wafMatch = isPathWhitelisted(path, WhitelistType.WAF, properties.getWafPaths());
        boolean replayMatch = isPathWhitelisted(path, WhitelistType.REPLAY, properties.getReplayPaths());
        boolean rateLimitMatch = isPathWhitelisted(path, WhitelistType.RATE_LIMIT, properties.getRateLimitPaths());
        boolean prefixStripMatch = isPathWhitelisted(path, WhitelistType.PREFIX_STRIP, properties.getPrefixStripPaths());

        return WhitelistCheckResult.builder()
            .globalWhitelisted(globalMatch)
            .authWhitelisted(authMatch)
            .wafWhitelisted(wafMatch)
            .replayWhitelisted(replayMatch)
            .rateLimitWhitelisted(rateLimitMatch)
            .prefixStripWhitelisted(prefixStripMatch)
            .build();
    }

    /**
     * 检查路径是否在白名单中（带缓存）
     */
    private boolean isPathWhitelisted(String path, WhitelistType type, Set<String> whitelist) {
        // 空白名单快速返回
        if (whitelist == null || whitelist.isEmpty()) {
            return false;
        }

        // 构建缓存键
        String cacheKey = path + ":" + type.name();

        // 从缓存获取
        Boolean cached = matchCache.get(cacheKey);
        if (cached != null) {
            return cached;
        }

        // 执行匹配
        boolean matched = PathMatchUtil.antMatchAny(whitelist, path);

        // 存入缓存（限制大小）
        if (matchCache.size() < MAX_CACHE_SIZE) {
            matchCache.put(cacheKey, matched);
        } else {
            // 缓存满了，清空后重新放入（简单策略）
            matchCache.clear();
            matchCache.put(cacheKey, matched);
        }

        return matched;
    }

    /**
     * 清除匹配缓存（供外部调用，如配置变更时）
     */
    public void clearCache() {
        matchCache.clear();
        if (log.isInfoEnabled()) {
            log.info("Unified whitelist match cache cleared");
        }
    }

    /**
     * 获取缓存统计信息
     */
    public WhitelistCacheStats getCacheStats() {
        return new WhitelistCacheStats(matchCache.size(), MAX_CACHE_SIZE);
    }

    @Override
    public int getOrder() {
        // 最高优先级之一，仅次于 Tracing
        return Ordered.HIGHEST_PRECEDENCE + 100;
    }

    /**
     * 白名单类型枚举
     */
    public enum WhitelistType {
        GLOBAL,      // 全局白名单
        AUTH,        // 认证白名单
        WAF,         // WAF 白名单
        REPLAY,      // 防重放白名单
        RATE_LIMIT,  // 限流白名单
        PREFIX_STRIP // 路径前缀剥离白名单
    }

    /**
     * 缓存统计信息
     */
    public record WhitelistCacheStats(int currentSize, int maxSize) {
        public double hitRate() {
            return currentSize > 0 ? (double) currentSize / maxSize : 0.0;
        }
    }
}
