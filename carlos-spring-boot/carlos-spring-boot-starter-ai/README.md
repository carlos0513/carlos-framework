# Carlos AI Starter

基于 LangChain4j 的 AI 集成 Starter，支持多种大语言模型和向量数据库。

## 功能特性

- 🤖 **多模型支持**：OpenAI、智谱AI、百度千帆、阿里云 DashScope、Ollama
- 💬 **对话管理**：支持多轮对话、流式输出、对话历史记忆
- 🔤 **嵌入向量**：文档向量化、相似度检索、Redis 向量存储
- 🧩 **简单集成**：Spring Boot 自动配置，开箱即用

## 快速开始

### 1. 添加依赖

```xml
<dependency>
    <groupId>com.carlos</groupId>
    <artifactId>carlos-spring-boot-starter-ai</artifactId>
    <version>${carlos.version}</version>
</dependency>
```

### 2. 配置 AI 参数

```yaml
carlos:
  ai:
    enabled: true
    default-provider: openai
    memory-enabled: true
    memory-max-messages: 20
    
    providers:
      openai:
        api-key: ${OPENAI_API_KEY}
        model: gpt-4o
        temperature: 0.7
        max-tokens: 2000
      
      zhipu:
        api-key: ${ZHIPU_API_KEY}
        model: glm-4
        temperature: 0.7
      
      ollama:
        base-url: http://localhost:11434
        model: llama3
    
    embedding:
      enabled: true
      provider: openai
      model: text-embedding-3-small
      store-type: redis
      redis:
        host: localhost
        port: 6379
        index-name: carlos-ai-embeddings
```

### 3. 使用 AI 服务

```java
@Autowired
private AiChatService chatService;

// 简单对话
String response = chatService.chat("你好，请介绍一下 Spring Boot");

// 带上下文的对话
String response = chatService.chat("session-123", "你好");

// 流式输出
chatService.chatStream("你好", content -> {
    System.out.print(content);
});

// 切换模型
String response = chatService.chatWithProvider("zhipu", "你好");
```

### 4. 使用嵌入向量服务

```java
@Autowired
private AiEmbeddingService embeddingService;

// 添加文档
embeddingService.addDocument("Spring Boot 是一个开源的 Java 框架...", "doc-1");

// 搜索相似内容
List<String> results = embeddingService.search("什么是 Spring Boot？", 5);
```

## 支持的模型提供商

| 提供商 | 配置名称 | 说明 |
|--------|----------|------|
| OpenAI | `openai` | GPT-4, GPT-3.5 等 |
| 智谱AI | `zhipu` | GLM-4, GLM-3 等 |
| 百度千帆 | `qianfan` | 文心一言等 |
| 阿里云 | `dashscope` | 通义千问等 |
| Ollama | `ollama` | 本地模型，如 Llama3 |

## 配置属性

| 属性 | 默认值 | 说明 |
|------|--------|------|
| `carlos.ai.enabled` | `true` | 是否启用 AI 功能 |
| `carlos.ai.default-provider` | `openai` | 默认模型提供商 |
| `carlos.ai.memory-enabled` | `true` | 是否启用对话记忆 |
| `carlos.ai.memory-max-messages` | `20` | 最大记忆消息数 |

## 更多示例

见框架示例项目：`carlos-samples/carlos-sample-ai`

## 版本历史

- **1.0.0** (2026-04-03)
  - 初始版本
  - 支持多模型对话
  - 支持嵌入向量检索
