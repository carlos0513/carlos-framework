package com.carlos.apm.skywalking.util;

import org.apache.skywalking.apm.toolkit.trace.Tags;
import org.apache.skywalking.apm.toolkit.trace.TraceContext;

/**
 * SkyWalking 工具类
 * <p>
 * 提供 SkyWalking 追踪相关的工具方法
 *
 * @author Carlos
 * @date 2024-12-09
 */
public final class SkyWalkingUtil {

    private SkyWalkingUtil() {
        // 工具类禁止实例化
    }

    /**
     * 获取 SkyWalking Trace ID
     * <p>
     * 注意：此方法需要在 SkyWalking Agent 启动后才能正常返回 Trace ID，
     * 如果未接入 Agent，将返回 "N/A"
     *
     * @return Trace ID 或 "N/A"
     */
    public static String getTraceId() {
        return TraceContext.traceId();
    }

    /**
     * 获取 SkyWalking Segment ID
     *
     * @return Segment ID 或 "N/A"
     */
    public static String getSegmentId() {
        return TraceContext.segmentId();
    }

    /**
     * 获取 SkyWalking Span ID
     *
     * @return Span ID 或 -1
     */
    public static int getSpanId() {
        return TraceContext.spanId();
    }

    /**
     * 检查当前是否在 SkyWalking 追踪上下文中
     *
     * @return true 如果在追踪上下文中
     */
    public static boolean inTraceContext() {
        String traceId = getTraceId();
        return traceId != null && !"N/A".equals(traceId) && !"Ignored_Trace".equals(traceId);
    }

    /**
     * 设置自定义标签到当前 Span
     * <p>
     * 需要在方法上添加 {@link Tags} 注解，或使用 ActiveSpan API
     *
     * @param key   标签键
     * @param value 标签值
     */
    public static void setTag(String key, String value) {
        if (inTraceContext()) {
            org.apache.skywalking.apm.toolkit.trace.ActiveSpan.tag(key, value);
        }
    }

    /**
     * 记录错误信息到当前 Span
     *
     * @param errorMessage 错误信息
     */
    public static void setError(String errorMessage) {
        if (inTraceContext()) {
            org.apache.skywalking.apm.toolkit.trace.ActiveSpan.error(errorMessage);
        }
    }

    /**
     * 记录异常到当前 Span
     *
     * @param throwable 异常对象
     */
    public static void setError(Throwable throwable) {
        if (inTraceContext() && throwable != null) {
            org.apache.skywalking.apm.toolkit.trace.ActiveSpan.error(throwable);
        }
    }

    /**
     * 记录信息日志到当前 Span
     *
     * @param message 日志信息
     */
    public static void logInfo(String message) {
        if (inTraceContext()) {
            org.apache.skywalking.apm.toolkit.trace.ActiveSpan.info(message);
        }
    }

    /**
     * 记录调试日志到当前 Span
     *
     * @param message 日志信息
     */
    public static void logDebug(String message) {
        if (inTraceContext()) {
            org.apache.skywalking.apm.toolkit.trace.ActiveSpan.debug(message);
        }
    }

    /**
     * 获取追踪上下文中的自定义值
     *
     * @param key 键
     * @return 值，如果不存在返回 null
     */
    public static String getCorrelation(String key) {
        if (!inTraceContext() || key == null) {
            return null;
        }
        return TraceContext.getCorrelation(key).orElse(null);
    }

    /**
     * 设置追踪上下文中的自定义值
     *
     * @param key   键
     * @param value 值
     */
    public static void setCorrelation(String key, String value) {
        if (key != null && value != null) {
            TraceContext.putCorrelation(key, value);
        }
    }

    /**
     * 获取完整的 SkyWalking 追踪上下文信息
     *
     * @return 格式: traceId|segmentId|spanId 或 "N/A"
     */
    public static String getFullContext() {
        if (!inTraceContext()) {
            return "N/A";
        }
        return String.format("%s|%s|%d", getTraceId(), getSegmentId(), getSpanId());
    }
}
