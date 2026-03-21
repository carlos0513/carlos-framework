# 运行测试

运行后端单元测试并生成覆盖率报告。

```bash
cd D:/ide_project/carlos/framework/carlos-framework

# 运行全部测试
mvn test

# 运行指定测试类
mvn test -Dtest=OrgUserServiceTest

# 运行指定测试方法
mvn test -Dtest=OrgUserServiceTest#should_create_user_when_params_valid

# 运行集成测试
mvn verify

# 生成 JaCoCo 覆盖率报告
mvn test jacoco:report
# 报告位置：target/site/jacoco/index.html
```

## 覆盖率目标

- Line Coverage ≥ **80%**
- Branch Coverage ≥ **70%**
