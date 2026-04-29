package com.carlos.ai.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * <p>
 * Carlos AI 配置属性
 * </p>
 *
 * <p>
 * 配置前缀：{@code carlos.ai}
 * </p>
 *
 * @author carlos
 * @since 3.0.0
 */
@Data
@ConfigurationProperties(prefix = "carlos.ai")
public class CarlosAiProperties {

    /**
     * 是否启用 AI 功能
     */
    private boolean enabled = true;

    /**
     * 是否启用对话记忆
     */
    private boolean memoryEnabled = true;

    /**
     * 对话记忆最大消息数
     */
    private int memoryMaxMessages = 20;

}
