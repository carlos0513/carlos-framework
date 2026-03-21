---
name: springboot-tdd
description: >
  Carlos Framework TDD 助手。新增功能或修复 Bug 时自动调用。
  强制执行 RED→GREEN→IMPROVE 流程，确保测试覆盖率达到 80%+。
---

# Carlos Framework TDD 流程

## 强制流程：RED → GREEN → IMPROVE

### RED 阶段 — 先写测试

1. 在 `src/test/java` 中创建对应测试类（`*Test.java` 或 `*IT.java`）
2. 按 BCDE 原则覆盖场景：
   - **B**order：边界值（空值、最大/最小值、空列表）
   - **C**orrect：正常输入得到预期结果
   - **D**esign：与设计文档对应的业务场景
   - **E**rror：异常输入触发正确异常

```java
@ExtendWith(MockitoExtension.class)
class OrgUserServiceTest {

    @Mock
    private OrgUserManager userManager;

    @InjectMocks
    private OrgUserServiceImpl userService;

    @Test
    @DisplayName("应该成功创建用户当参数合法时")
    void should_create_user_when_params_valid() {
        // given
        OrgUserCreateParam param = new OrgUserCreateParam();
        param.setUserName("testuser");

        // when
        userService.create(param);

        // then
        verify(userManager, times(1)).save(any(OrgUser.class));
    }

    @Test
    @DisplayName("应该抛出异常当用户名为空时")
    void should_throw_when_username_blank() {
        OrgUserCreateParam param = new OrgUserCreateParam();
        assertThrows(ServiceException.class, () -> userService.create(param));
    }
}
```

3. 运行测试 — **必须失败（RED）**：`mvn test -Dtest=OrgUserServiceTest`

### GREEN 阶段 — 最小实现

- 只编写让测试通过的最少代码
- 不要过度设计

### IMPROVE 阶段 — 重构

- 消除重复代码
- 改善命名和结构
- 确保所有测试仍然通过

## 覆盖率要求

```bash
mvn test jacoco:report
# 查看 target/site/jacoco/index.html
# 目标：Line Coverage ≥ 80%，Branch Coverage ≥ 70%
```

## 数据库测试规范

- 不假设数据库有特定数据，测试前自行准备数据
- 使用 `@Transactional` + `@Rollback` 避免脏数据
- 集成测试使用 H2 内存数据库或 Testcontainers
