package com.carlos.ai.embedding;

import com.carlos.ai.config.CarlosAiProperties;
import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.DocumentSplitter;
import dev.langchain4j.data.document.splitter.DocumentSplitters;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.openai.OpenAiEmbeddingModel;
import dev.langchain4j.store.embedding.EmbeddingMatch;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import dev.langchain4j.store.embedding.redis.RedisEmbeddingStore;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.util.List;

/**
 * AI 嵌入向量服务
 *
 * <p>提供文档嵌入、向量存储和相似度检索功能</p>
 *
 * <h3>使用示例：</h3>
 * <pre>{@code
 * @Autowired
 * private AiEmbeddingService embeddingService;
 *
 * // 添加文档到向量库
 * embeddingService.addDocument("Spring Boot是一个开源的Java框架...", "spring-boot-intro");
 *
 * // 搜索相似内容
 * List<String> results = embeddingService.search("什么是Spring Boot？", 5);
 * }</pre>
 *
 * @author Carlos
 * @version 1.0.0
 * @since 2026-04-03
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AiEmbeddingService {

    private final CarlosAiProperties aiProperties;

    private EmbeddingModel embeddingModel;
    private EmbeddingStore<TextSegment> embeddingStore;

    @PostConstruct
    public void init() {
        if (!aiProperties.getEmbedding().isEnabled()) {
            log.warn("嵌入功能已禁用");
            return;
        }

        log.info("初始化 AI 嵌入服务");

        // 初始化嵌入模型
        initEmbeddingModel();

        // 初始化向量存储
        initEmbeddingStore();

        log.info("AI 嵌入服务初始化完成");
    }

    /**
     * 添加文档到向量库
     *
     * @param content   文档内容
     * @param sourceId  文档来源ID
     */
    public void addDocument(String content, String sourceId) {
        addDocument(content, sourceId, null);
    }

    /**
     * 添加文档到向量库（带元数据）
     *
     * @param content   文档内容
     * @param sourceId  文档来源ID
     * @param metadata  元数据
     */
    public void addDocument(String content, String sourceId, dev.langchain4j.data.document.Metadata metadata) {
        if (embeddingStore == null) {
            throw new IllegalStateException("嵌入存储未初始化");
        }

        TextSegment segment = TextSegment.from(content, metadata != null ? metadata : dev.langchain4j.data.document.Metadata.from(dev.langchain4j.data.document.Metadata.metadata("source", sourceId)));
        Embedding embedding = embeddingModel.embed(segment).content();

        embeddingStore.add(embedding, segment);
        log.debug("文档已添加到向量库: {}", sourceId);
    }

    /**
     * 批量添加文档
     *
     * @param contents  文档内容列表
     * @param sourceId  文档来源ID
     */
    public void addDocuments(List<String> contents, String sourceId) {
        for (int i = 0; i < contents.size(); i++) {
            addDocument(contents.get(i), sourceId + "-" + i);
        }
    }

    /**
     * 搜索相似内容
     *
     * @param query      查询文本
     * @param maxResults 最大结果数
     * @return 相似内容列表
     */
    public List<String> search(String query, int maxResults) {
        if (embeddingStore == null) {
            throw new IllegalStateException("嵌入存储未初始化");
        }

        Embedding queryEmbedding = embeddingModel.embed(query).content();
        List<EmbeddingMatch<TextSegment>> matches = embeddingStore.findRelevant(queryEmbedding, maxResults);

        return matches.stream()
            .map(match -> match.embedded().text())
            .toList();
    }

    /**
     * 搜索相似内容（带分数）
     *
     * @param query      查询文本
     * @param maxResults 最大结果数
     * @param minScore   最小相似度分数
     * @return 相似内容列表
     */
    public List<EmbeddingResult> searchWithScore(String query, int maxResults, double minScore) {
        if (embeddingStore == null) {
            throw new IllegalStateException("嵌入存储未初始化");
        }

        Embedding queryEmbedding = embeddingModel.embed(query).content();
        List<EmbeddingMatch<TextSegment>> matches = embeddingStore.findRelevant(queryEmbedding, maxResults, minScore);

        return matches.stream()
            .map(match -> new EmbeddingResult(
                match.embedded().text(),
                match.score(),
                match.embedded().metadata().toString()
            ))
            .toList();
    }

    /**
     * 删除文档
     *
     * @param sourceId 文档来源ID
     */
    public void deleteDocument(String sourceId) {
        // Redis Embedding Store 暂时不支持按 ID 删除
        log.warn("删除功能未实现，请手动清理 Redis 中的向量数据");
    }

    // ==================== 私有方法 ====================

    private void initEmbeddingModel() {
        CarlosAiProperties.EmbeddingConfig config = aiProperties.getEmbedding();

        String provider = config.getProvider();
        String model = config.getModel();

        log.info("初始化嵌入模型: provider={}, model={}", provider, model);

        switch (provider.toLowerCase()) {
            case "openai" -> {
                CarlosAiProperties.ProviderConfig openaiConfig = aiProperties.getProviders().get("openai");
                if (openaiConfig == null) {
                    throw new IllegalStateException("OpenAI 配置不存在");
                }
                this.embeddingModel = OpenAiEmbeddingModel.builder()
                    .apiKey(openaiConfig.getApiKey())
                    .modelName(model)
                    .build();
            }
            default -> throw new IllegalArgumentException("不支持的嵌入模型提供商: " + provider);
        }
    }

    private void initEmbeddingStore() {
        CarlosAiProperties.EmbeddingConfig config = aiProperties.getEmbedding();
        String storeType = config.getStoreType();

        log.info("初始化向量存储: type={}", storeType);

        switch (storeType.toLowerCase()) {
            case "redis" -> {
                CarlosAiProperties.RedisConfig redisConfig = config.getRedis();
                this.embeddingStore = RedisEmbeddingStore.builder()
                    .host(redisConfig.getHost())
                    .port(redisConfig.getPort())
                    .password(redisConfig.getPassword())
                    .indexName(redisConfig.getIndexName())
                    .dimension(config.getDimensions())
                    .build();
            }
            default -> throw new IllegalArgumentException("不支持的向量存储类型: " + storeType);
        }
    }

    /**
     * 嵌入结果
     */
    public record EmbeddingResult(String content, double score, String metadata) {
    }
}
