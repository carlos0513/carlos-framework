---
name: carlos-framework-standard
description: Carlos Framework 项目编码规范，包含架构分层、命名规范、数据查询、Redis 缓存、异常处理、统一响应、错误码设计等强制规范。当用户需要编写 Carlos Framework 项目的 Java 代码、创建新模块、设计数据库表或 API 接口时使用此技能。
---

# Carlos Framework 编码规范

本 Skill 定义 Carlos Framework 项目的完整编码规范，所有代码必须严格遵守。违反强制规范将导致代码被拒绝合并。

## 关键强制规范速查

| 规范              | 要求                                              | 禁止                    |
|-----------------|-------------------------------------------------|-----------------------|
| **数据查询**        | 使用 MyBatis-Plus + mybatis-plus-join 在 Manager 层 | 严禁直接编写 SQL            |
| **Redis 缓存**    | 在 Manager 层统一实现                                 | Service 层禁止直接操作 Redis |
| **异常处理**        | 使用框架定义异常类（BusinessException/SystemException）    | 严禁使用 RuntimeException |
| **错误码**         | 使用 CommonErrorCode 或自定义 ErrorCode 枚举            | 严禁硬编码错误码              |
| **统一响应**        | 使用 ApiResponse/Result 返回                        | 严禁直接返回裸数据             |
| **Lombok**      | 使用注解生成 getter/setter                            | 严禁手写 get/set 方法       |
| **属性注入**        | 使用 @ConfigurationProperties                     | 严禁使用 @Value           |
| **Service 返回值** | 返回 DTO 或 Entity                                 | 严禁返回 VO               |
| **枚举类**         | 实现 BaseEnum，使用 @AppEnum                         | 普通枚举定义                |

## 架构分层

```
API 接口层 (api) → API 实现层 (apiimpl) → Controller 层 → Service 层 → Manager 层 → Mapper 层
```

### 分层职责

| 层级             | 职责             | 规范                                 |
|----------------|----------------|------------------------------------|
| **Controller** | 接收请求、参数校验、对象转换 | 调用 Service/Manager，不处理业务逻辑         |
| **Service**    | 业务逻辑串联         | 通过 Manager 获取数据，**严禁直接引用 Mapper**  |
| **Manager**    | 数据查询封装         | 继承 BaseService，实现 CRUD，统一缓存        |
| **Mapper**     | 数据访问           | MyBatis 实现，复杂查询用 mybatis-plus-join |

### 领域模型

| 模型       | 后缀               | 包路径              | 说明                          |
|----------|------------------|------------------|-----------------------------|
| Param    | `XxxCreateParam` | `pojo.param`     | 前端参数（细分 Create/Update/Page） |
| DTO      | `XxxDTO`         | `pojo.dto`       | 服务层传输对象                     |
| VO       | `XxxVO`          | `pojo.vo`        | 视图对象，**仅 Controller 使用**    |
| Entity   | `Xxx`            | `pojo.entity`    | 数据库实体                       |
| AO       | `XxxAO`          | `api.pojo.ao`    | API 响应对象                    |
| ApiParam | `ApiXxxParam`    | `api.pojo.param` | API 参数对象                    |
| Enum     | `XxxEnum`        | `pojo.enums`     | 业务枚举                        |

## 统一响应规范（强制）

### 响应结构

所有 API 必须返回统一的响应格式：

```json
{
  "success": true,
  "code": "00000",
  "msg": "操作成功",
  "data": { },
  "timestamp": 1710638258000,
  "details": null
}
```

| 字段          | 类型      | 必填 | 说明                    |
|-------------|---------|----|-----------------------|
| `success`   | boolean | 是  | `true`=成功, `false`=失败 |
| `code`      | string  | 是  | 5位错误码，成功为 `00000`     |
| `msg`       | string  | 是  | 人类可读的错误信息             |
| `data`      | object  | 否  | 业务数据，失败时为 `null`      |
| `timestamp` | long    | 是  | 响应时间戳（毫秒）             |
| `details`   | array   | 否  | 字段级错误详情（校验失败时返回）      |

### Controller 返回规范

```java
// ✅ 正确：使用 ApiResponse 成功返回
@GetMapping("/user/{id}")
public ApiResponse<UserVO> getUser(@PathVariable Long id) {
    UserDTO user = userService.getById(id);
    UserVO vo = userConvert.dtoToVo(user);
    return Result.success(vo);
}

// ✅ 正确：使用 ApiResponse 错误返回
@GetMapping("/user/{id}")
public ApiResponse<UserVO> getUser(@PathVariable Long id) {
    if (id <= 0) {
        return ApiResponse.error(CommonErrorCode.PARAM_VALIDATION_ERROR, "用户ID必须大于0");
    }
    // ...
}

// ✅ 兼容：使用 Result 类（内部使用 ApiResponse）
@GetMapping("/user/{id}")
public Result<UserVO> getUser(@PathVariable Long id) {
    UserDTO user = userService.getById(id);
    return Result.ok(userConvert.dtoToVo(user));
}

// ❌ 错误：直接返回裸数据
@GetMapping("/user/{id}")
public UserVO getUser(@PathVariable Long id) {  // 严禁！
    // ...
}

// ❌ 错误：返回 Map
@GetMapping("/user/{id}")
public Map<String, Object> getUser(@PathVariable Long id) {  // 严禁！
    // ...
}
```

### ApiResponse 工厂方法

```java
// 成功响应
Result.success();                              // 无数据
Result.success(data);                          // 带数据
Result.success(data, "自定义成功消息");           // 带数据和消息

// 错误响应
ApiResponse.error(CommonErrorCode.USER_NOT_FOUND);                    // 使用默认消息
ApiResponse.error(CommonErrorCode.USER_NOT_FOUND, "该用户不存在");      // 自定义消息
ApiResponse.validationError(details);                                // 参数校验错误
```

## 错误码规范（强制）

### 错误码格式

错误码采用 **5位数字字符串** 格式：`A-BB-CC`

```
A    - BB   - CC
|       |       |
|       |       └── 具体错误序号（2位）
|       └────────── 服务/模块编码（2位）
└────────────────── 错误级别（1位）
```

### 错误级别 (A)

| 级别    | 代码 | 说明       | HTTP 状态码范围 | 示例             |
|-------|----|----------|------------|----------------|
| 成功    | 0  | 操作成功     | 200        | `00000`        |
| 客户端错误 | 1  | 用户请求参数问题 | 400-499    | `10001` 参数错误   |
| 业务错误  | 2  | 业务规则限制   | 400-499    | `20101` 用户不存在  |
| 第三方错误 | 3  | 外部服务调用失败 | 500-599    | `30101` 短信服务异常 |
| 系统错误  | 5  | 系统内部故障   | 500-599    | `50001` 内部错误   |

### 模块编码 (BB)

| 编码    | 模块   | 说明           | 错误码范围   |
|-------|------|--------------|---------|
| 00    | 通用   | 全局通用错误       | x-00-xx |
| 01    | 用户服务 | 用户相关操作       | x-01-xx |
| 02    | 认证授权 | 登录、权限、Token  | x-02-xx |
| 03    | 订单服务 | 订单相关操作       | x-03-xx |
| 04    | 支付服务 | 支付相关操作       | x-04-xx |
| 05    | 消息服务 | 通知、短信、邮件     | x-05-xx |
| 06    | 文件服务 | 上传、存储、OSS    | x-06-xx |
| 07    | 工作流  | Flowable 工作流 | x-07-xx |
| 08    | 数据权限 | 数据范围控制       | x-08-xx |
| 09    | 系统管理 | 字典、配置、菜单     | x-09-xx |
| 10-99 | 预留   | 业务扩展使用       | x-10-xx |

### 常用错误码速查

| 错误码     | 消息        | HTTP | 使用场景          |
|---------|-----------|------|---------------|
| `00000` | 操作成功      | 200  | 通用成功          |
| `10001` | 请求参数错误    | 400  | 通用参数错误        |
| `10002` | 未授权，请先登录  | 401  | 未登录或 Token 过期 |
| `10003` | 禁止访问，权限不足 | 403  | 无权限访问         |
| `10004` | 请求的资源不存在  | 404  | 资源不存在         |
| `10101` | 参数校验失败    | 400  | 字段校验失败        |
| `20101` | 用户不存在     | 404  | 用户 ID 不存在     |
| `20102` | 用户名或密码错误  | 400  | 登录验证失败        |
| `20201` | 登录凭证已过期   | 401  | Token 过期      |
| `50001` | 系统内部错误    | 500  | 未预期异常         |
| `50002` | 数据库操作异常   | 500  | 数据库错误         |

### 异常抛出规范

```java
// ✅ 正确：使用 CommonErrorCode 抛出业务异常
throw CommonErrorCode.USER_NOT_FOUND.exception();
throw CommonErrorCode.USER_ACCOUNT_LOCKED.exception("账号已被锁定，30分钟后重试");
throw CommonErrorCode.USER_NOT_FOUND.exception("该手机号未注册");

// ✅ 正确：自定义错误码（业务模块）
@Getter
@RequiredArgsConstructor
public enum OrderErrorCode implements ErrorCode {
    ORDER_NOT_FOUND("20301", "订单不存在", 404),
    ORDER_STATUS_INVALID("20302", "订单状态不正确", 400);
    
    private final String code;
    private final String message;
    private final int httpStatus;
}

throw OrderErrorCode.ORDER_NOT_FOUND.exception();

// ✅ 正确：使用 BusinessException（带 HTTP 状态码）
throw new BusinessException(CommonErrorCode.USER_NOT_FOUND);
throw new BusinessException(CommonErrorCode.USER_NOT_FOUND, "该用户不存在", 404);

// ✅ 正确：使用 SystemException
throw new SystemException(CommonErrorCode.DATABASE_ERROR, "连接超时");
throw new SystemException(CommonErrorCode.SERVICE_UNAVAILABLE, "限流中", 503);

// ❌ 错误：硬编码错误码
throw new BusinessException("20101", "用户不存在");  // 严禁！

// ❌ 错误：使用原生异常
throw new RuntimeException("用户不存在");  // 严禁！
```

## 异常处理规范（强制）

### 严禁使用原生异常

**严禁**使用 `RuntimeException` 或 `Exception`！

### 可用异常类

| 异常类                  | 场景          | 默认 HTTP 状态码 | 示例                                                                   |
|----------------------|-------------|-------------|----------------------------------------------------------------------|
| `BusinessException`  | 可预期业务错误     | 400         | `throw CommonErrorCode.USER_NOT_FOUND.exception()`                   |
| `SystemException`    | 不可预期系统错误    | 500         | `throw new SystemException(CommonErrorCode.CACHE_ERROR, "Redis 超时")` |
| `ServiceException`   | 业务异常（旧版，兼容） | 400         | `throw new ServiceException("业务错误")`                                 |
| `DaoException`       | 数据库操作异常     | 500         | `throw new DaoException(e)`                                          |
| `RestException`      | REST 接口层异常  | 400         | `throw new RestException("参数错误")`                                    |
| `ComponentException` | 组件异常        | 500         | `throw new ComponentException("初始化失败")`                              |

### 异常 HTTP 状态码

所有异常类都支持自定义 HTTP 状态码：

```java
// 从 ErrorCode 自动获取 httpStatus
throw new BusinessException(CommonErrorCode.NOT_FOUND);  // httpStatus = 404

// 覆盖 httpStatus
throw new BusinessException(CommonErrorCode.NOT_FOUND, "自定义消息", 400);  // httpStatus = 400

// 仅使用消息和状态码
throw new ServiceException("参数错误", 400);
throw new DaoException("数据库超时", 504);

// 带原因的异常
throw new RestException("请求无效", 400, cause);
throw new SystemException("服务不可用", 503, cause);
```

### 模块自定义异常

按业务场景定义具体异常类：

```java
// ✅ 推荐
public class UserNotFoundException extends BusinessException {
    public UserNotFoundException(String userId) {
        super(CommonErrorCode.USER_NOT_FOUND, "用户不存在：" + userId);
    }
}

throw new UserNotFoundException(userId);

// ❌ 不推荐
throw new OrgModuleException("用户不存在");
```

## 数据查询规范（强制）

### 核心原则

- **严禁在代码中直接编写 SQL 语句**（包括原生 SQL、XML Mapper）
- **必须使用 MyBatis-Plus + mybatis-plus-join 在 Manager 层实现**
- 仅当 MyBatis-Plus 无法实现时才允许自定义 SQL

### 正确示例

```java
@Service
public class OrgUserManagerImpl extends BaseServiceImpl<OrgUserMapper, OrgUser> 
        implements OrgUserManager {
    
    @Override
    public Paging<OrgUserDTO> getPage(OrgUserPageParam param) {
        MPJLambdaWrapper<OrgUser> wrapper = new MPJLambdaWrapper<OrgUser>()
            .selectAll(OrgUser.class)
            .selectAs(OrgDept::getDeptName, OrgUserDTO::getDeptName)
            .leftJoin(OrgDept.class, OrgDept::getId, OrgUser::getDeptId)
            .eq(param.getStatus() != null, OrgUser::getStatus, param.getStatus())
            .like(StringUtils.isNotBlank(param.getUserName()), 
                  OrgUser::getUserName, param.getUserName());
        
        return selectJoinListPage(param.toPage(), wrapper, OrgUserDTO.class);
    }
}
```

### 禁止事项

```java
// ❌ 错误：直接写 SQL
@Select("SELECT * FROM org_user WHERE status = #{status}")
List<OrgUser> selectByStatus(Integer status);

// ❌ 错误：Service 直接操作 Mapper
@Service
public class OrgUserService {
    @Autowired  // ❌ 应该通过 Manager
    private OrgUserMapper userMapper;
}
```

## Redis 缓存规范（强制）

### 核心原则

- **所有 Redis 操作必须在 Manager 层实现**
- **Service 层禁止直接操作 Redis**
- 缓存注解统一应用在 Manager 层

### 缓存 Key 命名

格式：`{模块}:{业务}:{标识}`

| 类型 | 格式                              | 示例                       |
|----|---------------------------------|--------------------------|
| 单条 | `{module}:{entity}:{id}`        | `org:user:1001`          |
| 列表 | `{module}:{entity}:list:{cond}` | `org:user:list:status:1` |
| 分页 | `{module}:{entity}:page:{cond}` | `org:user:page:1:10`     |

### 正确示例

```java
@Service
public class OrgUserManagerImpl extends BaseServiceImpl<OrgUserMapper, OrgUser> {
    
    @Override
    @Cacheable(value = "org:user", key = "#id")
    public OrgUserDTO getDtoById(Serializable id) {
        // 查询逻辑
    }
    
    @Override
    @CacheEvict(value = "org:user", key = "#param.id")
    public boolean modify(OrgUserUpdateParam param) {
        // 更新逻辑
    }
}
```

### 禁止事项

```java
@Service
public class OrgUserServiceImpl {
    @Autowired
    private StringRedisTemplate redisTemplate;  // ❌ 错误
    
    // ❌ 错误：Service 直接操作 Redis
    public OrgUserDTO getUser(Long id) {
        String key = "user:" + id;
        return redisTemplate.opsForValue().get(key);
    }
}
```

## Lombok 使用规范（强制）

### 核心原则

**所有 POJO 类必须使用 Lombok 注解，禁止手写 get/set 方法。**

```java
// ❌ 错误
public class UserDTO {
    private Long id;
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
}

// ✅ 正确
@Data
public class UserDTO {
    private Long id;
    private String name;
}
```

### 常用注解

| 注解                         | 用途                                             | 场景                 |
|----------------------------|------------------------------------------------|--------------------|
| `@Data`                    | getter + setter + toString + equals + hashCode | POJO、DTO、VO、Param  |
| `@Getter`                  | 只生成 getter                                     | 只读对象               |
| `@NoArgsConstructor`       | 无参构造器                                          | 需要无参构造             |
| `@AllArgsConstructor`      | 全参构造器                                          | 配合 @Builder        |
| `@RequiredArgsConstructor` | final 字段构造器                                    | Service、Controller |
| `@Builder`                 | Builder 模式                                     | 构建复杂对象             |
| `@Slf4j`                   | 日志对象                                           | 需要记录日志             |

## Spring Boot 属性注入规范（强制）

**禁止使用 `@Value`，统一使用 `@ConfigurationProperties`。**

```java
// ❌ 错误
@Value("${carlos.redis.host}")
private String redisHost;

// ✅ 正确
@ConfigurationProperties(prefix = "carlos.redis")
@Data
public class RedisProperties {
    private String host;
    private int port = 6379;
}

// 注入使用
@Service
@RequiredArgsConstructor
public class RedisService {
    private final RedisProperties properties;
}
```

## 枚举类规范（强制）

### 标准模板

```java
package com.carlos.{module}.pojo.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.carlos.core.enums.AppEnum;
import com.carlos.core.enums.BaseEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AppEnum(code = "OrgUserState")  // code 值唯一
@Getter
@AllArgsConstructor
public enum OrgUserStateEnum implements BaseEnum {

    DISABLE(0, "禁用"),
    ENABLE(1, "启用"),
    LOCK(2, "锁定");

    @EnumValue
    private final Integer code;    // 数据库存储值
    private final String desc;     // 描述

    @Override
    public Integer getCode() {
        return code;
    }

    @Override
    public String getDesc() {
        return desc;
    }
}
```

### 必备要素

- `@AppEnum(code = "xxx")` - 用于反射扫描，code 值在应用中唯一
- `@Getter`, `@AllArgsConstructor` - Lombok 注解
- `@EnumValue` - 标记数据库存储字段
- 实现 `BaseEnum` 接口

### 使用方式

```java
// Entity 中使用枚举
public class OrgUser {
    private OrgUserStateEnum state;  // 自动映射数据库 Integer
}
```

## MapStruct 对象转换规范

### 转换规则

- **Controller 层**：Param → DTO（入参）、DTO → VO（出参）
- 转换接口位于 `convert/` 目录

```java
@Mapper(componentModel = "spring")
public interface OrgUserConvert {
    OrgUserConvert INSTANCE = Mappers.getMapper(OrgUserConvert.class);
    
    OrgUserDTO toDTO(OrgUserCreateParam param);
    
    @Mapping(target = "deptName", source = "dept.deptName")
    OrgUserVO toVO(OrgUserDTO dto);
    
    List<OrgUserVO> toVOList(List<OrgUserDTO> dtoList);
}
```

## Service 与 Controller 数据转换（强制）

### Service 层禁止返回 VO

**Service 层只能返回 DTO 或 Entity，Controller 层转换为 VO。**

```java
// ❌ 错误
@Service
public class OrgUserService {
    public OrgUserVO getUser(Long id) {  // 错误！
        // ...
    }
}

// ✅ 正确
@Service
public class OrgUserService {
    public OrgUserDTO getUser(Long id) {  // 正确！
        // ...
    }
}

@RestController
public class OrgUserController {
    public ApiResponse<OrgUserVO> getUser(Long id) {
        OrgUserDTO dto = userService.getUser(id);
        return Result.success(OrgUserConvert.INSTANCE.toVO(dto));
    }
}
```

### 主键类型规范

- Entity 和 DTO 中使用 `Long` 类型
- 方法签名中使用 `Serializable` 类型
- 转换使用 `cn.hutool.core.convert.Convert`

```java
public OrgUserDTO getById(Serializable id) {
    Long userId = Convert.toLong(id);
    // ...
}
```

### 逻辑删除规范

**无需显式设置 deleted 字段，框架自动处理。**

```java
// ❌ 错误
public boolean delete(Serializable id) {
    OrgUser entity = new OrgUser();
    entity.setId(Convert.toLong(id));
    entity.setDeleted(true);  // 不需要！
    return updateById(entity);
}

// ✅ 正确
public boolean delete(Serializable id) {
    return removeById(id);  // 框架自动处理
}
```

## 命名规范

### 通用规则

| 类型    | 规范             | 示例                               |
|-------|----------------|----------------------------------|
| 类名    | UpperCamelCase | `UserService`, `AbstractService` |
| 方法/变量 | lowerCamelCase | `getUserById()`, `userName`      |
| 常量    | 全大写下划线         | `MAX_STOCK_COUNT`                |
| 包名    | 全小写            | `com.carlos.user.service`        |
| 布尔变量  | 不加 is 前缀       | `deleted`（非 `isDeleted`）         |

### 模块命名

| 模块类型    | 命名格式                                    | 示例                                 |
|---------|-----------------------------------------|------------------------------------|
| Commons | `carlos-{function}`                     | `carlos-utils`                     |
| Starter | `carlos-spring-boot-starter-{function}` | `carlos-spring-boot-starter-redis` |

## MySQL 数据库规约

### 建表规范

1. **命名**：
    - 是/否字段：`is_xxx` + `tinyint unsigned`
    - 小写字母或数字，禁用保留字

2. **索引命名**：
    - 主键：`pk_字段名`
    - 唯一：`uk_字段名`
    - 普通：`idx_字段名`

3. **数据类型**：
    - 小数用 `decimal`，禁止 `float`/`double`
    - `varchar` 不超过 5000，超过用 `text`

4. **必备字段**：

| 字段名           | 类型               | 说明                      |
|---------------|------------------|-------------------------|
| `id`          | char(32)         | 主键，`IdUtils.date32Id()` |
| `create_by`   | varchar(32)      | 创建人                     |
| `create_time` | datetime         | 创建时间                    |
| `update_by`   | varchar(32)      | 更新人                     |
| `update_time` | datetime         | 更新时间                    |
| `is_deleted`  | tinyint unsigned | 逻辑删除                    |

### SQL 规范

- 使用 `#{ }`，禁止 `${ }` 防止 SQL 注入
- **禁止使用存储过程**
- 不使用外键与级联

## 代码检查清单（提交前必查）

### 安全检查

- [ ] 无硬编码凭据
- [ ] 用户输入已验证
- [ ] 预防 SQL 注入

### 规范检查

- [ ] **使用了 MyBatis-Plus 进行查询（严禁直接写 SQL）**
- [ ] **Redis 缓存在 Manager 层实现（Service 不直接操作）**
- [ ] **使用 ApiResponse/Result 统一响应（不返回裸数据）**
- [ ] **使用 CommonErrorCode 抛出异常（不硬编码错误码）**
- [ ] 使用了 Lombok（无手写 get/set）
- [ ] 使用了 `@ConfigurationProperties`（无 `@Value`）
- [ ] Service 层返回 DTO（不返回 VO）
- [ ] 异常使用框架定义类（无 `RuntimeException`）
- [ ] 枚举类实现 `BaseEnum` 并使用 `@AppEnum`

### 质量检查

- [ ] 函数 < 50 行
- [ ] 文件 < 800 行
- [ ] 无深度嵌套（< 4 层）
- [ ] 使用日志框架（无 `System.out`）

## 参考文档

- `AGENTS.md` - 项目概述和架构
- `CLAUDE.md` - 开发指南
- `carlos-spring-boot/carlos-spring-boot-core/ERROR_CODE_SPEC.md` - 错误码设计规范
- `README.md` - 快速参考
