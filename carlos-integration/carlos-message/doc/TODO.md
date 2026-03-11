# Carlos Message 模块开发任务清单

> 最后更新: 2026-03-11

---

## 🔴 P0 - 紧急（阻塞性问题）

### 核心功能

- [ ] **MSG-P0-001**: 实现 `ChannelAdapter` 接口定义 - 渠道适配器接口
    - 文件: `carlos-message-core/.../channel/ChannelAdapter.java`
    - 预计工时: 2h

- [ ] **MSG-P0-002**: 完成 `ChannelFactory` 工厂实现 - 渠道工厂
    - 文件: `carlos-message-core/.../channel/ChannelFactory.java`
    - 预计工时: 4h

- [ ] **MSG-P0-003**: 实现短信渠道适配器 `SmsChannelAdapter`
    - 文件: `carlos-message-bus/.../channel/sms/SmsChannelAdapter.java`
    - 预计工时: 8h
    - 依赖: MSG-P0-001, MSG-P0-002

- [ ] **MSG-P0-004**: 实现钉钉渠道适配器 `DingtalkChannelAdapter`
    - 文件: `carlos-message-bus/.../channel/dingtalk/DingtalkChannelAdapter.java`
    - 预计工时: 8h
    - 依赖: MSG-P0-001, MSG-P0-002

- [ ] **MSG-P0-005**: 完成消息发送主逻辑 `MessageSendService.send()`
    - 文件: `carlos-message-bus/.../service/MessageSendService.java`
    - 预计工时: 8h
    - 依赖: MSG-P0-003, MSG-P0-004

- [ ] **MSG-P0-006**: 完善 `MessageContext` 消息上下文
    - 文件: `carlos-message-core/.../protocol/MessageContext.java`
    - 预计工时: 2h

### API 接口

- [ ] **MSG-P0-007**: 启用 Feign 接口 `MessageSendApi`
    - 文件: `carlos-message-api/.../api/MessageSendApi.java`
    - 预计工时: 2h

- [ ] **MSG-P0-008**: 完善 Feign 接口实现 `MessageSendApiImpl`
    - 文件: `carlos-message-bus/.../apiimpl/MessageSendApiImpl.java`
    - 预计工时: 4h
    - 依赖: MSG-P0-007

---

## 🟡 P1 - 高优先级（重要功能）

### 消息队列

- [ ] **MSG-P1-001**: 添加 Redis Stream 消息队列支持
    - 预计工时: 16h
    - 依赖: MSG-P0-005

- [ ] **MSG-P1-002**: 实现延时消息功能
    - 预计工时: 8h
    - 依赖: MSG-P1-001

- [ ] **MSG-P1-003**: 实现定时消息调度
    - 预计工时: 8h
    - 依赖: MSG-P1-001

### 渠道扩展

- [ ] **MSG-P1-004**: 实现邮件渠道适配器
    - 预计工时: 8h
    - 依赖: MSG-P0-001

- [ ] **MSG-P1-005**: 启用 WebSocket 站内信
    - 文件: `carlos-message-bus/.../websocket/MessageWebSocketHandler.java`
    - 预计工时: 12h

- [ ] **MSG-P1-006**: 实现企业微信渠道适配器
    - 预计工时: 8h
    - 依赖: MSG-P0-001

### 客户端 SDK

- [ ] **MSG-P1-007**: 完善客户端模块 `carlos-message-spring-boot-starter`
    - 文件: `carlos-message-spring-boot-starter/.../MessageClient.java`
    - 预计工时: 8h
    - 依赖: MSG-P0-007

- [ ] **MSG-P1-008**: 实现客户端自动配置
    - 文件: `carlos-message-spring-boot-starter/.../MessageClientAutoConfiguration.java`
    - 预计工时: 4h
    - 依赖: MSG-P1-007

---

## 🟢 P2 - 中优先级（增强功能）

### 模板引擎

- [ ] **MSG-P2-001**: 集成 FreeMarker 模板引擎
    - 预计工时: 8h

- [ ] **MSG-P2-002**: 实现模板变量校验
    - 预计工时: 4h
    - 依赖: MSG-P2-001

### 限流与熔断

- [ ] **MSG-P2-003**: 实现渠道限流控制
    - 预计工时: 8h

- [ ] **MSG-P2-004**: 实现熔断降级机制
    - 预计工时: 8h

### 重试机制

- [ ] **MSG-P2-005**: 实现消息发送重试
    - 预计工时: 8h
    - 依赖: MSG-P1-001

- [ ] **MSG-P2-006**: 实现死信队列
    - 预计工时: 4h
    - 依赖: MSG-P2-005

### 消息管理

- [ ] **MSG-P2-007**: 实现消息撤回功能
    - 预计工时: 4h

- [ ] **MSG-P2-008**: 实现批量发送优化
    - 预计工时: 8h

---

## ⚪ P3 - 低优先级（优化与扩展）

### 监控与告警

- [ ] **MSG-P3-001**: 集成 Micrometer 指标监控
    - 预计工时: 4h

- [ ] **MSG-P3-002**: 配置 Grafana 监控大盘
    - 预计工时: 4h
    - 依赖: MSG-P3-001

- [ ] **MSG-P3-003**: 实现告警通知
    - 预计工时: 4h

### 内容安全

- [ ] **MSG-P3-004**: 实现敏感词过滤
    - 预计工时: 8h

- [ ] **MSG-P3-005**: 实现内容审计
    - 预计工时: 8h

### 渠道扩展

- [ ] **MSG-P3-006**: 实现飞书渠道适配器
    - 预计工时: 8h

- [ ] **MSG-P3-007**: 实现移动推送渠道（极光推送）
    - 预计工时: 12h

### 管理后台

- [ ] **MSG-P3-008**: 开发消息管理后台
    - 预计工时: 40h

- [ ] **MSG-P3-009**: 开发发送统计报表
    - 预计工时: 16h

---

## 📋 代码重构

- [ ] **MSG-REF-001**: 重构渠道配置序列化 - 完善 `ChannelConfig` JSON 多态序列化
    - 文件: `carlos-message-core/.../channel/ChannelConfig.java`
    - 预计工时: 4h

- [ ] **MSG-REF-002**: 统一异常处理 - 完善 `MessageException` 体系
    - 文件: `carlos-message-core/.../exception/MessageException.java`
    - 预计工时: 4h

- [ ] **MSG-REF-003**: 创建数据库初始化脚本 - 参考 `DATABASE_SCHEMA.md`
    - 文件: `doc/database_init.sql`
    - 预计工时: 8h

- [ ] **MSG-REF-004**: 完善单元测试覆盖
    - 预计工时: 16h

---

## 📚 文档任务

- [ ] **MSG-DOC-001**: 完善 README.md - 添加使用说明
    - 预计工时: 4h

- [ ] **MSG-DOC-002**: 编写 API 接口文档
    - 预计工时: 4h

- [ ] **MSG-DOC-003**: 编写渠道接入指南
    - 预计工时: 4h

---

## 📊 进度统计

| 优先级    | 任务数    | 已完成   | 进行中   | 待开始    |
|--------|--------|-------|-------|--------|
| P0 紧急  | 8      | 0     | 0     | 8      |
| P1 高优  | 8      | 0     | 0     | 8      |
| P2 中优  | 8      | 0     | 0     | 8      |
| P3 低优  | 9      | 0     | 0     | 9      |
| 重构     | 4      | 0     | 0     | 4      |
| 文档     | 3      | 0     | 0     | 3      |
| **总计** | **40** | **0** | **0** | **40** |

---

## 🎯 里程碑计划

### Milestone 1: 基础功能可用 (预计 2 周)

**目标**: 实现短信、钉钉发送，API 可用

包含任务:

- P0: MSG-P0-001 ~ MSG-P0-008

### Milestone 2: 功能增强 (预计 2 周)

**目标**: 支持异步、延时、邮件、WebSocket

包含任务:

- P1: MSG-P1-001 ~ MSG-P1-008
- REF: MSG-REF-001 ~ MSG-REF-002

### Milestone 3: 企业级特性 (预计 2 周)

**目标**: 限流、熔断、重试、批量、监控

包含任务:

- P2: MSG-P2-001 ~ MSG-P2-008
- REF: MSG-REF-003 ~ MSG-REF-004
- P3: MSG-P3-001 ~ MSG-P3-003

### Milestone 4: 生态扩展 (持续)

**目标**: 多渠道支持、管理后台、内容安全

包含任务:

- P3: MSG-P3-004 ~ MSG-P3-009
- DOC: MSG-DOC-001 ~ MSG-DOC-003

---

## 📝 备注

1. 任务编号格式: `MSG-{优先级}-{序号}`
2. 工时估算基于单人开发，实际情况可能因人员能力、需求变更等因素调整
3. 依赖关系需严格按顺序执行，避免阻塞
4. 建议每个 Milestone 完成后进行一次代码评审和文档更新
