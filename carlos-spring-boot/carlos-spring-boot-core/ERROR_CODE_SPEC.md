# Carlos Framework 错误码设计规范

> 版本: 1.0  
> 日期: 2026-03-17  
> 状态: 草案

---

## 目录

- [1. 设计原则](#1-设计原则)
- [2. 错误码格式](#2-错误码格式)
- [3. 错误码定义](#3-错误码定义)
- [4. 响应结构](#4-响应结构)
- [5. 使用指南](#5-使用指南)
- [6. 迁移指南](#6-迁移指南)
- [7. 最佳实践](#7-最佳实践)

---

## 1. 设计原则

### 1.1 核心原则

| 原则          | 说明                        |
|-------------|---------------------------|
| **语义化**     | 错误码应自解释，通过错误码能快速定位问题类型和模块 |
| **层级清晰**    | 错误码按层级划分，便于分类统计和监控告警      |
| **避免冲突**    | 微服务环境下，错误码全局唯一，防止"撞码"     |
| **兼容 HTTP** | 错误码与 HTTP 状态码对应，便于网关统一处理  |
| **可追溯**     | 每个错误响应包含 traceId，便于链路追踪   |

### 1.2 新旧对比

| 维度      | 旧规范 (StatusCode) | 新规范 (ErrorCode) |
|---------|------------------|-----------------|
| 格式      | 纯数字 (2000, 4000) | 5位分层码 (A-BB-CC) |
| 含义      | 需要查表才能理解         | 自解释，一看便知        |
| HTTP 映射 | 无                | 明确绑定 HTTP 状态码   |
| 扩展性     | 扩展困难             | 预留空间，易于扩展       |
| 微服务支持   | 不支持              | 支持服务前缀隔离        |

---

## 2. 错误码格式

### 2.1 分层结构

错误码采用 **5位数字字符串** 格式：`A-BB-CC`

```
A    - BB   - CC
|       |       |
|       |       └── 具体错误序号（2位）
|       └────────── 服务/模块编码（2位）
└────────────────── 错误级别（1位）
```

### 2.2 错误级别 (A)

| 级别    | 代码 | 说明       | 示例             |
|-------|----|----------|----------------|
| 成功    | 0  | 操作成功     | `00000`        |
| 客户端错误 | 1  | 用户请求参数问题 | `10001` 参数错误   |
| 业务错误  | 2  | 业务规则限制   | `20101` 用户不存在  |
| 第三方错误 | 3  | 外部服务调用失败 | `30101` 短信服务异常 |
| 系统错误  | 5  | 系统内部故障   | `50001` 内部错误   |

### 2.3 服务/模块编码 (BB)

| 编码    | 模块   | 说明           |
|-------|------|--------------|
| 00    | 通用   | 全局通用错误       |
| 01    | 用户服务 | 用户相关操作       |
| 02    | 认证授权 | 登录、权限、Token  |
| 03    | 订单服务 | 订单相关操作       |
| 04    | 支付服务 | 支付相关操作       |
| 05    | 消息服务 | 通知、短信、邮件     |
| 06    | 文件服务 | 上传、存储、OSS    |
| 07    | 工作流  | Flowable 工作流 |
| 08    | 数据权限 | 数据范围控制       |
| 09    | 系统管理 | 字典、配置、菜单     |
| 10-99 | 预留   | 业务扩展使用       |

---

## 3. 错误码定义

### 3.1 成功响应

| 错误码     | 消息   | HTTP状态码 | 说明     |
|---------|------|---------|--------|
| `00000` | 操作成功 | 200     | 通用成功响应 |

### 3.2 客户端错误 (1-xx-xx)

#### 通用客户端错误 (1-00-xx)

| 错误码     | 消息        | HTTP状态码 | 说明            |
|---------|-----------|---------|---------------|
| `10001` | 请求参数错误    | 400     | 通用参数错误        |
| `10002` | 未授权，请先登录  | 401     | 未登录或 Token 过期 |
| `10003` | 禁止访问，权限不足 | 403     | 无权限访问         |
| `10004` | 请求的资源不存在  | 404     | 资源不存在         |
| `10005` | 请求方法不支持   | 405     | HTTP 方法错误     |
| `10006` | 请求超时      | 408     | 请求处理超时        |
| `10007` | 请求过于频繁    | 429     | 限流触发          |
| `10008` | 请求实体过大    | 413     | 文件/数据过大       |

#### 参数校验错误 (1-01-xx)

| 错误码     | 消息     | HTTP状态码 | 说明     |
|---------|--------|---------|--------|
| `10101` | 参数校验失败 | 400     | 通用校验失败 |
| `10102` | 缺少必要参数 | 400     | 必填参数缺失 |
| `10103` | 参数类型错误 | 400     | 类型不匹配  |
| `10104` | 参数格式错误 | 400     | 格式不正确  |
| `10105` | 参数超出范围 | 400     | 数值范围错误 |

### 3.3 业务错误 (2-xx-xx)

#### 用户模块错误 (2-01-xx)

| 错误码     | 消息       | HTTP状态码 | 说明        |
|---------|----------|---------|-----------|
| `20101` | 用户不存在    | 404     | 用户 ID 不存在 |
| `20102` | 用户名或密码错误 | 400     | 登录验证失败    |
| `20103` | 账号已被锁定   | 403     | 账号锁定状态    |
| `20104` | 账号已过期    | 403     | 账号过期状态    |
| `20105` | 用户已存在    | 409     | 重复注册      |
| `20106` | 原密码错误    | 400     | 密码修改验证失败  |
| `20107` | 用户未激活    | 403     | 需要激活账号    |
| `20108` | 用户已被禁用   | 403     | 管理员禁用     |

#### 认证授权错误 (2-02-xx)

| 错误码     | 消息         | HTTP状态码 | 说明            |
|---------|------------|---------|---------------|
| `20201` | 登录凭证已过期    | 401     | Token 过期      |
| `20202` | 登录凭证无效     | 401     | Token 格式/签名错误 |
| `20203` | 没有访问权限     | 403     | 权限不足          |
| `20204` | 验证码错误或已过期  | 400     | 验证码验证失败       |
| `20205` | 登录设备受限     | 403     | 设备绑定限制        |
| `20206` | 并发登录超出限制   | 403     | 同时在线数限制       |
| `20207` | Token 已被吊销 | 401     | 主动登出/吊销       |

#### 文件服务错误 (2-06-xx)

| 错误码     | 消息       | HTTP状态码 | 说明        |
|---------|----------|---------|-----------|
| `20601` | 文件上传失败   | 500     | 上传过程异常    |
| `20602` | 文件类型不允许  | 400     | 非法文件类型    |
| `20603` | 文件大小超出限制 | 400     | 超过最大限制    |
| `20604` | 文件不存在    | 404     | 文件已删除/不存在 |
| `20605` | 文件下载失败   | 500     | 存储服务异常    |

### 3.4 第三方服务错误 (3-xx-xx)

#### 短信服务错误 (3-05-xx)

| 错误码     | 消息       | HTTP状态码 | 说明      |
|---------|----------|---------|---------|
| `30501` | 短信发送失败   | 500     | 短信服务商异常 |
| `30502` | 短信发送过于频繁 | 429     | 频率限制    |
| `30503` | 短信模板不存在  | 400     | 模板配置错误  |

### 3.5 系统错误 (5-xx-xx)

#### 通用系统错误 (5-00-xx)

| 错误码     | 消息         | HTTP状态码 | 说明           |
|---------|------------|---------|--------------|
| `50001` | 系统内部错误     | 500     | 未预期异常        |
| `50002` | 数据库操作异常    | 500     | 数据库错误        |
| `50003` | 缓存服务异常     | 500     | Redis 异常     |
| `50004` | 服务调用异常     | 503     | RPC/Feign 失败 |
| `50005` | 系统繁忙，请稍后重试 | 503     | 限流/降级        |
| `50006` | 配置加载失败     | 500     | 配置中心异常       |

---

## 4. 响应结构

### 4.1 成功响应

```json
{
  "success": true,
  "code": "00000",
  "msg": "操作成功",
  "data": {
    "id": 1,
    "name": "张三"
  },
  "traceId": "a1b2c3d4-e5f6-7890-1234-567890abcdef",
  "timestamp": 1710638258000
}
```

### 4.2 单字段错误响应

```json
{
  "success": false,
  "code": "20101",
  "msg": "用户不存在",
  "data": null,
  "traceId": "a1b2c3d4-e5f6-7890-1234-567890abcdef",
  "timestamp": 1710638258000,
  "details": null
}
```

### 4.3 表单验证错误响应

```json
{
  "success": false,
  "code": "10101",
  "msg": "参数校验失败",
  "data": null,
  "traceId": "a1b2c3d4-e5f6-7890-1234-567890abcdef",
  "timestamp": 1710638258000,
  "details": [
    {
      "field": "username",
      "msg": "用户名不能为空",
      "rejectedValue": ""
    },
    {
      "field": "email",
      "msg": "邮箱格式不正确",
      "rejectedValue": "invalid-email"
    },
    {
      "field": "age",
      "msg": "年龄必须在 18-100 之间",
      "rejectedValue": 150
    }
  ]
}
```

### 4.4 字段说明

| 字段          | 类型      | 必填 | 说明                    |
|-------------|---------|----|-----------------------|
| `success`   | boolean | 是  | `true`=成功, `false`=失败 |
| `code`      | string  | 是  | 5位错误码，成功时为 `00000`    |
| `msg`       | string  | 是  | 人类可读的错误信息             |
| `data`      | object  | 否  | 业务数据，失败时为 `null`      |
| `traceId`   | string  | 是  | 链路追踪 ID，用于排查问题        |
| `timestamp` | long    | 是  | 响应时间戳（毫秒）             |
| `details`   | array   | 否  | 字段级错误详情列表             |

---

## 5. 使用指南

### 5.1 抛出业务异常

```java
// 方式1：使用默认消息
throw StatusCode.USER_NOT_FOUND.exception();

// 方式2：自定义消息
throw StatusCode.USER_NOT_FOUND.exception("该手机号未注册");

// 方式3：格式化消息
throw StatusCode.PARAM_VALIDATION_ERROR.exception("用户名长度必须在 %d-%d 之间", 4, 20);
```

### 5.2 Controller 返回成功响应

```java
@GetMapping("/user/{id}")
public Result<UserVO> getUser(@PathVariable Long id) {
    UserDTO user = userService.getById(id);
    UserVO vo = userConvert.dtoToVo(user);
    return Result.success(vo);
}
```

### 5.3 Service 层业务校验

```java
@Service
public class UserService {
    
    public UserDTO login(String username, String password) {
        User user = userMapper.selectByUsername(username);
        
        // 用户不存在
        if (user == null) {
            throw StatusCode.USER_NOT_FOUND.exception();
        }
        
        // 账号被锁定
        if (user.getStatus() == UserStatus.LOCKED) {
            throw StatusCode.USER_ACCOUNT_LOCKED.exception(
                "账号已被锁定，请联系客服或 %d 分钟后重试", 30
            );
        }
        
        // 密码错误
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw StatusCode.USER_PASSWORD_ERROR.exception();
        }
        
        return userConvert.entityToDto(user);
    }
}
```

### 5.4 参数校验自动处理

```java
@Data
public class UserCreateParam {
    
    @NotBlank(msg = "用户名不能为空")
    @Size(min = 4, max = 20, msg = "用户名长度必须在 4-20 之间")
    private String username;
    
    @NotBlank(msg = "邮箱不能为空")
    @Email(msg = "邮箱格式不正确")
    private String email;
    
    @Min(value = 18, msg = "年龄必须大于等于 18")
    @Max(value = 100, msg = "年龄必须小于等于 100")
    private Integer age;
}

@PostMapping("/users")
public Result<Void> createUser(@RequestBody @Valid UserCreateParam param) {
    // 如果参数校验失败，自动返回 code=10101，details 包含具体字段错误
    userService.create(param);
    return Result.success();
}
```

### 5.5 自定义业务错误码

业务模块可以在各自模块中定义专属错误码：

```java
// 订单服务模块
public enum OrderErrorCode implements ErrorCode {
    
    ORDER_NOT_FOUND("20301", "订单不存在", 404),
    ORDER_STATUS_INVALID("20302", "订单状态不正确", 400),
    ORDER_ALREADY_PAID("20303", "订单已支付", 409),
    ORDER_INSUFFICIENT_STOCK("20304", "商品库存不足", 400),
    ORDER_PAYMENT_TIMEOUT("20305", "支付超时，订单已关闭", 400);
    
    private final String code;
    private final String msg;
    private final int httpStatus;
    
    // ... constructor and getters
}
```

---

## 6. 迁移指南

### 6.1 旧错误码映射

| 旧错误码 | 旧枚举                         | 新错误码  | 新枚举                      |
|------|-----------------------------|-------|--------------------------|
| 2000 | SUCCESS                     | 00000 | SUCCESS                  |
| 4000 | LOGIN_EXCEPTION             | 20102 | USER_PASSWORD_ERROR      |
| 4001 | UNAUTHORIZED                | 10002 | UNAUTHORIZED             |
| 4003 | NOT_PERMISSION              | 10003 | FORBIDDEN                |
| 4004 | NOT_FOUND                   | 10004 | NOT_FOUND                |
| 5000 | FAIL                        | 10001 | BAD_REQUEST              |
| 5001 | PARAMETER_EXCEPTION         | 10101 | PARAM_VALIDATION_ERROR   |
| 5101 | BUSINESS_EXCEPTION          | 20001 | BUSINESS_ERROR           |
| 5102 | DAO_EXCEPTION               | 50002 | DATABASE_ERROR           |
| 5103 | VERIFICATION_CODE_EXCEPTION | 20204 | AUTH_VERIFICATION_FAILED |
| 5104 | AUTHENTICATION_EXCEPTION    | 10002 | UNAUTHORIZED             |
| 5105 | UNAUTHENTICATED_EXCEPTION   | 20203 | AUTH_ACCESS_DENIED       |
| 5107 | JWT_DECODE_EXCEPTION        | 20202 | AUTH_TOKEN_INVALID       |

### 6.2 迁移步骤

1. **Phase 1: 兼容期（1-2 周）**
    - 保留旧 `StatusCode` 枚举作为兼容层
    - 新增 `ErrorCode` 接口和新错误码枚举
    - 旧枚举调用转发到新实现

2. **Phase 2: 替换期（2-4 周）**
    - 逐步替换代码中的旧错误码引用
    - 更新单元测试
    - 文档同步更新

3. **Phase 3: 清理期（1 周）**
    - 移除旧 `StatusCode` 枚举
    - 全量回归测试

---

## 7. 最佳实践

### 7.1 DO（推荐）

```java
// ✅ 使用枚举抛出异常
throw StatusCode.USER_NOT_FOUND.exception();

// ✅ 提供有意义的错误消息
throw StatusCode.USER_ACCOUNT_LOCKED.exception(
    "账号已被锁定，解锁时间: " + unlockTime
);

// ✅ 只在 Service/Manager 层抛出业务异常
// Controller 只负责转换和返回

// ✅ 使用参数校验注解，让框架自动处理
@NotNull(msg = "用户ID不能为空")
private Long userId;
```

### 7.2 DON'T（避免）

```java
// ❌ 不要直接使用 RuntimeException
throw new RuntimeException("用户不存在");

// ❌ 不要硬编码错误码
throw new BusinessException("20101", "用户不存在");

// ❌ 不要把业务逻辑放在异常消息中
throw new Exception("Error occurred at UserService line 45");

// ❌ 不要在 Controller 中抛出业务异常
// 应该由 Service 层处理业务逻辑并抛出
```

### 7.3 错误码分配流程

1. 开发人员在模块中需要新增错误码时，先查看现有错误码表
2. 如果现有错误码不能满足，向模块负责人申请新错误码
3. 模块负责人分配错误码，并在本规范中登记
4. 新增错误码需要经过 Code Review 确认

### 7.4 监控告警

```yaml
# Prometheus 告警规则示例
groups:
  - name: error_code_alerts
    rules:
      # 系统错误率告警
      - alert: HighSystemErrorRate
        expr: rate(api_errors_total{code=~"5.*"}[5m]) > 0.1
        for: 5m
        labels:
          severity: critical
        annotations:
          summary: "系统错误率过高"
          
      # 业务错误监控
      - alert: HighBusinessErrorRate
        expr: rate(api_errors_total{code=~"2.*"}[5m]) > 0.3
        for: 10m
        labels:
          severity: warning
        annotations:
          summary: "业务错误率过高"
```

---

## 附录

### A. 错误码速查表

| 错误级别  | 代码范围        | 用途        |
|-------|-------------|-----------|
| 成功    | 00000       | 成功响应      |
| 客户端错误 | 10000-19999 | 请求参数/权限问题 |
| 业务错误  | 20000-29999 | 业务规则限制    |
| 第三方错误 | 30000-39999 | 外部服务异常    |
| 系统错误  | 50000-59999 | 系统内部故障    |

### B. 模块编码速查表

| 编码 | 模块   | 错误码范围   |
|----|------|---------|
| 00 | 通用   | x-00-xx |
| 01 | 用户服务 | x-01-xx |
| 02 | 认证授权 | x-02-xx |
| 03 | 订单服务 | x-03-xx |
| 04 | 支付服务 | x-04-xx |
| 05 | 消息服务 | x-05-xx |
| 06 | 文件服务 | x-06-xx |
| 07 | 工作流  | x-07-xx |
| 08 | 数据权限 | x-08-xx |
| 09 | 系统管理 | x-09-xx |

---

**维护者**: Carlos Framework Team  
**最后更新**: 2026-03-17
