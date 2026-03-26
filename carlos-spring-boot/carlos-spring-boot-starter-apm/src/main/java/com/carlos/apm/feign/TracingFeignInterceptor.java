package com.carlos.apm.feign;

import brave.Span;
import brave.Tracer;
import brave.propagation.TraceContext;
import com.carlos.apm.mdc.MdcUtil;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * Feign 链路追踪拦截器
 * <p>
 * 自动将 Request ID 和 B3 Trace 头信息传递到下游服务
 *
 * @author Carlos
 * @date 2025-03-26
 */
@Slf4j
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class TracingFeignInterceptor implements RequestInterceptor {

    @Autowired(required = false)
    private Tracer tracer;

    @Override
    public void apply(RequestTemplate template) {
        // 1. 透传自定义 Request ID（业务追踪）
        String requestId = MdcUtil.getRequestId();
        if (requestId != null) {
            template.header(MdcUtil.REQUEST_ID_HEADER, requestId);
            log.debug("[Feign] 透传 Request ID: {}", requestId);
        }

        // 2. 透传 Micrometer Tracing B3 头（链路追踪）
        if (tracer != null) {
            Span currentSpan = tracer.currentSpan();
            if (currentSpan != null) {
                TraceContext context = currentSpan.context();

                // B3 单头格式（推荐）
                template.header("b3", context.traceIdString() + "-" + context.spanIdString() + "-1");

                // 或者使用 B3 多头部格式（兼容旧版）
                // template.header("X-B3-TraceId", context.traceIdString());
                // template.header("X-B3-SpanId", context.spanIdString());
                // template.header("X-B3-Sampled", "1");

                log.debug("[Feign] 透传 Trace ID: {}, Span ID: {}",
                    context.traceIdString(), context.spanIdString());
            }
        }

        // 3. 透传 SkyWalking Trace ID（如果启用）
        try {
            String swTraceId = org.apache.skywalking.apm.toolkit.trace.TraceContext.traceId();
            if (swTraceId != null && !swTraceId.isEmpty() && !"N/A".equals(swTraceId)) {
                template.header("sw-trace-id", swTraceId);
            }
        } catch (Exception e) {
            // SkyWalking 可能未启用，忽略异常
        }
    }
}
