package com.carlos.message.boot;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * <p>
 * 消息客户端配置属性
 * </p>
 *
 * @author Carlos
 * @date 2026/3/11
 */
@Data
@ConfigurationProperties(prefix = "carlos.message.client")
public class MessageClientProperties {

    /**
     * 是否启用
     */
    private boolean enabled = true;

    /**
     * 服务URL（用于非服务发现场景）
     */
    private String url;

    /**
     * 连接超时（毫秒）
     */
    private int connectTimeout = 5000;

    /**
     * 读取超时（毫秒）
     */
    private int readTimeout = 10000;
}
