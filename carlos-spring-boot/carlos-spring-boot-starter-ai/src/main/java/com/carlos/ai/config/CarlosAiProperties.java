package com.carlos.ai.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/**
 * Carlos AI 配置属性
 *
 * <p>支持多模型配置，包括 OpenAI、智谱AI、百度千帆、阿里云 DashScope、Ollama 等</p>
 *
 * <h3>配置示例：</h3>
 * <pre>{@code
 * carlos:
 *   ai:
 *     enabled: true
 *     default-provider: openai
 *     providers:
 *       openai:
 *         api-key: ${OPENAI_API_KEY}
 *         base-url: https://api.openai.com/v1
 *         model: gpt-4o
 *         temperature: 0.7
 *         max-tokens: 2000
 *       zhipu:
 *         api-key: ${ZHIPU_API_KEY}
 *         model: glm-4
 *       ollama:
 *         base-url: http://localhost:11434
 *         model: llama3
 * }</pre>
 *
 * @author Carlos
 * @version 1.0.0
 * @since 2026-04-03
 */
@Data
@ConfigurationProperties(prefix = "carlos.ai")
public class CarlosAiProperties {

    /**
     * 是否启用 AI 功能
     */
    private boolean enabled = true;

    /**
     * 默认模型提供商
     */
    private String defaultProvider = "openai";

    /**
     * 是否启用聊天记忆功能
     */
    private boolean memoryEnabled = true;

    /**
     * 聊天记忆最大消息数
     */
    private int memoryMaxMessages = 20;

    /**
     * 连接超时时间
     */
    private Duration connectTimeout = Duration.ofSeconds(10);

    /**
     * 读取超时时间
     */
    private Duration readTimeout = Duration.ofSeconds(60);

    /**
     * 提供商配置
     */
    private Map<String, ProviderConfig> providers = new HashMap<>();

    /**
     * 嵌入模型配置
     */
    private EmbeddingConfig embedding = new EmbeddingConfig();

    /**
     * RAG 配置
     */
    private RagConfig rag = new RagConfig();

    /**
     * 提供商配置
     */
    @Data
    public static class ProviderConfig {
        /**
         * API Key
         */
        private String apiKey;

        /**
         * Base URL（可选，用于自定义端点或代理）
         */
        private String baseUrl;

        /**
         * 模型名称
         */
        private String model;

        /**
         * 温度参数 (0.0 - 2.0)
         */
        private Double temperature = 0.7;

        /**
         * 最大 Token 数
         */
        private Integer maxTokens = 2000;

        /**
         * 超时时间（秒）
         */
        private Integer timeoutSeconds = 60;

        /**
         * 是否启用流式响应
         */
        private boolean streaming = false;

        /**
         * 自定义参数
         */
        private Map<String, String> customParams = new HashMap<>();
    }

    /**
     * 嵌入模型配置
     */
    @Data
    public static class EmbeddingConfig {
        /**
         * 是否启用嵌入功能
         */
        private boolean enabled = true;

        /**
         * 嵌入模型提供商
         */
        private String provider = "openai";

        /**
         * 嵌入模型名称
         */
        private String model = "text-embedding-3-small";

        /**
         * 向量维度
         */
        private Integer dimensions = 1536;

        /**
         * 向量存储类型：redis, in-memory
         */
        private String storeType = "redis";

        /**
         * Redis 配置
         */
        private RedisConfig redis = new RedisConfig();
    }

    /**
     * Redis 配置
     */
    @Data
    public static class RedisConfig {
        /**
         * Redis 主机
         */
        private String host = "localhost";

        /**
         * Redis 端口
         */
        private int port = 6379;

        /**
         * Redis 密码
         */
        private String password;

        /**
         * 向量索引名称
         */
        private String indexName = "carlos-ai-embeddings";
    }

    /**
     * RAG 配置
     */
    @Data
    public static class RagConfig {
        /**
         * 是否启用 RAG
         */
        private boolean enabled = true;

        /**
         * 文档分块大小
         */
        private int chunkSize = 1000;

        /**
         * 分块重叠大小
         */
        private int chunkOverlap = 100;

        /**
         * 检索时返回的最大结果数
         */
        private int maxResults = 5;

        /**
         * 相似度阈值 (0.0 - 1.0)
         */
        private double similarityThreshold = 0.7;
    }
}
