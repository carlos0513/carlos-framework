package com.carlos.boot.filters;

import cn.hutool.extra.servlet.JakartaServletUtil;
import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;

/**
 * <p>
 * 自定义request请求封装,解决请求中的参数无法二次读取的问题
 * </p>
 *
 * @author carlos
 * @date 2020/4/27 0:10
 */
public class GlobalBodyHttpServletRequestWrapper extends HttpServletRequestWrapper {


    private final String body;

    public GlobalBodyHttpServletRequestWrapper(final HttpServletRequest request) {
        super(request);
        body = JakartaServletUtil.getBody(request);
    }

    @Override
    public BufferedReader getReader() {
        return new BufferedReader(new InputStreamReader(getInputStream()));
    }

    @Override
    public ServletInputStream getInputStream() {
        final ByteArrayInputStream bais = new ByteArrayInputStream(body.getBytes());
        return new ServletInputStream() {

            @Override
            public boolean isFinished() {
                return false;
            }

            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public void setReadListener(final ReadListener listener) {
            }

            @Override
            public int read() {
                return bais.read();
            }
        };
    }

    /**
     * 获取请求的内容
     *
     * @author carlos
     * @date 2020/5/22 17:24
     */
    public String getBody() {
        return this.body;
    }

}