# Carlos Framework - Claude Code 使用指南

本文档汇总了本项目配置的所有 Agent、Skill、Command 的使用方法。

---

## 目录

- [Commands（快捷命令）](#commands快捷命令)
- [Agents（智能体）](#agents智能体)
- [Skills（技能）](#skills技能)
- [MCP（模型上下文协议）](#mcp模型上下文协议)
- [使用示例](#使用示例)

---

## Commands（快捷命令）

Commands 是可以通过 `/命令名` 直接调用的快捷指令，无需 Agent 介入。

| 命令                | 用途       | 调用方式              |
|-------------------|----------|-------------------|
| `/build`          | 构建后端全部模块 | `/build`          |
| `/build-frontend` | 构建前端项目   | `/build-frontend` |
| `/test`           | 运行后端单元测试 | `/test`           |
| `/gen-route`      | 生成前端路由配置 | `/gen-route`      |
| `/new-module`     | 新建业务模块   | `/new-module`     |
| `/new-page`       | 新增前端页面   | `/new-page`       |
| `/update-doc`     | 更新项目文档   | `/update-doc`     |

### 命令详解

#### `/build` - 构建后端

构建 carlos-framework 全部模块（跳过测试）。

```bash
cd D:/ide_project/carlos/carlos-framework
mvn clean install -DskipTests
```

**常用变体：**

```bash
# 包含测试
mvn clean install

# 部署到公共 Nexus（zcarlos.com:8081）
mvn clean deploy -P carlos-public

# 部署到私有 Nexus（192.168.3.30:8081）
mvn clean deploy -P carlos-private

# 构建指定模块
cd carlos-spring-boot/carlos-spring-boot-starter-redis
mvn clean install -DskipTests
```

**构建顺序（失败时参考）：**

1. carlos-dependencies
2. carlos-parent
3. carlos-commons（carlos-utils）
4. carlos-spring-boot（23 个模块，含 carlos-spring-boot-core）
5. carlos-integration（auth → system → org → audit → message → license → tools）
6. carlos-samples

---

#### `/build-frontend` - 构建前端

构建 carlos-ui 前端项目。

**执行步骤：**

1. 进入前端目录：`cd D:/ide_project/carlos/carlos-framework/carlos-ui`
2. 确认依赖已安装（如未安装则运行 `pnpm install`）
3. 执行构建：`pnpm build`
4. 显示构建产物路径

**可选参数：**

- 测试环境构建：`pnpm build:test`
- 仅类型检查：`pnpm typecheck`
- 代码检查：`pnpm lint`

**注意事项：**

- 使用 pnpm 工作空间，禁止使用 npm 或 yarn
- 构建前先运行 `pnpm typecheck` 确保无类型错误
- 构建前先运行 `pnpm lint` 确保代码规范

---

#### `/test` - 运行测试

运行后端单元测试并生成覆盖率报告。

```bash
cd D:/ide_project/carlos/carlos-framework

# 运行全部测试
mvn test

# 运行指定测试类
mvn test -Dtest=OrgUserServiceTest

# 运行指定测试方法
mvn test -Dtest=OrgUserServiceTest#should_create_user_when_params_valid

# 运行集成测试
mvn verify

# 生成 JaCoCo 覆盖率报告
mvn test jacoco:report
# 报告位置：target/site/jacoco/index.html
```

**覆盖率目标：**

- Line Coverage ≥ **80%**
- Branch Coverage ≥ **70%**

---

#### `/gen-route` - 生成前端路由

根据 `src/views/` 目录文件结构，通过 Elegant Router 自动生成路由配置。

**执行步骤：**

1. 进入前端目录：`cd D:/ide_project/carlos/carlos-framework/carlos-ui`
2. 执行：`pnpm gen-route`
3. 说明生成了哪些新路由文件

**使用场景：**

- 在 `src/views/` 下新增了页面文件
- 修改了页面文件的命名或目录结构
- 删除了页面文件

**路由命名规范：**

- `src/views/system/user/index.vue` → 路由名 `system_user`
- `src/views/system/user/[id].vue` → 动态路由 `system_user-detail`

---

#### `/new-module` - 新建业务模块

在 `carlos-integration/` 下创建一个新的业务模块，遵循标准分层架构。

**执行步骤：**

1. 确认模块名称（如 `carlos-order`）
2. 创建目录结构：
   ```
   carlos-integration/carlos-{name}/
   ├── carlos-{name}-api/          # Feign 接口 + AO/Param
   └── carlos-{name}-bus/          # 业务实现
       ├── apiimpl/
       ├── config/
       ├── controller/
       ├── convert/
       ├── manager/impl/
       ├── mapper/
       ├── pojo/dto|entity|enums|param|vo/
       └── service/impl/
   ```
3. 创建 `pom.xml`（继承 `carlos-integration`，版本 `${revision}`）
4. 在 `carlos-integration/pom.xml` 中注册模块
5. 在 `carlos-dependencies/pom.xml` 中添加依赖管理条目

**注意事项：**

- 严格遵守 `.claude/skills/carlos-framework-standard/SKILL.md` 中的编码规范
- 模块命名：`carlos-{功能名}`
- 包路径：`com.carlos.{模块名}`
- 创建后立即编写对应测试类

---

#### `/new-page` - 新增页面

在 `src/views/` 下创建新页面，并生成对应路由和 service 层代码。

**执行步骤：**

1. **确认页面信息**：模块名、页面名、对应后端接口
2. **创建页面文件**：
   ```
   src/views/{module}/{page}/
   ├── index.vue          # 页面主文件
   └── modules/           # 页面子组件（表单、弹窗等）
       ├── search-form.vue
       └── edit-modal.vue
   ```
3. **创建 service 层**：
   ```
   src/service/api/{module}/{page}.ts
   ```
4. **生成路由**：`pnpm gen-route`
5. **类型检查**：`pnpm typecheck`

**页面模板：**

```vue
<script setup lang="ts">
import { ref, onMounted } from 'vue';

defineOptions({ name: '{ModulePage}' });

const loading = ref(false);

onMounted(() => {
  // 初始化
});
</script>

<template>
  <div class="h-full">
    <!-- 页面内容 -->
  </div>
</template>
```

**注意事项：**

- 文件名使用 kebab-case（如 `user-list`）
- 组件名使用 PascalCase（如 `SystemUserList`）
- 新增页面后必须运行 `pnpm gen-route`
- 后端菜单需同步配置才能在侧边栏显示

---

#### `/update-doc` - 更新项目文档

更新 CLAUDE.md、MEMORY.md 和相关模块文档，确保文档与代码同步。

**执行步骤：**

1. 检查最近的代码变更（git diff）
2. 识别需要更新的文档
3. 调用 @carlos-doc-updater 执行文档更新

**更新范围：**

- **CLAUDE.md**: 项目主文档、架构变更、模块结构
- **memory/MEMORY.md**: 跨会话快速参考、模块列表
- **模块 README**: 各模块的功能说明和配置
- **SKILL.md**: 编码规范文档

---

## Agents（智能体）

Agents 是专业领域的 AI 助手，通过 `@agent-name` 调用，执行复杂任务。

| Agent                        | 用途          | 调用时机                     |
|------------------------------|-------------|--------------------------|
| `@carlos-planner`            | 功能规划助手      | 开发新业务模块或复杂功能前            |
| `@carlos-api-designer`       | API 设计专家    | 设计新接口、新实体 CRUD 时         |
| `@carlos-db-designer`        | 数据库设计专家     | 设计新表结构、审查 Schema 变更时     |
| `@carlos-module-scaffolder`  | 模块脚手架生成器    | 新建业务模块时                  |
| `@carlos-build-resolver`     | 构建错误修复专家    | Maven 构建失败时              |
| `@carlos-security-reviewer`  | 安全审查专家      | 代码提交前强制调用                |
| `@carlos-integration-tester` | 集成测试专家      | 业务模块开发完成后                |
| `@carlos-doc-updater`        | 文档更新助手      | 模块重大变更、新功能上线后            |
| `@java-reviewer`             | Java 代码审查专家 | 编写或修改 Java 代码后           |
| `@vue-reviewer`              | 前端代码审查专家    | 编写或修改 Vue/TypeScript 代码后 |
| `@springboot-tdd`            | TDD 助手      | 新增功能或修复 Bug 时            |
| `@api-connector`             | 前后端对接助手     | 新增或修改 API 对接时            |

### Agent 详解

#### `@carlos-planner` - 功能规划助手

**职责：** 在开发新业务模块或复杂功能前，输出标准 PRD、模块设计方案、分层任务清单。

**调用时机：**

- 开发新业务模块前
- 复杂功能设计前
- 架构调整前

**输出内容：**

- 需求分析
- 模块设计方案
- 分层任务清单
- 依赖关系梳理
- 风险评估

**使用示例：**

```
@carlos-planner 我需要设计一个订单管理模块，包含订单创建、支付、发货、退款等功能
```

---

#### `@carlos-api-designer` - API 设计专家

**职责：** 设计新接口时，输出完整的 Controller/Service/Manager/Mapper 骨架代码及所有 POJO 对象。

**设计原则：**

1. 严格遵守 Controller → Service → Manager → Mapper 分层
2. Service 层只做业务串联，禁止直接引用 Mapper
3. Manager 层继承 BaseServiceImpl，统一处理 CRUD 和缓存
4. 所有 POJO 使用 Lombok，禁止手写 getter/setter

**输出内容：**

- Controller 层代码
- Service 层代码
- Manager 层代码
- Mapper 层代码
- 所有 POJO 对象（Param、DTO、VO、Entity、AO）

**使用示例：**

```
@carlos-api-designer 设计一个商品管理模块的 CRUD 接口，包含商品创建、查询、更新、删除
```

---

#### `@carlos-db-designer` - 数据库设计专家

**职责：** 设计新表结构、审查 Schema 变更、生成 Migration 脚本。

**表设计规范：**

- 表名：`{module}_{entity}` 蛇形命名（如 `org_user`, `sys_menu`）
- 字段名：蛇形命名小写（如 `user_name`, `create_time`）
- 主键：`id` BIGINT，雪花算法

**输出内容：**

- 建表 SQL
- MyBatis-Plus 实体类
- 索引设计建议
- Migration 脚本（Flyway/Liquibase）

**使用示例：**

```
@carlos-db-designer 设计订单表，包含订单基本信息、金额、状态、关联用户等字段
```

---

#### `@carlos-module-scaffolder` - 模块脚手架生成器

**职责：** 新建业务模块时，自动生成符合规范的 api/bus/boot/cloud 四层子模块完整骨架代码。

**生成内容：**

- 完整的目录结构
- pom.xml（继承关系正确）
- 基础配置文件
- 示例代码文件

**使用示例：**

```
@carlos-module-scaffolder 请生成 carlos-order 模块的脚手架代码
```

---

#### `@carlos-build-resolver` - 构建错误修复专家

**职责：** Maven 构建失败时，诊断编译错误、依赖冲突、插件问题，提供最小化修复方案。

**诊断流程：**

1. 定位错误模块和具体错误信息
2. 分析依赖冲突（使用 `mvn dependency:tree`）
3. 检查版本兼容性
4. 提供修复方案

**使用示例：**

```
@carlos-build-resolver mvn clean install 失败了，请帮我诊断
```

---

#### `@carlos-security-reviewer` - 安全审查专家

**职责：** 代码提交前强制调用，检查 SQL 注入、XSS、硬编码凭据、越权访问、敏感数据泄露、CSRF 等安全问题。

**审查维度：**

- SQL 注入检查
- XSS 防护检查
- 硬编码凭据检查
- 越权访问检查
- 敏感数据泄露检查
- CSRF 防护检查

**使用示例：**

```
@carlos-security-reviewer 请审查 org 模块的最新代码
```

---

#### `@carlos-integration-tester` - 集成测试专家

**职责：** 业务模块开发完成后调用，生成 MockMvc 接口测试、Testcontainers 数据库集成测试，验证 80%+ 测试覆盖率。

**测试层次：**

- 单元测试（JUnit 5 + Mockito）
- Controller 集成测试（MockMvc）
- 数据库集成测试（Testcontainers + MySQL）

**使用示例：**

```
@carlos-integration-tester 请为 carlos-order 模块生成集成测试
```

---

#### `@java-reviewer` - Java 代码审查专家

**职责：** 审查 Spring Boot / MyBatis-Plus 代码，检查分层架构、编码规范、安全问题。

**审查维度：**

- 分层架构合规性
- 数据查询规范
- Redis 缓存规范
- 异常处理规范
- Lombok 使用规范

**使用示例：**

```
@java-reviewer 请审查这段 Service 代码
```

---

#### `@vue-reviewer` - 前端代码审查专家

**职责：** 编写或修改 Vue/TypeScript 代码后自动调用，检查类型安全、组件规范、状态管理、API调用规范、样式规范。

**审查维度：**

- TypeScript 类型安全
- 组件规范
- 状态管理规范
- API 调用规范
- 样式规范

**使用示例：**

```
@vue-reviewer 请审查这个 Vue 组件
```

---

#### `@springboot-tdd` - TDD 助手

**职责：** 新增功能或修复 Bug 时自动调用，强制执行 RED→GREEN→IMPROVE 流程，确保测试覆盖率达到 80%+。

**强制流程：**

1. **RED 阶段** — 先写测试（覆盖 BCDE 原则）
2. **GREEN 阶段** — 实现代码使测试通过
3. **IMPROVE 阶段** — 重构代码，提升质量

**BCDE 原则：**

- **B**order：边界值测试
- **C**orrect：正确的输入，并得到预期的结果
- **D**esign：与设计文档相结合
- **E**rror：强制错误信息输入

**使用示例：**

```
@springboot-tdd 我需要实现用户创建功能，请帮我按照 TDD 流程进行
```

---

## Skills（技能）

Skills 是 Claude Code 的知识库，提供编码规范、最佳实践等指导。

| Skill                       | 用途                    | 调用方式      |
|-----------------------------|-----------------------|-----------|
| `carlos-framework-standard` | Carlos Framework 编码规范 | 自动加载或手动引用 |
| `carlos-ui-standard`        | 前端编码规范                | 自动加载或手动引用 |

### Skill 详解

#### `carlos-framework-standard` - Carlos Framework 编码规范

**内容：**

- 架构分层规范
- 命名规范
- 数据查询规范（强制）
- Redis 缓存规范（强制）
- 异常处理规范（强制）
- Lombok 使用规范
- 属性注入规范
- Service 返回值规范
- 枚举类规范

**关键强制规范：**

| 规范              | 要求                                              | 禁止                    |
|-----------------|-------------------------------------------------|-----------------------|
| **数据查询**        | 使用 MyBatis-Plus + mybatis-plus-join 在 Manager 层 | 严禁直接编写 SQL            |
| **Redis 缓存**    | 在 Manager 层统一实现                                 | Service 层禁止直接操作 Redis |
| **异常处理**        | 使用框架定义异常类（ServiceException 等）                   | 严禁使用 RuntimeException |
| **Lombok**      | 使用注解生成 getter/setter                            | 严禁手写 get/set 方法       |
| **属性注入**        | 使用 @ConfigurationProperties                     | 严禁使用 @Value           |
| **Service 返回值** | 返回 DTO 或 Entity                                 | 严禁返回 VO               |
| **枚举类**         | 实现 BaseEnum，使用 @AppEnum                         | 普通枚举定义                |

**文件位置：** `.claude/skills/carlos-framework-standard/SKILL.md`

---

#### `carlos-ui-standard` - 前端编码规范

**内容：**

- 组件规范
- 类型规范
- 状态管理规范
- API 请求规范
- 样式规范
- 路由规范

**强制规范速查：**

| 规范   | 要求                               | 禁止             |
|------|----------------------------------|----------------|
| 组件语法 | `<script setup>` + TypeScript    | Options API    |
| 类型   | 明确类型，禁止 `any`                    | `any`、`as any` |
| 包管理  | pnpm                             | npm、yarn       |
| 样式   | UnoCSS 工具类 + `<style scoped>`    | 内联样式           |
| 状态   | Pinia store                      | 组件间直接传递复杂状态    |
| 请求   | `src/service/api/` + `@sa/axios` | 直接 axios/fetch |

**文件位置：** `.claude/skills/carlos-ui-standard/SKILL.md`

---

## MCP（模型上下文协议）

MCP（Model Context Protocol）用于扩展 Claude Code 的能力，连接外部工具和服务。本项目已配置 6 个 MCP 服务器。

### MCP 服务器列表

| MCP            | 用途                     | 环境变量            |
|----------------|------------------------|-----------------|
| `filesystem`   | 文件系统访问                 | 无               |
| `github`       | GitHub API 访问          | `GITHUB_TOKEN`  |
| `fetch`        | HTTP 请求                | 无               |
| `brave-search` | 网络搜索                   | `BRAVE_API_KEY` |
| `playwright`   | 浏览器自动化                 | 无               |
| `mysql`        | MySQL 数据库访问（OLTP）      | `MYSQL_*`       |
| `clickhouse`   | ClickHouse 数据库访问（OLAP） | `CLICKHOUSE_*`  |

### 配置环境变量

在使用需要认证的 MCP 之前，配置以下环境变量：

```powershell
# GitHub Token（用于 GitHub MCP）
$env:GITHUB_TOKEN = "ghp_xxxxxxxxxxxxxxxx"

# Brave API Key（用于搜索 MCP）
$env:BRAVE_API_KEY = "BSxxxxxxxxxxxxxxxx"

# MySQL 数据库连接（用于 OLTP 业务数据）
$env:MYSQL_HOST = "localhost"
$env:MYSQL_PORT = "3306"
$env:MYSQL_USER = "root"
$env:MYSQL_PASSWORD = "your_password"
$env:MYSQL_DATABASE = "carlos"

# ClickHouse 数据库连接（用于 OLAP 数据分析）
$env:CLICKHOUSE_HOST = "localhost"
$env:CLICKHOUSE_PORT = "8123"
$env:CLICKHOUSE_USER = "default"
$env:CLICKHOUSE_PASSWORD = "your_password"
$env:CLICKHOUSE_DATABASE = "default"
```

### MCP 使用示例

**文件操作：**

```
使用 filesystem MCP 列出 carlos-spring-boot 目录下的所有 starter 模块
```

**GitHub 搜索：**

```
使用 github MCP 搜索 spring-boot-starter 的最佳实践实现
```

**API 测试：**

```
使用 fetch MCP 测试 http://localhost:8080/actuator/health 健康检查接口
```

**网络搜索：**

```
使用 brave-search MCP 搜索 "Spring Boot 3.5 MyBatis-Plus 配置最佳实践"
```

**前端测试：**

```
使用 playwright MCP 访问 http://localhost:5173 并截图登录页面
```

**MySQL 数据库查询（OLTP）：**

```
使用 mysql MCP 查询 org_user 表的结构
```

**ClickHouse 数据分析（OLAP）：**

```
使用 clickhouse MCP 查询日志表的统计数据
```

### 详细文档

完整 MCP 使用指南请参见：[MCP使用指南.md](./MCP使用指南.md)

### 配置文件位置

`.claude/mcp.json`

---

## 使用示例

### 场景1：新建业务模块（完整流程）

```
# 1. 规划
@carlos-planner 我需要设计一个订单管理模块

# 2. 生成脚手架
@carlos-module-scaffolder 请生成 carlos-order 模块

# 3. 设计数据库
@carlos-db-designer 设计订单表结构

# 4. 设计 API
@carlos-api-designer 设计订单 CRUD 接口

# 5. TDD 开发
@springboot-tdd 实现订单创建功能

# 6. 生成测试
@carlos-integration-tester 生成订单模块集成测试

# 7. 安全审查
@carlos-security-reviewer 审查订单模块代码

# 8. 更新文档
/update-doc 新增订单模块文档
```

### 场景2：前端新增页面

```
# 1. 创建页面
/new-page

# 2. 生成路由
/gen-route

# 3. 类型检查
pnpm typecheck

# 4. 代码审查
@vue-reviewer 审查新页面代码
```

### 场景3：修复 Bug

```
# 1. TDD 流程修复
@springboot-tdd 修复用户登录时的空指针异常

# 2. 代码审查
@java-reviewer 审查修复代码

# 3. 安全审查（如涉及用户输入）
@carlos-security-reviewer 审查修复代码

# 4. 运行测试
/test
```

### 场景4：构建失败排查

```
# 自动诊断
@carlos-build-resolver mvn clean install 失败了

# 或手动排查
/build
```

---

## 快速参考

### 开发流程速查

```
[需求分析] → @carlos-planner
    ↓
[数据库设计] → @carlos-db-designer
    ↓
[API 设计] → @carlos-api-designer
    ↓
[TDD 开发] → @springboot-tdd
    ↓
[代码审查] → @java-reviewer / @vue-reviewer
    ↓
[安全审查] → @carlos-security-reviewer
    ↓
[集成测试] → @carlos-integration-tester
    ↓
[构建验证] → /build
    ↓
[文档更新] → /update-doc
```

### 常用命令速查

| 操作   | 命令                |
|------|-------------------|
| 构建后端 | `/build`          |
| 构建前端 | `/build-frontend` |
| 运行测试 | `/test`           |
| 生成路由 | `/gen-route`      |
| 新建模块 | `/new-module`     |
| 新建页面 | `/new-page`       |
| 更新文档 | `/update-doc`     |

### 常用 Agent 速查

| 场景        | Agent                        |
|-----------|------------------------------|
| 功能规划      | `@carlos-planner`            |
| API 设计    | `@carlos-api-designer`       |
| 数据库设计     | `@carlos-db-designer`        |
| 模块脚手架     | `@carlos-module-scaffolder`  |
| 构建修复      | `@carlos-build-resolver`     |
| 安全审查      | `@carlos-security-reviewer`  |
| 集成测试      | `@carlos-integration-tester` |
| Java 代码审查 | `@java-reviewer`             |
| Vue 代码审查  | `@vue-reviewer`              |
| TDD 开发    | `@springboot-tdd`            |
| 前后端对接     | `@api-connector`             |

---

*最后更新：2026-03-22*
