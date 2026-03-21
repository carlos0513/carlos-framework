# Claude Code 最佳实践学习计划

> 针对 Carlos Framework 全栈项目的 Claude Code mastery 路线图

---

## 📚 阶段一：基础入门 (第 1-2 周)

### 目标

掌握 Claude Code 基本操作，熟悉项目结构，完成简单功能开发

### 1.1 环境准备与初识

#### 第 1 天：Claude Code 基础

```bash
# 安装 Claude Code CLI
npm install -g @anthropic-ai/claude-code

# 验证安装
claude --version

# 在项目中启动
claude
```

**学习任务：**

- [ ] 阅读 `/help` 了解所有命令
- [ ] 学习 `/init` 初始化工作流程
- [ ] 了解权限模式（Auto/Ask/Plan）
- [ ] 配置个人设置 `~/.claude/settings.json`

**实践：**

- 让 Claude 分析你的项目结构
- 读取并理解 `CLAUDE.md`

#### 第 2-3 天：基本操作

**核心技能：**

- 使用 `@` 引用文件
- 使用工具（Read、Edit、Write、Bash、Grep、Glob）
- 理解工具使用规则（Read 优先于 Bash）

**练习任务：**

```markdown
1. 使用 Glob 查找所有 Controller 文件
2. 使用 Grep 查找特定方法
3. 读取一个完整的 Service 实现
4. 使用 Edit 修改一个简单的配置
```

#### 第 4-5 天：第一次完整功能

**选择一个小功能练习：**

- 后端：添加一个简单的查询接口
- 前端：添加一个页面组件

**遵循流程：**

1. 询问 Claude 项目架构
2. 确认技术栈和约束
3. 让 Claude 生成代码
4. 手动验证结果
5. 修正问题

### 1.2 工具与技能

#### 第 6-7 天：掌握 Skills

**学习路径：**

```bash
# 查看可用技能
/skill:list

# 学习 carlos-framework-standard 技能
# 位置：carlos-framework/.claude/skills/carlos-framework-standard/SKILL.md
```

**核心技能清单：**
| 技能 | 用途 | 何时使用 |
|------|------|----------|
| carlos-framework-standard | 后端编码规范 | 每次写 Java 代码 |
| tdd-workflow | 测试驱动开发 | 新功能开发 |
| code-review | 代码审查 | 写完代码后 |
| security-review | 安全检查 | 涉及安全相关代码 |
| verification-loop | 验证流程 | 提交前 |

**实践：**

- [ ] 阅读 `carlos-framework-standard` 技能
- [ ] 使用该技能生成一个实体类 + CRUD

---

## 🚀 阶段二：进阶实践 (第 3-4 周)

### 目标

掌握 Agent 使用、TDD 流程、多模块协作开发

### 2.1 Agent 协作模式

#### 第 1-2 天：理解 Agent 类型

**Agent 类型速查表：**

```markdown
| Agent | 用途 | 触发方式 |
|-------|------|----------|
| planner | 复杂功能规划 | 复杂功能自动触发 |
| tdd-guide | 测试驱动开发 | 新功能/修复 bug |
| code-reviewer | 代码审查 | 写完代码后 |
| security-reviewer | 安全检查 | 安全相关代码 |
| build-error-resolver | 构建错误 | 构建失败时 |
| go-reviewer/python-reviewer | 语言专用审查 | 对应语言代码 |
```

**练习：**

```markdown
1. 描述一个复杂功能给 Claude
2. 观察 planner agent 自动触发
3. 查看生成的计划文件
4. 批准或修改计划
```

#### 第 3-4 天：多 Agent 并行

**场景示例：新功能开发**

```markdown
# 同时启动多个 Agent
1. Agent A: 分析现有数据库表结构
2. Agent B: 检查相关 API 规范
3. Agent C: 搜索相似实现参考

# 收集结果后
4. 让 Claude 综合结果生成实现方案
```

**实践任务：**

- 开发一个完整的用户管理功能
- 使用 Agent 并行处理：数据库设计、API 设计、前端界面

### 2.2 TDD 实战

#### 第 5-7 天：测试驱动开发流程

**学习 TDD 循环：**

```
RED → GREEN → IMPROVE
```

**后端 TDD 示例：**

```java
// 1. 先写测试 (RED)
@Test
void should_create_user_successfully_when_params_valid() {
    // given
    UserCreateParam param = new UserCreateParam();
    param.setUsername("test");

    // when
    User user = userService.createUser(param);

    // then
    assertNotNull(user);
    assertEquals("test", user.getUsername());
}

// 2. 运行测试 - 应当失败
// 3. 实现最简代码 (GREEN)
// 4. 运行测试 - 应当通过
// 5. 重构 (IMPROVE)
```

**练习任务：**

- [ ] 使用 `/tdd` 命令启动 TDD 模式
- [ ] 完成一个 Service 层的完整 TDD 流程
- [ ] 验证 80%+ 代码覆盖率

### 2.3 前后端联调

#### 第 8-10 天：全栈开发流程

**完整开发流程：**

```markdown
1. 需求分析
   - 使用 planner agent 制定计划
   - 生成 PRD、技术文档

2. 后端开发
   - TDD 方式开发 API
   - 使用 code-reviewer 审查
   - 使用 security-reviewer 检查

3. 前端开发
   - 生成 TypeScript 类型
   - 开发 Vue 组件
   - 对接后端 API

4. 联调测试
   - 验证端到端流程
   - 修复问题
```

**实践项目：**
实现一个完整的业务模块（如：订单管理）

- 后端：Entity → Mapper → Manager → Service → Controller
- 前端：页面 → 组件 → API 对接 → 状态管理

---

## 🎯 阶段三：高级技巧 (第 5-6 周)

### 目标

掌握计划模式、自主工作流、团队协

### 3.1 Plan 模式深度使用

#### 第 1-3 天：Plan 模式实战

**何时使用 Plan 模式：**

- 新功能开发
- 复杂重构
- 多文件修改

**Plan 模式流程：**

```markdown
1. 输入 /plan 进入计划模式
2. Claude 分析问题并制定计划
3. 用户审查并批准计划
4. Claude 执行计划
5. 每一步都可审查和调整
```

**实践：**

- 计划一次数据库表重构
- 涉及：实体类、Mapper、Service、前端接口

### 3.2 自主工作流

#### 第 4-5 天：让 Claude 自主工作

**设置检查点 (Checkpoint)：**

```bash
/checkpoint
# 保存当前工作进度
# 可随时回滚
```

**长任务处理：**

```markdown
1. 给 Claude 一个完整的任务描述
2. 设置阶段性检查点
3. 定期审查进度
4. 必要时调整方向
```

#### 第 6-7 天：批量处理

**批量重构示例：**

```markdown
"请帮我将所有使用 @Value 的地方改为 @ConfigurationProperties

步骤：
1. 搜索所有 @Value 使用位置
2. 分析每个使用场景
3. 创建对应的 Properties 类
4. 替换所有引用
5. 验证编译通过"
```

### 3.3 团队协作模式

#### 第 8-10 天：多 Agent 团队

**创建专项团队：**

```bash
# 启动团队模式
# 可同时运行多个 Agent 处理不同方面

Example:
- Agent 1: 后端 API 开发
- Agent 2: 前端页面开发
- Agent 3: 测试用例编写
```

**实践：**
使用 TeamCreate 创建开发团队，分工完成一个完整功能模块

---

## 🔥 阶段四：精通与优化 (第 7-8 周)

### 目标

建立个人工作流，优化效率，形成最佳实践

### 4.1 自定义工作流

#### 第 1-3 天：建立个人节奏

**模板化常见任务：**

1. **新功能开发模板：**

```markdown
我要开发一个新功能：{功能描述}

请按以下步骤：
1. 使用 planner 制定实现计划
2. 使用 tdd-guide 开发后端
3. 使用 code-reviewer 审查代码
4. 生成前端对接代码
5. 使用 verification-loop 验证
```

2. **Bug 修复模板：**

```markdown
修复 Bug：{Bug 描述}

步骤：
1. 分析问题根因
2. 编写复现测试
3. 修复代码
4. 验证测试通过
5. 回归测试
```

#### 第 4-5 天：Prompt 工程

**高效 Prompt 技巧：**

```markdown
✅ 好的 Prompt：
"在 UserService 中添加一个根据部门 ID 查询用户列表的方法，
要求：
1. 使用 MyBatis-Plus 的 QueryWrapper
2. 添加数据权限过滤
3. 返回 DTO 而不是 Entity
4. 添加单元测试"

❌ 差的 Prompt：
"帮我写个查询用户的方法"
```

### 4.2 持续学习

#### 第 6-7 天：模式提取

**使用 /learn 命令：**

```bash
# 从会话中提取可复用模式
/learn

# 将学到的模式保存为技能
# 下次可自动应用
```

**创建自定义技能：**

- 记录项目中常用的代码模式
- 创建个人技能文件
- 在后续开发中复用

### 4.3 性能优化

#### 第 8-10 天：大项目管理

**上下文管理：**

```markdown
1. 大文件处理
   - 使用 offset 和 limit 分段读取
   - 避免一次加载超大文件

2. 上下文压缩
   - 使用 /compact 命令
   - 在关键节点总结进度

3. 会话管理
   - 使用 /save-session 保存进度
   - 使用 /resume-session 恢复工作
```

---

## 📋 实战项目清单

按顺序完成这些项目，全面提升 Claude Code 技能：

### 入门级

- [ ] 添加一个简单查询接口（单表查询）
- [ ] 修改前端页面样式
- [ ] 添加一个工具类方法

### 进阶级

- [ ] 开发完整的 CRUD 模块（含前后端）
- [ ] 实现多表关联查询
- [ ] 添加 Redis 缓存功能
- [ ] 集成第三方 API

### 高级

- [ ] 重构一个现有模块
- [ ] 实现复杂业务流程
- [ ] 添加权限控制功能
- [ ] 性能优化专项

---

## 🛠️ 推荐工具链

### 必备技能使用频率

| 频率   | 技能/命令                     | 用途     |
|------|---------------------------|--------|
| 每次   | carlos-framework-standard | 后端编码规范 |
| 每次   | /checkpoint               | 保存进度   |
| 新功能  | /plan                     | 计划模式   |
| 新功能  | /tdd                      | 测试驱动开发 |
| 写完代码 | /review                   | 代码审查   |
| 安全相关 | security-review           | 安全检查   |
| 提交前  | /verify                   | 验证流程   |
| 每天   | /learn                    | 提取模式   |

### 快捷键

```bash
Ctrl+C          # 中断当前操作
Ctrl+O          # 查看思考过程
Alt+T / Opt+T   # 切换思考模式
```

---

## ✅ 每日检查清单

每天使用 Claude Code 时，检查以下事项：

### 开始工作

- [ ] 读取 CLAUDE.md 了解项目上下文
- [ ] 确认当前任务范围
- [ ] 必要时设置 checkpoint

### 开发过程

- [ ] 使用 carlos-framework-standard 技能
- [ ] 遵循分层架构规范
- [ ] 写代码前先写测试（TDD）
- [ ] 及时运行验证

### 代码完成

- [ ] 使用 code-reviewer 审查
- [ ] 检查安全规范
- [ ] 验证测试覆盖率 80%+
- [ ] 保存会话 /save-session

---

## 🎓 学习资源

### 内部文档

- `CLAUDE.md` - 项目级指导
- `carlos-framework/CLAUDE.md` - 后端详细规范
- `carlos-framework-standard` 技能 - 编码标准

### 命令参考

- `/help` - 查看所有命令
- `/skills` - 查看可用技能

### 最佳实践来源

- 每个阶段结束后，总结经验
- 使用 /learn 提取个人最佳实践
- 持续更新此计划文档

---

## 📝 进阶技巧汇总

### 1. 高效搜索

```markdown
# 使用 Grep 而不是 Bash grep
❌ grep -r "pattern" .
✅ Grep pattern

# 使用 Glob 查找文件
❌ find . -name "*.java"
✅ Glob **/*.java
```

### 2. 并行处理

```markdown
# 同时读取多个相关文件
Read A.java
Read B.java
Read C.java

# 然后统一分析
```

### 3. 增量修改

```markdown
# 大文件分块处理
Read file.java limit 50
# 修改前50行
Read file.java offset 51 limit 50
# 修改接下来的50行
```

### 4. Agent 协作

```markdown
# 复杂任务拆分给多个 Agent
Agent 1: 研究现有代码
Agent 2: 设计新架构
Agent 3: 编写测试

# 综合结果
```

---

## 🏆 成功指标

完成此学习计划后，你应该能够：

- [ ] 独立完成前后端完整功能开发
- [ ] 熟练使用 TDD 流程
- [ ] 高效使用 Agent 协作模式
- [ ] 建立个人开发工作流
- [ ] 提取并复用代码模式
- [ ] 处理复杂重构任务
- [ ] 优化大项目开发效率

---

## 🚀 下一步

完成此计划后，考虑：

1. **建立团队规范** - 为团队制定 Claude Code 使用指南
2. **自动化工作流** - 结合 CI/CD 自动化验证
3. **知识库建设** - 积累项目专属技能和模式
4. **持续优化** - 根据实际情况调整工作流

---

> **记住：** 实践是最好的老师。每天使用 Claude Code，不断调整和优化你的工作流，找到最适合自己的方式。

**开始学习吧！建议从阶段一开始，按顺序完成每个练习。**
