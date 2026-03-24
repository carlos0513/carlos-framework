package com.carlos.gateway.exception;

/**
 * <p>
 * 限流异常
 * 用于处理请求被限流拦截的场景
 * 返回 HTTP 429 Too Many Requests
 * </p>
 *
 * @author carlos
 * @date 2026/3/24
 */
public class RateLimitException extends GatewayException {

    private static final long serialVersionUID = 1L;

    /**
     * 限流维度（IP、用户、接口等）
     */
    private final String limitDimension;

    /**
     * 限流阈值
     */
    private final long limitRate;

    /**
     * 重置时间（秒）
     */
    private final long retryAfter;

    public RateLimitException(String message) {
        super(message, 429, 5429);
        this.limitDimension = "unknown";
        this.limitRate = 0;
        this.retryAfter = 60;
    }

    public RateLimitException(String message, String limitDimension, long limitRate, long retryAfter) {
        super(message, 429, 5429);
        this.limitDimension = limitDimension;
        this.limitRate = limitRate;
        this.retryAfter = retryAfter;
    }

    public String getLimitDimension() {
        return limitDimension;
    }

    public long getLimitRate() {
        return limitRate;
    }

    public long getRetryAfter() {
        return retryAfter;
    }
}
