package com.carlos.boot;

import com.google.common.collect.Sets;

import java.util.Set;

/**
 * <p>
 * springboot 常量管理
 * </p>
 *
 * @author Carlos
 * @date 2023/5/5 9:35
 */
public interface BootConstant {

    /**
     * 通用忽略过滤路径
     */
    Set<String> COMMON_EXCLUDE_PATH = Sets.newHashSet(
        "/swagger-ui.html",
        "/swagger-ui/**",
        "/doc.html",
        "/favicon.ico",
        "/swagger-resources/**",
        "/v3/api-docs/**",
        "/webjars/**",
        "/csrf",
        "/v3/api-docs-ext"
    );

    /**
     * 匹配所有路径
     */
    String ALL_PATH = "/**";

    /**
     * 过滤器排除的路径
     */
    String FILTER_INIT_PARAM_EXCLUDES_URIS_ = "excludedUris";

}
