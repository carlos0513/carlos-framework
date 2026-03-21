# 更新项目文档

更新 CLAUDE.md、MEMORY.md 和相关模块文档，确保文档与代码同步。

## 执行步骤

1. 检查最近的代码变更（git diff）
2. 识别需要更新的文档
3. 调用 @carlos-doc-updater 执行文档更新

## 更新范围

- **CLAUDE.md**: 项目主文档、架构变更、模块结构
- **memory/MEMORY.md**: 跨会话快速参考、模块列表
- **模块 README**: 各模块的功能说明和配置
- **SKILL.md**: 编码规范文档

## 使用示例

```
/update-doc
/update-doc 请更新 CLAUDE.md 中的路径配置
/update-doc 新增模块 carlos-order 的文档
```

## 注意事项

- 重大架构变更必须更新 CLAUDE.md
- 新增模块必须创建 README.md
- 版本升级需同步更新所有版本号
