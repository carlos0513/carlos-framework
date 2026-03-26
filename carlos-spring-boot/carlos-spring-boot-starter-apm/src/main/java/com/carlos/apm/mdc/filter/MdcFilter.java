package com.carlos.apm.mdc.filter;

import com.carlos.apm.config.ApmProperties;
import com.carlos.apm.mdc.MdcUtil;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * MDC 过滤器
 * <p>
 * 在请求开始时将 Trace ID 设置到 MDC 中，在请求结束时清理 MDC
 *
 * @author Carlos
 * @date 2024-12-09
 */
@Slf4j
public class MdcFilter implements Filter {

    private final ApmProperties apmProperties;

    public MdcFilter(ApmProperties apmProperties) {
        this.apmProperties = apmProperties;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.debug("[Carlos APM] MDC Filter 初始化完成");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
        throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        try {
            // 1. 从请求头中提取自定义 Request ID，如果没有则生成
            String requestId = httpRequest.getHeader(MdcUtil.REQUEST_ID_HEADER);
            requestId = MdcUtil.setRequestId(requestId);

            // 2. 设置 Micrometer Tracing Trace ID 到 MDC
            MdcUtil.setTraceId();

            // 3. 设置 SkyWalking Trace ID 到 MDC
            MdcUtil.setSkyWalkingTraceId();

            // 4. 将 Request ID 和 Trace ID 添加到响应头
            if (apmProperties.getMdc().isAddToResponse()) {
                // 返回 Request ID（业务追踪用）
                httpResponse.setHeader(MdcUtil.RESPONSE_ID_HEADER, requestId);

                // 返回 Trace ID（链路追踪用）
                String traceId = MdcUtil.getTraceId();
                if (traceId != null) {
                    httpResponse.setHeader(apmProperties.getMdc().getResponseHeaderName(), traceId);
                }
            }

            // 继续处理请求
            chain.doFilter(request, response);

        } finally {
            // 清理 MDC，防止内存泄漏
            MdcUtil.clear();
        }
    }

    @Override
    public void destroy() {
        log.debug("[Carlos APM] MDC Filter 销毁");
    }
}
