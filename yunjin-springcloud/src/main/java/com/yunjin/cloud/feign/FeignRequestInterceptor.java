package com.yunjin.cloud.feign;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.http.Header;
import com.yunjin.core.constant.CoreConstant;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Collection;
import java.util.Enumeration;
import java.util.Map;

/**
 * <p>
 * 传递请求头信息
 * </p>
 *
 * @author yunjin
 * @date 2021/12/7 15:06
 */
@Slf4j
public class FeignRequestInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate requestTemplate) {
        Map<String, Collection<String>> headers = requestTemplate.headers();
        Collection<String> paltReq = headers.get("Platform-Request");
        if (CollUtil.isNotEmpty(paltReq)) {
            return;
        }

        // 标志该请求为feign请求
        requestTemplate.header(CoreConstant.HEADER_NAME_RPC, CoreConstant.HEADER_RPC_VALUE_FEIGN);
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            return;
        }
        HttpServletRequest request = attributes.getRequest();
        Enumeration<String> headerNames = request.getHeaderNames();
        if (headerNames == null) {
            return;
        }
        while (headerNames.hasMoreElements()) {
            String name = headerNames.nextElement();
            String values = request.getHeader(name);
            // 跳过 content-length
            if (CharSequenceUtil.equalsIgnoreCase(Header.CONTENT_TYPE.getValue(), name)) {
                continue;
            }
            if (CharSequenceUtil.equalsIgnoreCase(Header.CONTENT_LENGTH.getValue(), name)) {
                continue;
            }
            if (CharSequenceUtil.equalsIgnoreCase("X-Forwarded-For", name)) {
                continue;
            }
            if (CharSequenceUtil.equalsIgnoreCase(Header.REFERER.getValue(), name)) {
                continue;
            }
            requestTemplate.header(name, values);
        }
    }
}