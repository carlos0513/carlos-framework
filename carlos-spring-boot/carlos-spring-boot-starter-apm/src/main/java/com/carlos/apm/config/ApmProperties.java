package com.carlos.apm.config;

import com.carlos.core.constant.HttpHeadersConstant;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * APM 模块配置属性
 *
 * @author Carlos
 * @date 2024-12-09
 */
@Data
@ConfigurationProperties(prefix = "carlos.apm")
public class ApmProperties {

    /**
     * 是否启用 APM 模块
     */
    private boolean enabled = true;

    /**
     * Sleuth 追踪配置
     */
    private Sleuth sleuth = new Sleuth();

    /**
     * SkyWalking 配置
     */
    private SkyWalking skywalking = new SkyWalking();

    /**
     * MDC 上下文配置
     */
    private Mdc mdc = new Mdc();

    /**
     * Sleuth 配置
     */
    @Data
    public static class Sleuth {

        /**
         * 是否启用 Sleuth 追踪
         */
        private boolean enabled = true;

        /**
         * 采样率 (0.0 - 1.0)，1.0 表示全量采样
         */
        private double probability = 1.0;

        /**
         * 传播类型，支持 B3、W3C
         */
        private List<String> propagationType = Collections.singletonList("B3");

        /**
         * 是否启用 Web 追踪
         */
        private boolean webEnabled = true;

        /**
         * 是否启用异步追踪
         */
        private boolean asyncEnabled = true;

        /**
         * 需要跳过的 URL 路径
         */
        private List<String> skipPaths = Arrays.asList(
            "/actuator/**",
            "/health",
            "/favicon.ico",
            "/swagger-ui/**",
            "/v3/api-docs/**"
        );
    }

    /**
     * SkyWalking 配置
     */
    @Data
    public static class SkyWalking {

        /**
         * 是否启用 SkyWalking Toolkit 支持
         */
        private boolean enabled = true;

        /**
         * 是否启用日志上报
         */
        private boolean logReportEnabled = true;

        /**
         * 自定义标签配置
         */
        private List<Tag> tags = Collections.emptyList();
    }

    /**
     * 自定义标签配置
     */
    @Data
    public static class Tag {

        /**
         * 标签键
         */
        private String key;

        /**
         * 标签值表达式
         */
        private String value;
    }

    /**
     * MDC 配置
     */
    @Data
    public static class Mdc {

        /**
         * 是否启用 MDC 上下文
         */
        private boolean enabled = true;

        /**
         * Request ID 在 MDC 中的 key（兼容原有自定义 requestId）
         */
        private String requestIdKey = "requestId";

        /**
         * Trace ID 在 MDC 中的 key
         */
        private String traceIdKey = "traceId";

        /**
         * Span ID 在 MDC 中的 key
         */
        private String spanIdKey = "spanId";

        /**
         * 是否将 Request ID / Trace ID 添加到响应头
         */
        private boolean addToResponse = true;

        /**
         * Request ID 响应头名称
         */
        private String requestIdHeaderName = HttpHeadersConstant.X_REQUEST_ID;

        /**
         * Trace ID 响应头名称
         */
        private String responseHeaderName = HttpHeadersConstant.X_TRACE_ID;

        /**
         * 如果请求未提供 Request ID，是否自动生成
         */
        private boolean autoGenerateRequestId = true;
    }
}
