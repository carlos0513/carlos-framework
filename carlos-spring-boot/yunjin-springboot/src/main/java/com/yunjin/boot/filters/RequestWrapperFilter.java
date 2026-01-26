package com.yunjin.boot.filters;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.servlet.JakartaServletUtil;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.MediaType;

import java.io.IOException;

/**
 * <p>
 * 请求封装
 * </p>
 *
 * @author yunjin
 * @date 2020/4/11 23:00
 */
public class RequestWrapperFilter implements Filter {


    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest servletRequest = (HttpServletRequest) request;
        String contentType = request.getContentType();
        if (StrUtil.isBlank(contentType)) {
            contentType = MediaType.APPLICATION_JSON_VALUE;
        }
        if (JakartaServletUtil.isMultipart(servletRequest)) {
            request = servletRequest;
        } else if (contentType.equalsIgnoreCase(MediaType.APPLICATION_JSON_VALUE)
                || contentType.startsWith(MediaType.APPLICATION_JSON_VALUE)) {
            request = new GlobalBodyHttpServletRequestWrapper(servletRequest);
            //form表单形式
        } else if ((contentType.equals(MediaType.APPLICATION_FORM_URLENCODED_VALUE) || contentType.contains(MediaType.MULTIPART_FORM_DATA_VALUE))
                && !request.getParameterMap().isEmpty()) {
            //将参数放入重写的方法中
            request = new GlobalParamHttpServletRequestWrapper(servletRequest);
        }
        chain.doFilter(request, response);
    }

}
