package com.carlos.ai.model;

import com.carlos.ai.config.CarlosAiProperties;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.chat.StreamingChatLanguageModel;
import dev.langchain4j.model.dashscope.QwenChatModel;
import dev.langchain4j.model.dashscope.QwenStreamingChatModel;
import dev.langchain4j.model.ollama.OllamaChatModel;
import dev.langchain4j.model.ollama.OllamaStreamingChatModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.openai.OpenAiStreamingChatModel;
import dev.langchain4j.model.qianfan.QianfanChatModel;
import dev.langchain4j.model.qianfan.QianfanStreamingChatModel;
import dev.langchain4j.model.zhipu.ZhipuAiChatModel;
import dev.langchain4j.model.zhipu.ZhipuAiStreamingChatModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Duration;

/**
 * AI 模型提供者工厂
 *
 * <p>根据配置创建对应的 ChatLanguageModel 实例</p>
 *
 * @author Carlos
 * @version 1.0.0
 * @since 2026-04-03
 */
@Slf4j
@Component
public class AiModelProviderFactory {

    /**
     * 创建聊天模型
     *
     * @param provider 提供商名称
     * @param config   配置
     * @return ChatLanguageModel
     */
    public ChatLanguageModel createChatModel(String provider, CarlosAiProperties.ProviderConfig config) {
        if (config == null) {
            throw new IllegalArgumentException("Provider config cannot be null");
        }

        log.info("创建 {} 聊天模型: {}", provider, config.getModel());

        return switch (provider.toLowerCase()) {
            case "openai" -> createOpenAiModel(config);
            case "zhipu", "zhipuai" -> createZhipuModel(config);
            case "qianfan", "baidu" -> createQianfanModel(config);
            case "dashscope", "aliyun" -> createDashscopeModel(config);
            case "ollama" -> createOllamaModel(config);
            default -> throw new IllegalArgumentException("Unsupported provider: " + provider);
        };
    }

    /**
     * 创建流式聊天模型
     *
     * @param provider 提供商名称
     * @param config   配置
     * @return StreamingChatLanguageModel
     */
    public StreamingChatLanguageModel createStreamingChatModel(String provider, CarlosAiProperties.ProviderConfig config) {
        if (config == null) {
            throw new IllegalArgumentException("Provider config cannot be null");
        }

        log.info("创建 {} 流式聊天模型: {}", provider, config.getModel());

        return switch (provider.toLowerCase()) {
            case "openai" -> createOpenAiStreamingModel(config);
            case "zhipu", "zhipuai" -> createZhipuStreamingModel(config);
            case "qianfan", "baidu" -> createQianfanStreamingModel(config);
            case "dashscope", "aliyun" -> createDashscopeStreamingModel(config);
            case "ollama" -> createOllamaStreamingModel(config);
            default -> throw new IllegalArgumentException("Unsupported provider: " + provider);
        };
    }

    // ==================== OpenAI ====================

    private ChatLanguageModel createOpenAiModel(CarlosAiProperties.ProviderConfig config) {
        OpenAiChatModel.OpenAiChatModelBuilder builder = OpenAiChatModel.builder()
            .apiKey(config.getApiKey())
            .modelName(config.getModel())
            .temperature(config.getTemperature())
            .timeout(Duration.ofSeconds(config.getTimeoutSeconds()));

        if (config.getBaseUrl() != null) {
            builder.baseUrl(config.getBaseUrl());
        }

        if (config.getMaxTokens() != null) {
            builder.maxTokens(config.getMaxTokens());
        }

        return builder.build();
    }

    private StreamingChatLanguageModel createOpenAiStreamingModel(CarlosAiProperties.ProviderConfig config) {
        OpenAiStreamingChatModel.OpenAiStreamingChatModelBuilder builder = OpenAiStreamingChatModel.builder()
            .apiKey(config.getApiKey())
            .modelName(config.getModel())
            .temperature(config.getTemperature())
            .timeout(Duration.ofSeconds(config.getTimeoutSeconds()));

        if (config.getBaseUrl() != null) {
            builder.baseUrl(config.getBaseUrl());
        }

        if (config.getMaxTokens() != null) {
            builder.maxTokens(config.getMaxTokens());
        }

        return builder.build();
    }

    // ==================== ZhipuAI (智谱) ====================

    private ChatLanguageModel createZhipuModel(CarlosAiProperties.ProviderConfig config) {
        ZhipuAiChatModel.ZhipuAiChatModelBuilder builder = ZhipuAiChatModel.builder()
            .apiKey(config.getApiKey())
            .model(config.getModel())
            .temperature(config.getTemperature())
            .timeout(Duration.ofSeconds(config.getTimeoutSeconds()));

        if (config.getMaxTokens() != null) {
            builder.maxTokens(config.getMaxTokens());
        }

        return builder.build();
    }

    private StreamingChatLanguageModel createZhipuStreamingModel(CarlosAiProperties.ProviderConfig config) {
        ZhipuAiStreamingChatModel.ZhipuAiStreamingChatModelBuilder builder = ZhipuAiStreamingChatModel.builder()
            .apiKey(config.getApiKey())
            .model(config.getModel())
            .temperature(config.getTemperature())
            .timeout(Duration.ofSeconds(config.getTimeoutSeconds()));

        if (config.getMaxTokens() != null) {
            builder.maxTokens(config.getMaxTokens());
        }

        return builder.build();
    }

    // ==================== QianFan (百度千帆) ====================

    private ChatLanguageModel createQianfanModel(CarlosAiProperties.ProviderConfig config) {
        return QianfanChatModel.builder()
            .apiKey(config.getApiKey())
            .modelName(config.getModel())
            .temperature(config.getTemperature())
            .timeout(Duration.ofSeconds(config.getTimeoutSeconds()))
            .build();
    }

    private StreamingChatLanguageModel createQianfanStreamingModel(CarlosAiProperties.ProviderConfig config) {
        return QianfanStreamingChatModel.builder()
            .apiKey(config.getApiKey())
            .modelName(config.getModel())
            .temperature(config.getTemperature())
            .timeout(Duration.ofSeconds(config.getTimeoutSeconds()))
            .build();
    }

    // ==================== DashScope (阿里云) ====================

    private ChatLanguageModel createDashscopeModel(CarlosAiProperties.ProviderConfig config) {
        return QwenChatModel.builder()
            .apiKey(config.getApiKey())
            .modelName(config.getModel())
            .temperature(config.getTemperature())
            .timeout(Duration.ofSeconds(config.getTimeoutSeconds()))
            .build();
    }

    private StreamingChatLanguageModel createDashscopeStreamingModel(CarlosAiProperties.ProviderConfig config) {
        return QwenStreamingChatModel.builder()
            .apiKey(config.getApiKey())
            .modelName(config.getModel())
            .temperature(config.getTemperature())
            .timeout(Duration.ofSeconds(config.getTimeoutSeconds()))
            .build();
    }

    // ==================== Ollama (本地模型) ====================

    private ChatLanguageModel createOllamaModel(CarlosAiProperties.ProviderConfig config) {
        OllamaChatModel.OllamaChatModelBuilder builder = OllamaChatModel.builder()
            .modelName(config.getModel())
            .temperature(config.getTemperature())
            .timeout(Duration.ofSeconds(config.getTimeoutSeconds()));

        if (config.getBaseUrl() != null) {
            builder.baseUrl(config.getBaseUrl());
        }

        return builder.build();
    }

    private StreamingChatLanguageModel createOllamaStreamingModel(CarlosAiProperties.ProviderConfig config) {
        OllamaStreamingChatModel.OllamaStreamingChatModelBuilder builder = OllamaStreamingChatModel.builder()
            .modelName(config.getModel())
            .temperature(config.getTemperature())
            .timeout(Duration.ofSeconds(config.getTimeoutSeconds()));

        if (config.getBaseUrl() != null) {
            builder.baseUrl(config.getBaseUrl());
        }

        return builder.build();
    }
}
