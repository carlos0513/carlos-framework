# Carlos Framework Admin API 接口文档

> **版本**: v1.0  
> **基地址**: `http://localhost:8080`  
> **协议**: HTTP/HTTPS  
> **数据格式**: JSON

---

## 目录

1. [通用规范](#1-通用规范)
2. [认证模块](#2-认证模块)
3. [路由模块](#3-路由模块)
4. [用户管理](#4-用户管理)
5. [角色管理](#5-角色管理)
6. [菜单管理](#6-菜单管理)
7. [错误码说明](#7-错误码说明)

---

## 1. 通用规范

### 1.1 请求规范

**请求头**:

```http
Content-Type: application/json
Authorization: Bearer {access_token}
```

**请求方法**:

- `GET` - 获取资源
- `POST` - 创建资源
- `PUT` - 更新资源
- `DELETE` - 删除资源

### 1.2 响应格式

**成功响应**:

```json
{
  "code": "0000",
  "msg": "操作成功",
  "data": { ... }
}
```

**错误响应**:

```json
{
  "code": "5000",
  "msg": "操作失败",
  "data": null
}
```

### 1.3 分页参数

| 参数      | 类型      | 必填 | 说明         |
|---------|---------|----|------------|
| current | integer | 否  | 当前页码，默认 1  |
| size    | integer | 否  | 每页条数，默认 10 |

**分页响应**:

```json
{
  "code": "0000",
  "msg": "操作成功",
  "data": {
    "records": [ ... ],
    "current": 1,
    "size": 10,
    "total": 100
  }
}
```

---

## 2. 认证模块

### 2.1 用户登录

**接口说明**: 用户名密码登录

```http
POST /auth/login
Content-Type: application/json
```

**请求参数**:

| 参数       | 类型     | 必填 | 说明         |
|----------|--------|----|------------|
| userName | string | 是  | 用户名        |
| password | string | 是  | 密码（SM4加密后） |

**请求示例**:

```json
{
  "userName": "admin",
  "password": "encrypted_password"
}
```

**响应参数**:

| 参数           | 类型      | 说明      |
|--------------|---------|---------|
| token        | string  | 访问令牌    |
| refreshToken | string  | 刷新令牌    |
| expiresIn    | integer | 过期时间（秒） |

**响应示例**:

```json
{
  "code": "0000",
  "msg": "登录成功",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiIs...",
    "refreshToken": "eyJhbGciOiJIUzI1NiIs...",
    "expiresIn": 7200
  }
}
```

---

### 2.2 获取用户信息

**接口说明**: 获取当前登录用户详细信息

```http
GET /auth/getUserInfo
Authorization: Bearer {token}
```

**响应参数**:

| 参数       | 类型     | 说明       |
|----------|--------|----------|
| userId   | string | 用户ID     |
| userName | string | 用户名      |
| nickName | string | 昵称       |
| avatar   | string | 头像URL    |
| roles    | array  | 角色编码列表   |
| buttons  | array  | 按钮权限编码列表 |

**响应示例**:

```json
{
  "code": "0000",
  "msg": "操作成功",
  "data": {
    "userId": "1",
    "userName": "admin",
    "nickName": "管理员",
    "avatar": "https://example.com/avatar.png",
    "roles": ["R_SUPER", "R_ADMIN"],
    "buttons": ["btn:user:add", "btn:user:edit", "btn:user:delete"]
  }
}
```

---

### 2.3 刷新 Token

**接口说明**: 使用 refreshToken 获取新的访问令牌

```http
POST /auth/refreshToken
Content-Type: application/json
```

**请求参数**:

| 参数           | 类型     | 必填 | 说明   |
|--------------|--------|----|------|
| refreshToken | string | 是  | 刷新令牌 |

**响应示例**:

```json
{
  "code": "0000",
  "msg": "刷新成功",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiIs...",
    "refreshToken": "eyJhbGciOiJIUzI1NiIs...",
    "expiresIn": 7200
  }
}
```

---

### 2.4 用户登出

**接口说明**: 退出登录，使当前 Token 失效

```http
POST /auth/logout
Authorization: Bearer {token}
```

**响应示例**:

```json
{
  "code": "0000",
  "msg": "登出成功",
  "data": null
}
```

---

## 3. 路由模块

### 3.1 获取常量路由

**接口说明**: 获取无需权限的路由（登录页、404等）

```http
GET /route/getConstantRoutes
```

**响应参数**:

路由对象数组：

| 参数        | 类型     | 说明    |
|-----------|--------|-------|
| id        | string | 路由ID  |
| name      | string | 路由名称  |
| path      | string | 路由路径  |
| component | string | 组件路径  |
| meta      | object | 路由元数据 |
| children  | array  | 子路由   |

**Meta 对象**:

| 参数         | 类型      | 说明       |
|------------|---------|----------|
| title      | string  | 标题       |
| icon       | string  | 图标       |
| i18nKey    | string  | 国际化键     |
| keepAlive  | boolean | 是否缓存     |
| constant   | boolean | 是否常量路由   |
| order      | integer | 排序       |
| hideInMenu | boolean | 是否在菜单中隐藏 |

**响应示例**:

```json
{
  "code": "0000",
  "msg": "操作成功",
  "data": [
    {
      "id": "1",
      "name": "login",
      "path": "/login",
      "component": "layout.blank",
      "meta": {
        "title": "登录",
        "constant": true
      }
    },
    {
      "id": "2",
      "name": "404",
      "path": "/404",
      "component": "view.404",
      "meta": {
        "title": "404",
        "constant": true,
        "hideInMenu": true
      }
    }
  ]
}
```

---

### 3.2 获取用户路由

**接口说明**: 根据用户角色获取动态路由

```http
GET /route/getUserRoutes
Authorization: Bearer {token}
```

**响应参数**:

| 参数     | 类型     | 说明     |
|--------|--------|--------|
| routes | array  | 路由列表   |
| home   | string | 首页路由名称 |

**响应示例**:

```json
{
  "code": "0000",
  "msg": "操作成功",
  "data": {
    "routes": [
      {
        "id": "10",
        "name": "dashboard",
        "path": "/dashboard",
        "component": "view.dashboard.index",
        "meta": {
          "title": "仪表盘",
          "icon": "mdi:home",
          "order": 1
        }
      },
      {
        "id": "20",
        "name": "manage",
        "path": "/manage",
        "component": "layout.base",
        "meta": {
          "title": "系统管理",
          "icon": "mdi:cog",
          "order": 2
        },
        "children": [
          {
            "id": "21",
            "name": "manage_user",
            "path": "/manage/user",
            "component": "view.manage.user.index",
            "meta": {
              "title": "用户管理",
              "icon": "mdi:account-group",
              "order": 1
            }
          }
        ]
      }
    ],
    "home": "dashboard"
  }
}
```

---

### 3.3 检查路由是否存在

**接口说明**: 检查指定路由名称是否存在

```http
GET /route/isRouteExist?routeName={routeName}
```

**请求参数**:

| 参数        | 类型     | 必填 | 说明   |
|-----------|--------|----|------|
| routeName | string | 是  | 路由名称 |

**响应示例**:

```json
{
  "code": "0000",
  "msg": "操作成功",
  "data": true
}
```

---

## 4. 用户管理

### 4.1 获取用户列表

**接口说明**: 分页查询用户列表

```http
GET /systemManage/getUserList
Authorization: Bearer {token}
```

**请求参数**:

| 参数         | 类型      | 必填 | 说明           |
|------------|---------|----|--------------|
| current    | integer | 否  | 当前页码，默认 1    |
| size       | integer | 否  | 每页条数，默认 10   |
| userName   | string  | 否  | 用户名（模糊查询）    |
| userGender | string  | 否  | 性别：1=男，2=女   |
| nickName   | string  | 否  | 昵称（模糊查询）     |
| userPhone  | string  | 否  | 手机号          |
| userEmail  | string  | 否  | 邮箱           |
| status     | string  | 否  | 状态：1=启用，2=禁用 |

**响应参数**:

用户对象：

| 参数         | 类型      | 说明     |
|------------|---------|--------|
| id         | integer | 用户ID   |
| userName   | string  | 用户名    |
| userGender | string  | 性别     |
| nickName   | string  | 昵称     |
| userPhone  | string  | 手机号    |
| userEmail  | string  | 邮箱     |
| userRoles  | array   | 角色编码列表 |
| status     | string  | 状态     |
| createBy   | string  | 创建人    |
| createTime | string  | 创建时间   |
| updateBy   | string  | 更新人    |
| updateTime | string  | 更新时间   |

**响应示例**:

```json
{
  "code": "0000",
  "msg": "操作成功",
  "data": {
    "records": [
      {
        "id": 1,
        "userName": "admin",
        "userGender": "1",
        "nickName": "管理员",
        "userPhone": "13800138000",
        "userEmail": "admin@example.com",
        "userRoles": ["R_SUPER"],
        "status": "1",
        "createBy": "system",
        "createTime": "2024-01-01 00:00:00",
        "updateBy": "admin",
        "updateTime": "2024-01-15 10:30:00"
      }
    ],
    "current": 1,
    "size": 10,
    "total": 1
  }
}
```

---

### 4.2 新增用户

**接口说明**: 创建新用户

```http
POST /systemManage/addUser
Authorization: Bearer {token}
Content-Type: application/json
```

**请求参数**:

| 参数         | 类型     | 必填 | 说明          |
|------------|--------|----|-------------|
| userName   | string | 是  | 用户名（2-20字符） |
| password   | string | 是  | 密码（6-20字符）  |
| userGender | string | 否  | 性别：1=男，2=女  |
| nickName   | string | 否  | 昵称          |
| userPhone  | string | 否  | 手机号         |
| userEmail  | string | 否  | 邮箱          |
| roleIds    | array  | 否  | 角色ID列表      |

**请求示例**:

```json
{
  "userName": "zhangsan",
  "password": "encrypted_password",
  "userGender": "1",
  "nickName": "张三",
  "userPhone": "13800138001",
  "userEmail": "zhangsan@example.com",
  "roleIds": [2, 3]
}
```

**响应示例**:

```json
{
  "code": "0000",
  "msg": "新增成功",
  "data": true
}
```

---

### 4.3 编辑用户

**接口说明**: 更新用户信息

```http
PUT /systemManage/updateUser
Authorization: Bearer {token}
Content-Type: application/json
```

**请求参数**:

| 参数         | 类型      | 必填 | 说明     |
|------------|---------|----|--------|
| id         | integer | 是  | 用户ID   |
| userGender | string  | 否  | 性别     |
| nickName   | string  | 否  | 昵称     |
| userPhone  | string  | 否  | 手机号    |
| userEmail  | string  | 否  | 邮箱     |
| status     | string  | 否  | 状态     |
| roleIds    | array   | 否  | 角色ID列表 |

**请求示例**:

```json
{
  "id": 1,
  "userGender": "1",
  "nickName": "管理员（已修改）",
  "userPhone": "13800138000",
  "userEmail": "admin@example.com",
  "status": "1",
  "roleIds": [1, 2]
}
```

**响应示例**:

```json
{
  "code": "0000",
  "msg": "更新成功",
  "data": true
}
```

---

### 4.4 删除用户

**接口说明**: 删除指定用户

```http
DELETE /systemManage/deleteUser/{id}
Authorization: Bearer {token}
```

**路径参数**:

| 参数 | 类型      | 必填 | 说明   |
|----|---------|----|------|
| id | integer | 是  | 用户ID |

**响应示例**:

```json
{
  "code": "0000",
  "msg": "删除成功",
  "data": true
}
```

---

## 5. 角色管理

### 5.1 获取角色列表

**接口说明**: 分页查询角色列表

```http
GET /systemManage/getRoleList
Authorization: Bearer {token}
```

**请求参数**:

| 参数       | 类型      | 必填 | 说明       |
|----------|---------|----|----------|
| current  | integer | 否  | 当前页码     |
| size     | integer | 否  | 每页条数     |
| roleName | string  | 否  | 角色名称（模糊） |
| roleCode | string  | 否  | 角色编码（模糊） |
| status   | string  | 否  | 状态       |

**响应参数**:

角色对象：

| 参数         | 类型      | 说明   |
|------------|---------|------|
| id         | integer | 角色ID |
| roleName   | string  | 角色名称 |
| roleCode   | string  | 角色编码 |
| roleDesc   | string  | 角色描述 |
| status     | string  | 状态   |
| createTime | string  | 创建时间 |

**响应示例**:

```json
{
  "code": "0000",
  "msg": "操作成功",
  "data": {
    "records": [
      {
        "id": 1,
        "roleName": "超级管理员",
        "roleCode": "R_SUPER",
        "roleDesc": "系统超级管理员，拥有所有权限",
        "status": "1",
        "createTime": "2024-01-01 00:00:00"
      },
      {
        "id": 2,
        "roleName": "普通管理员",
        "roleCode": "R_ADMIN",
        "roleDesc": "普通管理员，拥有部分权限",
        "status": "1",
        "createTime": "2024-01-01 00:00:00"
      }
    ],
    "current": 1,
    "size": 10,
    "total": 2
  }
}
```

---

### 5.2 获取所有角色

**接口说明**: 获取所有启用状态的角色（用于下拉选择）

```http
GET /systemManage/getAllRoles
Authorization: Bearer {token}
```

**响应示例**:

```json
{
  "code": "0000",
  "msg": "操作成功",
  "data": [
    {
      "id": 1,
      "roleName": "超级管理员",
      "roleCode": "R_SUPER"
    },
    {
      "id": 2,
      "roleName": "普通管理员",
      "roleCode": "R_ADMIN"
    }
  ]
}
```

---

### 5.3 新增角色

**接口说明**: 创建新角色

```http
POST /systemManage/addRole
Authorization: Bearer {token}
Content-Type: application/json
```

**请求参数**:

| 参数       | 类型     | 必填 | 说明       |
|----------|--------|----|----------|
| roleName | string | 是  | 角色名称     |
| roleCode | string | 是  | 角色编码（唯一） |
| roleDesc | string | 否  | 角色描述     |

**请求示例**:

```json
{
  "roleName": "运营人员",
  "roleCode": "R_OPERATOR",
  "roleDesc": "负责日常运营管理"
}
```

---

### 5.4 编辑角色

**接口说明**: 更新角色信息

```http
PUT /systemManage/updateRole
Authorization: Bearer {token}
Content-Type: application/json
```

**请求参数**:

| 参数       | 类型      | 必填 | 说明   |
|----------|---------|----|------|
| id       | integer | 是  | 角色ID |
| roleName | string  | 否  | 角色名称 |
| roleCode | string  | 否  | 角色编码 |
| roleDesc | string  | 否  | 角色描述 |
| status   | string  | 否  | 状态   |

---

### 5.5 删除角色

**接口说明**: 删除指定角色

```http
DELETE /systemManage/deleteRole/{id}
Authorization: Bearer {token}
```

---

### 5.6 角色菜单授权

**接口说明**: 为角色分配菜单权限

```http
PUT /systemManage/roleMenuAuth
Authorization: Bearer {token}
Content-Type: application/json
```

**请求参数**:

| 参数      | 类型      | 必填 | 说明     |
|---------|---------|----|--------|
| roleId  | integer | 是  | 角色ID   |
| menuIds | array   | 是  | 菜单ID列表 |

**请求示例**:

```json
{
  "roleId": 2,
  "menuIds": [10, 11, 12, 20, 21]
}
```

**响应示例**:

```json
{
  "code": "0000",
  "msg": "授权成功",
  "data": true
}
```

---

### 5.7 角色按钮授权

**接口说明**: 为角色分配按钮权限

```http
PUT /systemManage/roleButtonAuth
Authorization: Bearer {token}
Content-Type: application/json
```

**请求参数**:

| 参数          | 类型      | 必填 | 说明       |
|-------------|---------|----|----------|
| roleId      | integer | 是  | 角色ID     |
| buttonCodes | array   | 是  | 按钮权限编码列表 |

**请求示例**:

```json
{
  "roleId": 2,
  "buttonCodes": ["btn:user:add", "btn:user:edit", "btn:role:view"]
}
```

---

## 6. 菜单管理

### 6.1 获取菜单列表

**接口说明**: 分页查询菜单列表

```http
GET /systemManage/getMenuList/v2
Authorization: Bearer {token}
```

**请求参数**:

| 参数       | 类型      | 必填 | 说明                |
|----------|---------|----|-------------------|
| current  | integer | 否  | 当前页码              |
| size     | integer | 否  | 每页条数              |
| menuName | string  | 否  | 菜单名称              |
| menuType | string  | 否  | 类型：1=目录，2=菜单，3=按钮 |
| status   | string  | 否  | 状态                |

**响应参数**:

菜单对象：

| 参数         | 类型      | 说明                     |
|------------|---------|------------------------|
| id         | integer | 菜单ID                   |
| parentId   | integer | 父菜单ID（0为根菜单）           |
| menuType   | string  | 菜单类型                   |
| menuName   | string  | 菜单名称                   |
| routeName  | string  | 路由名称                   |
| routePath  | string  | 路由路径                   |
| component  | string  | 组件路径                   |
| icon       | string  | 图标                     |
| iconType   | string  | 图标类型：1=iconify，2=local |
| sortOrder  | integer | 排序                     |
| status     | string  | 状态                     |
| keepAlive  | boolean | 是否缓存                   |
| constant   | boolean | 是否常量路由                 |
| hideInMenu | boolean | 是否在菜单中隐藏               |
| activeMenu | string  | 激活菜单                   |
| i18nKey    | string  | 国际化键                   |
| buttons    | array   | 按钮列表（仅菜单类型）            |
| createTime | string  | 创建时间                   |

**响应示例**:

```json
{
  "code": "0000",
  "msg": "操作成功",
  "data": {
    "records": [
      {
        "id": 10,
        "parentId": 0,
        "menuType": "1",
        "menuName": "仪表盘",
        "routeName": "dashboard",
        "routePath": "/dashboard",
        "component": "view.dashboard.index",
        "icon": "mdi:home",
        "iconType": "1",
        "sortOrder": 1,
        "status": "1",
        "keepAlive": true,
        "constant": false,
        "hideInMenu": false,
        "createTime": "2024-01-01 00:00:00"
      },
      {
        "id": 20,
        "parentId": 0,
        "menuType": "1",
        "menuName": "系统管理",
        "routeName": "manage",
        "routePath": "/manage",
        "component": "layout.base",
        "icon": "mdi:cog",
        "iconType": "1",
        "sortOrder": 2,
        "status": "1",
        "createTime": "2024-01-01 00:00:00"
      },
      {
        "id": 21,
        "parentId": 20,
        "menuType": "2",
        "menuName": "用户管理",
        "routeName": "manage_user",
        "routePath": "/manage/user",
        "component": "view.manage.user.index",
        "icon": "mdi:account-group",
        "iconType": "1",
        "sortOrder": 1,
        "status": "1",
        "buttons": [
          { "code": "btn:user:add", "desc": "新增用户" },
          { "code": "btn:user:edit", "desc": "编辑用户" },
          { "code": "btn:user:delete", "desc": "删除用户" }
        ],
        "createTime": "2024-01-01 00:00:00"
      }
    ],
    "current": 1,
    "size": 10,
    "total": 3
  }
}
```

---

### 6.2 获取菜单树

**接口说明**: 获取树形结构的菜单（用于角色授权选择）

```http
GET /systemManage/getMenuTree
Authorization: Bearer {token}
```

**响应参数**:

树节点对象：

| 参数       | 类型      | 说明    |
|----------|---------|-------|
| id       | integer | 菜单ID  |
| label    | string  | 菜单名称  |
| pId      | integer | 父ID   |
| children | array   | 子节点列表 |

**响应示例**:

```json
{
  "code": "0000",
  "msg": "操作成功",
  "data": [
    {
      "id": 10,
      "label": "仪表盘",
      "pId": 0,
      "children": []
    },
    {
      "id": 20,
      "label": "系统管理",
      "pId": 0,
      "children": [
        {
          "id": 21,
          "label": "用户管理",
          "pId": 20,
          "children": []
        },
        {
          "id": 22,
          "label": "角色管理",
          "pId": 20,
          "children": []
        }
      ]
    }
  ]
}
```

---

### 6.3 获取所有页面

**接口说明**: 获取系统中所有可用的页面组件名称

```http
GET /systemManage/getAllPages
Authorization: Bearer {token}
```

**响应示例**:

```json
{
  "code": "0000",
  "msg": "操作成功",
  "data": [
    "view.home.index",
    "view.manage.user.index",
    "view.manage.role.index",
    "view.manage.menu.index",
    "view.system.config.index"
  ]
}
```

---

## 7. 错误码说明

### 7.1 系统级错误码

| 错误码  | 说明    | 处理建议   |
|------|-------|--------|
| 0000 | 成功    | -      |
| 5000 | 系统错误  | 联系管理员  |
| 5001 | 参数错误  | 检查请求参数 |
| 5002 | 数据库错误 | 联系管理员  |

### 7.2 认证相关错误码

| 错误码  | 说明             | 处理建议        |
|------|----------------|-------------|
| 8888 | 未登录/Token无效    | 跳转登录页       |
| 8889 | 无权限访问          | 显示无权限页面     |
| 9999 | Token已过期       | 调用刷新Token接口 |
| 9998 | RefreshToken过期 | 跳转登录页重新登录   |
| 7777 | 账号被禁用          | 提示账号已被禁用    |
| 7778 | 账号被锁定          | 提示账号已被锁定    |

### 7.3 业务相关错误码

| 错误码  | 说明             |
|------|----------------|
| 4001 | 用户不存在          |
| 4002 | 用户名已存在         |
| 4003 | 原密码错误          |
| 4004 | 角色编码已存在        |
| 4005 | 菜单路由名称已存在      |
| 4006 | 父菜单不存在         |
| 4007 | 不能将菜单设置为自己的子菜单 |

---

## 附录

### A. 测试工具推荐

- **Apifox**: 国产 API 管理工具，支持文档生成、Mock、自动化测试
- **Postman**: 国际通用的 API 测试工具
- **curl**: 命令行测试

### B. curl 测试示例

```bash
# 登录
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{"userName":"admin","password":"encrypted_password"}'

# 获取用户信息
curl -X GET http://localhost:8080/auth/getUserInfo \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIs..."

# 获取用户列表
curl -X GET "http://localhost:8080/systemManage/getUserList?current=1&size=10" \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIs..."
```

---

**文档结束**

*版本: v1.0*  
*更新日期: 2026-03-15*
