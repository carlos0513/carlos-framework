# carlos-message v2.0.0

<h4 align="center">Spring Boot/Spring Cloud & Alibaba 统一消息中心</h4>

<p align="center">
  <a href="#"><img src="https://img.shields.io/badge/version-2.0.0-blue.svg" alt="Version"></a>
  <a href="#"><img src="https://img.shields.io/badge/Spring%20Boot-3.5.9-green.svg" alt="Spring Boot"></a>
  <a href="#"><img src="https://img.shields.io/badge/JDK-17+-orange.svg" alt="JDK"></a>
  <a href="#"><img src="https://img.shields.io/badge/License-Apache%202.0-red.svg" alt="License"></a>
</p>

---

## 📖 目录

- [简介](#-简介)
- [特性](#-特性)
- [架构设计](#-架构设计)
- [快速开始](#-快速开始)
- [模块说明](#-模块说明)
- [文档](#-文档)
- [路线图](#-路线图)
- [贡献](#-贡献)

---

## 🚀 简介

Carlos Message 是一个企业级统一消息中心，为业务系统提供一站式消息发送解决方案。支持短信、邮件、钉钉、企业微信、站内信等多种渠道，提供模板管理、定时发送、批量发送、消息追踪等完整功能。

### 核心能力

- 📨 **多渠道支持**: 短信、邮件、钉钉、企业微信、WebSocket站内信
- 📝 **模板管理**: 支持变量替换、条件渲染的模板引擎
- ⏰ **定时调度**: 支持定时发送、延时消息
- 📊 **消息追踪**: 完整的发送记录和状态追踪
- 🔄 **失败重试**: 自动重试机制，确保消息送达
- 🛡️ **限流熔断**: 保护下游渠道，防止过载

---

## ✨ 特性

| 特性           | 状态 | 说明             |
|--------------|----|----------------|
| 短信发送         | ✅  | 支持阿里云、腾讯云等主流厂商 |
| 邮件发送         | ✅  | 支持 SMTP、企业邮箱   |
| 钉钉通知         | ✅  | 支持工作通知、群机器人    |
| 企业微信         | 🚧 | 开发中            |
| WebSocket站内信 | 🚧 | 开发中            |
| 定时消息         | 🚧 | 开发中            |
| 延时消息         | 🚧 | 开发中            |
| 批量发送         | 🚧 | 开发中            |

---

## 🏗️ 架构设计

```
┌─────────────────────────────────────────────────────────────────┐
│                    统一消息中心 (Unified Message Center)           │
├─────────────────────────────────────────────────────────────────┤
│  接入层                                                          │
│  ├── REST API (同步/异步发送)                                     │
│  ├── gRPC (高性能内部调用)                                        │
│  ├── WebSocket (实时推送)                                         │
│  └── MQ Consumer (异步消费)                                       │
├─────────────────────────────────────────────────────────────────┤
│  业务层                                                          │
│  ├── 消息模板引擎 (变量替换、条件渲染)                              │
│  ├── 渠道路由策略 (优先级、负载均衡、故障转移)                       │
│  ├── 消息优先级队列 (高/中/低优先级)                               │
│  ├── 消息定时调度 (延时消息、定时广播)                              │
│  ├── 批量发送优化 (合并发送、批量通道)                              │
│  └── 发送频率控制 (限流、防刷)                                     │
├─────────────────────────────────────────────────────────────────┤
│  渠道适配层                                                       │
│  ├── 短信 (阿里云/腾讯云/华为云/聚合数据)                           │
│  ├── 邮件 (SMTP/企业邮箱/邮件模板)                                 │
│  ├── 即时通讯 (钉钉/企业微信/飞书)                                  │
│  └── 站内信 (WebSocket/SSE/轮询)                                   │
├─────────────────────────────────────────────────────────────────┤
│  存储层                                                          │
│  ├── 消息主库 (MySQL - 消息元数据)                                 │
│  ├── 消息内容存储 (可选: MongoDB/对象存储)                          │
│  └── 缓存层 (Redis - 限流、模板缓存、用户连接)                       │
└─────────────────────────────────────────────────────────────────┘
```

---

## 🚀 快速开始

### 环境要求

- JDK 17+
- Maven 3.8+
- MySQL 8.0+
- Redis 6.0+

### 1. 初始化数据库

```bash
# 创建数据库
mysql -u root -p < carlos-message-server/src/main/resources/message_v2.sql
```

### 2. 配置渠道

编辑 `application.yml`:

```yaml
message:
  channels:
    sms:
      enabled: true
      provider: aliyun
      aliyun:
        access-key: your-access-key
        secret-key: your-secret-key
        sign-name: your-sign-name
    dingtalk:
      enabled: true
      app-key: your-app-key
      app-secret: your-app-secret
```

### 3. 启动服务

```bash
cd carlos-message-boot
mvn spring-boot:run
```

### 4. 发送消息

```java
@Autowired
private MessageClient messageClient;

public void sendMessage() {
    SendRequest request = new SendRequest()
        .setTemplateCode("ORDER_SUCCESS")
        .setParams(Map.of(
            "orderNo", "202403110001",
            "amount", "199.00"
        ))
        .setReceivers(List.of(
            new Receiver().setPhone("13800138000")
        ))
        .setChannels(List.of(ChannelType.SMS));
    
    SendResult result = messageClient.send(request);
    System.out.println("消息ID: " + result.getMsgId());
}
```

---

## 📦 模块说明

```
carlos-message/
├── carlos-message-api                    # API接口定义（Feign/gRPC）
├── carlos-message-core                   # 核心模块（协议、枚举、配置）
├── carlos-message-client                 # 客户端SDK
├── carlos-message-server-api             # 服务端API层
├── carlos-message-server-service         # 服务端业务层
├── carlos-message-server-channel         # 渠道实现
├── carlos-message-server-infrastructure  # 基础设施
├── carlos-message-boot                   # Spring Boot 启动入口
└── carlos-message-cloud                  # Spring Cloud 启动入口
```

| 模块                    | 说明              | 依赖                                                |
|-----------------------|-----------------|---------------------------------------------------|
| carlos-message-core   | 核心协议和接口定义       | 无                                                 |
| carlos-message-client | 客户端 SDK，供业务系统引用 | carlos-message-core                               |
| carlos-message-server | 服务端实现           | carlos-message-core, carlos-spring-boot-starter-* |
| carlos-message-boot   | 单机版启动入口         | carlos-message-server                             |
| carlos-message-cloud  | 微服务版启动入口        | carlos-message-server, spring-cloud-starter-*     |

---

## 📚 文档

- [模块分析报告](./MESSAGE_MODULE_ANALYSIS.md) - 当前状态分析与优化建议
- [设计规格说明书](./MESSAGE_DESIGN_SPEC.md) - 详细设计文档
- [开发任务清单](./TODO.md) - 开发计划与进度跟踪

---

## 🗺️ 路线图

### Phase 1: 基础能力建设（2-3周）

- [x] 基础框架搭建
- [ ] 完善短信、钉钉渠道实现
- [ ] 实现消息发送核心流程
- [ ] 添加消息队列支持

### Phase 2: 功能增强（2-3周）

- [ ] 增加邮件渠道
- [ ] 实现 WebSocket 站内信
- [ ] 添加消息模板引擎
- [ ] 实现延时/定时消息

### Phase 3: 企业级特性（2-3周）

- [ ] 限流、熔断能力
- [ ] 消息优先级队列
- [ ] 批量发送优化
- [ ] 监控和告警

### Phase 4: 生态扩展（持续）

- [ ] 更多渠道（企业微信、飞书、移动推送）
- [ ] 国际化支持
- [ ] 消息分析报表

---

## 🤝 贡献

欢迎提交 Issue 和 Pull Request！

### 开发规范

1. 遵循 [Carlos Framework 开发规范](../../AGENTS.md)
2. 所有代码必须通过单元测试
3. 提交前执行 `mvn clean verify`

### 代码提交

```bash
# 1. Fork 本仓库
# 2. 创建特性分支
git checkout -b feature/your-feature

# 3. 提交更改
git commit -am "Add: your feature"

# 4. 推送到分支
git push origin feature/your-feature

# 5. 创建 Pull Request
```

---

## 📄 许可证

[Apache License 2.0](../../LICENSE)

---

## 💬 联系我们

如有问题或建议，欢迎通过以下方式联系：

- 提交 [GitHub Issue](../../issues)
- 发送邮件至: support@carlos.com

---

<p align="center">Made with ❤️ by Carlos Team</p>
