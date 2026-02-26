# Carlos Spring Boot Starter Gateway

微服务网关基础组件，基于 Spring Cloud Gateway 构建，提供统一的路由转发、负载均衡和 OAuth2 资源服务器支持。

## 功能特性

- **服务发现与路由转发**：基于 Nacos 实现动态服务发现和路由
- **负载均衡**：集成 Spring Cloud LoadBalancer
- **OAuth2 资源服务器**：统一在网关层验证 JWT Token
- **API 文档聚合**：集成 Knife4j Gateway 实现文档聚合

## 快速开始

### 1. 添加依赖

```xml
<dependency>
    <groupId>com.carlos</groupId>
    <artifactId>carlos-spring-boot-starter-gateway</artifactId>
</dependency>
```

### 2. 基础配置

```yaml
spring:
  application:
    name: api-gateway
  cloud:
    nacos:
      discovery:
        server-addr: localhost:8848
      config:
        server-addr: localhost:8848
    gateway:
      discovery:
        locator:
          enabled: true  # 开启服务发现路由
      routes:
        - id: user-service
          uri: lb://user-service
          predicates:
            - Path=/api/user/**
          filters:
            - StripPrefix=1
```

## OAuth2 资源服务器配置

### 启用资源服务器

在网关层统一验证 JWT Token，保护下游微服务：

```yaml
carlos:
  gateway:
    oauth2:
      resource-server:
        enabled: true
        # 方式1：使用 JWK Set URI（推荐）
        jwk-set-uri: http://auth-server:9000/oauth2/jwks
        # 方式2：使用 Issuer URI
        # issuer-uri: http://auth-server:9000
        # 白名单路径（可选）
        permit-all-paths:
          - /api/public/**
          - /actuator/health
```

### 配置说明

| 配置项                                                      | 说明                | 默认值     |
|----------------------------------------------------------|-------------------|---------|
| `carlos.gateway.oauth2.resource-server.enabled`          | 是否启用资源服务器         | `false` |
| `carlos.gateway.oauth2.resource-server.jwk-set-uri`      | 授权服务器的 JWK Set 端点 | -       |
| `carlos.gateway.oauth2.resource-server.issuer-uri`       | 授权服务器的 Issuer URI | -       |
| `carlos.gateway.oauth2.resource-server.permit-all-paths` | 白名单路径列表           | 见下方默认路径 |

### 默认白名单路径

以下路径默认不需要认证：

- `/error` - 错误页面
- `/actuator/health` - 健康检查
- `/v3/api-docs/**` - OpenAPI 文档
- `/swagger-ui/**` - Swagger UI
- `/swagger-ui.html` - Swagger UI 首页
- `/doc.html` - Knife4j 文档
- `/webjars/**` - WebJars 资源

### 权限提取

网关从 JWT Token 中提取以下权限：

1. **scope** - OAuth2 标准作用域，转换为 `SCOPE_*` 权限
2. **authorities** - 自定义权限声明，逗号分隔
3. **role_ids** - 角色 ID 列表，转换为 `ROLE_*` 权限

### 完整配置示例

```yaml
server:
  port: 8080

spring:
  application:
    name: api-gateway
  cloud:
    nacos:
      discovery:
        server-addr: localhost:8848
      config:
        server-addr: localhost:8848
    gateway:
      discovery:
        locator:
          enabled: true
      routes:
        - id: order-service
          uri: lb://order-service
          predicates:
            - Path=/api/order/**
          filters:
            - StripPrefix=1
        - id: user-service
          uri: lb://user-service
          predicates:
            - Path=/api/user/**
          filters:
            - StripPrefix=1

carlos:
  gateway:
    oauth2:
      resource-server:
        enabled: true
        jwk-set-uri: http://localhost:9000/oauth2/jwks
        permit-all-paths:
          - /api/public/**
          - /actuator/health
          - /api/auth/**
```

## 工作原理

### 资源服务器流程

```
┌─────────────┐      ┌─────────────┐      ┌─────────────┐
│   客户端     │─────>│  API 网关   │─────>│  下游服务   │
│  (携带Token)│      │ (验证Token) │      │ (无需验证)  │
└─────────────┘      └─────────────┘      └─────────────┘
                            │
                            v
                    ┌─────────────┐
                    │  授权服务器  │
                    │(获取JWK公钥) │
                    └─────────────┘
```

1. 客户端请求携带 JWT Token（Header: `Authorization: Bearer {token}`）
2. 网关提取 Token 并从授权服务器获取公钥验证签名
3. 验证通过后，从 Token 中提取用户身份和权限
4. 请求转发到下游服务（可携带用户信息 Header）
5. 下游服务无需再次验证 Token，直接处理业务

### 与 carlos-auth-service 的关系

- **carlos-auth-service**: 作为授权服务器，负责颁发 Token
- **carlos-spring-boot-starter-gateway**: 作为资源服务器，负责验证 Token

## 注意事项

1. **响应式编程**: 由于 Gateway 基于 WebFlux，资源服务器配置使用响应式版本（`@EnableWebFluxSecurity`）
2. **JWK Set URI 优先**: 同时配置 `jwk-set-uri` 和 `issuer-uri` 时，优先使用 `jwk-set-uri`
3. **必须配置其一**: 启用资源服务器时，`jwk-set-uri` 和 `issuer-uri` 至少配置一个

## 版本历史

### 3.0.0

- 初始版本
- 集成 Spring Cloud Gateway
- 集成 Nacos 服务发现和配置中心
- 集成 Knife4j Gateway 文档聚合
- 添加 OAuth2 资源服务器支持（JWT Token 统一验证）
