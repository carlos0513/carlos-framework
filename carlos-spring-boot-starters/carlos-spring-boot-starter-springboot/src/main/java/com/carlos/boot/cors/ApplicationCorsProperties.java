package com.carlos.boot.cors;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.http.HttpMethod;
import org.springframework.web.cors.CorsConfiguration;

import java.time.Duration;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * <p>
 * 跨域相关可配置属性
 * </p>
 *
 * @author carlos
 * @date 2020/6/16 9:30
 */
@Data
@ConfigurationProperties(prefix = "carlos.boot.cors")
public class ApplicationCorsProperties {


    private static final List<String> DEFAULT_ALLOW_METHODS = Collections.unmodifiableList(
            Arrays.asList(HttpMethod.GET.name(),
                    HttpMethod.HEAD.name(),
                    HttpMethod.POST.name(),
                    HttpMethod.OPTIONS.name(),
                    HttpMethod.PUT.name(),
                    // HttpMethod.TRACE.name(),
                    HttpMethod.DELETE.name()));

    /**
     * 是否启用跨域，默认启用
     */
    private boolean enable = false;

    /**
     * CORS过滤的路径，默认：/**
     */
    private String path = "/**";

    /**
     * 允许访问的源
     */
    private List<String> allowedOriginsPattens = Collections.singletonList(CorsConfiguration.ALL);

    /**
     * 允许访问的请求头
     */
    private List<String> allowedHeaders = Collections.singletonList(CorsConfiguration.ALL);

    /**
     * 是否允许发送cookie
     */
    private boolean allowCredentials = true;

    /**
     * 允许访问的请求方式
     */
    private List<String> allowedMethods = DEFAULT_ALLOW_METHODS;
    ;

    /**
     * 允许响应的头
     */
    private List<String> exposedHeaders = Collections.singletonList(CorsConfiguration.ALL);
    ;

    /**
     * 该响应的有效时间默认为30分钟，在有效时间内，浏览器无须为同一请求再次发起预检请求
     */
    private Duration maxAge = Duration.ofDays(1);

}
