package com.carlos.boot.filters;

import cn.hutool.core.io.IoUtil;
import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * <p>
 * 自定义request请求封装,解决请求中的参数无法二次读取的问题
 * </p>
 *
 * @author yunjin
 * @date 2020/4/27 0:10
 */
public class GlobalHttpServletRequestWrapper extends HttpServletRequestWrapper {


    private final ServletInputStream inputStream;

    private BufferedReader reader;

    private String requestBodyLine;

    /**
     * Constructs a request object wrapping the given request.
     *
     * @param request The request to wrap
     * @throws IllegalArgumentException if the request is null
     */
    public GlobalHttpServletRequestWrapper(HttpServletRequest request) throws IOException {
        super(request);
        //读一次 然后缓存 实现同一request多次读取
        byte[] body = IoUtil.readBytes(request.getInputStream());
        this.inputStream = new RequestCachingInputStream(body);
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        if (this.inputStream != null) {
            return this.inputStream;
        }
        return super.getInputStream();
    }

    @Override
    public BufferedReader getReader() throws IOException {
        if (this.reader == null) {
            this.reader = new BufferedReader(new InputStreamReader(getInputStream(), getCharacterEncoding()));
        }
        return this.reader;
    }


    public String getRequestBodyToStr() throws IOException {
        if (this.requestBodyLine == null) {
            BufferedReader reader = this.getReader();
            StringBuilder builder = new StringBuilder();
            String line = reader.readLine();
            while (line != null) {
                builder.append(line);
                line = reader.readLine();
            }
            requestBodyLine = builder.toString();
            reader.close();
        }
        return requestBodyLine;
    }

    //代理ServletInputStream 内容为当前缓存的bytes
    private static class RequestCachingInputStream extends ServletInputStream {

        private final ByteArrayInputStream is;

        public RequestCachingInputStream(byte[] bytes) {
            this.is = new ByteArrayInputStream(bytes);
        }

        @Override
        public int read() throws IOException {
            return this.is.read();
        }

        @Override
        public boolean isFinished() {
            return this.is.available() == 0;
        }

        @Override
        public boolean isReady() {
            return true;
        }

        @Override
        public void setReadListener(ReadListener readlistener) {
        }
    }


    // private final String body;
    //
    // public GlobalHttpServletRequestWrapper(final HttpServletRequest request) {
    //     super(request);
    //     body = JakartaServletUtil.getBody(request);
    // }
    //
    // @Override
    // public BufferedReader getReader() {
    //     return new BufferedReader(new InputStreamReader(getInputStream()));
    // }
    //
    // @Override
    // public ServletInputStream getInputStream() {
    //     final ByteArrayInputStream bais = new ByteArrayInputStream(body.getBytes());
    //     return new ServletInputStream() {
    //
    //         @Override
    //         public boolean isFinished() {
    //             return false;
    //         }
    //
    //         @Override
    //         public boolean isReady() {
    //             return false;
    //         }
    //
    //         @Override
    //         public void setReadListener(final ReadListener listener) {
    //         }
    //
    //         @Override
    //         public int read() {
    //             return bais.read();
    //         }
    //     };
    // }
    //
    // /**
    //  * 获取请求的内容
    //  *
    //  * @author yunjin
    //  * @date 2020/5/22 17:24
    //  */
    // public String getBody() {
    //     return this.body;
    // }

}