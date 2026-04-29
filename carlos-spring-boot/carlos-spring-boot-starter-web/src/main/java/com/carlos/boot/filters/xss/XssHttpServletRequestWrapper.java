package com.carlos.boot.filters.xss;

import cn.hutool.core.util.StrUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.text.StringEscapeUtils;

import java.util.ArrayList;

/**
 * <p>
 * XSS 跨站脚本攻击(Cross Site Scripting) 处理
 * </p>
 *
 * @author carlos
 * @date 2020/4/15 16:05
 */
public class XssHttpServletRequestWrapper extends HttpServletRequestWrapper {

    public XssHttpServletRequestWrapper(HttpServletRequest request) {
        super(request);
    }

    @Override
    public String getQueryString() {
        return cleanXSS(super.getQueryString());
    }

    /**
     * 对数组参数进行特殊字符过滤
     */
    @Override
    public String[] getParameterValues(String name) {
        String[] values = super.getParameterValues(name);
        if (ArrayUtils.isEmpty(values)) {
            return values;
        }
        ArrayList<String> escapeValues = new ArrayList<>(values.length);
        for (String value : values) {
            escapeValues.add(cleanXSS(value));
        }
        return escapeValues.toArray(new String[0]);
    }


    /**
     * 对参数中特殊字符进行过滤
     */
    @Override
    public String getParameter(String name) {
        String value = super.getParameter(name);
        if (StrUtil.isBlank(value)) {
            return value;
        }
        return cleanXSS(value);
    }

    /**
     * 获取attribute,特殊字符过滤
     */
    @Override
    public Object getAttribute(String name) {
        Object value = super.getAttribute(name);
        if (value instanceof String str && StrUtil.isNotBlank(str)) {
            return cleanXSS(str);
        }
        return value;
    }

    /**
     * 对请求头部进行特殊字符过滤
     */
    @Override
    public String getHeader(String name) {
        String value = super.getHeader(name);
        if (StrUtil.isBlank(value)) {
            return value;
        }
        return cleanXSS(value);
    }

    /**
     * 清除特殊字符方法
     *
     * @param value 需要处理的字符串
     * @return java.lang.String
     * @author carlos
     * @date 2021/4/22 10:21
     */
    private String cleanXSS(String value) {
        return StringEscapeUtils.escapeHtml4(value);
    }
}
