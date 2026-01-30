package com.carlos.boot.interceptors;

import lombok.Data;

import java.util.HashSet;
import java.util.Set;

/**
 * <p>
 * 全局拦截器基本信息
 * </p>
 *
 * @author carlos
 * @date 2020/6/5 10:13
 */
@Data
public class GlobalInterceptorProperties {

    /**
     * 打印是否格式化
     */
    private boolean format = true;

    /**
     * 日志输出类型：print-type, 默认按照顺序打印
     */
    private PrintType printType = PrintType.REQUEST;

    /**
     * 排除路径
     */
    private Set<String> excludePaths = new HashSet<>();

    /**
     * 包含的路径
     */
    private Set<String> includePaths = new HashSet<>();


    /**
     * 日志打印类型
     *
     * @author carlos
     * @date 2020/3/19
     **/
    public enum PrintType {

        /**
         * 不打印
         */
        NONE,
        /**
         * 打印请求信息
         */
        REQUEST,
        /**
         * 打印响应信息
         */
        RESPONSE,
        /**
         * 同时打印请求信息和响应信息, 按顺序打印
         */
        BOTH_ORDER,
        /**
         * 同一位置打印请求信息和响应信息
         */
        BOTH_TOGETHER,

    }

}