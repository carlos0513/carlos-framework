package com.carlos.boot.filters.xss;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * <p>
 * Xss过滤器
 * </p>
 *
 * @author carlos
 * @date 2020/4/15 15:55
 */
@Slf4j
public class XssFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) {
        if (log.isDebugEnabled()) {
            log.debug("XssFilter注册化成功");
        }
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
        throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        // 对原始请求进行处理
        filterChain.doFilter(new XssHttpServletRequestWrapper(request), servletResponse);
    }

    @Override
    public void destroy() {
        if (log.isDebugEnabled()) {
            log.debug("XssFilter取消注册");
        }
    }
}
