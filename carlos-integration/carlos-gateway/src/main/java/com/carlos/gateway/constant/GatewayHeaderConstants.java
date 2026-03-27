package com.carlos.gateway.constant;

/**
 * Gateway 模块 HTTP 请求头常量
 * <p>
 * 定义网关模块专用的 HTTP 请求头标识，包括：
 * - 灰度发布相关
 * - 请求转换相关
 * - 安全防护相关（WAF、防重放）
 * - 缓存相关
 * - API 版本控制相关
 * - Exchange 属性键名
 *
 * @author carlos
 * @date 2026/3/27
 */
public interface GatewayHeaderConstants {

    // ==================== 灰度发布 ====================

    /**
     * 灰度发布标记请求头（传递给后端服务）
     */
    String X_GRAY_RELEASE = "X-Gray-Release";

    /**
     * Exchange 属性中存储灰度标记的键名
     */
    String GRAY_RELEASE_ATTR = "gray.release";

    /**
     * 服务实例元数据中的灰度标记键名
     */
    String GRAY_METADATA_KEY = "gray.release";

    /**
     * 服务实例元数据中的灰度标记（简写形式）
     */
    String GRAY_METADATA_SHORT = "gray";

    // ==================== 请求转换 ====================

    /**
     * API 版本请求头（客户端传入）
     */
    String X_API_VERSION = "X-API-Version";

    /**
     * 路由后的 API 版本请求头（传递给后端）
     */
    String X_ROUTED_VERSION = "X-Routed-Version";

    /**
     * 网关标识请求头
     */
    String X_GATEWAY_ID = "X-Gateway-Id";

    /**
     * 网关转发时间戳请求头
     */
    String X_GATEWAY_TIMESTAMP = "X-Gateway-Timestamp";

    // ==================== 安全防护 - 防重放攻击 ====================

    /**
     * 请求时间戳头（用于防重放攻击验证）
     */
    String X_TIMESTAMP = "X-Timestamp";

    /**
     * 请求随机数头（用于防重放攻击验证）
     */
    String X_NONCE = "X-Nonce";

    /**
     * 请求签名头（用于防重放攻击验证）
     */
    String X_SIGNATURE = "X-Signature";

    /**
     * CSRF Token 请求头
     */
    String X_CSRF_TOKEN = "X-CSRF-Token";

    /**
     * Redis 中存储 Nonce 的键前缀
     */
    String NONCE_CACHE_PREFIX = "nonce:";

    // ==================== 缓存 ====================

    /**
     * 缓存命中标记头
     */
    String X_CACHE = "X-Cache";

    /**
     * 缓存命中标记值
     */
    String CACHE_HIT = "HIT";

    /**
     * 缓存未命中标记值
     */
    String CACHE_MISS = "MISS";

    /**
     * Redis 缓存键前缀
     */
    String CACHE_KEY_PREFIX = "cache:response:";

    // ==================== OAuth2 认证 ====================

    /**
     * Query 参数中的 Token 键名（用于 WebSocket 等场景）
     */
    String ACCESS_TOKEN_PARAM = "access_token";

    /**
     * Bearer Token 前缀
     */
    String BEARER_PREFIX = "Bearer ";

    // ==================== 指标监控 ====================

    /**
     * Micrometer 指标名称 - 请求总数
     */
    String METRIC_REQUEST_COUNTER = "gateway.requests.total";

    /**
     * Micrometer 指标名称 - 请求延迟
     */
    String METRIC_REQUEST_LATENCY = "gateway.requests.duration";

    /**
     * Micrometer 指标名称 - 活跃请求数
     */
    String METRIC_ACTIVE_REQUESTS = "gateway.requests.active";

    /**
     * Micrometer 指标名称 - 成功请求数
     */
    String METRIC_SUCCESS = "gateway.requests.success";

    /**
     * Micrometer 指标名称 - 客户端错误数
     */
    String METRIC_CLIENT_ERROR = "gateway.requests.client_error";

    /**
     * Micrometer 指标名称 - 服务端错误数
     */
    String METRIC_SERVER_ERROR = "gateway.requests.server_error";

    /**
     * Micrometer 指标名称 - 限流触发数
     */
    String METRIC_RATE_LIMITED = "gateway.requests.rate_limited";

    /**
     * Micrometer 指标名称 - 被阻止请求数
     */
    String METRIC_BLOCKED = "gateway.requests.blocked";

}
