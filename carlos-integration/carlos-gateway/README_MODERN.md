# Carlos Gateway 现代版 - 云原生 API 网关

## 🚀 概述

Carlos Gateway 现代版是一个功能完善、安全可靠的云原生 API 网关，基于 Spring Cloud Gateway 构建，集成了当前最流行的网关功能。

## ✨ 核心特性

### 🔐 认证授权

- **OAuth2 双模式**：支持 JWT 自包含令牌和 Opaque Token 中心化验证
- **RBAC/ABAC**：基于角色或属性的权限控制
- **Token 续期**：自动刷新 Token 过期时间

### 🛡️ 安全防护

- **WAF 防火墙**：防护 SQL 注入、XSS、路径遍历、命令注入
- **防重放攻击**：基于时间戳 + Nonce + 签名的三重验证
- **CSRF 保护**：跨站请求伪造防护

### ⚡ 流量控制

- **分布式限流**：Redis + Lua 令牌桶算法，支持 IP/用户/API 多维度
- **熔断降级**：基于 Resilience4j，支持自定义降级策略
- **灰度发布**：支持权重、用户、版本、Header 多策略灰度

### 📊 可观测性

- **链路追踪**：Micrometer Tracing，兼容 Zipkin/Jaeger
- **指标监控**：Prometheus 格式，全链路指标采集
- **健康检查**：Spring Boot Actuator 端点

### ⚙️ 高级功能

- **多级缓存**：Caffeine 本地缓存 + Redis 分布式缓存
- **API 版本控制**：Header/URL 双模式版本管理
- **请求转换**：路径重写、Header 转换、协议转换

## 📁 项目结构

```
carlos-gateway/
├── src/main/java/com/carlos/gateway/
│   ├── oauth2/                    # OAuth2 认证模块
│   ├── ratelimit/                 # 限流模块
│   ├── circuitbreaker/            # 熔断模块
│   ├── gray/                      # 灰度发布模块
│   ├── security/                  # 安全防护模块
│   ├── cache/                     # 缓存模块
│   ├── observability/             # 可观测性模块
│   ├── transform/                 # 转换模块
│   └── config/                    # 配置模块
├── src/main/resources/
│   ├── application-modern.yml     # 完整配置示例
│   └── ...
├── DESIGN.md                      # 原始设计文档
├── MODERN_DESIGN.md               # 现代版设计文档
└── README_MODERN.md               # 本文件
```

## 🚀 快速开始

### 1. 添加依赖

```xml
<dependency>
    <groupId>com.carlos</groupId>
    <artifactId>carlos-gateway</artifactId>
    <version>3.0.0-SNAPSHOT</version>
</dependency>
```

### 2. 最小配置

```yaml
server:
  port: 9510

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

carlos:
  gateway:
    oauth2:
      enabled: true
      token-type: JWT
      jwt-public-key: ${JWT_PUBLIC_KEY}
```

### 3. 启动应用

```bash
./mvnw spring-boot:run
```

## 📖 配置详解

### OAuth2 配置

```yaml
carlos:
  gateway:
    oauth2:
      enabled: true
      token-type: JWT  # 或 OPAQUE
      jwt-public-key: |
        -----BEGIN PUBLIC KEY-----
        MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA...
        -----END PUBLIC KEY-----
      jwt-issuer: carlos-auth
      jwt-audience: carlos-gateway
      
      # Opaque Token 配置
      introspection-uri: http://carlos-auth/oauth2/introspect
      client-id: gateway-client
      client-secret: gateway-secret
```

### 限流配置

```yaml
spring:
  cloud:
    gateway:
      routes:
        - id: user-service
          filters:
            - name: RedisRateLimiter
              args:
                replenishRate: 100    # 每秒令牌数
                burstCapacity: 200    # 桶容量
                keyResolver: IP       # IP/USER/API/COMBINED
```

### 灰度发布配置

```yaml
carlos:
  gateway:
    gray:
      enabled: true
      strategies:
        user-service:
          enabled: true
          weight: 10              # 10% 灰度流量
          version: "2.0.0"
          user-ids: ["10001", "10002"]
```

### WAF 配置

```yaml
carlos:
  gateway:
    waf:
      enabled: true
      sql-injection-protection: true
      xss-protection: true
      path-traversal-protection: true
      csrf-protection: true
```

## 🔍 监控端点

| 端点                         | 说明              |
|----------------------------|-----------------|
| `/actuator/health`         | 健康检查            |
| `/actuator/metrics`        | 指标数据            |
| `/actuator/prometheus`     | Prometheus 格式指标 |
| `/actuator/gateway/routes` | 网关路由信息          |

## 📊 Prometheus 指标

```
# 请求计数
gateway_requests_total{route="user-service",status="200"}

# 请求延迟
gateway_requests_duration_seconds_bucket{route="user-service"}

# 限流触发次数
gateway_requests_rate_limited_total{route="user-service"}

# 活跃请求数
gateway_requests_active
```

## 🔒 安全头要求

### 认证请求

```
Authorization: Bearer <token>
# 或
access_token: <token>
```

### 防重放请求

```
X-Timestamp: 1710000000
X-Nonce: <uuid>
X-Signature: <hmac-sha256>
```

## 🧪 测试示例

```bash
# 1. 获取 Token
curl -X POST http://auth-server/oauth2/token \
  -d "grant_type=client_credentials" \
  -u "client-id:client-secret"

# 2. 访问受保护接口
curl http://gateway:9510/user-service/api/users \
  -H "Authorization: Bearer <token>"

# 3. 带防重放头部访问
curl http://gateway:9510/user-service/api/users \
  -H "Authorization: Bearer <token>" \
  -H "X-Timestamp: $(date +%s)" \
  -H "X-Nonce: $(uuidgen)" \
  -H "X-Signature: <computed-signature>"
```

## 📚 文档

- [原始设计文档](DESIGN.md) - 基础架构设计
- [现代版设计文档](MODERN_DESIGN.md) - 完整功能设计
- [配置示例](src/main/resources/application-modern.yml) - 完整配置参考

## 🤝 贡献

欢迎提交 Issue 和 PR！

## 📄 许可证

Apache License 2.0

---

**版本**: 3.0.0-MODERN  
**更新日期**: 2026-03-16
