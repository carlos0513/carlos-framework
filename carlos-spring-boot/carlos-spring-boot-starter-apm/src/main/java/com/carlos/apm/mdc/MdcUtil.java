package com.carlos.apm.mdc;

import brave.Span;
import brave.Tracer;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.carlos.core.constant.HttpHeadersConstant;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;

/**
 * MDC (Mapped Diagnostic Context) 工具类
 * <p>
 * 用于在日志中统一管理 Trace ID 等上下文信息
 *
 * @author Carlos
 * @date 2024-12-09
 */
@Slf4j
public final class MdcUtil {

    /**
     * MDC 中 Trace ID 的 key
     */
    public static final String MDC_TRACE_ID_KEY = "traceId";

    /**
     * MDC 中 Span ID 的 key
     */
    public static final String MDC_SPAN_ID_KEY = "spanId";

    /**
     * MDC 中 SkyWalking Trace ID 的 key
     */
    public static final String MDC_SW_TRACE_ID_KEY = "tid";

    /**
     * MDC 中自定义 Request ID 的 key（业务请求ID，兼容原有实现）
     */
    public static final String MDC_REQUEST_ID_KEY = "requestId";

    /**
     * 请求头中 Request ID 的名称
     * @deprecated 使用 {@link HttpHeadersConstant#X_REQUEST_ID}
     */
    @Deprecated
    public static final String REQUEST_ID_HEADER = HttpHeadersConstant.X_REQUEST_ID;

    /**
     * 响应头中 Request ID 的名称
     * @deprecated 使用 {@link HttpHeadersConstant#X_REQUEST_ID}
     */
    @Deprecated
    public static final String RESPONSE_ID_HEADER = HttpHeadersConstant.X_REQUEST_ID;

    private MdcUtil() {
        // 工具类禁止实例化
    }

    /**
     * 设置 MDC 中的 Trace ID（来自 Sleuth）
     */
    public static void setTraceId() {
        try {
            Tracer tracer = SpringUtil.getBean(Tracer.class);
            if (tracer == null) {
                return;
            }
            Span currentSpan = tracer.currentSpan();
            if (currentSpan != null) {
                String traceId = currentSpan.context().traceIdString();
                if (StrUtil.isNotBlank(traceId)) {
                    MDC.put(MDC_TRACE_ID_KEY, traceId);
                    MDC.put(MDC_SPAN_ID_KEY, currentSpan.context().spanIdString());
                }
            }
        } catch (Exception e) {
            log.debug("设置 MDC Trace ID 失败: {}", e.getMessage());
        }
    }

    /**
     * 设置 MDC 中的指定 Trace ID
     *
     * @param traceId Trace ID
     */
    public static void setTraceId(String traceId) {
        if (StrUtil.isNotBlank(traceId)) {
            MDC.put(MDC_TRACE_ID_KEY, traceId);
        }
    }

    /**
     * 设置 MDC 中的 SkyWalking Trace ID
     */
    public static void setSkyWalkingTraceId() {
        try {
            String traceId = org.apache.skywalking.apm.toolkit.trace.TraceContext.traceId();
            if (StrUtil.isNotBlank(traceId) && !"N/A".equals(traceId)) {
                MDC.put(MDC_SW_TRACE_ID_KEY, traceId);
            }
        } catch (Exception e) {
            log.debug("设置 MDC SkyWalking Trace ID 失败: {}", e.getMessage());
        }
    }

    /**
     * 获取 MDC 中的 Trace ID
     *
     * @return Trace ID，如果不存在返回 null
     */
    public static String getTraceId() {
        String traceId = MDC.get(MDC_TRACE_ID_KEY);
        if (StrUtil.isBlank(traceId)) {
            traceId = MDC.get(MDC_SW_TRACE_ID_KEY);
        }
        return traceId;
    }

    /**
     * 获取 MDC 中的 Span ID
     *
     * @return Span ID，如果不存在返回 null
     */
    public static String getSpanId() {
        return MDC.get(MDC_SPAN_ID_KEY);
    }

    /**
     * 设置 MDC 中的 Request ID（业务自定义请求ID）
     * <p>
     * 如果传入的 requestId 为空，会自动生成一个基于 UUID 的 ID
     *
     * @param requestId Request ID
     * @return 实际设置的 Request ID
     */
    public static String setRequestId(String requestId) {
        if (StrUtil.isBlank(requestId)) {
            requestId = generateRequestId();
        }
        MDC.put(MDC_REQUEST_ID_KEY, requestId);
        return requestId;
    }

    /**
     * 获取 MDC 中的 Request ID
     *
     * @return Request ID，如果不存在则返回 traceId 作为兜底
     */
    public static String getRequestId() {
        String requestId = MDC.get(MDC_REQUEST_ID_KEY);
        // 兼容：如果 requestId 为空，尝试返回 traceId
        if (StrUtil.isBlank(requestId)) {
            requestId = getTraceId();
        }
        return requestId;
    }

    /**
     * 生成新的 Request ID
     *
     * @return 基于时间戳和随机数的 Request ID
     */
    public static String generateRequestId() {
        return java.util.UUID.randomUUID().toString().replace("-", "").substring(0, 16);
    }

    /**
     * 清除 MDC 中的所有追踪信息
     */
    public static void clear() {
        MDC.remove(MDC_TRACE_ID_KEY);
        MDC.remove(MDC_SPAN_ID_KEY);
        MDC.remove(MDC_SW_TRACE_ID_KEY);
        MDC.remove(MDC_REQUEST_ID_KEY);
    }

    /**
     * 设置自定义 MDC 值
     *
     * @param key   键
     * @param value 值
     */
    public static void put(String key, String value) {
        if (StrUtil.isNotBlank(key) && StrUtil.isNotBlank(value)) {
            MDC.put(key, value);
        }
    }

    /**
     * 获取自定义 MDC 值
     *
     * @param key 键
     * @return 值
     */
    public static String get(String key) {
        return MDC.get(key);
    }

    /**
     * 移除指定 MDC 值
     *
     * @param key 键
     */
    public static void remove(String key) {
        if (StrUtil.isNotBlank(key)) {
            MDC.remove(key);
        }
    }
}
