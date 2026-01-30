# carlos-magicapi

Magic API集成组件，提供低代码接口开发能力，支持通过可视化界面快速开发API接口。

## 功能特性

- **可视化开发**: 通过Web界面编写接口逻辑
- **动态编译**: 无需重启应用即可生效
- **数据库操作**: 内置数据库操作DSL
- **接口测试**: 内置接口测试工具
- **版本管理**: 支持接口版本控制和回滚
- **权限控制**: 支持接口级别的权限控制

## 快速开始

### Maven依赖

```xml
<dependency>
    <groupId>com.carlos</groupId>
    <artifactId>carlos-magicapi</artifactId>
    <version>${carlos.version}</version>
</dependency>
```

### 配置示例

```yaml
magic-api:
  # UI访问路径
  web: /magic
  # 接口前缀
  prefix: /api
  # 存储方式
  resource:
    type: database
    table-name: magic_api_file
    readonly: false
  # 安全配置
  security:
    username: ${MAGIC_API_USERNAME:admin}
    password: ${MAGIC_API_PASSWORD:change-me}
  # 响应格式
  response: |-
    {
      code: code,
      message: message,
      data
    }
  response-code:
    success: 2000
    invalid: 4000
    exception: 5000
  # 其他配置
  show-sql: true
  support-cross-domain: true
  throw-exception: false
```

## 使用示例

### 访问管理界面

启动应用后，访问: `http://localhost:8080/magic`

使用配置的用户名密码登录。

### 编写接口

1. 点击"新建接口"
2. 填写接口路径，如: `/user/list`
3. 编写脚本:

```javascript
return db.select('select * from user where status = ?', 1);
```

4. 点击"保存"并"测试"
5. 接口立即生效，可通过 `/api/user/list` 访问

### 数据库操作

```javascript
// 查询
var users = db.select('select * from user where age > ?', 18);

// 插入
var result = db.insert('user', {
    name: '张三',
    age: 25,
    email: 'zhangsan@example.com'
});

// 更新
db.update('user', {
    name: '李四'
}, {
    id: 1
});

// 删除
db.delete('user', {
    id: 1
});

// 分页查询
return db.page('select * from user where status = ?', [1]);
```

### 调用其他接口

```javascript
// 调用内部接口
var result = magic.call('/user/info', {
    id: 1
});

// HTTP请求
var response = http.get('https://api.example.com/data');
```

## 内置对象

| 对象       | 说明            |
|----------|---------------|
| db       | 数据库操作对象       |
| http     | HTTP请求对象      |
| magic    | Magic API内部调用 |
| request  | HTTP请求信息      |
| response | HTTP响应对象      |
| session  | Session对象     |
| cookie   | Cookie操作      |
| env      | 环境变量          |

## 依赖模块

- **carlos-core**: 核心基础功能
- **carlos-mybatis**: 数据库操作支持
- **Magic API**: 低代码接口开发框架

## 注意事项

- **生产环境必须修改默认密码**
- 建议生产环境设置 `readonly: true`，禁止在线修改
- 接口脚本应经过充分测试后再发布
- 注意SQL注入风险，使用参数化查询
- 定期备份接口配置数据

## 版本要求

- JDK 17+
- Spring Boot 3.x
- Magic API 2.x
