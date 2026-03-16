# Carlos Gateway 升级迁移指南

## 📋 升级概述

本次升级将 Carlos Gateway 从基础网关升级为**现代化云原生 API 网关**，集成 OAuth2 认证、WAF 防护、限流熔断、灰度发布、链路追踪等企业级功能。

## 🆕 新增功能清单

### 1. OAuth2 认证模块 (`oauth2/`)

| 类                            | 功能                          |
|------------------------------|-----------------------------|
| `OAuth2GatewayProperties`    | OAuth2 配置属性（JWT/Opaque 双模式） |
| `TokenValidator`             | Token 验证器接口                 |
| `JwtTokenValidator`          | JWT Token 验证实现              |
| `OpaqueTokenValidator`       | Opaque Token 验证实现           |
| `OAuth2AuthenticationFilter` | OAuth2 认证过滤器                |
| `OAuth2AuthorizationFilter`  | OAuth2 鉴权过滤器（RBAC/ABAC）     |
| `DefaultPermissionProvider`  | 默认权限提供者                     |

### 2. 限流模块 (`ratelimit/`)

| 类                  | 功能                |
|--------------------|-------------------|
| `RedisRateLimiter` | Redis + Lua 令牌桶限流 |

**特性：**

- 支持 IP/USER/API/COMBINED 多维度限流
- 基于 Redis 的分布式限流
- 平滑令牌桶算法

### 3. 熔断模块 (`circuitbreaker/`)

| 类                                  | 功能      |
|------------------------------------|---------|
| `Resilience4jCircuitBreakerFilter` | 熔断降级过滤器 |

**特性：**

- 失败率阈值控制
- 慢调用检测
- 自定义降级策略

### 4. 灰度发布模块 (`gray/`)

| 类                       | 功能      |
|-------------------------|---------|
| `GrayReleaseFilter`     | 灰度发布过滤器 |
| `GrayReleaseProperties` | 灰度配置属性  |

**策略：**

- 权重灰度
- 用户灰度
- IP 灰度
- Header 灰度
- 版本灰度

### 5. 安全防护模块 (`security/`)

| 类                            | 功能        |
|------------------------------|-----------|
| `WafFilter`                  | Web 应用防火墙 |
| `WafProperties`              | WAF 配置属性  |
| `ReplayProtectionFilter`     | 防重放攻击过滤器  |
| `ReplayProtectionProperties` | 防重放配置属性   |
| `ServerHttpRequestDecorator` | 请求装饰器     |

**防护能力：**

- SQL 注入
- XSS 攻击
- 路径遍历
- 敏感文件访问
- 命令注入
- CSRF 保护

### 6. 缓存模块 (`cache/`)

| 类                     | 功能      |
|-----------------------|---------|
| `ResponseCacheFilter` | 响应缓存过滤器 |
| `CacheProperties`     | 缓存配置属性  |

**特性：**

- Caffeine 本地缓存
- Redis 分布式缓存
- 智能缓存策略

### 7. 可观测性模块 (`observability/`)

| 类                   | 功能      |
|---------------------|---------|
| `TracingFilter`     | 链路追踪过滤器 |
| `TracingProperties` | 追踪配置属性  |
| `MetricsFilter`     | 指标收集过滤器 |

**能力：**

- Micrometer Tracing（兼容 Zipkin/Jaeger）
- Prometheus 指标导出
- 全链路监控

### 8. 转换模块 (`transform/`)

| 类                        | 功能      |
|--------------------------|---------|
| `RequestTransformFilter` | 请求转换过滤器 |
| `TransformProperties`    | 转换配置属性  |

**功能：**

- API 版本控制
- 路径重写
- Header 转换

### 9. 自动配置 (`config/`)

| 类                     | 功能        |
|-----------------------|-----------|
| `ModernGatewayConfig` | 现代网关自动配置类 |

## 🔧 依赖变更

### pom.xml 新增依赖

```xml
<!-- Resilience4j 熔断 -->
<dependency>
    <groupId>io.github.resilience4j</groupId>
    <artifactId>resilience4j-spring-boot3</artifactId>
    <version>2.1.0</version>
</dependency>

<!-- Caffeine 本地缓存 -->
<dependency>
    <groupId>com.github.ben-manes.caffeine</groupId>
    <artifactId>caffeine</artifactId>
    <version>3.1.8</version>
</dependency>

<!-- Micrometer Tracing 链路追踪 -->
<dependency>
    <groupId>io.micrometer</groupId>
    <artifactId>micrometer-tracing</artifactId>
</dependency>

<!-- Prometheus 指标 -->
<dependency>
    <groupId>io.micrometer</groupId>
    <artifactId>micrometer-registry-prometheus</artifactId>
</dependency>

<!-- Actuator -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
```

## 🚀 快速迁移步骤

### 步骤 1：更新依赖

```bash
# 更新 pom.xml 后执行
./mvnw clean install
```

### 步骤 2：启用现代配置

在 `bootstrap.yml` 或 `application.yml` 中导入现代配置：

```yaml
spring:
  profiles:
    active: modern
```

### 步骤 3：配置 OAuth2

```yaml
carlos:
  gateway:
    oauth2:
      enabled: true
      token-type: JWT
      jwt-public-key: ${JWT_PUBLIC_KEY}
```

### 步骤 4：配置路由

```yaml
spring:
  cloud:
    gateway:
      routes:
        - id: user-service
          uri: lb://user-service
          predicates:
            - Path=/user-service/**
          filters:
            - StripPrefix=1
            - name: RedisRateLimiter
              args:
                replenishRate: 100
                burstCapacity: 200
```

## ⚙️ 配置对比

### 原配置 vs 新配置

| 功能  | 原配置                   | 新配置                          |
|-----|-----------------------|------------------------------|
| 认证  | `carlos.gateway.auth` | `carlos.gateway.oauth2`      |
| 限流  | 无                     | `RedisRateLimiter`           |
| 熔断  | 无                     | `Resilience4jCircuitBreaker` |
| 灰度  | 无                     | `carlos.gateway.gray`        |
| WAF | 无                     | `carlos.gateway.waf`         |
| 防重放 | 无                     | `carlos.gateway.replay`      |
| 追踪  | 无                     | `carlos.gateway.tracing`     |
| 指标  | 无                     | `carlos.gateway.metrics`     |

## 📊 性能对比

| 指标     | 原版   | 现代版            |
|--------|------|----------------|
| 并发处理能力 | 中等   | 高（响应式优化）       |
| 安全能力   | 基础   | 企业级（WAF + 防重放） |
| 可观测性   | 基础日志 | 全链路追踪 + 指标     |
| 流量控制   | 无    | 限流 + 熔断        |
| 灰度能力   | 无    | 多策略灰度发布        |

## 🔄 回滚方案

如需回滚到原版：

1. 注释掉 `ModernGatewayConfig` 中的 Bean 定义
2. 恢复使用原版 `GatewayAuthConfig`
3. 移除现代版配置属性

```java
// 在 ModernGatewayConfig 中添加条件禁用
@ConditionalOnProperty(name = "carlos.gateway.modern.enabled", havingValue = "true")
public class ModernGatewayConfig { ... }
```

## 📚 相关文档

- [原始设计文档](DESIGN.md)
- [现代版设计文档](MODERN_DESIGN.md)
- [配置示例](src/main/resources/application-modern.yml)
- [快速开始](README_MODERN.md)

## ✅ 检查清单

- [ ] 更新 pom.xml 依赖
- [ ] 配置 JWT 公钥或 Opaque Token 端点
- [ ] 配置 Redis 连接
- [ ] 配置 Nacos 服务发现
- [ ] 测试认证流程
- [ ] 测试限流功能
- [ ] 测试熔断降级
- [ ] 配置监控告警
- [ ] 更新 API 文档

---

**迁移版本**: 2.0  
**日期**: 2026-03-16
