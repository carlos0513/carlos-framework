# Carlos Gateway 现代网关设计文档

## 一、概述

### 1.1 设计目标

Carlos Gateway 是一个现代化的云原生 API 网关，集成了当前最流行的网关功能，为企业提供统一的流量入口、安全防护和可观测性能力。

### 1.2 技术架构

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                           Carlos Gateway                                     │
│                                                                              │
│  ┌─────────────────────────────────────────────────────────────────────┐   │
│  │                        入口层 (Ingress)                              │   │
│  │   WAF Filter → Replay Protection → Tracing → Metrics                │   │
│  └─────────────────────────────────────────────────────────────────────┘   │
│                                    │                                        │
│                                    ▼                                        │
│  ┌─────────────────────────────────────────────────────────────────────┐   │
│  │                     认证授权层 (Security)                             │   │
│  │   OAuth2 Filter (JWT/Opaque) → Authorization Filter                 │   │
│  │              Token Validator → Permission Provider                    │   │
│  └─────────────────────────────────────────────────────────────────────┘   │
│                                    │                                        │
│                                    ▼                                        │
│  ┌─────────────────────────────────────────────────────────────────────┐   │
│  │                      流量控制层 (Traffic)                             │   │
│  │   Rate Limit (Redis) → Circuit Breaker → Gray Release               │   │
│  └─────────────────────────────────────────────────────────────────────┘   │
│                                    │                                        │
│                                    ▼                                        │
│  ┌─────────────────────────────────────────────────────────────────────┐   │
│  │                      转换层 (Transform)                               │   │
│  │   API Version → Path Rewrite → Header Transform                     │   │
│  └─────────────────────────────────────────────────────────────────────┘   │
│                                    │                                        │
│                                    ▼                                        │
│  ┌─────────────────────────────────────────────────────────────────────┐   │
│  │                      路由层 (Routing)                                 │   │
│  │   Load Balancer → Service Discovery (Nacos)                         │   │
│  └─────────────────────────────────────────────────────────────────────┘   │
│                                                                              │
└─────────────────────────────────────────────────────────────────────────────┘
```

### 1.3 核心特性

| 特性            | 实现方案                    | 说明                   |
|---------------|-------------------------|----------------------|
| **OAuth2 认证** | JWT + Opaque Token 双模式  | 支持自包含令牌和中心化验证        |
| **分布式限流**     | Redis + Lua 令牌桶         | 支持 IP/用户/API 多维度限流   |
| **熔断降级**      | Resilience4j            | 服务级别熔断保护             |
| **灰度发布**      | 多策略灰度路由                 | 支持权重、用户、版本、Header 灰度 |
| **WAF 防护**    | 多维度安全检测                 | SQL 注入、XSS、路径遍历等     |
| **防重放攻击**     | 时间戳 + Nonce             | 基于 Redis 的重复请求检测     |
| **链路追踪**      | Micrometer Tracing      | 兼容 Zipkin/Jaeger     |
| **指标监控**      | Micrometer + Prometheus | 全链路指标采集              |
| **多级缓存**      | Caffeine + Redis        | 响应缓存加速               |
| **API 版本控制**  | Header/URL 双模式          | 平滑版本升级               |

---

## 二、模块结构

```
carlos-gateway/
├── src/main/java/com/carlos/gateway/
│   ├── oauth2/                           # OAuth2 认证模块
│   │   ├── OAuth2GatewayProperties.java      # OAuth2 配置属性
│   │   ├── TokenValidator.java               # Token 验证器接口
│   │   ├── JwtTokenValidator.java            # JWT 验证器
│   │   ├── OpaqueTokenValidator.java         # Opaque Token 验证器
│   │   ├── OAuth2AuthenticationFilter.java   # 认证过滤器
│   │   └── OAuth2AuthorizationFilter.java    # 鉴权过滤器
│   ├── ratelimit/                        # 限流模块
│   │   └── RedisRateLimiter.java             # Redis 令牌桶限流
│   ├── circuitbreaker/                   # 熔断模块
│   │   └── Resilience4jCircuitBreakerFilter.java  # 熔断过滤器
│   ├── gray/                             # 灰度发布模块
│   │   ├── GrayReleaseFilter.java            # 灰度过滤器
│   │   └── GrayReleaseProperties.java        # 灰度配置
│   ├── security/                         # 安全防护模块
│   │   ├── WafFilter.java                    # WAF 过滤器
│   │   ├── WafProperties.java                # WAF 配置
│   │   ├── ReplayProtectionFilter.java       # 防重放过滤器
│   │   └── ReplayProtectionProperties.java   # 防重放配置
│   ├── cache/                            # 缓存模块
│   │   ├── ResponseCacheFilter.java          # 响应缓存过滤器
│   │   └── CacheProperties.java              # 缓存配置
│   ├── observability/                    # 可观测性模块
│   │   ├── TracingFilter.java                # 链路追踪过滤器
│   │   ├── TracingProperties.java            # 追踪配置
│   │   └── MetricsFilter.java                # 指标收集过滤器
│   ├── transform/                        # 转换模块
│   │   ├── RequestTransformFilter.java       # 请求转换过滤器
│   │   └── TransformProperties.java          # 转换配置
│   └── config/                           # 配置模块
│       └── ModernGatewayConfig.java          # 网关自动配置
└── src/main/resources/
    └── application-modern.yml                # 完整配置示例
```

---

## 三、过滤器执行顺序

```
执行顺序（数值越小优先级越高）：

-5000  WafFilter                    # WAF 防护（最先执行）
-4000  ReplayProtectionFilter       # 防重放攻击
-3000  TracingFilter                # 链路追踪
-2000  OAuth2AuthenticationFilter   # OAuth2 认证
-1500  OAuth2AuthorizationFilter    # OAuth2 鉴权
-1000  RedisRateLimiter             # 限流
-100   GrayReleaseFilter            # 灰度发布
  0    (Spring Cloud Gateway 默认)
 100   RequestTransformFilter       # 请求转换
 500   Resilience4jCircuitBreakerFilter  # 熔断
1000   ResponseCacheFilter          # 响应缓存
```

---

## 四、核心功能详解

### 4.1 OAuth2 认证（双模式）

#### JWT 模式

```yaml
carlos:
  gateway:
    oauth2:
      token-type: JWT
      jwt-public-key: "${JWT_PUBLIC_KEY}"
      jwt-issuer: carlos-auth
      jwt-audience: carlos-gateway
```

**流程：**

1. 从请求头提取 Token
2. 使用公钥验证 JWT 签名
3. 验证发行者和受众
4. 解析 Token 载荷构建用户上下文
5. 将用户信息注入下游请求头

#### Opaque Token 模式

```yaml
carlos:
  gateway:
    oauth2:
      token-type: OPAQUE
      introspection-uri: http://carlos-auth/oauth2/introspect
      client-id: gateway-client
      client-secret: gateway-secret
      introspection-cache-duration: 5m
```

**流程：**

1. 从请求头提取 Token
2. 调用认证服务器 introspection 端点
3. 缓存验证结果（避免频繁调用）
4. 构建用户上下文

**Opaque Token vs JWT：**

| 特性    | Opaque Token | JWT        |
|-------|--------------|------------|
| 信息透明度 | 不透明，需查询      | 自包含，可直接解析  |
| 吊销时效性 | 实时生效         | 依赖黑名单或短有效期 |
| 网络依赖  | 需要调用认证接口     | 本地验证，无网络依赖 |
| 适用场景  | 高安全要求系统      | 分布式无状态架构   |

### 4.2 分布式限流

**基于 Redis + Lua 的令牌桶算法**

```yaml
spring:
  cloud:
    gateway:
      routes:
        - id: user-service
          filters:
            - name: RedisRateLimiter
              args:
                replenishRate: 100    # 每秒产生 100 个令牌
                burstCapacity: 200    # 桶容量 200
                keyResolver: IP       # 限流维度：IP/USER/API/COMBINED
```

**限流维度：**

- **IP**: 基于客户端 IP 限流
- **USER**: 基于用户 ID 限流
- **API**: 基于 API 路径限流
- **COMBINED**: 组合限流（IP + API）

### 4.3 熔断降级

**基于 Resilience4j**

```yaml
spring:
  cloud:
    gateway:
      routes:
        - id: user-service
          filters:
            - name: Resilience4jCircuitBreaker
              args:
                name: user-service
                fallbackUri: forward:/fallback/user
```

**配置参数：**

- 失败率阈值：50%
- 慢调用阈值：80%（2秒以上为慢调用）
- 熔断持续时间：30秒
- 半开状态调用数：10

### 4.4 灰度发布

**支持多种灰度策略：**

```yaml
carlos:
  gateway:
    gray:
      enabled: true
      strategies:
        user-service:
          enabled: true
          weight: 10              # 权重灰度：10% 流量
          version: "2.0.0"        # 版本灰度
          hash-key: USER          # 哈希键：IP/USER/HEADER/RANDOM
          user-ids:               # 用户灰度
            - "10001"
            - "10002"
          ip-ranges:              # IP 灰度
            - "192.168.1."
          headers:                # Header 灰度
            X-Gray-Test: "true"
```

### 4.5 WAF 安全防护

**防护类型：**

| 防护类型   | 检测内容                                    |
|--------|-----------------------------------------|
| SQL 注入 | `union`, `select`, `drop`, `--` 等关键字    |
| XSS 攻击 | `<script>`, `javascript:`, `onerror=` 等 |
| 路径遍历   | `../`, `..\` 等                          |
| 敏感文件   | `.git`, `.env`, `web.xml` 等             |
| 命令注入   | `;`, `&&`, `cat`, `ls` 等                |
| CSRF   | Origin/Referer 验证                       |

### 4.6 防重放攻击

**机制：**

1. **时间戳验证**：请求时间戳与服务器时间差在 5 分钟内
2. **Nonce 唯一性**：使用 Redis 存储已使用的 Nonce
3. **签名验证**：HMAC-SHA256 验证请求完整性

**请求头要求：**

```
X-Timestamp: 1710000000       # Unix 时间戳（秒）
X-Nonce: uuid                  # 唯一随机字符串
X-Signature: hmac_sha256(...)  # 签名
```

### 4.7 链路追踪

**基于 Micrometer Tracing**

- 自动注入 Trace ID 和 Span ID
- 支持 B3、W3C 等传播协议
- 与 Zipkin、Jaeger 兼容

**响应头：**

```
X-Trace-Id: abc123
X-Span-Id: def456
```

### 4.8 指标监控

**Prometheus 指标：**

```
# 请求计数
gateway_requests_total{route="user-service",path="/api/users",method="GET",status="200"}

# 请求延迟
gateway_requests_duration_seconds_sum{route="user-service",status="200"}

# 活跃请求
gateway_requests_active

# 限流触发
gateway_requests_rate_limited_total{route="user-service"}

# 熔断触发
gateway_circuitbreaker_state{route="user-service",state="OPEN"}
```

---

## 五、配置指南

### 5.1 最小配置（快速开始）

```yaml
server:
  port: 9510

spring:
  application:
    name: carlos-gateway
  cloud:
    gateway:
      routes:
        - id: user-service
          uri: lb://user-service
          predicates:
            - Path=/user-service/**
          filters:
            - StripPrefix=1

carlos:
  gateway:
    oauth2:
      enabled: true
      token-type: JWT
      jwt-public-key: ${JWT_PUBLIC_KEY}
```

### 5.2 生产配置（完整功能）

详见 `application-modern.yml`

---

## 六、过滤器顺序常量

```java
public interface GatewayFilterOrder {
    // 安全层
    int WAF = -5000;
    int REPLAY_PROTECTION = -4000;
    
    // 可观测性层
    int TRACING = -3000;
    int METRICS = -2500;
    
    // 认证层
    int AUTHENTICATION = -2000;
    int AUTHORIZATION = -1500;
    
    // 流量控制层
    int RATE_LIMIT = -1000;
    int CIRCUIT_BREAKER = -500;
    int GRAY_RELEASE = -100;
    
    // 转换层
    int TRANSFORM = 100;
    int CACHE = 500;
}
```

---

## 七、扩展开发

### 7.1 自定义 Token 验证器

```java
@Component
public class CustomTokenValidator implements TokenValidator {
    
    @Override
    public Mono<UserContext> validate(String token) {
        // 自定义验证逻辑
    }
    
    @Override
    public TokenType getType() {
        return TokenType.CUSTOM;
    }
}
```

### 7.2 自定义权限提供者

```java
@Component
public class CustomPermissionProvider implements PermissionProvider {
    
    @Override
    public Mono<Set<String>> getUserPermissions(String userId) {
        // 从数据库或缓存获取权限
    }
    
    @Override
    public Mono<Boolean> evaluateAbacPolicy(Map<String, Object> attributes) {
        // ABAC 策略评估
    }
}
```

---

## 八、性能优化

### 8.1 缓存策略

| 缓存层级  | 类型       | 过期时间 | 适用场景            |
|-------|----------|------|-----------------|
| 本地缓存  | Caffeine | 5分钟  | Token 验证结果、权限数据 |
| 分布式缓存 | Redis    | 10分钟 | 限流计数、Nonce 记录   |

### 8.2 异步处理

- 所有过滤器基于 Reactive 编程模型
- 非阻塞 IO，高并发场景下性能优异

### 8.3 连接池优化

```yaml
spring:
  cloud:
    gateway:
      httpclient:
        pool:
          type: elastic
          max-connections: 1000
          max-idle-time: 10s
        connect-timeout: 2000
        response-timeout: 10s
```

---

## 九、监控告警

### 9.1 健康检查端点

```
GET /actuator/health
GET /actuator/metrics
GET /actuator/prometheus
GET /actuator/gateway/routes
```

### 9.2 关键指标

| 指标                                         | 告警阈值      | 说明     |
|--------------------------------------------|-----------|--------|
| gateway_requests_total                     | -         | QPS 监控 |
| gateway_requests_duration_seconds          | P99 > 1s  | 延迟监控   |
| gateway_requests_rate_limited_total        | > 100/min | 限流触发   |
| gateway_circuitbreaker_state{state="OPEN"} | -         | 熔断监控   |

---

## 十、总结

Carlos Gateway 现代版是一个功能完善、安全可靠的云原生 API 网关，具备：

1. **双模式 OAuth2 认证**：灵活支持 JWT 和 Opaque Token
2. **全方位安全防护**：WAF、防重放、CSRF 保护
3. **智能流量控制**：限流、熔断、灰度发布
4. **完整可观测性**：链路追踪、指标监控
5. **高性能响应式架构**：基于 WebFlux 的非阻塞 IO
6. **灵活扩展能力**：插件化设计，易于定制

---

**文档版本**: 2.0  
**更新日期**: 2026-03-16  
**作者**: Carlos
