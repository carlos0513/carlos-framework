# Carlos Framework 问题扫描清单

> 扫描时间: 2026-04-03
> 扫描范围: /home/node/.openclaw/agents/carlos-framework/workspace/carlos-framework
> 扫描文件: Java源文件(*.java)、XML配置文件(*.xml)、YAML配置(*.yml)、Markdown文档(*.md)

---

## 1. 未完成任务

### 高优先级

| 序号 | 文件 | 行号 | 类型 | 问题描述 | 建议处理方案 |
|------|------|------|------|----------|--------------|
| 1 | carlos-integration/carlos-auth/carlos-auth-service/src/main/java/com/carlos/auth/config/OAuth2AuthorizationServerConfig.java | 547 | TODO | SM4PasswordEncoder 未实现，配置为sm4时仍使用BCrypt | 实现国密SM4密码加密器 |
| 2 | carlos-integration/carlos-auth/carlos-auth-service/src/main/java/com/carlos/auth/config/OAuth2AuthorizationServerConfig.java | 632 | TODO | JDBC OAuth2AuthorizationService 未实现 | 实现基于数据库存储的授权服务 |
| 3 | carlos-integration/carlos-auth/carlos-auth-service/src/main/java/com/carlos/auth/login/LoginService.java | 184 | TODO | JWT令牌颁发未实现，返回空令牌 | 完成JWT令牌颁发逻辑 |
| 4 | carlos-integration/carlos-audit/carlos-audit-bus/src/main/java/com/carlos/audit/service/AuditLogQueryService.java | 48 | TODO | ClickHouse查询实现为空 | 完成ClickHouse查询实现 |
| 5 | carlos-integration/carlos-audit/carlos-audit-bus/src/main/java/com/carlos/audit/service/AuditLogQueryService.java | 69 | TODO | getById查询实现为空，返回null | 实现日志详情查询 |
| 6 | carlos-integration/carlos-auth/carlos-auth-service/src/main/java/com/carlos/auth/config/repository/RedisOAuth2AuthorizationService.java | 283 | TODO | OAuth2Authorization反序列化未实现 | 使用Jackson模块实现反序列化 |

### 中优先级

| 序号 | 文件 | 行号 | 类型 | 问题描述 | 建议处理方案 |
|------|------|------|------|----------|--------------|
| 1 | carlos-integration/carlos-org/carlos-org-bus/src/main/java/com/carlos/org/service/OrgUserService.java | 268 | TODO | Excel解析和导入逻辑未实现 | 使用EasyExcel实现导入功能 |
| 2 | carlos-integration/carlos-org/carlos-org-bus/src/main/java/com/carlos/org/service/OrgUserService.java | 287 | TODO | Excel导出逻辑未实现 | 使用EasyExcel实现导出功能 |
| 3 | carlos-integration/carlos-org/carlos-org-bus/src/main/java/com/carlos/org/service/OrgUserService.java | 307 | TODO | 部门分配逻辑未实现 | 关联org_user_department表实现 |
| 4 | carlos-integration/carlos-org/carlos-org-bus/src/main/java/com/carlos/org/service/OrgUserService.java | 336 | TODO | 角色分配逻辑未实现 | 关联org_user_role表实现 |
| 5 | carlos-integration/carlos-org/carlos-org-bus/src/main/java/com/carlos/org/service/OrgUserService.java | 358 | TODO | 岗位分配逻辑未实现 | 关联org_user_position表实现 |
| 6 | carlos-integration/carlos-org/carlos-org-bus/src/main/java/com/carlos/org/service/OrgUserService.java | 373 | TODO | 从org_user_department表查询用户部门列表 | 实现部门查询DTO返回 |
| 7 | carlos-integration/carlos-org/carlos-org-bus/src/main/java/com/carlos/org/service/OrgUserService.java | 385 | TODO | 从org_user_role表查询用户角色列表 | 实现角色查询DTO返回 |
| 8 | carlos-integration/carlos-org/carlos-org-bus/src/main/java/com/carlos/org/service/OrgUserService.java | 397 | TODO | 从org_user_position表查询用户岗位列表 | 实现岗位查询DTO返回 |
| 9 | carlos-integration/carlos-org/carlos-org-bus/src/main/java/com/carlos/org/service/OrgDepartmentService.java | 340 | TODO | 部门角色关联逻辑未实现 | 实现部门-角色关联功能 |
| 10 | carlos-integration/carlos-message/carlos-message-bus/src/main/java/com/carlos/message/channel/dingtalk/DingtalkChannelAdapter.java | 60 | TODO | 钉钉SDK未集成，仅模拟发送 | 集成钉钉SDK实现真实发送 |
| 11 | carlos-integration/carlos-message/carlos-message-bus/src/main/java/com/carlos/message/channel/sms/SmsChannelAdapter.java | 61 | TODO | 短信服务商SDK未集成 | 集成阿里云/腾讯云短信SDK |
| 12 | carlos-integration/carlos-auth/carlos-auth-service/src/main/java/com/carlos/auth/captcha/CaptchaService.java | 144 | TODO | 真实短信服务API未调用 | 集成短信服务商SDK |
| 13 | carlos-integration/carlos-auth/carlos-auth-service/src/main/java/com/carlos/auth/captcha/CaptchaService.java | 166 | TODO | 邮件服务发送验证码未实现 | 集成邮件服务 |
| 14 | carlos-integration/carlos-auth/carlos-auth-service/src/main/java/com/carlos/auth/audit/service/SecurityAlertServiceImpl.java | 191 | TODO | 邮件发送服务未集成 | 集成邮件服务发送告警 |
| 15 | carlos-integration/carlos-auth/carlos-auth-service/src/main/java/com/carlos/auth/audit/service/SecurityAlertServiceImpl.java | 197 | TODO | 短信发送服务未集成 | 集成短信服务发送告警 |
| 16 | carlos-integration/carlos-auth/carlos-auth-service/src/main/java/com/carlos/auth/audit/service/SecurityAlertServiceImpl.java | 202 | TODO | 钉钉Webhook未集成 | 集成钉钉机器人发送告警 |
| 17 | carlos-integration/carlos-gateway/src/main/java/com/carlos/gateway/ratelimit/listener/RateLimitAlertListener.java | 84 | TODO | 限流告警未对接实际告警系统 | 对接钉钉/企业微信/邮件告警 |
| 18 | carlos-integration/carlos-audit/carlos-audit-bus/src/main/java/com/carlos/audit/clickhouse/ClickHouseBatchWriter.java | 141 | TODO | 写入失败时未转入磁盘备份或死信队列 | 实现失败消息持久化机制 |
| 19 | carlos-integration/carlos-auth/carlos-auth-service/src/main/java/com/carlos/auth/captcha/CaptchaController.java | 152 | TODO | 验证码登录后自动注册逻辑未实现 | 实现用户自动注册功能 |
| 20 | carlos-integration/carlos-auth/carlos-auth-service/src/main/java/com/carlos/auth/captcha/CaptchaController.java | 153 | TODO | 验证码登录后JWT令牌返回未实现 | 生成并返回JWT令牌 |

### 低优先级

| 序号 | 文件 | 行号 | 类型 | 问题描述 | 建议处理方案 |
|------|------|------|------|----------|--------------|
| 1 | carlos-integration/carlos-auth/carlos-auth-service/src/main/java/com/carlos/auth/oauth2/client/CustomClientAuthenticationSuccessHandler.java | 25 | TODO | 客户端登录成功响应未输出 | 实现登录成功响应输出 |
| 2 | carlos-integration/carlos-auth/carlos-auth-service/src/main/java/com/carlos/auth/oauth2/client/CustomizeClientOAuth2TokenCustomizer.java | 21 | TODO | 不同grant_type处理逻辑未完善 | 根据授权类型定制Token |
| 3 | carlos-integration/carlos-auth/carlos-auth-service/src/main/java/com/carlos/auth/oauth2/user/CustomizeUserOAuth2TokenCustomizer.java | 21 | TODO | client模式用户信息处理未完善 | 区分client模式和用户模式 |
| 4 | carlos-integration/carlos-auth/carlos-auth-service/src/main/java/com/carlos/auth/support/core/DefaultAuthenticationProvider.java | 74 | TODO | 暂时使用固定值 | 改为动态配置获取 |
| 5 | carlos-integration/carlos-auth/carlos-auth-service/src/main/java/com/carlos/auth/login/LoginService.java | 270 | TODO | 令牌黑名单功能未实现（可选） | 实现Redis黑名单机制 |
| 6 | carlos-integration/carlos-auth/carlos-auth-service/src/main/java/com/carlos/auth/login/LoginService.java | 275 | TODO | Redis授权信息清除未实现（可选） | 实现登出清理逻辑 |
| 7 | carlos-integration/carlos-auth/carlos-auth-service/src/main/java/com/carlos/auth/login/AuthController.java | 131 | TODO | 失败次数超限告警未实现 | 添加Redis失败计数检查 |
| 8 | carlos-integration/carlos-auth/carlos-auth-service/src/main/java/com/carlos/auth/login/AuthController.java | 238 | TODO | 令牌有效性检查逻辑未实现 | 实现令牌有效性验证 |
| 9 | carlos-spring-boot/carlos-spring-boot-starter-datacenter/src/main/java/com/carlos/datacenter/config/DatacenterMainConfig.java | 21 | TODO | 数据平台组件公共属性配置待完善 | 配置线程池等公共属性 |
| 10 | carlos-integration/carlos-tools/src/main/java/com/carlos/fx/codege/service/CodeGeneratorService.java | 95 | TODO | 多模块模板处理逻辑待完善 | 处理多模块项目生成 |
| 11 | carlos-integration/carlos-tools/src/main/java/com/carlos/fx/codege/CodeGeneratorController.java | 205 | TODO | 代码生成器功能待完善 | 完成控制器功能 |
| 12 | carlos-integration/carlos-license/carlos-spring-boot-starter-license-generate/src/main/java/com/carlos/license/generate/LicenseCreatorController.java | 32 | TODO | 服务器信息获取接口应改为脚本方式 | 提供Shell/Python脚本 |
| 13 | carlos-integration/carlos-tools/src/main/java/com/carlos/fx/gitlab/controller/* | 多处 | TODO | FX工具GitLab控制器多处待实现 | 实现FX界面功能 |

### FIXME 待修复问题

| 序号 | 文件 | 行号 | 类型 | 问题描述 | 建议处理方案 |
|------|------|------|------|----------|--------------|
| 1 | carlos-spring-boot/carlos-spring-boot-starter-snowflake/src/main/java/com/carlos/snowflake/SnowflakeInitiator.java | 95 | FIXME | 获取的IP可能不正确 | 使用更可靠的方式获取IP |
| 2 | carlos-integration/carlos-system/carlos-system-bus/src/main/java/com/carlos/system/region/manager/impl/SysRegionManagerImpl.java | 425 | FIXME | 缓存失效情况未处理 | 处理缓存失效逻辑 |
| 3 | carlos-integration/carlos-system/carlos-system-bus/src/main/java/com/carlos/system/region/manager/impl/SysRegionManagerImpl.java | 431 | FIXME | 只考虑更改基本信息，不更改层级 | 完善层级变更处理 |
| 4 | carlos-integration/carlos-system/carlos-system-bus/src/main/java/com/carlos/system/region/service/SysRegionService.java | 355 | FIXME | 实现方式待优化 | 优化数据处理逻辑 |
| 5 | carlos-spring-boot/carlos-spring-boot-starter-integration/src/main/java/com/carlos/integration/module/dingtalk/service/DingtalkService.java | 423 | FIXME | 该代码后续将移除 | 标记@Deprecated或移除 |
| 6 | carlos-spring-boot/carlos-spring-boot-starter-datacenter/src/main/java/com/carlos/datacenter/config/DatacenterInstanceInitializer.java | 64 | FIXME | 实例ID重复配置问题 | 优化配置逻辑避免重复 |
| 7 | carlos-integration/carlos-system/carlos-system-bus/src/main/java/com/carlos/system/configration/service/impl/SysConfigServiceImpl.java | 92 | FIXME | 文件类型配置处理待实现 | 实现文件类型转换 |

---

## 2. 设计问题

### 代码异味

| 序号 | 文件 | 行号 | 问题类型 | 问题描述 | 严重程度 | 建议处理方案 |
|------|------|------|----------|----------|----------|--------------|
| 1 | carlos-samples/carlos-test/src/main/java/com/carlos/test/controller/TestController.java | 129 | 硬编码 | 使用System.out.println输出 | 低 | 使用日志框架替代 |
| 2 | carlos-samples/carlos-test/src/main/java/com/carlos/test/controller/EncryptController.java | 22-23 | 硬编码 | 使用System.out.println输出 | 低 | 使用日志框架替代 |
| 3 | carlos-samples/carlos-sample-docking/src/main/java/com/carlos/sample/docking/DockingSampleApplication.java | 29-30 | 硬编码 | 使用System.out.println输出 | 低 | 使用日志框架替代 |
| 4 | carlos-samples/carlos-test/src/test/java/com/carlos/util/GitlabAPI.java | 多处 | 异常处理 | 使用e.printStackTrace() | 中 | 使用日志框架记录异常 |
| 5 | carlos-dependencies/pom.xml | 安全版本 | 版本冲突 | spring-security-oauth2-jose定义了两次不同版本(6.2.7和5.7.3) | 高 | 移除重复定义，统一版本 |
| 6 | carlos-dependencies/pom.xml | 安全版本 | 版本冲突 | spring-boot-starter-security定义了两次不同版本(3.2.12和2.7.4) | 高 | 移除重复定义，统一版本 |

### 架构问题

| 序号 | 问题描述 | 涉及模块 | 严重程度 | 建议处理方案 |
|------|----------|----------|----------|--------------|
| 1 | @Autowired字段注入仍然存在31处 | 多个模块 | 低 | 统一改为构造函数注入 |
| 2 | 测试文件只有35个@Test标记 | 整个框架 | 高 | 为核心类补充单元测试 |
| 3 | 代码注释存在乱码问题 | carlos-spring-boot-starter-integration | 低 | 修复文件编码为UTF-8 |
| 4 | 大量TODO未实现导致功能不完整 | auth/message/audit/org模块 | 高 | 优先实现核心功能TODO |
| 5 | 限流熔断功能标记TODO待完善 | gateway模块 | 中 | 完善限流熔断实现 |

### 命名规范问题

| 序号 | 文件 | 问题描述 | 建议处理方案 |
|------|------|----------|--------------|
| 1 | carlos-integration/carlos-tools FX模块 | FX控制器使用JavaFX但命名不够规范 | 统一FX相关类命名规范 |
| 2 | 包名com.carlos.configration | 拼写错误，应为configuration | 修正包名拼写 |

---

## 3. 模块不一致

| 序号 | 问题描述 | 涉及模块 | 建议处理方案 |
|------|----------|----------|--------------|
| 1 | 不同模块中异常处理方式不一致 | commons/各starter | 统一异常处理机制 |
| 2 | 日志输出方式不一致，部分使用System.out | samples模块 | 统一使用SLF4J日志 |
| 3 | 配置属性命名风格不一致 | 各starter | 统一使用kebab-case命名 |
| 4 | 版本号定义重复且冲突 | carlos-dependencies | 清理重复定义，保持版本一致 |

---

## 4. 缺失功能

| 序号 | 缺失功能 | 影响范围 | 优先级 | 建议处理方案 |
|------|----------|----------|--------|--------------|
| 1 | 单元测试覆盖率低 | 核心模块 | 高 | 为核心Service/Manager添加单元测试 |
| 2 | 公共API缺少JavaDoc | controller/service | 中 | 补充完整的JavaDoc注释 |
| 3 | 配置属性缺少说明文档 | application.yml | 中 | 添加配置属性元数据和文档 |
| 4 | ClickHouse审计查询未实现 | carlos-audit-bus | 高 | 完成ClickHouse查询实现 |
| 5 | OAuth2 JDBC存储未实现 | carlos-auth-service | 高 | 实现JDBC授权服务 |
| 6 | SM4国密加密未实现 | carlos-auth-service | 中 | 实现SM4PasswordEncoder |
| 7 | 第三方集成待完善 | message模块 | 中 | 集成钉钉、短信、邮件SDK |
| 8 | Excel导入导出待实现 | org模块 | 中 | 实现用户数据Excel处理 |
| 9 | 部门/角色/岗位关联待实现 | org模块 | 高 | 完成用户关联关系功能 |

---

## 5. 版本依赖问题

### pom.xml 中发现的问题

| 序号 | 问题 | 位置 | 建议 |
|------|------|------|------|
| 1 | spring-security-oauth2-jose版本重复定义 | carlos-dependencies/pom.xml | 删除重复的旧版本定义(5.7.3) |
| 2 | spring-boot-starter-security版本重复定义 | carlos-dependencies/pom.xml | 删除重复的旧版本定义(2.7.4) |
| 3 | 大量注释掉的旧版本号 | carlos-dependencies/pom.xml | 清理不再需要的旧版本注释 |

---

## 6. 汇总统计

### 未完成任务统计
- **总计**: 100个 TODO/FIXME/XXX/HACK 标记
- **高优先级**: 6个 (核心功能缺失)
- **中优先级**: 20个 (主要功能待实现)
- **低优先级**: 13个 + FX工具类TODO (可选功能)
- **FIXME待修复**: 7个 (需要修复的问题)

### 设计问题统计
- **代码异味**: 6个
  - 硬编码输出: 3处
  - 异常处理不当: 1处
  - 版本冲突: 2处
- **架构问题**: 5个
- **命名规范问题**: 2个

### 模块不一致统计
- **总计**: 4类不一致问题

### 缺失功能统计
- **总计**: 9项关键缺失功能
  - 高优先级: 4项
  - 中优先级: 5项

### 总体评估
- **代码健康度**: ⚠️ 中等 (存在较多未实现的TODO)
- **核心功能完成度**: 约 70% (auth/org/message模块有待完善)
- **测试覆盖率**: 低 (需要补充单元测试)
- **文档完整度**: 中等 (需要补充JavaDoc)

---

## 7. 优先处理建议

### 立即处理 (本周内)
1. 修复pom.xml版本冲突问题 (高)
2. 实现JWT令牌颁发功能 (高)
3. 实现ClickHouse审计查询 (高)

### 短期处理 (本月内)
1. 实现OAuth2 JDBC存储
2. 实现SM4国密加密
3. 集成钉钉/短信SDK
4. 完成用户关联关系功能

### 中期处理 (下月内)
1. 补充单元测试
2. 完善JavaDoc
3. 统一异常处理机制
4. 清理FX工具TODO

### 长期优化
1. 完善配置文档
2. 优化包结构
3. 统一代码风格
4. 增加集成测试

---

*报告生成时间: 2026-04-03*
*扫描工具: 静态代码分析*
