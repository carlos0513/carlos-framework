package com.carlos.boot.interceptors;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

import java.util.Set;

/**
 * application拦截器配置属性
 *
 * @author carlos
 * @date 2019/8/5
 */
@Data
@ConfigurationProperties(prefix = "carlos.boot.interceptors")
public class ApplicationInterceptorProperties {

    /**
     * 全局拦截器
     */
    @NestedConfigurationProperty
    private final GlobalInterceptorProperties globalInterceptor = new GlobalInterceptorProperties();


    /**
     * 拦截器基本信息
     */
    @Data
    public static class InterceptorProperties {

        /**
         * 是否启用
         */
        private boolean enable;

        /**
         * 包含的路径
         */
        private Set<String> includePaths;

        /**
         * 排除路径
         */
        private Set<String> excludePaths;

    }
}
