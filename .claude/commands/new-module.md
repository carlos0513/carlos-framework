# 新建业务模块

在 `carlos-integration/` 下创建一个新的业务模块，遵循标准分层架构。

## 执行步骤

1. 确认模块名称（如 `carlos-order`）
2. 创建目录结构：
   ```
   carlos-integration/carlos-{name}/
   ├── carlos-{name}-api/          # Feign 接口 + AO/Param
   └── carlos-{name}-bus/          # 业务实现
       ├── apiimpl/
       ├── config/
       ├── controller/
       ├── convert/
       ├── manager/impl/
       ├── mapper/
       ├── pojo/dto|entity|enums|param|vo/
       └── service/impl/
   ```
3. 创建 `pom.xml`（继承 `carlos-integration`，版本 `${revision}`）
4. 在 `carlos-integration/pom.xml` 中注册模块
5. 在 `carlos-dependencies/pom.xml` 中添加依赖管理条目

## 注意事项

- 严格遵守 `.claude/skills/carlos-framework-standard/SKILL.md` 中的编码规范
- 模块命名：`carlos-{功能名}`
- 包路径：`com.carlos.{模块名}`
- 创建后立即编写对应测试类
