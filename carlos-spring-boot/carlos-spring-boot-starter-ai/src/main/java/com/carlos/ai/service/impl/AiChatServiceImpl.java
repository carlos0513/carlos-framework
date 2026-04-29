package com.carlos.ai.service.impl;

import com.carlos.ai.config.CarlosAiProperties;
import com.carlos.ai.enums.AiErrorCode;
import com.carlos.ai.exception.AiException;
import com.carlos.ai.service.AiChatService;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.chat.StreamingChatModel;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.chat.response.StreamingChatResponseHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

/**
 * <p>
 * AI 对话服务实现
 * </p>
 *
 * @author carlos
 * @since 3.0.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AiChatServiceImpl implements AiChatService {

    private final ObjectProvider<ChatModel> chatLanguageModelProvider;
    private final ObjectProvider<StreamingChatModel> streamingChatLanguageModelProvider;
    private final CarlosAiProperties properties;

    private final Map<String, ChatMemory> memoryStore = new ConcurrentHashMap<>();

    /**
     * 获取对话模型
     *
     * @return ChatModel
     */
    private ChatModel getChatModel() {
        ChatModel model = chatLanguageModelProvider.getIfAvailable();
        if (model == null) {
            throw AiErrorCode.AI_MODEL_NOT_CONFIGURED.exception();
        }
        return model;
    }

    /**
     * 获取流式对话模型
     *
     * @return StreamingChatModel
     */
    private StreamingChatModel getStreamingModel() {
        StreamingChatModel model = streamingChatLanguageModelProvider.getIfAvailable();
        if (model == null) {
            throw AiErrorCode.AI_STREAMING_NOT_SUPPORTED.exception();
        }
        return model;
    }

    /**
     * 获取或创建会话记忆
     *
     * @param sessionId 会话标识
     * @return ChatMemory
     */
    private ChatMemory getOrCreateMemory(String sessionId) {
        if (!properties.isMemoryEnabled()) {
            throw AiErrorCode.AI_MEMORY_NOT_ENABLED.exception();
        }
        return memoryStore.computeIfAbsent(sessionId, k ->
            MessageWindowChatMemory.builder()
                .maxMessages(properties.getMemoryMaxMessages())
                .build());
    }

    @Override
    public String chat(String message) {
        try {
            ChatModel model = getChatModel();
            return model.chat(message);
        } catch (AiException e) {
            throw e;
        } catch (Exception e) {
            log.error("AI 单轮对话失败", e);
            throw new AiException("AI 对话调用失败", e);
        }
    }

    @Override
    public String chat(String sessionId, String message) {
        try {
            ChatModel model = getChatModel();
            ChatMemory memory = getOrCreateMemory(sessionId);
            memory.add(UserMessage.from(message));
            ChatResponse response = model.chat(memory.messages());
            memory.add(response.aiMessage());
            return response.aiMessage().text();
        } catch (AiException e) {
            throw e;
        } catch (Exception e) {
            log.error("AI 多轮对话失败, sessionId={}", sessionId, e);
            throw new AiException("AI 对话调用失败", e);
        }
    }

    @Override
    public void chatStream(String message, Consumer<String> consumer) {
        chatStream(null, message, consumer);
    }

    @Override
    public void chatStream(String sessionId, String message, Consumer<String> consumer) {
        try {
            StreamingChatModel model = getStreamingModel();
            if (sessionId != null && properties.isMemoryEnabled()) {
                ChatMemory memory = getOrCreateMemory(sessionId);
                memory.add(UserMessage.from(message));
                StringBuilder fullResponse = new StringBuilder();
                model.chat(memory.messages(), new StreamingChatResponseHandler() {
                    @Override
                    public void onPartialResponse(String token) {
                        consumer.accept(token);
                        fullResponse.append(token);
                    }

                    @Override
                    public void onCompleteResponse(ChatResponse response) {
                        memory.add(AiMessage.from(fullResponse.toString()));
                    }

                    @Override
                    public void onError(Throwable error) {
                        log.error("AI 流式对话失败, sessionId={}", sessionId, error);
                        throw new AiException("AI 流式对话调用失败", error);
                    }
                });
            } else {
                model.chat(message, new StreamingChatResponseHandler() {
                    @Override
                    public void onPartialResponse(String token) {
                        consumer.accept(token);
                    }

                    @Override
                    public void onCompleteResponse(ChatResponse response) {
                        // 单轮流式无需处理记忆
                    }

                    @Override
                    public void onError(Throwable error) {
                        log.error("AI 流式对话失败", error);
                        throw new AiException("AI 流式对话调用失败", error);
                    }
                });
            }
        } catch (AiException e) {
            throw e;
        } catch (Exception e) {
            log.error("AI 流式对话失败", e);
            throw new AiException("AI 流式对话调用失败", e);
        }
    }

}
