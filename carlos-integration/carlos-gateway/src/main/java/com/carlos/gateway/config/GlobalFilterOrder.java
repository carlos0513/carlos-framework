package com.carlos.gateway.config;

import org.springframework.core.Ordered;

/**
 * <p>
 *   gateway全局filter顺序定义，所有自定义的全局filter务必采用此接口获取顺序
 *   为了防止顺序冲突，请不要使用数字，而是使用枚举，顺序采用10000/20000间隔性定义，防止后续插入其他filter
 *
 * </p>
 *
 * @author Carlos
 * @date 2025-01-10 11:49
 */

/**
 * Gateway 全局过滤器顺序定义
 * <p>
 * 注意：Spring Cloud Gateway 使用 Ordered.HIGHEST_PRECEDENCE 作为最高优先级，
 * 数值越小优先级越高。
 * <p>
 * 推荐的执行顺序：
 * 1. 链路追踪（RequestTracingFilter）- 最先执行，确保后续过滤器能获取到 traceId
 * 2. 安全防护（WafFilter, ReplayProtectionFilter）
 * 3. 认证授权（OAuth2AuthenticationFilter）
 * 4. 限流熔断（RateLimiter, CircuitBreaker）
 * 5. 灰度发布（GrayReleaseFilter）
 * 6. 请求转换（RequestTransformFilter）
 * 7. 访问日志（AccessLogFilter）- 最后执行，记录完整请求信息
 *
 * @author Carlos
 * @date 2025-01-10
 */
public interface GlobalFilterOrder extends Ordered {

    // ==================== 最高优先级（最先执行）====================

    /**
     * 链路追踪过滤器优先级
     * 确保最先执行，为后续过滤器提供 traceId 和 requestId
     */
    int TRACING_FILTER_ORDER = Ordered.HIGHEST_PRECEDENCE + 50;

    // ==================== 间隔定义（每 1000 为一个区间）====================

    int ORDER_FIRST = 1000;

    int ORDER_SECOND = 2000;

    int ORDER_THIRD = 3000;

    int ORDER_FOURTH = 4000;

    int ORDER_FIFTH = 5000;

    int ORDER_SIXTH = 6000;

    int ORDER_SEVENTH = 7000;

    /**
     * 访问日志过滤器优先级
     * 确保最后执行，记录完整请求信息
     */
    int ACCESS_LOG_ORDER = 8000;

    /**
     * 最低优先级（最后执行）
     */
    int ORDER_LAST = Ordered.LOWEST_PRECEDENCE - 100;

}
