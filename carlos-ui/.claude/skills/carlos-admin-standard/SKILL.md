---
name: carlos-admin-standard
description: >
  carlos-framework-admin 前端编码规范。编写 Vue 3 / TypeScript 代码时使用。
  包含组件规范、类型规范、状态管理、API 请求、样式、路由等完整规范。
---

# carlos-framework-admin 编码规范

## 强制规范速查

| 规范   | 要求                               | 禁止             |
|------|----------------------------------|----------------|
| 组件语法 | `<script setup>` + TypeScript    | Options API    |
| 类型   | 明确类型，禁止 `any`                    | `any`、`as any` |
| 包管理  | pnpm                             | npm、yarn       |
| 样式   | UnoCSS 工具类 + `<style scoped>`    | 内联样式           |
| 状态   | Pinia store                      | 组件间直接传递复杂状态    |
| 请求   | `src/service/api/` + `@sa/axios` | 直接 axios/fetch |

## 组件规范

### 标准结构

```vue
<script setup lang="ts">
// 1. 导入
import { ref, computed, onMounted } from 'vue';
import type { UserVO } from '@/typings/api';

// 2. defineOptions（必须定义组件名）
defineOptions({ name: 'SystemUserList' });

// 3. Props / Emits
interface Props {
  userId: string;
}
const props = defineProps<Props>();
const emit = defineEmits<{
  success: [];
}>();

// 4. 响应式状态
const loading = ref(false);
const userList = ref<UserVO[]>([]);

// 5. 计算属性
const hasData = computed(() => userList.value.length > 0);

// 6. 方法
async function loadData() {
  loading.value = true;
  try {
    // ...
  } finally {
    loading.value = false;
  }
}

// 7. 生命周期
onMounted(() => {
  loadData();
});
</script>

<template>
  <div class="p-4">
    <!-- 内容 -->
  </div>
</template>

<style scoped>
/* 仅在 UnoCSS 无法满足时使用 */
</style>
```

### 命名规范

| 类型    | 规范          | 示例                           |
|-------|-------------|------------------------------|
| 组件文件  | kebab-case  | `user-list.vue`              |
| 组件名   | PascalCase  | `SystemUserList`             |
| 变量/函数 | camelCase   | `userList`, `loadData`       |
| 常量    | UPPER_SNAKE | `MAX_PAGE_SIZE`              |
| 类型/接口 | PascalCase  | `UserVO`, `UserCreateParams` |

## TypeScript 规范

```typescript
// ❌ 禁止
const data: any = response;
const user = data as any;

// ✅ 正确
interface UserVO {
  id: string;
  userName: string;
}
const data: UserVO = response;
```

## Pinia Store 规范

```typescript
// src/store/modules/user.ts
export const useUserStore = defineStore('user', () => {
  const userInfo = ref<UserInfo | null>(null);

  // actions
  async function fetchUserInfo() {
    const { data } = await getUserInfo();
    userInfo.value = data;
  }

  function reset() {
    userInfo.value = null;
  }

  return { userInfo, fetchUserInfo, reset };
});
```

## API 请求规范

```typescript
// src/service/api/system/user.ts
import { request } from '../../request';

export function fetchUserPage(params: UserPageParams) {
  return request<PageResult<UserVO>>({
    url: '/system/user/page',
    method: 'get',
    params
  });
}
```

## UnoCSS 样式规范

```vue
<!-- ✅ 优先使用工具类 -->
<div class="flex items-center gap-4 p-4 bg-white rounded-lg shadow">

<!-- ✅ 动态样式可用内联 -->
<div :style="{ width: `${progress}%` }">

<!-- ❌ 避免静态内联样式 -->
<div style="display: flex; padding: 16px;">
```

## 路由规范

- 页面文件路径决定路由：`src/views/system/user/index.vue` → `/system/user`
- 动态路由：`src/views/system/user/[id].vue` → `/system/user/:id`
- 新增/修改页面后必须运行 `pnpm gen-route`

## 代码检查清单

- [ ] 无 `any` 类型
- [ ] 使用 `<script setup>`
- [ ] API 通过 service 层调用
- [ ] 无 `console.log`
- [ ] 组件 < 300 行
- [ ] 无硬编码凭据
- [ ] 新页面已运行 `pnpm gen-route`
