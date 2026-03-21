---
name: api-connector
description: >
  前后端对接助手。新增或修改 API 对接时调用。根据后端接口文档生成前端 service 层代码，
  包括类型定义、请求函数，并确保符合项目规范。
---

# 前后端 API 对接助手

## 职责

根据后端提供的接口信息，生成符合规范的前端 service 层代码。

## 标准输出结构

### 1. 类型定义（`src/typings/api.d.ts` 或模块专属文件）

```typescript
// 请求参数类型
interface UserCreateParams {
  userName: string;
  email: string;
  roleIds: string[];
}

// 响应数据类型
interface UserVO {
  id: string;
  userName: string;
  email: string;
  status: number;
  createTime: string;
}

// 分页响应（对应后端 Paging<T>）
interface UserListResult {
  records: UserVO[];
  total: number;
  size: number;
  current: number;
}
```

### 2. 接口函数（`src/service/api/` 对应模块文件）

```typescript
import { request } from '../request';

/** 获取用户分页列表 */
export function fetchUserList(params: UserPageParams) {
  return request<UserListResult>({
    url: '/system/user/page',
    method: 'get',
    params
  });
}

/** 创建用户 */
export function createUser(data: UserCreateParams) {
  return request<boolean>({
    url: '/system/user',
    method: 'post',
    data
  });
}

/** 更新用户 */
export function updateUser(data: UserUpdateParams) {
  return request<boolean>({
    url: '/system/user',
    method: 'put',
    data
  });
}

/** 删除用户 */
export function deleteUser(id: string) {
  return request<boolean>({
    url: `/system/user/${id}`,
    method: 'delete'
  });
}
```

### 3. 在组件中使用

```typescript
<script setup lang="ts">
import { fetchUserList } from '@/service/api/system/user';

const loading = ref(false);
const tableData = ref<UserVO[]>([]);

async function loadData(params: UserPageParams) {
  loading.value = true;
  try {
    const { data } = await fetchUserList(params);
    tableData.value = data?.records ?? [];
  } finally {
    loading.value = false;
  }
}
</script>
```

## 规范约束

- URL 路径与后端 `@RequestMapping` 完全对应
- 分页参数命名与后端 `XxxPageParam` 字段一致（camelCase）
- 后端返回 `Result<T>` 包装，`@sa/axios` 已自动解包，直接使用 `data`
- 枚举值与后端 `BaseEnum.code` 保持一致
