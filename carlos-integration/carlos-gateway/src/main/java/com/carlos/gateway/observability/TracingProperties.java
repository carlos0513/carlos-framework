package com.carlos.gateway.observability;

import com.carlos.core.constant.HttpHeadersConstant;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * <p>
 * 链路追踪配置属性
 * </p>
 *
 * @author carlos
 * @date 2026/3/16
 */
@Data
@ConfigurationProperties(prefix = "carlos.gateway.tracing")
public class TracingProperties {

    /**
     * 是否启用链路追踪
     */
    private boolean enabled = true;

    /**
     * 是否记录请求体
     */
    private boolean recordRequestBody = false;

    /**
     * 是否记录响应体
     */
    private boolean recordResponseBody = false;

    /**
     * 采样率（0.0 - 1.0）
     */
    private double samplingRate = 1.0;

    /**
     * 忽略的 URL 前缀
     */
    private String[] ignorePatterns = {"/actuator/**", "/health", "/prometheus"};

    /**
     * Request ID 配置
     */
    private RequestIdConfig requestId = new RequestIdConfig();

    /**
     * Request ID 配置类
     */
    @Data
    public static class RequestIdConfig {
        /**
         * 请求头名称
         */
        private String headerName = HttpHeadersConstant.X_REQUEST_ID;

        /**
         * 是否自动生成（当请求未提供时）
         */
        private boolean autoGenerate = true;

        /**
         * 是否添加到响应头
         */
        private boolean addToResponse = true;

        /**
         * 前缀
         */
        private String prefix = "";
    }
}
