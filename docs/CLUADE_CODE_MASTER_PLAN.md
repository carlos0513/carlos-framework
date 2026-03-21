# Carlos-Framework × Claude Code 精通计划

## 总体路线图

```
阶段一：基础入门（第1-2周）
  ↓
阶段二：工作流精通（第3-5周）
  ↓
阶段三：多代理协作（第6-8周）
  ↓
阶段四：自动化与精通（第9-12周）
```

---

## 阶段一：基础入门

**目标：** 掌握 Claude Code 的核心交互方式，完成首个端到端功能。

### 任务 1.1 — 项目探索（理解代码库）

**练习场景：** 让 Claude 帮你理解项目结构

```bash
# 进入项目，直接向 Claude 提问：
"解释 carlos-spring-boot-starter-web 模块的作用和核心配置"
"carlos-framework 中 Manager 层和 Service 层的职责区别是什么？"
"列出所有实现了 BaseEnum 的枚举类"
```

**学会使用的工具：**

- `Read` / `Grep` / `Glob` — Claude 读文件的方式
- `Explore` agent — 代码库探索

### 任务 1.2 — 第一个 TDD 功能

**练习场景：** 在 `carlos-system` 模块中添加一个简单的字典查询 API

```bash
# 触发 TDD 工作流：
/tdd
# 或直接说：
"使用TDD方式在carlos-system中添加一个按类型查询字典的接口"
```

**学会使用的工具：**

- `tdd-guide` agent（自动触发）
- `/tdd` skill
- `springboot-tdd` skill

**里程碑：** 提交第一个测试覆盖率 ≥80% 的功能 PR。

### 任务 1.3 — 规范代码提交

```bash
# 完成代码后运行：
/code-review    # 代码审查
/verify         # 验证循环（lint + test + security）
/commit         # 生成规范提交信息
```

---

## 阶段二：工作流精通

**目标：** 掌握 Plan → TDD → Review → Security 的完整开发流水线。

### 任务 2.1 — 使用 Blueprint 规划功能

**练习场景：** 实现 `carlos-audit` 审计日志模块的查询功能

```bash
/blueprint
# 告诉 Claude：
"实现审计日志的分页查询和导出Excel功能，
包括前端查询页面和后端API，遵循carlos-framework规范"
```

`/blueprint` 会生成：PRD → 架构设计 → 系统设计 → 技术文档 → 任务列表

### 任务 2.2 — 后端技能组合

实现一个完整 CRUD 模块时，按顺序使用：

| 顺序 | Skill/Agent               | 作用     |
|----|---------------------------|--------|
| 1  | `/plan`                   | 制定实现计划 |
| 2  | `springboot-patterns`     | 遵循框架模式 |
| 3  | `/tdd` → `tdd-guide`      | 测试先行   |
| 4  | `code-reviewer`           | 代码审查   |
| 5  | `security-reviewer`       | 安全审查   |
| 6  | `springboot-verification` | 综合验证   |

### 任务 2.3 — 前端技能组合

实现一个查询页面：

| 顺序 | Skill/Agent         | 作用        |
|----|---------------------|-----------|
| 1  | `frontend-patterns` | Vue3 模式参考 |
| 2  | `api-design`        | 接口设计验证    |
| 3  | `code-reviewer`     | TS 代码审查   |
| 4  | `verification-loop` | 完整验证      |

### 任务 2.4 — 掌握 CLAUDE.md 优化

学习向 CLAUDE.md 添加项目专属规则，让 Claude 每次都能精准遵循：

```bash
# 在 carlos-framework/CLAUDE.md 中补充：
- 项目特有的命名规范
- 常用模块的路径地图
- 禁止使用的反模式清单
```

---

## 阶段三：多代理并行协作

**目标：** 同时驱动多个 Agent 并行工作，大幅提升开发效率。

### 任务 3.1 — 并行代码分析

**练习场景：** 对 `carlos-auth` OAuth2 模块进行全面分析

```bash
# 在一个请求中同时启动3个Agent：
"请并行分析carlos-auth模块：
1. architect agent: 评估架构设计
2. security-reviewer agent: 安全漏洞扫描
3. code-reviewer agent: 代码质量审查"
```

### 任务 3.2 — 前后端联调工作流

使用 `TeamCreate` 组建团队完成前后端对接：

```
Team Lead (你) → 拆分任务
  ├── 前端 Agent：接口对接、类型定义
  └── 后端 Agent：API实现、文档生成
```

### 任务 3.3 — 使用 `/autonomous-loops`

设置自动化循环，让 Claude 持续优化某个模块直到达标：

```bash
# 适用场景：
- 持续运行测试直到覆盖率达到80%
- 循环修复 lint 问题直到0 warning
- 迭代优化SQL查询性能
```

### 任务 3.4 — 使用 `continuous-learning` 沉淀知识

```bash
/learn    # 从当前会话提取可复用模式
/learn-eval  # 自评质量后保存
```

---

## 阶段四：自动化与精通

**目标：** 构建自己的 Skill、自动化工作流，并成为团队 Claude Code 传教士。

### 任务 4.1 — 创建 `carlos-framework-standard` Skill

```bash
/skill-create
# 让 Claude 分析你的开发历史，生成专属技能文件：
# .claude/skills/carlos-framework-standard/SKILL.md
```

内容应包含：

- carlos-framework 命名规范自动检查
- 分层架构合规验证
- 常用代码片段模板

### 任务 4.2 — 构建评估体系 (Eval-Driven Development)

```bash
/eval-harness
# 建立评估框架，衡量 Claude Code 输出质量：
- API 设计合规率
- 测试覆盖率趋势
- 代码审查通过率
```

### 任务 4.3 — 利用 `instinct` 系统积累经验

```bash
/instinct-status   # 查看已积累的直觉规则
/learn-eval        # 每次会话后提炼新直觉
/promote           # 将项目级直觉提升为全局
```

### 任务 4.4 — 构建 carlos-framework 专属 Agent

参考 `~/.claude/agents/` 下的现有 Agent 格式，创建：

- `carlos-arch-reviewer.md` — 专门验证分层架构合规性
- `carlos-api-designer.md` — 生成符合项目规范的 API

---

## 能力成熟度里程碑

| 里程碑                 | 验证方式                                          |
|---------------------|-----------------------------------------------|
| L1：能用 Claude 完成简单功能 | 提交第一个 Claude 辅助的 PR                           |
| L2：掌握完整工作流          | `blueprint → tdd → review → commit` 全程 Claude |
| L3：并行多代理协作          | 同时驱动 3+ Agent 完成复杂任务                          |
| L4：产出自定义技能          | 发布 `carlos-framework-standard` skill          |
| L5：建立团队规范           | 编写 Claude Code 使用指南供团队使用                      |

---

## 立即开始的第一步

现在就可以开始，选择一个你最想完成的功能模块，执行：

```bash
/blueprint
```

然后告诉 Claude 你要实现什么，它会引导你走完整个流程。**这既是学习，也是真实的功能交付。**

---

**计划版本：** v1.0
**生成日期：** 2026-03-20
**适合项目：** carlos-framework 企业级全栈框架
**计划周期：** 12周进阶体系
