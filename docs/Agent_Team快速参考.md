# Carlos Framework - Agent Team 快速参考卡

一页纸速查表，帮助你快速选择和使用合适的 Agent Team。

---

## 🎯 场景速查表

| 我要做什么  | 使用场景                  | 快捷命令                                |
|--------|-----------------------|-------------------------------------|
| 新建后端模块 | `new-module`          | `@scenario:new-module carlos-order` |
| 新建前端页面 | `new-page`            | `@scenario:new-page user-list`      |
| 修复 Bug | `bug-fix`             | `@scenario:bug-fix 登录空指针异常`         |
| 构建失败   | `build-fix`           | `@scenario:build-fix`               |
| 全栈功能   | `fullstack-feature`   | `@scenario:fullstack-feature 订单支付`  |
| 架构评审   | `architecture-review` | `@scenario:architecture-review`     |

---

## 👥 团队速查表

| 团队                    | 适用场景 | Leader                   | 成员数 |
|-----------------------|------|--------------------------|-----|
| **backend-dev**       | 后端开发 | carlos-planner           | 6   |
| **frontend-dev**      | 前端开发 | carlos-planner           | 4   |
| **fullstack-dev**     | 全栈开发 | carlos-planner           | 8   |
| **qa-team**           | 质量保证 | carlos-security-reviewer | 5   |
| **devops-team**       | 运维部署 | carlos-build-resolver    | 4   |
| **architecture-team** | 架构设计 | architect                | 4   |

---

## 🚀 三种调用方式

### 1️⃣ 场景调用（推荐）

```bash
@scenario:new-module carlos-order
```

✅ 自动执行完整流程
✅ 无需记忆步骤
✅ 最佳实践内置

### 2️⃣ 团队调用

```bash
@team:backend-dev 开发订单管理模块
```

✅ 灵活描述需求
✅ 团队自动协作
⚠️ 需要清晰描述

### 3️⃣ Agent 调用

```bash
@carlos-planner 设计订单模块
@carlos-api-designer 设计 CRUD 接口
```

✅ 精细控制
⚠️ 需要手动协调
⚠️ 容易遗漏步骤

---

## 📋 后端开发流程（backend-dev）

```
1. carlos-planner          → 需求分析、模块设计
2. carlos-db-designer      → 数据库表设计
3. carlos-module-scaffolder → 生成脚手架
4. carlos-api-designer     → API 设计
5. springboot-tdd          → TDD 开发
6. java-reviewer           → 代码审查
7. carlos-security-reviewer → 安全审查
8. carlos-integration-tester → 集成测试
9. carlos-doc-updater      → 更新文档
```

**快捷命令：**

```bash
@scenario:new-module carlos-order
```

---

## 🎨 前端开发流程（frontend-dev）

```
1. carlos-planner  → 页面设计、组件规划
2. api-connector   → 生成 service 层
3. vue-reviewer    → 代码审查
4. e2e-runner      → E2E 测试
```

**快捷命令：**

```bash
@scenario:new-page user-list
```

---

## 🔄 全栈开发流程（fullstack-dev）

```
1. carlos-planner      → 整体架构设计
2. carlos-db-designer  → 数据库设计
3. carlos-api-designer → 后端 API 设计
4. [并行] springboot-tdd + api-connector
5. [并行] java-reviewer + vue-reviewer
6. carlos-security-reviewer → 安全审查
7. carlos-integration-tester → 集成测试
```

**快捷命令：**

```bash
@scenario:fullstack-feature 订单支付功能
```

---

## 🛡️ 质量保证流程（qa-team）

```
1. [并行] java-reviewer + vue-reviewer
2. carlos-security-reviewer  → 安全审查（强制）
3. carlos-integration-tester → 集成测试
4. e2e-runner                → E2E 测试
```

**使用时机：**

- ✅ 代码提交前（强制）
- ✅ PR 创建前
- ✅ 发布前

---

## 🔧 运维流程（devops-team）

```
1. carlos-build-resolver → 诊断构建错误
2. carlos-doc-updater    → 更新文档
```

**快捷命令：**

```bash
@scenario:build-fix
```

---

## 🏗️ 架构评审流程（architecture-team）

```
1. architect           → 架构分析和设计
2. carlos-planner      → 功能模块规划
3. carlos-db-designer  → 数据架构设计
4. database-reviewer   → 数据库优化建议
```

**快捷命令：**

```bash
@scenario:architecture-review
```

---

## 💡 最佳实践

### ✅ DO（推荐做法）

1. **使用场景调用**
   ```bash
   @scenario:new-module carlos-order
   ```

2. **遵循工作流顺序**
    - 先规划，后实现
    - 先测试，后审查
    - 先审查，后提交

3. **强制安全审查**
   ```bash
   @carlos-security-reviewer 审查代码
   ```

4. **并行执行独立任务**
   ```bash
   @springboot-tdd 实现后端
   @api-connector 生成前端 service
   ```

### ❌ DON'T（避免做法）

1. **跳过代码审查**
    - ❌ 直接提交代码
    - ✅ 先审查，后提交

2. **跳过安全审查**
    - ❌ 忽略安全检查
    - ✅ 强制执行安全审查

3. **手动调用所有 Agent**
    - ❌ 逐个调用 9 个 agent
    - ✅ 使用 `@scenario:new-module`

4. **忽略测试覆盖率**
    - ❌ 不写测试
    - ✅ 确保 80%+ 覆盖率

---

## 🎓 学习路径

### 第 1 天：基础使用

```bash
# 1. 新建简单模块
@scenario:new-module carlos-demo

# 2. 新建简单页面
@scenario:new-page demo-list

# 3. 修复简单 Bug
@scenario:bug-fix 修复登录问题
```

### 第 2 天：团队协作

```bash
# 1. 使用后端团队
@team:backend-dev 开发用户管理模块

# 2. 使用前端团队
@team:frontend-dev 创建用户管理页面

# 3. 使用质量团队
@team:qa-team 审查用户管理模块
```

### 第 3 天：全栈开发

```bash
# 完整功能开发
@scenario:fullstack-feature 用户权限管理功能
```

### 第 4 天：架构设计

```bash
# 架构评审
@scenario:architecture-review
```

---

## 📞 常见问题

### Q1: 场景和团队有什么区别？

**场景（Scenario）：**

- 预定义的完整流程
- 固定的步骤顺序
- 适合标准化任务

**团队（Team）：**

- 灵活的协作方式
- 根据需求调整
- 适合复杂任务

### Q2: 什么时候用场景，什么时候用团队？

| 情况             | 推荐方式  |
|----------------|-------|
| 标准化任务（新建模块、页面） | 场景    |
| 复杂需求（需要详细描述）   | 团队    |
| 快速执行（不想思考步骤）   | 场景    |
| 精细控制（需要调整流程）   | Agent |

### Q3: 可以跳过某些步骤吗？

可以，但不推荐。如果必须跳过：

```bash
# 直接调用需要的 agent
@carlos-api-designer 设计 API
@springboot-tdd 实现功能
@java-reviewer 审查代码
```

### Q4: 如何并行执行？

在一条消息中调用多个 agent：

```bash
@springboot-tdd 实现订单创建功能
@api-connector 生成订单前端 service 层
```

---

## 🔗 相关文档

- [Agent_Team使用指南.md](./Agent_Team使用指南.md) - 完整使用指南
- [CLAUDE_CODE使用指南.md](./CLAUDE_CODE使用指南.md) - Agent 详细说明
- [快速开始.md](./快速开始.md) - 5分钟快速配置

---

## 📌 记住这些

1. **场景优先**：`@scenario:new-module` 比手动调用 9 个 agent 更快
2. **安全强制**：`@carlos-security-reviewer` 必须在提交前执行
3. **测试覆盖**：确保 80%+ 测试覆盖率
4. **并行执行**：独立任务可以并行，提高效率
5. **遵循流程**：规划 → 设计 → 开发 → 审查 → 测试 → 文档

---

*打印此页，贴在显示器旁边！*

*最后更新：2026-03-22*
