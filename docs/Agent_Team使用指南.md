# Carlos Framework - Agent Team 使用指南

本文档介绍如何使用 Carlos Framework 项目配置的 Agent Team 系统。

---

## 📋 目录

- [Agent Team 概述](#agent-team-概述)
- [团队列表](#团队列表)
- [使用方法](#使用方法)
- [预定义场景](#预定义场景)
- [最佳实践](#最佳实践)
- [示例](#示例)

---

## Agent Team 概述

Agent Team 是一组协同工作的 AI 助手，每个团队专注于特定的开发场景。通过团队协作，可以自动化完成复杂的开发任务。

### 团队组成

每个团队包含：

- **Leader（领导者）**：负责整体协调和决策
- **Members（成员）**：各司其职的专业 agent
- **Workflow（工作流）**：定义 agent 之间的协作顺序
- **Skills（技能）**：团队共享的编码规范和最佳实践
- **MCP（工具）**：团队可用的外部工具和服务

---

## 团队列表

### 1. 后端开发团队（backend-dev）

**职责：** 负责后端模块开发、API 设计、数据库设计

**成员：**

- `carlos-planner` - 规划师（Leader）
- `carlos-db-designer` - 数据库设计师
- `carlos-module-scaffolder` - 脚手架生成器
- `carlos-api-designer` - API 设计师
- `springboot-tdd` - TDD 开发工程师
- `java-reviewer` - 代码审查员

**工作流：**

```
carlos-planner → carlos-db-designer → carlos-module-scaffolder
→ carlos-api-designer → springboot-tdd → java-reviewer
```

**适用场景：**

- 新建业务模块
- API 接口开发
- 数据库设计
- 后端功能实现

---

### 2. 前端开发团队（frontend-dev）

**职责：** 负责前端页面开发、组件开发、UI 实现

**成员：**

- `carlos-planner` - 规划师（Leader）
- `api-connector` - 前后端对接工程师
- `vue-reviewer` - 前端代码审查员
- `e2e-runner` - E2E 测试工程师

**工作流：**

```
carlos-planner → api-connector → vue-reviewer → e2e-runner
```

**适用场景：**

- 新增前端页面
- 组件开发
- 前后端对接
- UI 功能实现

---

### 3. 全栈开发团队（fullstack-dev）

**职责：** 负责前后端联调、完整功能实现

**成员：**

- `carlos-planner` - 全栈规划师（Leader）
- `carlos-db-designer` - 数据库设计师
- `carlos-api-designer` - API 设计师
- `api-connector` - 前后端对接工程师
- `springboot-tdd` - 后端 TDD 工程师
- `vue-reviewer` - 前端审查员
- `java-reviewer` - 后端审查员
- `carlos-integration-tester` - 集成测试工程师

**工作流：**

```
carlos-planner → carlos-db-designer → carlos-api-designer
→ [springboot-tdd, api-connector] (并行)
→ [java-reviewer, vue-reviewer] (并行)
→ carlos-integration-tester
```

**适用场景：**

- 完整功能模块开发
- 前后端联调
- 复杂业务实现

---

### 4. 质量保证团队（qa-team）

**职责：** 负责代码审查、安全审查、测试覆盖

**成员：**

- `carlos-security-reviewer` - 安全审查专家（Leader）
- `java-reviewer` - Java 代码审查员
- `vue-reviewer` - Vue 代码审查员
- `carlos-integration-tester` - 集成测试专家
- `e2e-runner` - E2E 测试专家

**工作流：**

```
[java-reviewer, vue-reviewer] (并行)
→ carlos-security-reviewer
→ carlos-integration-tester
→ e2e-runner
```

**适用场景：**

- 代码提交前审查
- 安全漏洞检查
- 测试覆盖率验证

---

### 5. 运维团队（devops-team）

**职责：** 负责构建、部署、文档更新

**成员：**

- `carlos-build-resolver` - 构建错误修复专家（Leader）
- `build-error-resolver` - 通用构建修复专家
- `carlos-doc-updater` - 文档更新专家
- `doc-updater` - 通用文档专家

**工作流：**

```
carlos-build-resolver → carlos-doc-updater
```

**适用场景：**

- 构建错误修复
- 文档更新
- 部署问题排查

---

### 6. 架构团队（architecture-team）

**职责：** 负责架构设计、技术决策、系统优化

**成员：**

- `architect` - 架构师（Leader）
- `carlos-planner` - 功能规划师
- `carlos-db-designer` - 数据架构师
- `database-reviewer` - 数据库优化专家

**工作流：**

```
architect → carlos-planner → carlos-db-designer → database-reviewer
```

**适用场景：**

- 架构设计
- 技术选型
- 系统重构
- 性能优化

---

## 使用方法

### 方法 1：调用整个团队

**语法：**

```
@team:<team-name> <task-description>
```

**示例：**

```
@team:backend-dev 开发订单管理模块
@team:frontend-dev 创建用户列表页面
@team:qa-team 审查最新代码
@team:devops-team 修复构建错误
```

### 方法 2：执行预定义场景

**语法：**

```
@scenario:<scenario-name> <parameters>
```

**示例：**

```
@scenario:new-module carlos-order
@scenario:new-page user-list
@scenario:bug-fix 用户登录空指针异常
@scenario:fullstack-feature 订单支付功能
```

### 方法 3：直接调用单个 Agent

**语法：**

```
@<agent-name> <task-description>
```

**示例：**

```
@carlos-planner 设计订单管理模块
@carlos-api-designer 设计用户 CRUD 接口
@springboot-tdd 实现订单创建功能
@java-reviewer 审查订单模块代码
```

---

## 预定义场景

### 场景 1：新建业务模块（new-module）

**团队：** backend-dev

**步骤：**

1. `carlos-planner` - 分析需求，输出 PRD 和模块设计方案
2. `carlos-db-designer` - 设计数据库表结构
3. `carlos-module-scaffolder` - 生成模块脚手架
4. `carlos-api-designer` - 设计 REST API 和 Feign 接口
5. `springboot-tdd` - TDD 开发功能
6. `java-reviewer` - 代码审查
7. `carlos-security-reviewer` - 安全审查
8. `carlos-integration-tester` - 生成集成测试
9. `carlos-doc-updater` - 更新文档

**使用示例：**

```
@scenario:new-module carlos-order
```

---

### 场景 2：新增前端页面（new-page）

**团队：** frontend-dev

**步骤：**

1. `carlos-planner` - 页面设计和组件规划
2. `api-connector` - 生成前端 service 层
3. `vue-reviewer` - 代码审查
4. `e2e-runner` - E2E 测试

**使用示例：**

```
@scenario:new-page user-list
```

---

### 场景 3：Bug 修复（bug-fix）

**团队：** qa-team

**步骤：**

1. `springboot-tdd` - TDD 流程修复 Bug
2. `java-reviewer` - 代码审查
3. `carlos-security-reviewer` - 安全审查（如涉及用户输入）

**使用示例：**

```
@scenario:bug-fix 用户登录空指针异常
```

---

### 场景 4：构建修复（build-fix）

**团队：** devops-team

**步骤：**

1. `carlos-build-resolver` - 诊断构建错误
2. `carlos-build-resolver` - 执行修复

**使用示例：**

```
@scenario:build-fix
```

---

### 场景 5：全栈功能开发（fullstack-feature）

**团队：** fullstack-dev

**步骤：**

1. `carlos-planner` - 整体架构设计
2. `carlos-db-designer` - 数据库设计
3. `carlos-api-designer` - 后端 API 设计
4. `[springboot-tdd, api-connector]` - 并行开发：后端实现 + 前端 service 层
5. `[java-reviewer, vue-reviewer]` - 并行审查：后端 + 前端
6. `carlos-security-reviewer` - 安全审查
7. `carlos-integration-tester` - 集成测试

**使用示例：**

```
@scenario:fullstack-feature 订单支付功能
```

---

### 场景 6：架构评审（architecture-review）

**团队：** architecture-team

**步骤：**

1. `architect` - 架构分析和设计
2. `carlos-planner` - 功能模块规划
3. `carlos-db-designer` - 数据架构设计
4. `database-reviewer` - 数据库优化建议

**使用示例：**

```
@scenario:architecture-review
```

---

## 最佳实践

### 1. 选择合适的团队

| 任务类型   | 推荐团队              |
|--------|-------------------|
| 新建后端模块 | backend-dev       |
| 新建前端页面 | frontend-dev      |
| 完整功能开发 | fullstack-dev     |
| 代码审查   | qa-team           |
| 构建问题   | devops-team       |
| 架构设计   | architecture-team |

### 2. 并行执行

当多个 agent 可以并行工作时，使用并行执行提高效率：

```
# 并行执行后端和前端开发
@springboot-tdd 实现订单创建功能
@api-connector 生成订单前端 service 层
```

### 3. 遵循工作流

按照团队定义的工作流顺序执行，确保每个步骤的输出可以作为下一步的输入。

### 4. 使用预定义场景

对于常见任务，使用预定义场景可以自动化整个流程：

```
# 使用场景而不是手动调用每个 agent
@scenario:new-module carlos-order
```

### 5. 代码审查必须执行

在代码提交前，必须执行以下审查：

- `java-reviewer` 或 `vue-reviewer` - 代码质量审查
- `carlos-security-reviewer` - 安全审查（强制）

---

## 示例

### 示例 1：开发订单管理模块（完整流程）

```bash
# 方法 1：使用预定义场景（推荐）
@scenario:new-module carlos-order

# 方法 2：手动调用团队
@team:backend-dev 开发订单管理模块，包含订单创建、支付、发货、退款等功能

# 方法 3：手动调用每个 agent（不推荐，除非需要精细控制）
@carlos-planner 设计订单管理模块
@carlos-db-designer 设计订单表结构
@carlos-module-scaffolder 生成 carlos-order 模块脚手架
@carlos-api-designer 设计订单 CRUD 接口
@springboot-tdd 实现订单创建功能
@java-reviewer 审查订单模块代码
@carlos-security-reviewer 审查订单模块安全性
@carlos-integration-tester 生成订单模块集成测试
@carlos-doc-updater 更新订单模块文档
```

### 示例 2：创建用户列表页面

```bash
# 使用预定义场景
@scenario:new-page user-list

# 或使用团队
@team:frontend-dev 创建用户列表页面，包含搜索、分页、编辑、删除功能
```

### 示例 3：修复登录 Bug

```bash
# 使用预定义场景
@scenario:bug-fix 用户登录时出现空指针异常

# 或使用团队
@team:qa-team 修复用户登录空指针异常
```

### 示例 4：全栈开发订单支付功能

```bash
# 使用预定义场景
@scenario:fullstack-feature 订单支付功能

# 或使用团队
@team:fullstack-dev 开发订单支付功能，包含后端支付接口和前端支付页面
```

### 示例 5：架构评审

```bash
# 使用预定义场景
@scenario:architecture-review

# 或使用团队
@team:architecture-team 评审订单模块的架构设计
```

---

## 配置文件

Agent Team 配置文件位置：`.claude/agent-teams.json`

配置文件包含：

- **teams**：所有团队的定义
- **scenarios**：预定义场景的步骤
- **usage**：使用方法说明

---

## 相关文档

- [CLAUDE_CODE使用指南.md](./CLAUDE_CODE使用指南.md) - Agent、Skill、Command 详细说明
- [配置总结.md](./配置总结.md) - 完整配置清单
- [快速开始.md](./快速开始.md) - 5分钟快速配置指南

---

*最后更新：2026-03-22*
*版本：v1.0*
