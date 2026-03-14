package com.carlos.apm;

import brave.Span;
import brave.Tracer;
import brave.propagation.TraceContext;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 分布式追踪工具类
 * <p>
 * 统一封装 Sleuth (Micrometer Tracing) 和 SkyWalking 的追踪工具方法
 *
 * @author Carlos
 * @date 2024-12-09
 */
@Slf4j
public final class TraceUtil {

    private TraceUtil() {
        // 工具类禁止实例化
    }

    // ==================== Sleuth (Micrometer Tracing) 相关方法 ====================

    /**
     * 获取当前 Sleuth Trace ID
     *
     * @return Trace ID，如果未在追踪上下文中返回空字符串
     */
    public static String getTraceId() {
        try {
            Tracer tracer = SpringUtil.getBean(Tracer.class);
            if (tracer == null) {
                return StrUtil.EMPTY;
            }
            Span currentSpan = tracer.currentSpan();
            if (currentSpan != null) {
                return currentSpan.context().traceIdString();
            }
        } catch (Exception e) {
            log.debug("获取 Sleuth Trace ID 失败: {}", e.getMessage());
        }
        return StrUtil.EMPTY;
    }

    /**
     * 获取当前 Sleuth Span ID
     *
     * @return Span ID，如果未在追踪上下文中返回空字符串
     */
    public static String getSpanId() {
        try {
            Tracer tracer = SpringUtil.getBean(Tracer.class);
            if (tracer == null) {
                return StrUtil.EMPTY;
            }
            Span currentSpan = tracer.currentSpan();
            if (currentSpan != null) {
                return currentSpan.context().spanIdString();
            }
        } catch (Exception e) {
            log.debug("获取 Sleuth Span ID 失败: {}", e.getMessage());
        }
        return StrUtil.EMPTY;
    }

    /**
     * 获取当前 Sleuth Parent Span ID
     *
     * @return Parent Span ID，如果没有父 Span 返回空字符串
     */
    public static String getParentId() {
        try {
            Tracer tracer = SpringUtil.getBean(Tracer.class);
            if (tracer == null) {
                return StrUtil.EMPTY;
            }
            Span currentSpan = tracer.currentSpan();
            if (currentSpan != null) {
                TraceContext context = currentSpan.context();
                return context.parentIdString();
            }
        } catch (Exception e) {
            log.debug("获取 Sleuth Parent ID 失败: {}", e.getMessage());
        }
        return StrUtil.EMPTY;
    }

    /**
     * 检查当前是否在 Sleuth 追踪上下文中
     *
     * @return true 如果在追踪上下文中
     */
    public static boolean isInSleuthContext() {
        return StrUtil.isNotBlank(getTraceId());
    }

    /**
     * 获取当前 Sleuth Span
     *
     * @return Span 对象，如果未在上下文中返回 null
     */
    public static Span getCurrentSpan() {
        try {
            Tracer tracer = SpringUtil.getBean(Tracer.class);
            if (tracer == null) {
                return null;
            }
            return tracer.currentSpan();
        } catch (Exception e) {
            log.debug("获取 Sleuth Span 失败: {}", e.getMessage());
            return null;
        }
    }

    /**
     * 为当前 Span 添加标签
     *
     * @param key   标签键
     * @param value 标签值
     */
    public static void tag(String key, String value) {
        Span currentSpan = getCurrentSpan();
        if (currentSpan != null && StrUtil.isNotBlank(key)) {
            currentSpan.tag(key, value);
        }
    }

    /**
     * 为当前 Span 添加事件注解
     *
     * @param value 注解值
     */
    public static void annotate(String value) {
        Span currentSpan = getCurrentSpan();
        if (currentSpan != null && StrUtil.isNotBlank(value)) {
            currentSpan.annotate(value);
        }
    }

    // ==================== SkyWalking 相关方法 ====================

    /**
     * 获取 SkyWalking Trace ID
     * <p>
     * 注意：需要在 SkyWalking Agent 启动后才能正常返回
     *
     * @return Trace ID 或 "N/A"
     */
    public static String getSkyWalkingTraceId() {
        try {
            return org.apache.skywalking.apm.toolkit.trace.TraceContext.traceId();
        } catch (Exception e) {
            log.debug("获取 SkyWalking Trace ID 失败: {}", e.getMessage());
            return "N/A";
        }
    }

    /**
     * 检查当前是否在 SkyWalking 追踪上下文中
     *
     * @return true 如果在追踪上下文中
     */
    public static boolean isInSkyWalkingContext() {
        String traceId = getSkyWalkingTraceId();
        return StrUtil.isNotBlank(traceId) && !"N/A".equals(traceId) && !"Ignored_Trace".equals(traceId);
    }

    // ==================== 通用方法 ====================

    /**
     * 获取 Trace ID（优先返回 Sleuth，如果不存在则返回 SkyWalking）
     *
     * @return Trace ID
     */
    public static String getUnifiedTraceId() {
        // 优先尝试 Sleuth
        String sleuthTraceId = getTraceId();
        if (StrUtil.isNotBlank(sleuthTraceId)) {
            return sleuthTraceId;
        }
        // 然后尝试 SkyWalking
        String swTraceId = getSkyWalkingTraceId();
        if (StrUtil.isNotBlank(swTraceId) && !"N/A".equals(swTraceId)) {
            return swTraceId;
        }
        return StrUtil.EMPTY;
    }

    /**
     * 检查当前是否在任意追踪上下文中
     *
     * @return true 如果在 Sleuth 或 SkyWalking 上下文中
     */
    public static boolean isInTraceContext() {
        return isInSleuthContext() || isInSkyWalkingContext();
    }

    /**
     * 获取完整的追踪上下文信息
     *
     * @return 格式化的追踪信息
     */
    public static String getTraceInfo() {
        StringBuilder sb = new StringBuilder();
        sb.append("TraceContext{");

        // Sleuth 信息
        String sleuthTraceId = getTraceId();
        if (StrUtil.isNotBlank(sleuthTraceId)) {
            sb.append("sleuth={");
            sb.append("traceId='").append(sleuthTraceId).append("\', ");
            sb.append("spanId='").append(getSpanId()).append("\', ");
            sb.append("parentId='").append(getParentId()).append("\'");
            sb.append("}");
        }

        // SkyWalking 信息
        String swTraceId = getSkyWalkingTraceId();
        if (StrUtil.isNotBlank(swTraceId) && !"N/A".equals(swTraceId)) {
            if (sb.length() > 14) {
                sb.append(", ");
            }
            sb.append("skywalking={");
            sb.append("traceId='").append(swTraceId).append("\', ");
            sb.append("segmentId='").append(org.apache.skywalking.apm.toolkit.trace.TraceContext.segmentId()).append("\', ");
            sb.append("spanId=").append(org.apache.skywalking.apm.toolkit.trace.TraceContext.spanId());
            sb.append("}");
        }

        sb.append("}");
        return sb.toString();
    }
}
