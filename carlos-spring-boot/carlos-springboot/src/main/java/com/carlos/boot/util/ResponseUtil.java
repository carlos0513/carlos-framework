package com.carlos.boot.util;

import cn.hutool.extra.servlet.JakartaServletUtil;
import cn.hutool.http.ContentType;
import cn.hutool.json.JSONUtil;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * <p>
 * 响应工具类
 * </p>
 *
 * @author yunjin
 * @date 2020/4/10 16:47
 */
public final class ResponseUtil {

    /**
     * 输出json到浏览器
     *
     * @param response 响应体
     * @param object   对象内容
     * @author yunjin
     * @date 2020/4/10 16:47
     */
    public static void printJson(HttpServletResponse response, Object object) {
        response.reset();
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        JakartaServletUtil.write(response, JSONUtil.toJsonStr(object), ContentType.JSON.toString());
    }

    public static HttpServletResponse getResponse() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            return null;
        }
        return attributes.getResponse();
    }
}
