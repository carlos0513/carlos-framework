package com.carlos.gateway.observability;

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
}
