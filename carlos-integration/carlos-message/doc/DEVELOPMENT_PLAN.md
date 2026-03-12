# Carlos Message 模块开发方案

> 版本: 1.1  
> 日期: 2026-03-11  
> 状态: 已确认（已按 Carlos Framework 研发规范调整）

---

## 规范遵循说明

本文档已根据 `AGENTS.md` 中的研发规范进行工程结构调整，主要变更包括：

| 规范项          | 调整内容                                                          |
|--------------|---------------------------------------------------------------|
| **模块命名**     | 统一使用 `-api` / `-core` / `-bus` / `-spring-boot-starter` 后缀    |
| **分层架构**     | 严格遵循 API / API实现 / Controller / Service / Manager / Mapper 分层 |
| **领域模型**     | 明确划分 AO / DTO / VO / Param / Entity / Enum / Excel            |
| **包结构**      | 统一使用 `com.carlos.message` 作为根包，按功能分层组织                        |
| **对象转换**     | 新增 Convert 层，使用 MapStruct 进行对象转换                              |
| **枚举规范**     | 统一放在 `pojo.enums` 包下，实现 `BaseEnum` 接口                         |
| **Feign 接口** | API 模块包含 Feign 接口和 fallback 工厂                                |
| **自动配置**     | Starter 模块遵循 Spring Boot 3.x 自动配置规范                           |

---

## 目录

1. [需求确认摘要](#一需求确认摘要)
2. [总体架构设计](#二总体架构设计)
3. [数据库设计](#三数据库设计)
4. [模块结构设计](#四模块结构设计)
5. [开发路线图](#五开发路线图)
6. [关键设计决策](#六关键设计决策)
7. [风险与应对](#七风险与应对)
8. [下一步行动](#八下一步行动)

---

## 一、需求确认摘要

### 1.1 业务需求

| 维度        | 确认内容                                    |
|-----------|-----------------------------------------|
| **消息场景**  | 任务通知(最高优)、验证码/系统通知(次高)、审批提醒(普通)、营销推送(低) |
| **时效性**   | 验证码10s、任务通知30s、审批5min、营销可延迟             |
| **消息有效期** | 发送失败1小时后放弃                              |
| **发送量**   | 日均20万、峰值1000 QPS、高峰期6小时                 |
| **用户规模**  | 50万总用户、8万日活、1万WebSocket并发               |

### 1.2 渠道需求

| 渠道      | 优先级   | 特性                                       |
|---------|-------|------------------------------------------|
| **短信**  | P0    | 多服务商切换（复用carlos-spring-boot-starter-sms） |
| **钉钉**  | P0    | 工作通知、需获取部门信息                             |
| **站内信** | P0    | WebSocket实时、未读数提醒                        |
| **邮件**  | P1/P2 | SMTP/企业邮箱（可延后）                           |

### 1.3 技术约束

```
部署：单机(开发) + K8s多实例(生产)
框架：Spring Cloud + Nacos
数据库：MySQL 8.0（消息独立库）
缓存：Redis（使用carlos-spring-boot-starter-redis）
队列：Redis Stream
定时任务：XXL-Job
监控：内置监控页面
```

---

## 二、总体架构设计

### 2.1 系统架构图

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                              接入层 (Access Layer)                           │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐     │
│  │  REST API    │  │  Java SDK    │  │  WebSocket   │  │  XXL-Job     │     │
│  │  同步/异步    │  │  Feign Client│  │  实时推送    │  │  定时任务    │     │
│  └──────┬───────┘  └──────┬───────┘  └──────┬───────┘  └──────┬───────┘     │
└─────────┼─────────────────┼─────────────────┼─────────────────┼─────────────┘
          │                 │                 │                 │
          └─────────────────┴────────┬────────┴─────────────────┘
                                     ▼
┌─────────────────────────────────────────────────────────────────────────────┐
│                              业务层 (Service Layer)                          │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐     │
│  │  模板引擎    │  │  渠道路由    │  │  限流控制    │  │  消息队列    │     │
│  │  (变量替换)  │  │  (负载均衡)  │  │  (滑动窗口)  │  │  (Redis)     │     │
│  └──────────────┘  └──────────────┘  └──────────────┘  └──────────────┘     │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐     │
│  │  定时调度    │  │  批量合并    │  │  重试机制    │  │  状态机      │     │
│  │  (XXL-Job)   │  │  (窗口合并)  │  │  (指数退避)  │  │  (状态流转)  │     │
│  └──────────────┘  └──────────────┘  └──────────────┘  └──────────────┘     │
└─────────────────────────────────────────────────────────────────────────────┘
                                     ▼
┌─────────────────────────────────────────────────────────────────────────────┐
│                             渠道层 (Channel Layer)                           │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐     │
│  │  短信渠道    │  │  钉钉渠道    │  │  站内信      │  │  邮件渠道    │     │
│  │  (多服务商)  │  │  (工作通知)  │  │  (WebSocket) │  │  (SMTP)      │     │
│  │              │  │              │  │              │  │              │     │
│  │ 阿里云/腾讯  │  │ 获取部门信息 │  │ 未读数提醒   │  │ P2阶段实现   │     │
│  └──────────────┘  └──────────────┘  └──────────────┘  └──────────────┘     │
└─────────────────────────────────────────────────────────────────────────────┘
                                     ▼
┌─────────────────────────────────────────────────────────────────────────────┐
│                             存储层 (Storage Layer)                           │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐     │
│  │  MySQL       │  │  Redis       │  │  Redis       │  │  Redis       │     │
│  │  消息主库    │  │  缓存        │  │  Stream队列  │  │  WebSocket   │     │
│  │              │  │  限流/模板   │  │  延时队列    │  │  Session管理 │     │
│  └──────────────┘  └──────────────┘  └──────────────┘  └──────────────┘     │
└─────────────────────────────────────────────────────────────────────────────┘
```

### 2.2 核心流程设计

#### 消息发送流程

```
┌─────────────┐     ┌─────────────┐     ┌─────────────┐     ┌─────────────┐
│  接收请求   │ --> │  参数校验   │ --> │  模板渲染   │ --> │  渠道路由   │
│             │     │             │     │  ${var}替换  │     │  选择渠道   │
└─────────────┘     └─────────────┘     └─────────────┘     └──────┬──────┘
                                                                   │
                    ┌─────────────┐     ┌─────────────┐           │
                    │  记录状态   │ <-- │  执行发送   │ <---------┘
                    │  (成功/失败)│     │             │
                    └─────────────┘     └─────────────┘
                          │
                    ┌─────┴─────┐
                    ▼           ▼
              ┌─────────┐  ┌─────────┐
              │  成功   │  │  失败   │
              │  返回   │  │  重试   │
              └─────────┘  │  告警   │
                           └─────────┘
```

#### 消息消费流程（Redis Stream）

```
┌─────────────────┐     ┌─────────────────┐     ┌─────────────────┐
│  Redis Stream   │ --> │  Consumer Group │ --> │  限流检查       │
│  (pending列表)  │     │  (消费者组)     │     │  (令牌桶算法)   │
└─────────────────┘     └─────────────────┘     └────────┬────────┘
                                                         │
                              ┌────────────────────────┐
                              ▼                        ▼
                       ┌─────────────┐          ┌─────────────┐
                       │  限流通过   │          │  限流拒绝   │
                       │  调用渠道   │          │  延迟重试   │
                       └──────┬──────┘          └─────────────┘
                              │
                       ┌──────┴──────┐
                       ▼             ▼
                ┌──────────┐   ┌──────────┐
                │  发送成功 │   │  发送失败 │
                │  ACK确认  │   │  重试计数 │
                └──────────┘   │  >3次？   │
                               └────┬─────┘
                                    │
                         ┌─────────┴─────────┐
                         ▼                   ▼
                  ┌────────────┐      ┌────────────┐
                  │  重试<3次  │      │  进入死信  │
                  │  放回队列  │      │  告警通知  │
                  └────────────┘      └────────────┘
```

---

## 三、数据库设计

详见: [DATABASE_SCHEMA.md](./DATABASE_SCHEMA.md)

### 核心表清单

| 表名               | 说明     | 数据量预估   | 备注                        |
|------------------|--------|---------|---------------------------|
| message_type     | 消息类型表  | < 200条  | 基础数据，包含任务通知、验证码等类型        |
| message_template | 消息模板表  | < 1000条 | 模板管理，支持变量替换               |
| message_channel  | 渠道配置表  | < 50条   | 渠道配置，支持多服务商               |
| message_record   | 消息记录表  | 20万条/天  | 30天 ≈ 600万条，存储消息整体信息      |
| message_receiver | 消息接收人表 | 40万条/天  | 30天 ≈ 1200万条，存储每个接收人的发送状态 |
| message_send_log | 发送日志表  | 20万条/天  | 7天 ≈ 140万条，详细发送日志         |

### 表关系说明

```
┌─────────────────┐     ┌─────────────────┐     ┌─────────────────┐
│  message_type   │────▶│ message_template│◀────│ message_channel │
│  (消息类型)      │     │  (消息模板)      │     │  (渠道配置)      │
└─────────────────┘     └────────┬────────┘     └─────────────────┘
                                 │
                                 ▼
                      ┌─────────────────────┐
                      │   message_record    │
                      │    (消息记录)        │
                      │   存储消息整体信息    │
                      └──────────┬──────────┘
                                 │ 1:N
                                 ▼
                      ┌─────────────────────┐
                      │  message_receiver   │
                      │   (消息接收人)       │
                      │ 存储每个接收人的状态  │
                      └──────────┬──────────┘
                                 │ 1:N
                                 ▼
                      ┌─────────────────────┐
                      │  message_send_log   │
                      │   (发送日志)         │
                      │ 记录每次发送的详情   │
                      └─────────────────────┘
```

**设计要点：**

1. **message_record** - 一条消息一条记录，存储消息整体信息和统计（total_count/success_count/fail_count）
2. **message_receiver** - 记录每个接收人在每个渠道的发送状态，支持状态流转（待发送→发送中→已发送→送达→已读/失败）
3. **message_send_log** - 记录每次发送的详细日志，用于问题排查和对账

### 数据清理策略

| 表名               | 保留时间 | 清理方式   |
|------------------|------|--------|
| message_record   | 30天  | 定时任务清理 |
| message_receiver | 30天  | 定时任务清理 |
| message_send_log | 7天   | 定时任务清理 |
| message_type     | 永久   | 手动维护   |
| message_template | 永久   | 逻辑删除   |
| message_channel  | 永久   | 逻辑删除   |

详见: [DATABASE_SCHEMA.md](./DATABASE_SCHEMA.md) 数据清理策略章节

---

## 四、模块结构设计

> 遵循 Carlos Framework 研发规范（AGENTS.md）的分层架构设计

```
carlos-message/
├── carlos-message-api                    # API接口定义（Feign接口）
│   └── src/main/java/com/carlos/message/
│       ├── api/                          # Feign接口定义
│       │   ├── MessageSendApi.java       # 消息发送接口
│       │   ├── MessageQueryApi.java      # 消息查询接口
│       │   ├── MessageTemplateApi.java   # 模板管理接口
│       │   └── fallback/                 # 熔断降级工厂
│       │       ├── MessageSendApiFallbackFactory.java
│       │       └── MessageQueryApiFallbackFactory.java
│       └── pojo/
│           ├── ao/                       # API响应对象（供调用方使用）
│           │   ├── MessageRecordAO.java
│           │   ├── MessageReceiverAO.java
│           │   └── MessageTemplateAO.java
│           └── param/                    # API请求参数（供调用方使用）
│               ├── MessageSendParam.java
│               ├── MessagePageParam.java
│               └── MessageTemplateParam.java
│
├── carlos-message-core                   # 核心模块（框架无关）
│   └── src/main/java/com/carlos/message/core/
│       ├── protocol/                     # 消息协议
│       │   ├── MessageProtocol.java
│       │   └── MessageStatus.java
│       ├── channel/                      # 渠道抽象层
│       │   ├── ChannelAdapter.java       # 渠道适配器接口
│       │   ├── ChannelFactory.java       # 渠道工厂
│       │   └── ChannelContext.java       # 渠道上下文
│       ├── template/                     # 模板引擎
│       │   ├── TemplateEngine.java
│       │   └── TemplateVariable.java
│       ├── queue/                        # 队列抽象
│       │   ├── MessageQueue.java
│       │   └── MessageConsumer.java
│       ├── limiter/                      # 限流组件
│       │   └── RateLimiter.java
│       ├── retry/                        # 重试组件
│       │   └── RetryPolicy.java
│       └── constants/                    # 常量定义
│           └── MessageConstants.java
│
├── carlos-message-bus                    # 业务模块（服务端实现）
│   └── src/main/java/com/carlos/message/
│       ├── apiimpl/                      # Feign接口实现（REST端点）
│       │   ├── MessageSendApiImpl.java
│       │   └── MessageQueryApiImpl.java
│       ├── controller/                   # Web控制器层（非Feign接口）
│       │   ├── MessageTemplateController.java
│       │   ├── MessageChannelController.java
│       │   └── MessageStatisticsController.java
│       ├── service/                      # Service层（业务逻辑）
│       │   ├── MessageSendService.java
│       │   ├── MessageQueryService.java
│       │   ├── MessageTemplateService.java
│       │   └── MessageChannelService.java
│       ├── manager/                      # Manager层（数据查询封装）
│       │   ├── MessageRecordManager.java
│       │   ├── MessageReceiverManager.java
│       │   ├── MessageTemplateManager.java
│       │   └── MessageChannelManager.java
│       ├── mapper/                       # MyBatis Mapper接口
│       │   ├── MessageRecordMapper.java
│       │   ├── MessageReceiverMapper.java
│       │   ├── MessageTemplateMapper.java
│       │   └── MessageChannelMapper.java
│       ├── convert/                      # MapStruct对象转换
│       │   ├── MessageConvert.java
│       │   ├── TemplateConvert.java
│       │   └── ChannelConvert.java
│       ├── websocket/                    # WebSocket处理器
│       │   ├── MessageWebSocketHandler.java
│       │   └── WebSocketSessionManager.java
│       ├── channel/                      # 渠道具体实现
│       │   ├── sms/
│       │   │   ├── SmsChannelAdapter.java
│       │   │   ├── SmsChannelProperties.java
│       │   │   ├── AliyunSmsProvider.java
│       │   │   └── TencentSmsProvider.java
│       │   ├── dingtalk/
│       │   │   ├── DingtalkChannelAdapter.java
│       │   │   └── DingtalkChannelProperties.java
│       │   ├── websocket/
│       │   │   └── WebSocketChannelAdapter.java
│       │   └── email/
│       │       └── EmailChannelAdapter.java
│       ├── scheduler/                    # 定时任务
│       │   ├── ScheduledMessageJob.java
│       │   └── DelayMessageJob.java
│       ├── config/                       # 配置类
│       │   ├── MessageProperties.java
│       │   ├── MessageAutoConfiguration.java
│       │   └── WebSocketConfig.java
│       ├── queue/                        # 队列实现
│       │   └── RedisMessageQueue.java
│       ├── limiter/                      # 限流实现
│       │   └── RedisRateLimiter.java
│       ├── monitor/                      # 监控
│       │   └── MessageMonitorService.java
│       └── pojo/
│           ├── dto/                      # DTO（服务层与数据层传输）
│           │   ├── MessageRecordDTO.java
│           │   ├── MessageReceiverDTO.java
│           │   └── MessageTemplateDTO.java
│           ├── entity/                   # Entity（数据库实体）
│           │   ├── MessageRecord.java
│           │   ├── MessageReceiver.java
│           │   ├── MessageTemplate.java
│           │   └── MessageChannel.java
│           ├── vo/                       # VO（视图对象）
│           │   ├── MessageRecordVO.java
│           │   ├── MessageReceiverVO.java
│           │   ├── MessageStatisticsVO.java
│           │   └── MessageTemplateVO.java
│           ├── param/                    # 请求参数（按操作细分）
│           │   ├── MessageCreateParam.java
│           │   ├── MessagePageParam.java
│           │   ├── MessageUpdateParam.java
│           │   ├── TemplateCreateParam.java
│           │   └── TemplateUpdateParam.java
│           ├── enums/                    # 枚举类型
│           │   ├── MessageStatusEnum.java
│           │   ├── MessageChannelEnum.java
│           │   ├── MessageTypeEnum.java
│           │   └── TemplateTypeEnum.java
│           └── excel/                    # Excel导入导出对象
│               └── MessageExportExcel.java
│
├── carlos-message-spring-boot-starter    # Spring Boot Starter
│   └── src/main/java/com/carlos/message/boot/
│       ├── MessageClient.java            # 客户端SDK主类
│       ├── MessageTemplate.java          # 模板注解
│       ├── MessageClientProperties.java  # 客户端配置
│       └── MessageClientAutoConfiguration.java
│
└── carlos-message-bus                 # 服务端启动模块
    └── src/main/java/com/carlos/message/server/
        ├── MessageServerApplication.java
        └── resources/
            ├── application.yml
            ├── mapper/                   # MyBatis XML映射文件
            │   └── message/
            │       ├── MessageRecordMapper.xml
            │       ├── MessageReceiverMapper.xml
            │       └── MessageTemplateMapper.xml
            └── META-INF/spring/
                └── org.springframework.boot.autoconfigure.AutoConfiguration.imports
```

### 分层领域模型说明

| 模型         | 所在包                                                                | 说明                       |
|------------|--------------------------------------------------------------------|--------------------------|
| **AO**     | `carlos-message-api/pojo/ao`                                       | API接口响应对象，供Feign调用方使用    |
| **Param**  | `carlos-message-api/pojo/param`<br>`carlos-message-bus/pojo/param` | API请求参数 / 前端参数接收对象，按操作细分 |
| **DTO**    | `carlos-message-bus/pojo/dto`                                      | 服务层与数据层之间的数据传输对象         |
| **VO**     | `carlos-message-bus/pojo/vo`                                       | 视图对象，响应给前端，需标注Swagger注解  |
| **Entity** | `carlos-message-bus/pojo/entity`                                   | 与数据库表结构一一对应              |
| **Enum**   | `carlos-message-bus/pojo/enums`                                    | 业务枚举，必须实现 `BaseEnum`     |
| **Excel**  | `carlos-message-bus/pojo/excel`                                    | 导入导出专用对象                 |

### 分层职责说明

| 层级              | 目录                               | 职责                                  |
|-----------------|----------------------------------|-------------------------------------|
| **API层**        | `carlos-message-api/api/`        | Feign接口定义，对外暴露服务，提供熔断降级工厂           |
| **API实现层**      | `carlos-message-bus/apiimpl/`    | Feign接口的REST实现，同时暴露HTTP端点           |
| **Controller层** | `carlos-message-bus/controller/` | 接收Web请求，完成参数校验，Param→DTO转换          |
| **Service层**    | `carlos-message-bus/service/`    | 业务逻辑服务层，处理业务流程，通过Manager层获取数据       |
| **Manager层**    | `carlos-message-bus/manager/`    | 数据查询封装层，继承 `BaseService`，实现CRUD原子操作 |
| **Mapper层**     | `carlos-message-bus/mapper/`     | 数据访问层，MyBatis实现，与数据库交互              |
| **Convert层**    | `carlos-message-bus/convert/`    | MapStruct对象转换接口                     |
| **渠道层**         | `carlos-message-bus/channel/`    | 各消息渠道的具体实现                          |

### 包命名规范

```java
// API模块
package com.carlos.message.api;
package com.carlos.message.pojo.ao;
package com.carlos.message.pojo.param;

// Business模块
package com.carlos.message.apiimpl;
package com.carlos.message.controller;
package com.carlos.message.service;
package com.carlos.message.manager;
package com.carlos.message.mapper;
package com.carlos.message.convert;
package com.carlos.message.channel.sms;
package com.carlos.message.websocket;
package com.carlos.message.scheduler;
package com.carlos.message.config;
package com.carlos.message.pojo.dto;
package com.carlos.message.pojo.entity;
package com.carlos.message.pojo.vo;
package com.carlos.message.pojo.param;
package com.carlos.message.pojo.enums;
package com.carlos.message.pojo.excel;
```

---

## 五、开发路线图

### Phase 1: 基础功能（3周）

**Week 1: 基础设施搭建**

| 任务                 | 工时 | 输出物                                                                 |
|--------------------|----|---------------------------------------------------------------------|
| 数据库表结构设计与创建        | 1天 | SQL脚本                                                               |
| 核心模块重构（API、Core）   | 3天 | carlos-message-api, carlos-message-core<br>carlos-message-bus（基础结构） |
| Redis Stream消息队列实现 | 2天 | RedisMessageQueue.java                                              |
| 渠道抽象与工厂设计          | 2天 | ChannelAdapter（core）<br>ChannelFactory（core）<br>各渠道实现（bus/channel/） |

**Week 2: 渠道实现**

| 任务           | 工时 | 输出物                                                                                                                     |
|--------------|----|-------------------------------------------------------------------------------------------------------------------------|
| 短信渠道适配（多服务商） | 3天 | SmsChannelAdapter（bus/channel/sms/）<br>AliyunSmsProvider, TencentSmsProvider                                            |
| 钉钉渠道适配（工作通知） | 2天 | DingtalkChannelAdapter（bus/channel/dingtalk/）                                                                           |
| 渠道配置管理功能     | 2天 | MessageChannelController（bus/controller/）<br>MessageChannelService（bus/service/）<br>MessageChannelManager（bus/manager/） |

**Week 3: 发送流程与模板**

| 任务         | 工时 | 输出物                                                                                                                        |
|------------|----|----------------------------------------------------------------------------------------------------------------------------|
| 消息发送核心流程   | 3天 | MessageSendService（bus/service/）<br>MessageSendApiImpl（bus/apiimpl/）                                                       |
| 模板引擎（变量替换） | 2天 | TemplateEngine（core/template/）                                                                                             |
| 模板管理功能     | 2天 | MessageTemplateController（bus/controller/）<br>MessageTemplateService（bus/service/）<br>MessageTemplateManager（bus/manager/） |

**Phase 1 交付物:**

- [ ] 短信、钉钉发送功能（carlos-message-bus）
- [ ] 基础模板管理（carlos-message-bus）
- [ ] REST API（carlos-message-api + carlos-message-bus/apiimpl/）
- [ ] Java SDK（carlos-message-spring-boot-starter）

---

### Phase 2: 功能完善（3周）

**Week 4: 站内信与消息追踪**

| 任务                  | 工时 | 输出物                                                                                        |
|---------------------|----|--------------------------------------------------------------------------------------------|
| WebSocket站内信实现      | 4天 | MessageWebSocketHandler（bus/websocket/）<br>WebSocketChannelAdapter（bus/channel/websocket/） |
| WebSocket Session管理 | 2天 | WebSocketSessionManager（bus/websocket/）                                                    |
| 消息状态流转（已读回执）        | 2天 | 状态机实现（core/protocol/ + bus/manager/）                                                       |

**Week 5: 定时/延时发送**

| 任务            | 工时 | 输出物                                                              |
|---------------|----|------------------------------------------------------------------|
| XXL-Job定时任务集成 | 2天 | ScheduledMessageJob（bus/scheduler/）                              |
| 延时队列实现        | 2天 | DelayMessageJob（bus/scheduler/）<br>RedisMessageQueue（bus/queue/） |
| 定时发送管理        | 2天 | ScheduleController（bus/controller/）                              |
| 消息撤回功能        | 2天 | revoke接口                                                         |

**Week 6: 增强功能**

| 任务          | 工时 | 输出物                                                          |
|-------------|----|--------------------------------------------------------------|
| 批量发送优化      | 2天 | MessageSendService.batchSend()（bus/service/）                 |
| 限流控制（滑动窗口）  | 2天 | RateLimiter（core/limiter/）<br>RedisRateLimiter（bus/limiter/） |
| 失败告警（邮件/短信） | 2天 | AlarmService（bus/service/）                                   |
| 邮件渠道（P2）    | 2天 | EmailChannelAdapter（bus/channel/email/）                      |

**Phase 2 交付物:**

- [ ] 站内信（WebSocket）（carlos-message-bus/websocket/ + bus/channel/websocket/）
- [ ] 定时/延时发送（carlos-message-bus/scheduler/）
- [ ] 消息状态追踪（carlos-message-bus/service/）
- [ ] 管理后台API（carlos-message-bus/controller/）

---

### Phase 3: 企业级特性（3周）

**Week 7-8: 监控与优化**

| 任务          | 工时 | 输出物                                                                              |
|-------------|----|----------------------------------------------------------------------------------|
| 内置监控页面      | 4天 | MessageMonitorController（bus/controller/）<br>MessageMonitorService（bus/monitor/） |
| 发送统计报表      | 3天 | MessageStatisticsController（bus/controller/）<br>统计查询方法（bus/manager/）             |
| 性能优化（批量/异步） | 3天 | 优化后的发送流程                                                                         |
| 压力测试与调优     | 4天 | 压测报告                                                                             |

**Week 9: 完善与文档**

| 任务      | 工时 | 输出物        |
|---------|----|------------|
| 单元测试覆盖  | 3天 | 测试用例       |
| 集成测试    | 2天 | 集成测试报告     |
| 文档完善    | 3天 | 使用文档、API文档 |
| 代码评审与优化 | 2天 | 优化后的代码     |

**Phase 3 交付物:**

- [ ] 内置监控页面
- [ ] 统计报表
- [ ] 完整测试覆盖
- [ ] 技术文档

---

## 六、关键设计决策

### 6.1 为什么使用Redis Stream而非RabbitMQ/RocketMQ？

| 对比项       | Redis Stream | RabbitMQ | RocketMQ |
|-----------|--------------|----------|----------|
| **部署成本**  | 低（已有Redis）   | 中（需额外部署） | 高（需集群）   |
| **运维复杂度** | 低            | 中        | 高        |
| **功能满足度** | 满足需求         | 功能过剩     | 功能过剩     |
| **团队熟悉度** | 高            | 中        | 低        |
| **延迟消息**  | 配合ZSet实现     | 插件支持     | 原生支持     |

**决策:** 使用Redis Stream + ZSet实现消息队列和延时队列，降低运维成本。

### 6.2 短信渠道多服务商设计

```java
// 策略：优先级 + 故障转移 + 负载均衡
public class SmsChannelRouter {
    
    // 1. 按优先级选择
    List<SmsProvider> providers = getProvidersByPriority();
    
    // 2. 过滤掉故障的渠道
    providers = providers.stream()
        .filter(p -> p.isHealthy())
        .collect(Collectors.toList());
    
    // 3. 按权重选择
    return selectByWeight(providers);
}
```

### 6.3 WebSocket Session管理

```
方案: Redis集中存储Session映射
┌──────────────┐      ┌──────────────┐      ┌──────────────┐
│   用户ID     │  --> │   Redis      │  --> │  服务器节点   │
│  user_123    │      │  Hash存储    │      │  ws-node-1   │
└──────────────┘      │  {userId:    │      └──────────────┘
                      │   serverId}  │
                      └──────────────┘
```

---

## 七、风险与应对

| 风险               | 影响 | 应对措施              |
|------------------|----|-------------------|
| 短信渠道接口变动         | 高  | 封装抽象层，隔离具体实现      |
| WebSocket连接数过高   | 高  | 使用Netty优化，支持10万连接 |
| Redis Stream消费延迟 | 中  | 多消费者并行，监控积压       |
| 钉钉API限流          | 中  | 本地缓存Token，限流控制    |
| 消息丢失             | 高  | 发送前落库，ACK确认机制     |

---

## 八、下一步行动

### 8.1 待确认事项

1. **✅ 确认整体方案** - 是否有需要调整的地方？
2. **⚠️ 确认数据库设计** - 表结构是否满足需求？
3. **✅ 确认模块划分** - 已按 Carlos Framework 规范调整完成
4. **⚠️ 确认开发节奏** - 3+3+3周的节奏是否合适？

### 8.2 启动开发所需准备

确认后，我将立即开始：

1. 创建详细的数据库变更脚本
2. 搭建 carlos-message-api 模块（Feign接口定义）
3. 搭建 carlos-message-core 模块（渠道抽象、模板引擎）
4. 搭建 carlos-message-bus 模块（业务实现、渠道实现）
5. 搭建 carlos-message-spring-boot-starter 模块（客户端SDK）
6. 搭建 carlos-message-bus 启动模块
7. 实现短信和钉钉渠道
8. 搭建基础发送流程

---

## 附录

- [需求问卷](./REQUIREMENT_QUESTIONNAIRE.md)
- [数据库设计](./DATABASE_SCHEMA.md)
- [模块分析报告](./MESSAGE_MODULE_ANALYSIS.md)
- [设计规格说明书](./MESSAGE_DESIGN_SPEC.md)
- [开发任务清单](./TODO.md)

---

**方案制定: AI Assistant**  
**日期: 2026-03-11**
