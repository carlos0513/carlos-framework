# Carlos Framework 待办事项

> 本文档用于记录和跟进 Carlos Framework 项目的待办工作项
>
> **更新日期**: 2026-03-25
>
> **版本**: 3.0.0-SNAPSHOT

---

## 🚀 推荐使用 GitHub Issues 管理

本项目已配置完整的 **GitHub Issues** 待办管理系统，推荐使用 Issues 替代本文件进行任务跟踪。

### 快速开始

```bash
# 1. 安装 GitHub CLI
# https://cli.github.com/

# 2. 登录 GitHub
gh auth login

# 3. 创建任务标签（只需执行一次）
## Linux/Mac:
./scripts/create-todos.sh create-labels

## Windows PowerShell:
.\scripts\create-todos.ps1 create-labels

# 4. 批量导入待办事项
## Linux/Mac:
./scripts/create-todos.sh create-all

## Windows PowerShell:
.\scripts\create-todos.ps1 create-all
```

### 手动创建 Issue

1. 访问仓库 Issues 页面
2. 点击 **New Issue**
3. 选择 **Task** 模板
4. 填写任务信息并提交

### Issue 标签体系

| 标签                  | 颜色 | 说明    |
|---------------------|----|-------|
| `task`              | 🟢 | 任务类型  |
| `priority-critical` | 🔴 | 紧急优先级 |
| `priority-high`     | 🟠 | 高优先级  |
| `priority-medium`   | 🟡 | 中优先级  |
| `priority-low`      | 🟢 | 低优先级  |
| `status-todo`       | 🔵 | 待处理   |
| `status-doing`      | 🟡 | 进行中   |
| `status-done`       | 🟢 | 已完成   |
| `category-core`     | 🔵 | 核心功能  |
| `category-test`     | 🟣 | 测试相关  |
| `category-docs`     | 🔵 | 文档完善  |
| `category-security` | 🔴 | 安全相关  |

---

## 📋 本文件使用说明

- [ ] 未开始 - 待处理的任务
- [~] 进行中 - 正在处理的任务
- [x] 已完成 - 已完成的任务

---

## 🚀 高优先级任务

### 核心功能完善

- [ ] 完善 `carlos-spring-boot-starter-oauth2` 模块文档
- [ ] 补充 `carlos-spring-boot-starter-datascope` 数据权限模块单元测试
- [ ] 完成 `carlos-spring-boot-starter-flowable` 工作流模块集成测试
- [ ] 优化 `carlos-spring-boot-starter-redis` 多级缓存配置文档

### 测试覆盖

- [ ] 提升 `carlos-commons` 模块单元测试覆盖率至 80%+
- [ ] 补充 `carlos-spring-boot-starter-encrypt` 国密加密模块测试用例
- [ ] 完善 `carlos-license` 模块许可证验证测试
- [ ] 编写 `carlos-tools` GUI 工具的自动化测试

### 文档完善

- [ ] 更新各 Starter 模块的 README.md 文档
- [ ] 编写 `carlos-spring-boot-starter-minio` 使用指南
- [ ] 补充 `carlos-spring-boot-starter-mq` 消息队列抽象文档
- [ ] 完善 `carlos-spring-boot-starter-sms` 多提供商配置说明

---

## 🔧 中优先级任务

### 性能优化

- [ ] 优化 `carlos-spring-boot-starter-disruptor` 高性能队列配置
- [ ] 评估并升级 `carlos-spring-boot-starter-apm` SkyWalking 版本
- [ ] 优化 `carlos-utils` 工具类的性能瓶颈
- [ ] 审查 `carlos-excel` 大数据量导出性能

### 代码质量

- [ ] 统一所有模块的代码格式（使用 .editorconfig）
- [ ] 检查并修复所有模块的 SonarQube 警告
- [ ] 优化 `carlos-spring-boot-starter-web` 全局异常处理
- [ ] 审查并优化 `carlos-spring-cloud-starter` 配置项

### 安全加固

- [ ] 审查所有模块的依赖安全漏洞
- [ ] 完善 `carlos-spring-boot-starter-oauth2` 安全配置
- [ ] 加强 `carlos-spring-boot-starter-encrypt` 密钥管理
- [ ] 补充 SQL 注入防护测试用例

---

## 📦 模块开发任务

### Spring Boot Starters

- [ ] `carlos-spring-boot-starter-datacenter` - 数据中心集成完善
- [ ] `carlos-spring-boot-starter-docking` - 钉钉/荣之通集成测试
- [ ] `carlos-spring-boot-starter-snowflake` - 分布式 ID 生成文档
- [ ] `carlos-spring-boot-starter-openapi` - Knife4j 配置优化

### 数据访问

- [ ] `carlos-spring-boot-starter-mybatis` - 多数据源事务测试
- [ ] `carlos-spring-boot-starter-mongodb` - 完善 CRUD 封装
- [ ] `carlos-spring-boot-starter-redis` - Redisson 分布式锁示例

### 基础设施

- [ ] `carlos-spring-boot-starter-log` - 日志增强功能完善
- [ ] `carlos-spring-boot-starter-oss` - OSS 抽象接口实现
- [ ] `carlos-spring-boot-starter-json` - Fastjson2 序列化优化

---

## 📝 文档和示例

### 使用文档

- [ ] 编写框架整体架构图
- [ ] 创建快速开始指南（Quick Start）
- [ ] 编写各模块的详细使用教程
- [ ] 补充常见问题 FAQ 文档

### 示例代码

- [ ] 完善 `carlos-test` 测试应用示例
- [ ] 添加多租户使用示例
- [ ] 补充工作流引擎使用示例
- [ ] 创建微服务调用链示例

### API 文档

- [ ] 完善 `carlos-spring-boot-starter-openapi` 注解
- [ ] 生成并发布 API 文档站点
- [ ] 编写接口变更日志

---

## 🔄 持续集成/部署

### CI/CD

- [ ] 配置 GitHub Actions 自动化构建
- [ ] 添加代码质量检查流程
- [ ] 配置自动化测试执行
- [ ] 设置自动化发布流程

### 依赖管理

- [ ] 检查并更新依赖版本
- [ ] 评估 Spring Boot 3.6.x 升级可行性
- [ ] 检查依赖冲突并解决
- [ ] 更新 `carlos-dependencies` BOM 版本

---

## 🌟 新功能规划

### 计划中的模块

- [ ] `carlos-spring-boot-starter-websocket` - WebSocket 支持
- [ ] `carlos-spring-boot-starter-job` - 定时任务封装
- [ ] `carlos-spring-boot-starter-search` - Elasticsearch 集成增强
- [ ] `carlos-spring-boot-starter-security` - 安全加固扩展

### 工具增强

- [ ] 优化 `carlos-tools` 代码生成器
- [ ] 添加项目脚手架模板
- [ ] 增加数据库文档生成工具
- [ ] 完善 GitLab 集成工具

---

## 🐛 已知问题

### Bug 修复

- [ ] 修复 `carlos-test` 中的测试失败用例
- [ ] 处理 `carlos-license` 在某些环境的兼容性问题
- [ ] 修复 `carlos-spring-boot-starter-redis` 序列化问题
- [ ] 处理 `carlos-excel` 大文件导出内存溢出问题

### 技术债务

- [ ] 重构 `carlos-utils` 中的过时工具类
- [ ] 优化 `carlos-spring-boot-core` 中的遗留代码
- [ ] 清理各模块的废弃配置项
- [ ] 统一异常处理机制

---

## 📊 度量和监控

### 质量指标

- [ ] 设置代码覆盖率监控
- [ ] 配置 SonarQube 质量门禁
- [ ] 建立性能基准测试
- [ ] 监控依赖安全漏洞

### 文档指标

- [ ] 完善 API 文档覆盖率
- [ ] 补充模块 README 完成度
- [ ] 编写架构决策记录（ADR）

---

## 👥 社区和协作

### 贡献指南

- [ ] 完善 CONTRIBUTING.md
- [ ] 编写代码审查指南
- [ ] 创建 Issue 模板
- [ ] 创建 Pull Request 模板

### 版本管理

- [ ] 制定版本发布计划
- [ ] 编写 CHANGELOG 规范
- [ ] 规划 3.0.0 正式版发布时间
- [ ] 准备版本迁移指南

---

## 📅 近期计划

### 本周计划

- [ ] 
- [ ] 
- [ ] 

### 本月计划

- [ ] 
- [ ] 
- [ ] 

---

## 📝 备注

<!-- 在此处添加额外的备注信息 -->

### 重要提示

1. **License 模块安全**: 生产环境严禁包含 `carlos-spring-boot-starter-license-generate`
2. **依赖方向**: Commons 模块禁止依赖 Spring Boot
3. **测试要求**: 核心模块覆盖率需达到 80%+
4. **编码规范**: 编写代码前阅读 `.claude/skills/carlos-framework-standard/SKILL.md`

### 参考资料

- [AGENTS.md](./AGENTS.md) - 项目背景和架构
- [CLAUDE.md](./CLAUDE.md) - 开发规范和指南
- [SECURITY.md](./SECURITY.md) - 安全规范
- [CONTRIBUTING.md](./CONTRIBUTING.md) - 贡献指南

---

*最后更新: 2026-03-25*
