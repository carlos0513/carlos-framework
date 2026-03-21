# Carlos Framework - Claude Code Skills 使用指南

> 基于 Spring Boot 3.5 + Vue 3 技术栈的 skill 使用手册

---

## 快速参考：何时用哪个 skill

| 场景                          | 使用的 skill                                     |
|-----------------------------|-----------------------------------------------|
| 新增功能开发                      | `/plan` → `/tdd` → `/code-review` → `/verify` |
| 写 Controller/Service/Mapper | `/springboot-patterns`                        |
| 配置 Spring Security / OAuth2 | `/springboot-security`                        |
| 写单元测试 / 集成测试                | `/springboot-tdd`                             |
| 提交前验证                       | `/springboot-verification`                    |
| 设计 REST API 接口              | `/api-design`                                 |
| 数据库 Schema 变更               | `/database-migrations`                        |
| JPA 实体 / 查询优化               | `/jpa-patterns`                               |
| Docker / 微服务部署              | `/docker-patterns` + `/deployment-patterns`   |
| 安全审查                        | `/security-review`                            |
| 前端 Vue 3 开发                 | `/frontend-patterns` + `/coding-standards`    |
| 会话结束前保存模式                   | `/learn`                                      |

---

## 一、日常开发工作流

### 标准功能开发流程

```
1. /plan          → 制定实现计划，等待确认
2. /search-first  → 搜索现有库/模式，避免重复造轮子
3. /tdd           → 先写测试（RED），再实现（GREEN），再重构
4. /code-review   → 代码审查
5. /springboot-verification → 构建+测试+安全扫描
6. /learn         → 提取本次会话的可复用模式
```

---

## 二、后端 Spring Boot Skills

### `/springboot-patterns`

**触发时机：** 写 Controller、Service、Manager、Mapper 任何一层时

```
# 示例用法
/springboot-patterns 帮我实现用户分页查询接口，包含 Controller → Service → Manager → Mapper 完整链路
```

覆盖内容：

- 分层架构（Controller → Service → Manager → Mapper）
- MyBatis-Plus 查询模式
- Redis 缓存（Manager 层）
- 统一异常处理（ServiceException）
- 分页响应格式

---

### `/springboot-security`

**触发时机：** 涉及认证、授权、OAuth2、JWT、接口权限时

```
# 示例用法
/springboot-security 审查 carlos-auth 模块的 OAuth2 配置是否符合安全规范
```

覆盖内容：

- Spring Security 配置
- OAuth2 Authorization Server
- JWT token 验证
- 方法级权限（@PreAuthorize）
- CSRF / XSS 防护
- 密钥管理

---

### `/springboot-tdd`

**触发时机：** 新增功能、修复 Bug、重构代码时

```
# 示例用法
/springboot-tdd 为 UserService.createUser() 方法编写测试，要求覆盖正常流程和异常流程
```

覆盖内容：

- JUnit 5 + Mockito 单元测试
- MockMvc 接口测试
- Testcontainers 集成测试
- JaCoCo 覆盖率（目标 80%+）

---

### `/springboot-verification`

**触发时机：** 提交代码前、PR 前、发版前

```
# 示例用法
/springboot-verification
```

执行顺序：

1. `mvn clean install` 构建验证
2. 静态分析（Checkstyle / SpotBugs）
3. 测试 + 覆盖率报告
4. 安全扫描
5. Diff 审查

---

### `/java-coding-standards`

**触发时机：** 代码审查时，或不确定命名/结构规范时

```
# 示例用法
/java-coding-standards 检查这段代码是否符合 Java 编码规范
```

覆盖内容：

- 命名规范（Param/DTO/VO/Entity/AO）
- Lombok 使用规范
- Optional 使用
- Stream API 最佳实践
- 不可变对象原则

---

### `/jpa-patterns`

**触发时机：** 设计实体关系、优化查询、处理事务时

```
# 示例用法
/jpa-patterns 帮我优化这个多表关联查询，避免 N+1 问题
```

覆盖内容：

- 实体设计与关联关系
- mybatis-plus-join 查询
- 事务边界
- 分页与索引优化
- 连接池配置

---

## 三、API 设计 Skills

### `/api-design`

**触发时机：** 设计新接口、评审接口规范时

```
# 示例用法
/api-design 设计组织管理模块的 CRUD 接口，要符合 RESTful 规范
```

覆盖内容：

- 资源命名规范
- HTTP 状态码使用
- 统一 `Result<T>` 响应格式
- 分页参数设计（XxxPageParam）
- 错误响应格式
- 接口版本管理

---

## 四、数据库 Skills

### `/database-migrations`

**触发时机：** 修改数据库 Schema、添加字段、创建索引时

```
# 示例用法
/database-migrations 为用户表新增 last_login_time 字段，生成迁移脚本
```

覆盖内容：

- Flyway / Liquibase 迁移脚本
- 零停机迁移策略
- 回滚方案
- 数据迁移安全性

---

### `/postgres-patterns`

**触发时机：** SQL 优化、索引设计、复杂查询时

```
# 示例用法
/postgres-patterns 分析这个查询的执行计划并给出优化建议
```

---

## 五、部署 & 运维 Skills

### `/docker-patterns`

**触发时机：** 编写 Dockerfile、docker-compose、容器化微服务时

```
# 示例用法
/docker-patterns 为 carlos-auth 服务创建生产级 Dockerfile 和 docker-compose 配置
```

覆盖内容：

- 多阶段构建（减小镜像体积）
- Spring Boot 容器最佳实践
- 服务间网络配置
- 数据卷策略
- 容器安全

---

### `/deployment-patterns`

**触发时机：** 配置 CI/CD、设计发布流程、生产就绪检查时

```
# 示例用法
/deployment-patterns 为 carlos-framework 设计 CI/CD 流水线，包含构建、测试、部署到 Nexus
```

覆盖内容：

- GitHub Actions / Jenkins 流水线
- 健康检查配置
- 回滚策略
- 生产就绪检查清单
- Nexus 制品发布（`-P carlos-public/carlos-private`）

---

## 六、安全 Skills

### `/security-review`

**触发时机：** 实现认证功能、处理用户输入、创建 API 端点时

```
# 示例用法
/security-review 审查登录接口的安全性
```

覆盖内容：

- SQL 注入防护（`#{}` vs `${}`）
- XSS 防护
- 认证授权验证
- 敏感数据处理
- 错误信息安全

---

### `/security-scan`

**触发时机：** 定期安全审计，或怀疑配置有安全问题时

```
# 示例用法
/security-scan
```

扫描范围：`.claude/` 目录配置、settings.json、MCP 服务器、hooks

---

## 七、前端 Vue 3 Skills

### `/frontend-patterns`

**触发时机：** 开发 Vue 3 组件、Pinia store、路由配置时

```
# 示例用法
/frontend-patterns 实现用户管理页面，包含列表、搜索、分页、新增/编辑弹窗
```

覆盖内容：

- Vue 3 Composition API 模式
- Pinia 状态管理
- VueUse 组合式工具
- Ant Design Vue 组件使用
- UnoCSS 样式

---

### `/coding-standards`

**触发时机：** TypeScript 代码审查、前端规范检查时

```
# 示例用法
/coding-standards 检查这个 Vue 组件是否符合 TypeScript 规范
```

---

## 八、工作流 & 效率 Skills

### `/plan`

**触发时机：** 任何复杂功能开始前（必须等用户确认再动代码）

```
# 示例用法
/plan 实现 carlos-message 消息推送模块，支持站内信和邮件
```

---

### `/search-first`

**触发时机：** 准备写新功能前，先搜索是否有现成方案

```
# 示例用法
/search-first 查找 Spring Boot 集成 TrueLicense 的最佳实践
```

---

### `/tdd`

**触发时机：** 所有新功能、Bug 修复

```
# 示例用法
/tdd 实现角色权限分配功能
```

流程：RED（写失败测试）→ GREEN（最小实现）→ IMPROVE（重构）

---

### `/learn` / `/continuous-learning`

**触发时机：** 每次会话结束前，提取本次发现的可复用模式

```
# 示例用法
/learn
```

提取的模式会保存到 `~/.claude/skills/learned/`，下次会话自动可用。

---

### `/blueprint`

**触发时机：** 跨多个会话的大型功能规划

```
# 示例用法
/blueprint 设计 carlos-framework 的多租户改造方案
```

---

### `/strategic-compact`

**触发时机：** 长会话中途，上下文接近限制时

```
# 示例用法
/strategic-compact
```

在逻辑节点手动压缩上下文，避免自动压缩丢失关键信息。

---

## 九、推荐的完整开发场景示例

### 场景：新增一个业务模块（如 carlos-notice 通知模块）

```bash
# Step 1: 规划
/plan 实现 carlos-notice 通知模块，支持系统通知的增删改查和已读状态管理

# Step 2: 搜索现有方案
/search-first Spring Boot 通知模块最佳实践

# Step 3: API 设计
/api-design 设计通知模块 REST 接口

# Step 4: TDD 开发
/tdd 实现 NoticeService

# Step 5: 安全审查
/security-review 审查通知接口

# Step 6: 提交前验证
/springboot-verification

# Step 7: 保存模式
/learn
```

### 场景：修复 Bug

```bash
# Step 1: TDD 先写复现测试
/springboot-tdd 为这个 Bug 写复现测试

# Step 2: 修复后验证
/springboot-verification

# Step 3: 代码审查
/code-review
```

### 场景：数据库变更

```bash
# Step 1: 迁移脚本
/database-migrations 新增 notice 表

# Step 2: JPA 实体
/jpa-patterns 设计 Notice 实体和关联关系

# Step 3: 安全检查
/security-review 检查 SQL 注入风险
```

---

## 十、skill 触发方式

所有 skill 通过斜杠命令触发：

```
/<skill-name> [可选描述]
```

例如：

```
/springboot-patterns
/springboot-tdd 为 UserService 写测试
/plan 实现XXX功能
```

---

*文档生成日期：2026-03-21*
