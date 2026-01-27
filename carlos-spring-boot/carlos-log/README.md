# yunjin-log

## 模块简介

`yunjin-log` 是 YunJin 框架的操作日志记录模块，提供了基于注解和AOP的自动化操作日志记录功能。该模块可以自动记录用户操作、系统事件、异常信息等，支持丰富的业务类型和操作者类型分类，便于系统审计和故障排查。

## 主要功能

### 1. 注解式日志记录

提供 `@Log` 注解，支持在方法级别声明日志记录规则：

```java
@RestController
@RequestMapping("/api/user")
public class UserController {

    @Log(title = "用户管理", businessType = BusinessType.INSERT)
    @PostMapping("/create")
    public Result<User> createUser(@RequestBody @Valid CreateUserRequest request) {
        // 业务逻辑
        return userService.createUser(request);
    }

    @Log(title = "用户管理", businessType = BusinessType.UPDATE)
    @PutMapping("/update/{id}")
    public Result<User> updateUser(@PathVariable Long id, @RequestBody @Valid UpdateUserRequest request) {
        // 业务逻辑
        return userService.updateUser(id, request);
    }

    @Log(title = "用户管理", businessType = BusinessType.DELETE, isSaveRequestData = false)
    @DeleteMapping("/delete/{id}")
    public Result<Void> deleteUser(@PathVariable Long id) {
        // 业务逻辑
        return userService.deleteUser(id);
    }
}
```

### 2. 丰富的业务类型枚举

内置22种业务操作类型，涵盖常见的系统操作场景：

| 业务类型                  | 代码 | 说明     |
|-----------------------|----|--------|
| `OTHER`               | 00 | 其它操作   |
| `INSERT`              | 01 | 新增数据   |
| `INSERT_FORCE`        | 02 | 强制新增   |
| `UPDATE`              | 03 | 修改数据   |
| `UPDATE_FORCE`        | 04 | 强制修改   |
| `AUTH`                | 05 | 权限控制   |
| `UPDATE_STATUS`       | 06 | 修改状态   |
| `UPDATE_STATUS_FORCE` | 07 | 强制修改状态 |
| `DELETE`              | 08 | 删除数据   |
| `DELETE_FORCE`        | 09 | 强制删除   |
| `GRANT`               | 10 | 授权操作   |
| `EXPORT`              | 11 | 数据导出   |
| `IMPORT`              | 12 | 数据导入   |
| `FORCE`               | 13 | 强制退出   |
| `GEN_CODE`            | 14 | 生成代码   |
| `CLEAN`               | 15 | 清空数据   |
| `REFRESH`             | 16 | 更新缓存   |
| `QUERY`               | 17 | 查询操作   |
| `QUERY_DETAIL`        | 18 | 查询详情   |
| `LOGIN`               | 19 | 用户登录   |
| `LOGOUT`              | 20 | 用户退出   |
| `SELECT_ROLE`         | 21 | 选择角色   |

### 3. 操作者类型区分

支持区分不同来源的操作者：

| 操作者类型    | 代码 | 说明     |
|----------|----|--------|
| `OTHER`  | 00 | 其它操作者  |
| `MANAGE` | 01 | 后台管理用户 |
| `MOBILE` | 02 | 移动端用户  |

### 4. 完整的日志信息记录

自动记录以下信息到操作日志：

- **操作人信息**：用户ID、账号、操作者类型
- **请求信息**：URL、IP地址、浏览器类型
- **业务信息**：业务类型、操作模块、操作结果
- **时间信息**：操作时间、响应时间
- **异常信息**：异常堆栈、错误消息
- **数据信息**：请求参数、响应数据（可配置）

### 5. AOP自动拦截

基于Spring AOP实现，自动拦截带有 `@Log` 注解的方法：

```java
@Aspect
@RequiredArgsConstructor
public class LogAspect {

    private final OperationLogService operationLogService;

    @Pointcut("@annotation(com.yunjin.log.annotation.Log)")
    public void webLog() {}

    @AfterReturning(pointcut = "@annotation(controllerLog)", returning = "jsonResult")
    public void doAfterReturning(JoinPoint joinPoint, Log controllerLog, Object jsonResult) {
        // 成功时记录日志
        handleLog(joinPoint, controllerLog, null, jsonResult);
    }

    @AfterThrowing(pointcut = "@annotation(controllerLog)", throwing = "e")
    public void doAfterThrowing(JoinPoint joinPoint, Log controllerLog, Exception e) {
        // 异常时记录日志
        handleLog(joinPoint, controllerLog, e, null);
    }

    private void handleLog(final JoinPoint joinPoint, final Log controllerLog,
                          final Exception e, final Object jsonResult) {
        // 构建日志信息
        SystemOperationLog logInfo = new SystemOperationLog();
        // 设置操作人、请求信息、业务信息等
        operationLogService.addSysLog(logInfo);
    }
}
```

### 6. 可扩展的日志服务

提供 `OperationLogService` 接口，支持自定义日志存储实现：

```java
public interface OperationLogService {

    /**
     * 添加系统操作日志
     */
    void addSysLog(SystemOperationLog logInfo);

    /**
     * 批量添加日志
     */
    void batchAddSysLog(List<SystemOperationLog> logInfos);

    /**
     * 根据条件查询日志
     */
    List<SystemOperationLog> queryLogs(LogQueryParam param);
}
```

## 快速开始

### 1. 添加依赖

```xml
<dependency>
    <groupId>com.yunjin</groupId>
    <artifactId>yunjin-log</artifactId>
    <version>${yunjin.version}</version>
</dependency>
```

### 2. 基本使用示例

#### Controller层使用：

```java
@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Log(title = "用户管理", businessType = BusinessType.INSERT,
         operatorType = OperatorType.MANAGE, isSaveRequestData = true)
    @PostMapping("/create")
    public Result<User> createUser(@RequestBody @Valid CreateUserRequest request) {
        return userService.createUser(request);
    }

    @Log(title = "用户管理", businessType = BusinessType.UPDATE,
         operatorType = OperatorType.MANAGE, isSaveRequestData = true)
    @PutMapping("/update/{id}")
    public Result<User> updateUser(@PathVariable Long id,
                                   @RequestBody @Valid UpdateUserRequest request) {
        return userService.updateUser(id, request);
    }

    @Log(title = "用户管理", businessType = BusinessType.QUERY,
         operatorType = OperatorType.MANAGE, isSaveRequestData = false)
    @GetMapping("/list")
    public Result<Page<User>> getUserList(PageParam param) {
        return userService.getUserPage(param);
    }

    @Log(title = "用户登录", businessType = BusinessType.LOGIN,
         operatorType = OperatorType.MANAGE, isSaveRequestData = false)
    @PostMapping("/login")
    public Result<TokenVO> login(@RequestBody @Valid LoginRequest request) {
        return authService.login(request);
    }

    @Log(title = "用户退出", businessType = BusinessType.LOGOUT,
         operatorType = OperatorType.MANAGE)
    @PostMapping("/logout")
    public Result<Void> logout() {
        return authService.logout();
    }
}
```

#### Service层使用：

```java
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    @Log(title = "用户服务", businessType = BusinessType.INSERT)
    public Result<User> createUser(CreateUserRequest request) {
        // 业务逻辑
        User user = convertToEntity(request);
        userMapper.insert(user);
        return Result.ok(user);
    }

    @Override
    @Log(title = "用户服务", businessType = BusinessType.UPDATE)
    public Result<User> updateUser(Long id, UpdateUserRequest request) {
        // 业务逻辑
        User user = userMapper.selectById(id);
        if (user == null) {
            return Result.fail("用户不存在");
        }
        updateEntity(user, request);
        userMapper.updateById(user);
        return Result.ok(user);
    }
}
```

#### 自定义日志服务实现：

```java
@Service
public class CustomOperationLogServiceImpl implements OperationLogService {

    @Autowired
    private OperationLogMapper operationLogMapper;

    @Override
    public void addSysLog(SystemOperationLog logInfo) {
        // 设置默认值
        if (logInfo.getCreateTime() == null) {
            logInfo.setCreateTime(LocalDateTime.now());
        }
        if (logInfo.getId() == null) {
            logInfo.setId(IdUtil.simpleUUID());
        }

        // 保存到数据库
        operationLogMapper.insert(logInfo);

        // 同时输出到日志文件（可选）
        log.info("操作日志记录: {}", JsonUtil.toJson(logInfo));
    }

    @Override
    public void batchAddSysLog(List<SystemOperationLog> logInfos) {
        // 批量保存
        operationLogMapper.batchInsert(logInfos);
    }

    @Override
    public List<SystemOperationLog> queryLogs(LogQueryParam param) {
        // 根据条件查询
        return operationLogMapper.selectByCondition(param);
    }
}
```

### 3. 日志实体说明

`SystemOperationLog` 实体包含以下字段：

| 字段名               | 类型            | 说明    |
|-------------------|---------------|-------|
| `id`              | String        | 主键ID  |
| `operatorId`      | String        | 操作人ID |
| `operatorAccount` | String        | 操作账号  |
| `operator`        | String        | 操作人姓名 |
| `createTime`      | LocalDateTime | 创建时间  |
| `remark`          | String        | 备注信息  |
| `layer`           | Long          | 日志级别  |
| `message`         | String        | 操作信息  |
| `url`             | String        | 请求URL |
| `exception`       | String        | 异常信息  |
| `ip`              | String        | IP地址  |
| `browser`         | String        | 浏览器信息 |

## 详细功能说明

### 1. 注解属性详解

`@Log` 注解支持以下属性配置：

| 属性名                  | 类型           | 默认值                   | 说明       |
|----------------------|--------------|-----------------------|----------|
| `title`              | String       | ""                    | 操作模块标题   |
| `businessType`       | BusinessType | `BusinessType.OTHER`  | 业务操作类型   |
| `operatorType`       | OperatorType | `OperatorType.MANAGE` | 操作者类型    |
| `isSaveRequestData`  | boolean      | `true`                | 是否保存请求参数 |
| `isSaveResponseData` | boolean      | `true`                | 是否保存响应数据 |

### 2. 敏感信息过滤

默认不记录敏感字段，如密码等：

```java
// 在需要过滤敏感信息的场景使用
@Log(title = "修改密码", businessType = BusinessType.UPDATE,
     isSaveRequestData = false)  // 不保存请求数据，避免密码泄露
@PostMapping("/change-password")
public Result<Void> changePassword(@RequestBody ChangePasswordRequest request) {
    // 业务逻辑
    return userService.changePassword(request);
}
```

### 3. 异步日志记录

支持异步记录日志，避免影响主业务流程：

```java
@Configuration
public class AsyncLogConfig {

    @Bean("logTaskExecutor")
    public ThreadPoolTaskExecutor logTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);
        executor.setMaxPoolSize(10);
        executor.setQueueCapacity(100);
        executor.setThreadNamePrefix("log-executor-");
        executor.initialize();
        return executor;
    }

    @Async("logTaskExecutor")
    public void asyncAddLog(SystemOperationLog logInfo) {
        operationLogService.addSysLog(logInfo);
    }
}
```

### 4. 与用户上下文集成

自动从 `UserContext` 获取当前用户信息：

```java
// 在LogAspect中自动获取用户信息
UserContext userContext = ExtendInfoUtil.getUserContext();
if (userContext != null) {
    logInfo.setOperatorId(userContext.getUserId().toString());
    logInfo.setOperatorAccount(userContext.getAccount());
    logInfo.setOperator(userContext.getUserName());
}
```

## 配置说明

### 1. 自动配置

模块提供自动配置，无需手动配置即可使用：

```java
@Configuration(proxyBeanMethods = false)
public class LogConfig {

    @Bean
    public LogAspect aspect(OperationLogService operationLogService) {
        return new LogAspect(operationLogService);
    }

    @Bean
    @ConditionalOnMissingBean(OperationLogService.class)
    public OperationLogService operationLogService() {
        // 默认实现：控制台输出日志
        return new DefaultOperationLogServiceImpl();
    }
}
```

### 2. 自定义配置

如果需要自定义日志处理逻辑，可以创建自己的 `OperationLogService` 实现：

```java
@Configuration
public class CustomLogConfig {

    @Bean
    public OperationLogService operationLogService() {
        // 自定义实现：保存到数据库 + 输出到ELK
        return new CustomOperationLogServiceImpl();
    }

    @Bean
    public LogAspect logAspect(OperationLogService operationLogService) {
        // 使用自定义的日志服务
        return new LogAspect(operationLogService);
    }
}
```

## 依赖项

- `yunjin-core`：用户上下文管理、工具类
- `yunjin-springboot`：Spring Boot自动配置支持
- `spring-boot-starter-aop`：AOP支持
- `spring-boot-starter-web`：Web请求处理

## 注意事项

### 1. 性能考虑

- 日志记录会增加方法执行时间，建议对高频操作谨慎使用
- 考虑使用异步方式记录日志，避免阻塞主业务流程
- 生产环境建议关闭详细的数据记录（`isSaveRequestData=false`）

### 2. 存储考虑

- 默认实现仅输出到控制台，生产环境需要实现持久化存储
- 日志数据量较大时，考虑分表存储或使用时序数据库
- 定期清理历史日志，避免存储空间不足

### 3. 安全考虑

- 敏感操作（如密码修改、权限变更）必须记录日志
- 日志中避免记录敏感信息（密码、密钥、身份证号等）
- 对日志访问进行权限控制，防止信息泄露

### 4. 使用建议

- 为关键业务操作添加日志注解
- 根据业务重要性设置合适的日志级别
- 统一日志格式，便于后续分析和监控
- 结合告警系统，对异常操作进行实时告警

### 5. 常见问题

**Q: 日志没有记录怎么办？**
A: 检查以下事项：

1. 确认方法上添加了 `@Log` 注解
2. 确认注解的 `businessType` 等属性配置正确
3. 确认Spring AOP配置正确，切面生效
4. 检查应用日志，查看是否有异常信息

**Q: 如何自定义日志存储位置？**
A: 实现 `OperationLogService` 接口：

```java
@Service
public class DatabaseLogServiceImpl implements OperationLogService {

    @Autowired
    private OperationLogMapper logMapper;

    @Override
    public void addSysLog(SystemOperationLog logInfo) {
        logMapper.insert(logInfo);
    }
}
```

**Q: 如何记录操作时长？**
A: 在切面中添加时间记录：

```java
@Around("@annotation(com.yunjin.log.annotation.Log)")
public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
    long startTime = System.currentTimeMillis();
    Object result = joinPoint.proceed();
    long endTime = System.currentTimeMillis();

    // 记录操作时长
    long duration = endTime - startTime;
    // 保存到日志信息中
    return result;
}
```

**Q: 如何过滤敏感字段？**
A: 在切面中实现字段过滤：

```java
private String filterSensitiveData(Object data) {
    if (data == null) {
        return null;
    }

    String json = JsonUtil.toJson(data);
    // 使用正则替换敏感字段
    json = json.replaceAll("\"password\":\"[^\"]*\"", "\"password\":\"***\"");
    json = json.replaceAll("\"idCard\":\"[^\"]*\"", "\"idCard\":\"***\"");

    return json;
}
```

## 版本要求

- JDK 17+
- Spring Boot 3.5.8+
- Spring AOP 6.x+

## 相关模块

- **yunjin-core**：用户上下文管理、工具类、基础实体
- **yunjin-mybatis**：数据库操作，用于日志持久化存储
- **yunjin-oauth2**：用户认证授权，获取当前用户信息
- **yunjin-apm**：应用性能监控，与日志记录结合