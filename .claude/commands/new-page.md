# 新增页面

在 `src/views/` 下创建新页面，并生成对应路由和 service 层代码。

## 执行步骤

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

## 页面模板

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

## 注意事项

- 文件名使用 kebab-case（如 `user-list`）
- 组件名使用 PascalCase（如 `SystemUserList`）
- 新增页面后必须运行 `pnpm gen-route`
- 后端菜单需同步配置才能在侧边栏显示
