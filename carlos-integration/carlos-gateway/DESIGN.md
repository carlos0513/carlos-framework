# Carlos Gateway 融合治理网关设计文档

## 一、概述

### 1.1 项目定位

Carlos Gateway 是 Carlos 框架的统一入口网关，基于 Spring Cloud Gateway 构建，采用响应式编程模型（WebFlux），提供路由转发、负载均衡、认证鉴权、API
文档聚合等核心能力。

### 1.2 技术栈

| 组件                         | 版本         | 说明            |
|----------------------------|------------|---------------|
| Spring Boot                | 3.5.9      | 基础框架          |
| Spring Cloud Gateway       | 4.x        | 网关核心          |
| Spring Cloud Alibaba Nacos | 2025.0.0.0 | 服务发现与配置中心     |
| Redis                      | -          | Token 缓存与会话管理 |
| Knife4j                    | 4.x        | API 文档聚合      |

### 1.3 核心特性

- **统一认证鉴权**：支持 Token 认证、白名单机制、细粒度权限控制
- **服务路由**：支持动态路由、负载均衡、路径重写
- **API 文档聚合**：自动聚合后端服务的 Swagger/Knife4j 文档
- **跨域支持**：全局 CORS 配置
- **异常处理**：统一的异常捕获与响应包装
- **响应式架构**：基于 WebFlux 的非阻塞 IO 模型

---

## 二、架构设计

### 2.1 整体架构图

```
┌─────────────────────────────────────────────────────────────────┐
│                         客户端请求                               │
└─────────────────────────────────────────────────────────────────┘
                                │
                                ▼
┌─────────────────────────────────────────────────────────────────┐
│                      Carlos Gateway                              │
│  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐              │
│  │  ReqHeader  │  │   Auth      │  │  PathPrefix │              │
│  │   Filter    │──│   Filter    │──│   Filter    │              │
│  │ (-2000)     │  │  (10000)    │  │   (-99)     │              │
│  └─────────────┘  └─────────────┘  └─────────────┘              │
│         │                │                │                      │
│         ▼                ▼                ▼                      │
│  ┌─────────────────────────────────────────────────────────┐    │
│  │                    路由断言 (Predicate)                   │    │
│  │         Path、SelectRoutePredicateFactory               │    │
│  └─────────────────────────────────────────────────────────┘    │
│                                │                                 │
│                                ▼                                 │
│  ┌─────────────────────────────────────────────────────────┐    │
│  │                    网关过滤器链                           │    │
│  │  SelectStripPrefixFilter、RoleCheckGlobalFilter         │    │
│  └─────────────────────────────────────────────────────────┘    │
│                                │                                 │
│                                ▼                                 │
│  ┌─────────────────────────────────────────────────────────┐    │
│  │              负载均衡 (LoadBalancer)                      │    │
│  │              lb://service-name                           │    │
│  └─────────────────────────────────────────────────────────┘    │
└─────────────────────────────────────────────────────────────────┘
                                │
                                ▼
┌─────────────────────────────────────────────────────────────────┐
│                      后端微服务集群                              │
│     ┌──────────┐  ┌──────────┐  ┌──────────┐                   │
│     │  user    │  │  order   │  │  system  │                   │
│     │ service  │  │ service  │  │ service  │                   │
│     └──────────┘  └──────────┘  └──────────┘                   │
└─────────────────────────────────────────────────────────────────┘
```

### 2.2 模块结构

```
carlos-gateway/
├── src/main/java/com/carlos/gateway/
│   ├── GovernGatewayApplication.java          # 启动类
│   ├── auth/                                   # 认证授权模块
│   │   ├── AccessTokenAuthFilter.java          # AccessToken 认证过滤器
│   │   ├── AuthGlobalFilter.java               # 全局认证过滤器
│   │   ├── GatewayAuthConfig.java              # 认证配置类
│   │   ├── GatewayAuthProperties.java          # 认证属性配置
│   │   ├── MenuApiMapping.java                 # 菜单-API 映射
│   │   ├── ApiInfo.java                        # API 信息
│   │   ├── UserMenu.java                       # 用户菜单
│   │   ├── RemoveJwtFilter.java                # Token 移除过滤器
│   │   └── RoleCheckGlobalFilter.java          # 角色权限校验过滤器
│   ├── config/                                 # 核心配置模块
│   │   ├── GatewayConfig.java                  # 网关主配置
│   │   ├── GatewayProperties.java              # 网关属性配置
│   │   ├── GatewayConstant.java                # 常量定义
│   │   ├── GlobalFilterOrder.java              # 过滤器顺序定义
│   │   ├── GatewayExceptionHandler.java        # 全局异常处理器
│   │   ├── GatewayErrorAttributes.java         # 错误属性定制
│   │   ├── GatewayRunnerWorker.java            # 启动工作器
│   │   ├── SelectRoutePredicateFactory.java    # 路由断言工厂
│   │   ├── ResponseCoverFilter.java            # 响应包装过滤器
│   │   └── GatewatApplicationExtendImpl.java   # 应用扩展实现
│   ├── filter/                                 # 过滤器模块
│   │   ├── BlackListUrlFilter.java             # 黑名单过滤器
│   │   ├── CacheRequestFilter.java             # 请求缓存过滤器
│   │   ├── PathPrefixFilter.java               # 路径前缀过滤器
│   │   ├── ReqHeaderFilter.java                # 请求头过滤器
│   │   ├── SelectStripPrefixGatewayFilterFactory.java  # 选择性路径截取
│   │   └── WebSocketFilter.java                # WebSocket 过滤器
│   └── resource/                               # 资源服务器模块（已注释）
│       ├── AuthorizationManager.java           # 鉴权管理器
│       ├── AuthService.java                    # 认证服务接口
│       ├── RemoteAuthServiceImpl.java          # 远程认证实现
│       ├── ResourceServerConfig.java           # 资源服务器配置
│       ├── RestAuthenticationEntryPoint.java   # 认证入口点
│       ├── RestfulAccessDeniedHandler.java     # 拒绝访问处理器
│       ├── CustomTokenFilter.java              # 自定义 Token 过滤器
│       └── TokenUtil.java                      # Token 工具类
└── src/main/resources/
    ├── bootstrap.yml                           # 启动配置
    ├── application-gateway.yml                 # 网关配置
    └── config/logback.xml                      # 日志配置
```

---

## 三、过滤器链设计

### 3.1 过滤器执行顺序

| 顺序值                  | 过滤器                       | 类型           | 说明                 |
|----------------------|---------------------------|--------------|--------------------|
| -2000                | ReqHeaderFilter           | GlobalFilter | 添加请求追踪 ID          |
| -1                   | AccessTokenAuthFilter     | GlobalFilter | AccessToken 认证（预留） |
| 0                    | AuthGlobalFilter          | GlobalFilter | Token 认证与用户信息解析    |
| -99                  | PathPrefixFilter          | WebFilter    | 路径前缀处理             |
| 10000 (ORDER_FIRST)  | AuthGlobalFilter (Config) | GlobalFilter | 认证配置化注册            |
| 20000 (ORDER_SECOND) | RoleCheckGlobalFilter     | GlobalFilter | 角色权限校验             |
| 101                  | ResponseCoverFilter       | WebFilter    | 响应包装（预留）           |

### 3.2 过滤器分类

#### 3.2.1 全局过滤器 (GlobalFilter)

```java
// 过滤器接口定义
public interface GlobalFilter {
    Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain);
}
```

**核心过滤器：**

1. **AuthGlobalFilter** - 用户认证
    - 从请求头提取 Token
    - 查询 Redis 验证 Token 有效性
    - 续期 Token 过期时间
    - 将用户信息注入请求头转发给下游服务

2. **RoleCheckGlobalFilter** - 权限校验
    - 根据配置的菜单-API 映射关系
    - 校验用户是否拥有访问该 API 的权限
    - 无权限时抛出异常

3. **AccessTokenAuthFilter** - AccessToken 认证（预留）
    - 支持三方 AccessToken 模式
    - 调用认证服务校验端点验证 Token

4. **ReqHeaderFilter** - 请求追踪
    - 为每个请求生成唯一的 x-request-id
    - 便于链路追踪和问题定位

#### 3.2.2 WebFilter

```java
public interface WebFilter {
    Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain);
}
```

**核心 WebFilter：**

1. **PathPrefixFilter** - 路径前缀处理
    - 统一处理 `/bbt-api` 前缀
    - 适配前端请求路径

2. **RemoveJwtFilter** - Token 清理
    - 白名单请求移除 Token 头
    - 防止 Token 泄露到下游服务

---

## 四、认证授权机制

### 4.1 认证流程

```
┌──────────┐     ┌─────────────┐     ┌─────────────┐     ┌──────────┐
│  客户端   │────▶│   Gateway   │────▶│    Redis    │────▶│ 下游服务  │
└──────────┘     └─────────────┘     └─────────────┘     └──────────┘
                      │
                      ▼
              ┌───────────────┐
              │ 1. 提取 Token  │
              │ 2. 白名单检查  │
              └───────────────┘
                      │
                      ▼
              ┌───────────────┐
              │ 3. Redis 查询  │
              │   UserContext  │
              └───────────────┘
                      │
                      ▼
              ┌───────────────┐
              │ 4. Token 续期  │
              │   更新过期时间 │
              └───────────────┘
                      │
                      ▼
              ┌───────────────┐
              │ 5. 用户信息注入│
              │   请求头转发   │
              └───────────────┘
```

### 4.2 Token 存储结构

```
Redis Key 设计：

1. context:{token}  - 用户上下文信息 (UserContext)
2. token:{token}    - Token 存活标记
3. user:menu:{token} - 用户菜单权限列表
```

### 4.3 用户信息注入请求头

| 请求头            | 说明       | 来源                    |
|----------------|----------|-----------------------|
| X-User-Token   | 用户 Token | context.token         |
| X-User-Account | 用户账号     | context.account       |
| X-User-Id      | 用户 ID    | context.userId        |
| X-Dept-Id      | 部门 ID    | context.departmentId  |
| X-Role-Id      | 角色 ID    | context.roleId        |
| X-Tenant-Id    | 租户 ID    | context.tenantId      |
| X-User-Phone   | 手机号      | context.phone         |
| X-Role-Ids     | 角色 ID 列表 | context.roleIds       |
| X-Dept-Ids     | 部门 ID 列表 | context.departmentIds |

### 4.4 权限校验机制

**配置结构：**

```yaml
carlos:
  gateway:
    role-check: true
    mappings:
      - menuId: 1
        menuPath: /system/user
        apis:
          - url: /user/**
            method: GET
            name: 用户查询
          - url: /user/**
            method: POST
            name: 用户新增
```

**校验逻辑：**

1. 根据请求方法和路径匹配对应的菜单
2. 从 Redis 获取用户菜单权限列表
3. 判断用户是否拥有匹配的菜单权限
4. 无权限返回 403

---

## 五、路由配置

### 5.1 路由配置示例

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
            - SelectStripPrefix=1

        - id: order-service
          uri: lb://order-service
          predicates:
            - Path=/order-service/**
          filters:
            - StripPrefix=1
```

### 5.2 自定义路由断言

**SelectRoutePredicateFactory** - 选择性路由断言

```java
public class SelectRoutePredicateFactory extends AbstractRoutePredicateFactory<Config> {
    // 支持路径模式匹配
    // 与标准 Path 断言的区别：支持更灵活的路径匹配规则
}
```

### 5.3 自定义过滤器

**SelectStripPrefixGatewayFilterFactory** - 选择性路径截取

```
功能说明：
- /xxx/doc.html      -> /doc.html      (匹配Swagger相关路径，截取)
- /xxx/v3/api-docs   -> /v3/api-docs   (匹配API文档路径，截取)
- /xxx/api/users     -> /xxx/api/users (不匹配，不处理)

预定义路径模式：
- /**/swagger-ui.html
- /**/doc.html
- /**/v2/api-docs/**
- /**/v3/api-docs
- /**/webjars/**
```

---

## 六、API 文档聚合

### 6.1 Knife4j 网关聚合配置

```yaml
knife4j:
  gateway:
    enabled: true
    strategy: discover  # 自动发现模式
    discover:
      enabled: true
      excluded-services:
        - gateway-service
        - monitor-service
      included-services:
        - user-service
        - order-service
        - system-service
  setting:
    title: 微服务 API 文档中心
    description: 网关聚合所有服务的 API 文档
```

### 6.2 访问地址

- 网关地址：`http://gateway:9510/bbt-api/doc.html`
- 自动聚合所有注册到 Nacos 的服务的 API 文档

---

## 七、配置属性

### 7.1 网关配置 (GatewayProperties)

```yaml
carlos:
  gateway:
    # API 前缀
    prefix: /bbt-api
    
    # 是否开启角色权限校验
    role-check: false
    
    # 菜单-API 映射配置
    mappings:
      - menuId: 1
        menuPath: /system/user
        apis:
          - url: /api/user/**
            method: GET
```

### 7.2 认证配置 (GatewayAuthProperties)

```yaml
carlos:
  gateway:
    auth:
      # 是否开启认证
      authenticate: true
      
      # 是否开启授权
      authorize: false
      
      # Token 过期时间
      token-expire: 6h
      
      # Token 名称
      token-name: access_token
      
      # 白名单列表
      whitelist:
        - /auth/oauth/token
        - /auth/rsa/public
        - /csrf
```

### 7.3 Nacos 配置

```yaml
spring:
  cloud:
    nacos:
      discovery:
        enabled: true
        server-addr: ${NACOS_ADDR}
        namespace: ${NACOS_NAMESPACE}
        group: ${NACOS_GROUP}
      config:
        enabled: true
        file-extension: yml
        refresh-enabled: true
        shared-configs:
          - data-id: share-config.yml
          - data-id: redis.yml
```

---

## 八、异常处理

### 8.1 异常处理流程

```
请求异常
    │
    ▼
┌─────────────────────┐
│ GatewayExceptionHandler  │
│  (ErrorWebExceptionHandler) │
└─────────────────────┘
    │
    ▼
┌─────────────────────┐
│  异常类型判断        │
│  GlobalException?    │
└─────────────────────┘
    │
    ├── 是 ──▶ HTTP 403 (FORBIDDEN)
    │
    └── 否 ──▶ HTTP 500 (INTERNAL_SERVER_ERROR)
              │
              ▼
        ┌─────────────────────┐
        │ 统一响应格式         │
        │ Result.fail()        │
        └─────────────────────┘
```

### 8.2 响应格式

```json
{
  "code": 500,
  "message": "请求异常",
  "data": "具体异常信息",
  "timestamp": 1710000000000
}
```

---

## 九、启动流程

### 9.1 启动工作器 (GatewayRunnerWorker)

```java
public class GatewayRunnerWorker implements ApplicationRunner {
    @Override
    public void run(ApplicationArguments args) {
        // 启动时打印 Knife4j 文档地址
        log.info("Knife4j: {}", getKnife4jUrl());
    }
}
```

### 9.2 配置加载顺序

1. **bootstrap.yml** - 基础配置、Nacos 配置
2. **Nacos 共享配置** - share-config.yml、redis.yml
3. **application-gateway.yml** - 网关路由配置、Knife4j 配置
4. **GatewayAuthProperties** - 认证属性初始化
5. **GatewayProperties** - 网关属性初始化

---

## 十、扩展设计

### 10.1 应用扩展接口 (ApplicationExtend)

```java
public interface ApplicationExtend {
    String getUserId();
    UserContext getUserContext();
    DepartmentInfo getDepartmentByCode(String code, int limit);
    UserInfo getUserById(Serializable userId);
    RegionInfo getRegionInfo(String regionCode, Integer limit);
}
```

### 10.2 过滤器扩展

自定义过滤器可通过以下方式注册：

```java
@Configuration
public class CustomGatewayConfig {
    
    @Bean
    @Order(GlobalFilterOrder.ORDER_THIRD)
    public GlobalFilter customFilter() {
        return new CustomGlobalFilter();
    }
}
```

---

## 十一、安全设计

### 11.1 白名单机制

- 支持 URL 级别的白名单配置
- 支持 Ant 风格的路径匹配
- 白名单请求自动移除 Token，防止泄露

### 11.2 Token 安全

- Token 存储在 Redis，支持快速吊销
- Token 自动续期，保持用户会话
- 支持 Token 过期时间配置

### 11.3 跨域安全

```java
@Bean
public CorsWebFilter corsWebFilter() {
    CorsConfiguration config = new CorsConfiguration();
    config.addAllowedHeader("*");
    config.addAllowedMethod("*");
    config.setAllowCredentials(true);
    config.setAllowedOriginPatterns(Collections.singletonList("*"));
    config.setExposedHeaders(Collections.singletonList("*"));
    config.setMaxAge(Duration.ofDays(1));
    // ...
}
```

---

## 十二、日志配置

### 12.1 日志级别

| 包路径        | 级别    | 输出                            |
|------------|-------|-------------------------------|
| com.carlos | DEBUG | CONSOLE、DEBUG                 |
| root       | INFO  | CONSOLE、DEBUG、INFO、ERROR、WARN |

### 12.2 日志文件

- debug.log - 调试日志
- info.log - 信息日志
- warn.log - 警告日志
- error.log - 错误日志

### 12.3 日志格式

```
2024-01-01 12:00:00.000  INFO [http-nio-9510] c.c.g.a.AuthGlobalFilter:54 : 认证通过
```

---

## 十三、部署说明

### 13.1 端口配置

- 默认端口：9510
- 可通过 `server.port` 修改

### 13.2 环境变量

| 变量名             | 说明          |
|-----------------|-------------|
| NACOS_ADDR      | Nacos 服务器地址 |
| NACOS_NAMESPACE | Nacos 命名空间  |
| NACOS_GROUP     | Nacos 分组    |
| NACOS_USERNAME  | Nacos 用户名   |
| NACOS_PASSWORD  | Nacos 密码    |
| PROFILE         | 激活的配置文件     |

### 13.3 启动命令

```bash
java -jar carlos-gateway.jar \
  --NACOS_ADDR=192.168.1.100:8848 \
  --NACOS_NAMESPACE=dev \
  --PROFILE=deploy
```

---

## 十四、预留扩展（已注释代码）

### 14.1 Spring Security OAuth2 资源服务器

- `ResourceServerConfig` - OAuth2 资源服务器配置
- `AuthorizationManager` - 响应式鉴权管理器
- `RestAuthenticationEntryPoint` - 认证失败处理器
- `RestfulAccessDeniedHandler` - 鉴权失败处理器

### 14.2 WebSocket 支持

- `WebSocketFilter` - WebSocket 转发过滤器

### 14.3 请求缓存

- `CacheRequestFilter` - 解决请求体重复读取问题

### 14.4 黑名单

- `BlackListUrlFilter` - URL 黑名单过滤器

---

## 十五、总结

Carlos Gateway 是一个功能完善的微服务网关，具备以下核心能力：

1. **高性能**：基于 WebFlux 的响应式架构，支持高并发
2. **易扩展**：模块化的过滤器设计，支持自定义扩展
3. **安全可靠**：完善的认证鉴权机制，细粒度权限控制
4. **运维友好**：统一的异常处理、日志记录、API 文档聚合
5. **云原生**：与 Nacos 深度集成，支持服务发现和配置管理

---

**文档版本**: 1.0  
**更新日期**: 2026-03-16  
**作者**: Carlos
