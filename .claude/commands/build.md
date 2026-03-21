# 构建后端

构建 carlos-framework 全部模块（跳过测试）。

```bash
cd D:/ide_project/carlos/framework/carlos-framework
mvn clean install -DskipTests
```

## 常用变体

```bash
# 包含测试
mvn clean install

# 部署到公共 Nexus（zcarlos.com:8081）
mvn clean deploy -P carlos-public

# 部署到私有 Nexus（192.168.3.30:8081）
mvn clean deploy -P carlos-private

# 构建指定模块
cd carlos-spring-boot/carlos-spring-boot-starter-redis
mvn clean install -DskipTests

# 检查依赖更新
mvn versions:display-dependency-updates
```

## 构建顺序（失败时参考）

1. carlos-dependencies
2. carlos-parent
3. carlos-commons（core → utils → excel）
4. carlos-spring-boot（22 个 starter）
5. carlos-integration（auth → system → org → audit → message → license → tools）
6. carlos-samples
