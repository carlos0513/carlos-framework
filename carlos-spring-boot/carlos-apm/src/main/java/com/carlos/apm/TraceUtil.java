package com.carlos.apm;

import brave.Span;
import brave.Tracer;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 获取IP地址工具类
 *
 * @author yunjin
 * @date 2018-11-08
 */
@Slf4j
public final class TraceUtil {


    /**
     * 获取当前traceId
     *
     * @return java.lang.String
     * @author Carlos
     * @date 2024-12-09 09:21
     */
    public static String getTraceId() {
        Tracer tracer = SpringUtil.getBean(Tracer.class);
        Span currentSpan = tracer.currentSpan();
        String traceId = StrUtil.EMPTY;
        if (currentSpan != null) {
            traceId = currentSpan.context().traceIdString();
        }
        return traceId;

    }

    /**
     * 获取当前spanId
     *
     * @return java.lang.String
     * @author Carlos
     * @date 2024-12-09 09:21
     */
    public static String getSpanId() {
        Tracer tracer = SpringUtil.getBean(Tracer.class);
        Span currentSpan = tracer.currentSpan();
        String spanId = StrUtil.EMPTY;
        if (currentSpan != null) {
            spanId = currentSpan.context().spanIdString();
        }
        return spanId;

    }

}
