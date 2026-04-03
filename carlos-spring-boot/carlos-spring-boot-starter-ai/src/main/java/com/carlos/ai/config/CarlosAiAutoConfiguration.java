package com.carlos.ai.config;

import com.carlos.ai.chat.AiChatService;
import com.carlos.ai.embedding.AiEmbeddingService;
import com.carlos.ai.model.AiModelProviderFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * Carlos AI 自动配置类
 *
 * @author Carlos
 * @version 1.0.0
 * @since 2026-04-03
 */
@Slf4j
@AutoConfiguration
@ConditionalOnProperty(prefix = "carlos.ai", name = "enabled", havingValue = "true", matchIfMissing = true)
@EnableConfigurationProperties(CarlosAiProperties.class)
public class CarlosAiAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public AiModelProviderFactory aiModelProviderFactory() {
        log.info("初始化 AI 模型提供者工厂");
        return new AiModelProviderFactory();
    }

    @Bean
    @ConditionalOnMissingBean
    public AiChatService aiChatService(CarlosAiProperties aiProperties, AiModelProviderFactory modelFactory) {
        log.info("初始化 AI 聊天服务");
        return new AiChatService(aiProperties, modelFactory);
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "carlos.ai.embedding", name = "enabled", havingValue = "true", matchIfMissing = true)
    public AiEmbeddingService aiEmbeddingService(CarlosAiProperties aiProperties) {
        log.info("初始化 AI 嵌入服务");
        return new AiEmbeddingService(aiProperties);
    }
}
