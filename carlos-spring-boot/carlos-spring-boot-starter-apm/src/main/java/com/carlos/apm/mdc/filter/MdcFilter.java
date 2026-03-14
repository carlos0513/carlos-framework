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
            // 设置 Sleuth Trace ID 到 MDC
            MdcUtil.setTraceId();

            // 设置 SkyWalking Trace ID 到 MDC
            MdcUtil.setSkyWalkingTraceId();

            // 将 Trace ID 添加到响应头
            if (apmProperties.getMdc().isAddToResponse()) {
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
