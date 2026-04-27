# RuntimeException 修复报告

## 修复时间
2026-04-25

## 修复范围
根据《Carlos Framework 每日优化计划 - 2026-04-25》中的高优先级问题 #1：
**RuntimeException 滥用 - 发现 40 处直接使用 RuntimeException，违反框架规范**

## 修复原则

| 场景 | 替换异常 | 说明 |
|------|----------|------|
| 业务逻辑错误 | `BusinessException` | 用户不存在、权限不足、业务规则限制等 |
| 数据库操作失败 | `DaoException` | 连接失败、SQL 错误、批量操作失败等 |
| 接口层/参数错误 | `RestException` | 参数解析失败、请求格式错误、导出失败等 |
| 通用系统错误 | `GlobalException` | 配置加载失败、未知系统异常等 |
| 工具类（无 core 依赖） | `IllegalStateException` | 避免循环依赖 |

## 修复文件清单（共 21 个文件）

### 1. 业务模块 → BusinessException
- `carlos-org-bus/OrgUserPositionService.java` - 岗位分配业务逻辑
- `carlos-audit-api/ApiAuditLogMainUsageExample.java` - 审计日志示例
- `carlos-auth-service/CustomizeOAuth2AuthorizationService.java` - OAuth2 认证
- `carlos-auth-service/SM4PasswordEncoder.java` - 密码编码
- `carlos-auth-service/QrCodeGenerator.java` - 二维码生成
- `carlos-spring-boot-starter-ai/AiChatService.java` - AI 对话
- `carlos-spring-boot-starter-datascope/SpelExpressionEngine.java` - 表达式执行
- `carlos-spring-boot-starter-migration/ChangelogGenerator.java` - 迁移日志
- `carlos-spring-boot-starter-oss/OssController.java` - 文件上传
- `carlos-spring-boot-starter-redis/RedissonLockUtil.java` - 分布式锁

### 2. 数据库操作 → DaoException
- `carlos-audit-bus/ClickHouseQueryClient.java` - ClickHouse 查询
- `carlos-spring-boot-starter-mongodb/BaseServiceImpl.java` - MongoDB 基础服务
- `carlos-spring-boot-starter-mybatis/BaseServiceImpl.java` - MyBatis 基础服务
- `carlos-spring-boot-starter-mybatis/BatchServiceImpl.java` - 批量数据库操作

### 3. 接口层 → RestException
- `carlos-utils/ExcelUtil.java` - Excel 导入导出（工具模块，使用标准异常）

### 4. 系统/配置错误 → GlobalException
- `carlos-gateway/CarlosRateLimiterAutoConfiguration.java` - 限流配置加载

### 5. 工具模块（避免循环依赖）→ 标准 Java 异常
- `carlos-tools/CodeGeneratorController.java` - 代码生成 → `IllegalStateException`
- `carlos-tools/GitlabService.java` - Gitlab 服务 → `IllegalStateException`
- `carlos-tools/ResourceUtil.java` - 资源加载 → `IllegalStateException`
- `carlos-tools/XmlUtils.java` - XML 解析 → `IllegalStateException`
- `carlos-tools/FxUtil.java` - FXML 加载 → `IllegalStateException`
- `carlos-utils/ExcelUtil.java` - Excel 处理 → `IllegalStateException`

## 未修复的 RuntimeException 引用（合理保留）

以下情况保留了 RuntimeException 的引用，属于合理用法：

1. **异常继承** - 框架自定义异常继承 `RuntimeException`：
   - `GlobalException extends RuntimeException`
   - `DataScopeDeniedException extends RuntimeException`
   - `OssException extends RuntimeException`
   - `GitlabToolException extends RuntimeException`
   - `GeneratorException extends RuntimeException`
   - `DatacenterInstanceException extends RuntimeException`
   - `DatacenterSqlException extends RuntimeException`
   - `MessageException/MessageSendException/MessageClientException extends RuntimeException`
   - `LicenseException extends RuntimeException`
   - `DockingException/DockingRequestException/DockingRequestParamException/DockingRestClientException extends RuntimeException`
   - `MigrationException extends RuntimeException`

2. **序列化异常内部类** - `SerializationException extends RuntimeException`
   - `FastjsonSerializer`, `KryoSerializer`, `JacksonSerializer`, `JdkSerializer`

3. **Spring Cache 错误处理回调** - `handleCacheGetError(RuntimeException e, ...)`
   - `RedisCacheConfig.java` - 这是 Spring 框架接口的方法签名

4. **注释/文档** - `@throws RuntimeException` 等 Javadoc

5. **单元测试文件** - `*Test.java` 中的测试代码

## 安全收益

- ✅ 异常信息统一由 `GlobalExceptionHandler` 处理，避免堆栈信息泄露到客户端
- ✅ 错误码体系一致，前端可基于 errorCode 做精准错误提示
- ✅ HTTP 状态码规范化（400/500 区分明确）
- ✅ 符合 Carlos Framework 异常处理规范

## 后续建议

1. 在 CI/CD 中增加 Checkstyle/SpotBugs 规则，禁止直接使用 `new RuntimeException()`
2. 对于 `carlos-utils` 和 `carlos-tools` 等基础模块，可考虑抽取独立的轻量异常模块，避免使用裸 RuntimeException
3. 完善各模块的 `ErrorCode` 枚举定义，让异常抛出时带上标准错误码
