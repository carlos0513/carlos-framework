package com.carlos.ai.chat;

import com.carlos.ai.config.CarlosAiProperties;
import com.carlos.ai.model.AiModelProviderFactory;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.chat.StreamingChatLanguageModel;
import dev.langchain4j.model.output.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import com.carlos.core.exception.BusinessException;

/**
 * AI 聊天服务
 *
 * <p>提供简单易用的 AI 对话功能，支持多轮对话、流式输出、多模型切换</p>
 *
 * <h3>使用示例：</h3>
 * <pre>{@code
 * @Autowired
 * private AiChatService chatService;
 *
 * // 简单对话
 * String response = chatService.chat("你好，请介绍一下Spring Boot");
 *
 * // 带上下文的对话
 * String response = chatService.chat("session-123", "你好");
 *
 * // 流式输出
 * chatService.chatStream("你好", content -> {
 *     System.out.print(content);
 * });
 *
 * // 切换模型
 * String response = chatService.chatWithProvider("zhipu", "你好");
 * }</pre>
 *
 * @author Carlos
 * @version 1.0.0
 * @since 2026-04-03
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AiChatService {

    private final CarlosAiProperties aiProperties;
    private final AiModelProviderFactory modelFactory;

    /**
     * 默认模型实例（延迟初始化）
     */
    private ChatLanguageModel defaultChatModel;

    /**
     * 默认流式模型实例
     */
    private StreamingChatLanguageModel defaultStreamingModel;

    /**
     * 模型缓存
     */
    private final Map<String, ChatLanguageModel> chatModelCache = new ConcurrentHashMap<>();
    private final Map<String, StreamingChatLanguageModel> streamingModelCache = new ConcurrentHashMap<>();

    /**
     * 对话历史缓存（简单内存实现）
     */
    private final Map<String, List<ChatMessage>> conversationHistory = new ConcurrentHashMap<>();

    @PostConstruct
    public void init() {
        if (!aiProperties.isEnabled()) {
            log.warn("AI 功能已禁用");
            return;
        }

        String defaultProvider = aiProperties.getDefaultProvider();
        log.info("初始化 AI 聊天服务，默认提供商: {}", defaultProvider);

        // 初始化默认模型
        CarlosAiProperties.ProviderConfig defaultConfig = aiProperties.getProviders().get(defaultProvider);
        if (defaultConfig != null) {
            this.defaultChatModel = modelFactory.createChatModel(defaultProvider, defaultConfig);
            this.defaultStreamingModel = modelFactory.createStreamingChatModel(defaultProvider, defaultConfig);
            log.info("默认模型初始化完成");
        } else {
            log.warn("默认提供商 {} 未配置", defaultProvider);
        }
    }

    /**
     * 简单对话（无上下文）
     *
     * @param message 用户消息
     * @return AI 回复
     */
    public String chat(String message) {
        return chatWithModel(defaultChatModel, message);
    }

    /**
     * 带系统提示的对话
     *
     * @param systemPrompt 系统提示
     * @param message      用户消息
     * @return AI 回复
     */
    public String chat(String systemPrompt, String message) {
        return chatWithModel(defaultChatModel, systemPrompt, message);
    }

    /**
     * 带对话历史的对话
     *
     * @param sessionId 会话ID
     * @param message   用户消息
     * @return AI 回复
     */
    public String chat(String sessionId, String message) {
        return chatWithHistory(sessionId, null, message);
    }

    /**
     * 带对话历史和系统提示的对话
     *
     * @param sessionId    会话ID
     * @param systemPrompt 系统提示
     * @param message      用户消息
     * @return AI 回复
     */
    public String chat(String sessionId, String systemPrompt, String message) {
        return chatWithHistory(sessionId, systemPrompt, message);
    }

    /**
     * 使用指定提供商进行对话
     *
     * @param provider 提供商名称
     * @param message  用户消息
     * @return AI 回复
     */
    public String chatWithProvider(String provider, String message) {
        ChatLanguageModel model = getOrCreateChatModel(provider);
        return chatWithModel(model, message);
    }

    /**
     * 流式对话
     *
     * @param message  用户消息
     * @param consumer 内容消费回调
     */
    public void chatStream(String message, Consumer<String> consumer) {
        chatStreamWithModel(defaultStreamingModel, message, consumer);
    }

    /**
     * 带会话历史的流式对话
     *
     * @param sessionId 会话ID
     * @param message   用户消息
     * @param consumer  内容消费回调
     */
    public void chatStream(String sessionId, String message, Consumer<String> consumer) {
        List<ChatMessage> history = getConversationHistory(sessionId);
        UserMessage userMessage = UserMessage.from(message);
        history.add(userMessage);

        StringBuilder fullResponse = new StringBuilder();

        defaultStreamingModel.generate(history, new dev.langchain4j.model.output.StreamingResponseHandler<AiMessage>() {
            @Override
            public void onNext(String token) {
                fullResponse.append(token);
                consumer.accept(token);
            }

            @Override
            public void onComplete(Response<AiMessage> response) {
                history.add(response.content());
                trimConversationHistory(sessionId);
            }

            @Override
            public void onError(Throwable error) {
                log.error("流式对话出错", error);
            }
        });
    }

    /**
     * 清空对话历史
     *
     * @param sessionId 会话ID
     */
    public void clearConversation(String sessionId) {
        conversationHistory.remove(sessionId);
        log.debug("清空会话历史: {}", sessionId);
    }

    // ==================== 私有方法 ====================

    private String chatWithModel(ChatLanguageModel model, String message) {
        if (model == null) {
            throw new IllegalStateException("AI 模型未初始化");
        }

        try {
            return model.generate(message);
        } catch (Exception e) {
            log.error("AI 对话失败", e);
            throw new BusinessException("AI 对话失败: " + e.getMessage(), e);
        }
    }

    private String chatWithModel(ChatLanguageModel model, String systemPrompt, String message) {
        if (model == null) {
            throw new IllegalStateException("AI 模型未初始化");
        }

        try {
            List<ChatMessage> messages = new ArrayList<>();
            messages.add(SystemMessage.from(systemPrompt));
            messages.add(UserMessage.from(message));

            Response<AiMessage> response = model.generate(messages);
            return response.content().text();
        } catch (Exception e) {
            log.error("AI 对话失败", e);
            throw new BusinessException("AI 对话失败: " + e.getMessage(), e);
        }
    }

    private String chatWithHistory(String sessionId, String systemPrompt, String message) {
        List<ChatMessage> history = getConversationHistory(sessionId);

        // 添加系统提示（如果有且是第一次对话）
        if (systemPrompt != null && history.isEmpty()) {
            history.add(SystemMessage.from(systemPrompt));
        }

        // 添加用户消息
        history.add(UserMessage.from(message));

        // 调用模型
        Response<AiMessage> response = defaultChatModel.generate(history);
        String aiResponse = response.content().text();

        // 保存 AI 回复到历史
        history.add(response.content());

        // 裁剪历史记录
        trimConversationHistory(sessionId);

        return aiResponse;
    }

    private void chatStreamWithModel(StreamingChatLanguageModel model, String message, Consumer<String> consumer) {
        if (model == null) {
            throw new IllegalStateException("AI 流式模型未初始化");
        }

        model.generate(message, new dev.langchain4j.model.output.StreamingResponseHandler<AiMessage>() {
            @Override
            public void onNext(String token) {
                consumer.accept(token);
            }

            @Override
            public void onComplete(Response<AiMessage> response) {
                // 流式完成
            }

            @Override
            public void onError(Throwable error) {
                log.error("流式对话出错", error);
            }
        });
    }

    private ChatLanguageModel getOrCreateChatModel(String provider) {
        return chatModelCache.computeIfAbsent(provider, p -> {
            CarlosAiProperties.ProviderConfig config = aiProperties.getProviders().get(p);
            if (config == null) {
                throw new IllegalArgumentException("未找到提供商配置: " + p);
            }
            return modelFactory.createChatModel(p, config);
        });
    }

    private List<ChatMessage> getConversationHistory(String sessionId) {
        return conversationHistory.computeIfAbsent(sessionId, k -> new ArrayList<>());
    }

    private void trimConversationHistory(String sessionId) {
        List<ChatMessage> history = conversationHistory.get(sessionId);
        if (history == null) return;

        int maxMessages = aiProperties.getMemoryMaxMessages() * 2; // 用户+AI 算两条
        if (history.size() > maxMessages) {
            // 保留系统消息（如果有），删除最早的用户-AI 对话对
            int startIndex = 0;
            if (history.get(0) instanceof SystemMessage) {
                startIndex = 1;
            }
            // 删除最老的一对对话（2条消息）
            history.subList(startIndex, startIndex + 2).clear();
        }
    }
}
