# 构建后端

构建 carlos-framework 后端所有模块。

## 执行步骤

1. 进入后端目录：`cd D:/ide_project/carlos/framework/carlos-framework`
2. 执行构建：`mvn clean install -DskipTests`
3. 显示构建结果摘要

## 可选参数

用户可以指定：

- 指定 profile：`-P carlos-public` 或 `-P carlos-private`
- 包含测试：去掉 `-DskipTests`
- 指定模块：`cd` 到具体子模块目录再构建

## 构建顺序提示

如果某个模块构建失败，提示用户按以下顺序检查：

1. carlos-dependencies
2. carlos-parent
3. carlos-commons
4. carlos-spring-boot
5. carlos-integration
6. carlos-samples
