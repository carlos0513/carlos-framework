package com.carlos.ai.service;

import java.util.function.Consumer;

/**
 * <p>
 * AI 对话服务接口
 * </p>
 *
 * <p>
 * 提供单轮对话、多轮对话（基于 Session 记忆）、流式输出等能力。
 * </p>
 *
 * @author carlos
 * @since 3.0.0
 */
public interface AiChatService {

    /**
     * 单轮对话（无记忆）
     *
     * @param message 用户消息
     * @return AI 回复内容
     */
    String chat(String message);

    /**
     * 多轮对话（基于 Session 记忆）
     *
     * @param sessionId 会话标识
     * @param message   用户消息
     * @return AI 回复内容
     */
    String chat(String sessionId, String message);

    /**
     * 流式单轮对话
     *
     * @param message  用户消息
     * @param consumer 内容消费回调（逐字/逐句接收）
     */
    void chatStream(String message, Consumer<String> consumer);

    /**
     * 流式多轮对话（基于 Session 记忆）
     *
     * @param sessionId 会话标识
     * @param message   用户消息
     * @param consumer  内容消费回调（逐字/逐句接收）
     */
    void chatStream(String sessionId, String message, Consumer<String> consumer);

}
