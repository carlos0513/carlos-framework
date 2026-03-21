# 构建前端

构建 carlos-framework-admin 前端项目。

```bash
cd D:/ide_project/carlos/framework/carlos-framework-admin

# 生产环境构建
pnpm build

# 测试环境构建
pnpm build:test
```

## 构建前检查

```bash
# 1. TypeScript 类型检查
pnpm typecheck

# 2. ESLint 检查
pnpm lint

# 3. 确认路由已生成（如有新增页面）
pnpm gen-route
```

## 构建产物

- 输出目录：`dist/`
- 静态资源已哈希命名，支持长期缓存
