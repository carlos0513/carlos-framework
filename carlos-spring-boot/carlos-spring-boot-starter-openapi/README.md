# carlos-openapi

## 模块简介

`carlos-openapi` 是 Carlos 框架的 API 文档生成模块，基于 SpringDoc OpenAPI 3 和 Knife4j 提供强大的 REST API 文档生成和管理功能。该模块支持自动生成 OpenAPI 规范文档，并提供美观的交互式文档界面。

## 主要功能

### 1. 自动 API 文档生成

基于 Spring Boot 3 的 SpringDoc OpenAPI 3 实现，自动扫描项目中的控制器和接口，生成符合 OpenAPI 3.0 规范的文档：

```java
@RestController
@RequestMapping("/api/user")
@Tag(name = "用户管理", description = "用户相关操作接口")
public class UserController {

    @Operation(summary = "获取用户列表", description = "分页查询用户列表")
    @GetMapping("/list")
    public Result<Page<User>> getUserList(@Parameter(description = "分页参数") PageParam param) {
        // 业务逻辑
    }

    @Operation(summary = "创建用户", description = "创建新用户")
    @PostMapping("/create")
    public Result<User> createUser(@RequestBody @Valid CreateUserRequest request) {
        // 业务逻辑
    }
}
```

### 2. Knife4j 集成

集成 Knife4j（原 Swagger-Bootstrap-UI），提供美观、功能丰富的文档界面：

- **交互式 API 测试**：直接在文档界面调用 API
- **离线文档下载**：支持导出 Markdown、Word、PDF 格式文档
- **接口分组管理**：支持按模块、按标签分组显示接口
- **全局参数配置**：支持配置全局请求头、认证信息

### 3. 丰富的配置选项

支持全面的 OpenAPI 配置，包括基本信息、联系人、许可证等：

```yaml
openapi:
  enable: true
  title: "Carlos 框架 API 文档"
  description: "基于 Spring Boot 3 的微服务框架"
  version: "3.0.0"
  terms-of-service-url: "https://example.com/terms"
  license: "Apache 2.0"
  license-url: "https://www.apache.org/licenses/LICENSE-2.0"
  contact:
    name: "技术支持"
    email: "support@example.com"
    url: "https://example.com"
```

### 4. OAuth2 认证支持

支持在 API 文档中配置 OAuth2 认证信息，方便测试需要认证的接口：

```yaml
openapi:
  auth2:
    enable: true
    token-url: "http://localhost:8080/oauth2/token"
    client-id: "my-client"
    grant-type: "password"  # 支持: password, client_credentials, authorization_code
```

### 5. 自定义参数配置

支持配置全局请求参数，如认证令牌、租户ID等：

```yaml
openapi:
  parameters:
    - name: "Authorization"
      description: "Bearer Token"
      type: "header"
      data-type: "String"
      required: true
      default-value: "Bearer "
    - name: "X-Tenant-Id"
      description: "租户ID"
      type: "header"
      data-type: "String"
      required: false
```

### 6. 接口排序和分组

支持按标签排序，自动为接口分组，提供更好的文档组织结构：

```java
@Configuration
public class OpenApiConfig {

    @Bean
    public GlobalOpenApiCustomizer orderGlobalOpenApiCustomizer() {
        return openApi -> {
            // 为标签添加排序扩展
            openApi.getTags().forEach(tag -> {
                Map<String, Object> extensions = new HashMap<>();
                extensions.put("x-order", RandomUtil.randomInt(0, 100));
                tag.setExtensions(extensions);
            });
        };
    }
}
```

## 快速开始

### 1. 添加依赖

```xml
<dependency>
    <groupId>com.carlos</groupId>
    <artifactId>carlos-openapi</artifactId>
    <version>${carlos.version}</version>
</dependency>
```

### 2. 基本配置

在 `application.yml` 中启用并配置 OpenAPI：

```yaml
# 启用 OpenAPI
openapi:
  enable: true

  # 基本信息
  title: "Carlos 框架 API 文档"
  description: "基于 Spring Boot 3 的微服务框架"
  version: "3.0.0"

  # 扫描配置
  base-package: "com.carlos.controller"
  base-path: "/api/**"

  # 排除路径
  exclude-path:
    - "/error"
    - "/actuator/**"

  # 联系人信息
  contact:
    name: "技术支持"
    email: "support@example.com"
    url: "https://example.com"

  # 许可证信息
  license: "Apache 2.0"
  license-url: "https://www.apache.org/licenses/LICENSE-2.0"
```

### 3. 使用示例

#### 控制器注解使用：

```java
@RestController
@RequestMapping("/api/user")
@Tag(name = "用户管理", description = "用户相关操作接口")
public class UserController {

    @Autowired
    private UserService userService;

    @Operation(summary = "获取用户列表", description = "分页查询用户列表")
    @ApiResponse(responseCode = "200", description = "成功返回用户列表")
    @ApiResponse(responseCode = "401", description = "未授权")
    @GetMapping("/list")
    public Result<Page<User>> getUserList(
            @Parameter(description = "分页参数", required = true) PageParam param,
            @Parameter(description = "搜索关键词") @RequestParam(required = false) String keyword) {
        return userService.getUserPage(param, keyword);
    }

    @Operation(summary = "创建用户", description = "创建新用户")
    @PostMapping("/create")
    public Result<User> createUser(
            @Parameter(description = "用户信息", required = true)
            @RequestBody @Valid CreateUserRequest request) {
        return userService.createUser(request);
    }

    @Operation(summary = "获取用户详情", description = "根据ID获取用户详情")
    @GetMapping("/{id}")
    public Result<User> getUserById(
            @Parameter(description = "用户ID", required = true)
            @PathVariable Long id) {
        return userService.getUserById(id);
    }
}
```

#### 实体类注解使用：

```java
@Data
@Schema(description = "用户信息")
public class User {

    @Schema(description = "用户ID", example = "1")
    private Long id;

    @Schema(description = "用户名", example = "admin", required = true)
    private String username;

    @Schema(description = "昵称", example = "管理员")
    private String nickName;

    @Schema(description = "邮箱", example = "admin@example.com")
    private String email;

    @Schema(description = "手机号", example = "13800138000")
    private String phone;

    @Schema(description = "创建时间", example = "2023-01-01 12:00:00")
    private LocalDateTime createTime;
}
```

#### 枚举类注解使用：

```java
@Getter
@AllArgsConstructor
@Schema(description = "用户状态枚举")
public enum UserStatus {

    @Schema(description = "正常")
    NORMAL(1, "正常"),

    @Schema(description = "禁用")
    DISABLED(0, "禁用"),

    @Schema(description = "锁定")
    LOCKED(2, "锁定");

    @Schema(description = "状态值")
    private final Integer value;

    @Schema(description = "状态描述")
    private final String description;
}
```

### 4. 访问文档界面

应用启动后，访问以下地址查看文档：

- **Knife4j 文档界面**: `http://localhost:8080/doc.html`
- **原生 Swagger UI**: `http://localhost:8080/swagger-ui.html`
- **OpenAPI JSON**: `http://localhost:8080/v3/api-docs`
- **OpenAPI YAML**: `http://localhost:8080/v3/api-docs.yaml`

## 详细功能说明

### 1. 文档分组配置

支持按模块、按包路径进行文档分组：

```yaml
openapi:
  group-name: "默认分组"

  # 或者按模块分组
  groups:
    user:
      display-name: "用户管理"
      paths-to-match: "/api/user/**"
    order:
      display-name: "订单管理"
      paths-to-match: "/api/order/**"
```

### 2. 安全配置

支持 API Key、HTTP Basic、OAuth2 等多种认证方式：

```java
@Configuration
public class SecurityConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
            .components(new Components()
                .addSecuritySchemes("bearer-jwt", new SecurityScheme()
                    .type(SecurityScheme.Type.HTTP)
                    .scheme("bearer")
                    .bearerFormat("JWT")
                    .description("JWT 认证")))
            .addSecurityItem(new SecurityRequirement().addList("bearer-jwt"));
    }
}
```

### 3. 自定义文档扩展

支持添加自定义扩展信息：

```java
@Bean
public OpenAPI customOpenAPI() {
    return new OpenAPI()
        .info(new Info()
            .title("API文档")
            .version("1.0.0")
            .description("系统API文档"))
        .addExtension("x-api-version", "v3")
        .addExtension("x-supported-languages", Arrays.asList("zh-CN", "en-US"));
}
```

### 4. 响应示例配置

支持配置响应示例，提高文档可读性：

```java
@Operation(summary = "获取用户", responses = {
    @ApiResponse(responseCode = "200", description = "成功",
        content = @Content(schema = @Schema(implementation = User.class),
        examples = @ExampleObject(
            name = "成功示例",
            value = "{\"code\":2000,\"message\":\"成功\",\"data\":{\"id\":1,\"username\":\"admin\"}}"
        ))),
    @ApiResponse(responseCode = "404", description = "用户不存在")
})
@GetMapping("/{id}")
public Result<User> getUser(@PathVariable Long id) {
    // 业务逻辑
}
```

## 配置项说明

| 配置项                            | 类型          | 默认值                          | 说明              |
|--------------------------------|-------------|------------------------------|-----------------|
| `openapi.enable`               | boolean     | `false`                      | 是否启用 OpenAPI    |
| `openapi.base-package`         | String      | -                            | 扫描的基本包，多个包用逗号分隔 |
| `openapi.base-path`            | Set<String> | `["/**"]`                    | 扫描的 URL 规则      |
| `openapi.exclude-path`         | Set<String> | `["/error", "/actuator/**"]` | 排除的 URL 规则      |
| `openapi.title`                | String      | -                            | 文档标题            |
| `openapi.description`          | String      | -                            | 文档描述            |
| `openapi.version`              | String      | -                            | API 版本          |
| `openapi.terms-of-service-url` | String      | -                            | 服务条款 URL        |
| `openapi.license`              | String      | -                            | 许可证名称           |
| `openapi.license-url`          | String      | -                            | 许可证 URL         |
| `openapi.contact.name`         | String      | -                            | 联系人姓名           |
| `openapi.contact.email`        | String      | -                            | 联系人邮箱           |
| `openapi.contact.url`          | String      | -                            | 联系人 URL         |
| `openapi.auth2.enable`         | boolean     | `false`                      | 是否启用 OAuth2     |
| `openapi.auth2.token-url`      | String      | -                            | Token 获取地址      |
| `openapi.auth2.client-id`      | String      | -                            | 客户端 ID          |
| `openapi.auth2.grant-type`     | String      | -                            | 授权类型            |

## 依赖项

- `springdoc-openapi-starter-webmvc-ui` (3.x): SpringDoc OpenAPI 核心依赖
- `knife4j-openapi3-spring-boot-starter`: Knife4j UI 界面
- `carlos-spring-boot-starter-core`: 基础工具类、常量定义
- `spring-boot-starter-web`: Web 应用基础依赖

## 注意事项

### 1. 性能考虑

- 生产环境建议关闭 OpenAPI 文档生成，或通过配置文件控制
- 扫描的包路径尽量精确，避免扫描不必要的包
- 大量接口时，考虑按模块分组显示

### 2. 安全建议

- 生产环境不要暴露文档界面，或添加访问控制
- 敏感接口（如管理员接口）建议单独分组并设置访问权限
- 文档中的示例数据不要使用真实敏感信息

### 3. 使用建议

- 为所有公共接口添加完整的文档注解
- 使用统一的响应格式和错误码
- 为复杂的业务对象提供详细的 Schema 描述
- 及时更新文档，保持与代码同步

### 4. 常见问题

**Q: 文档界面无法访问怎么办？**
A: 检查以下事项：

1. 确认 `openapi.enable=true`
2. 确认没有安全拦截器阻止访问 `/doc.html` 或 `/swagger-ui.html`
3. 确认项目依赖了正确的 Spring Boot 和 SpringDoc 版本
4. 查看应用启动日志，确认 Knife4j 初始化成功

**Q: 部分接口没有显示在文档中？**
A: 可能的原因：

1. 接口不在 `base-package` 扫描范围内
2. 接口路径被 `exclude-path` 排除
3. 控制器类缺少 `@RestController` 或 `@Controller` 注解
4. 方法缺少 `@RequestMapping` 系列注解

**Q: 如何自定义文档样式？**
A: Knife4j 支持主题定制：

1. 在 `resources` 目录创建 `META-INF/resources/` 目录
2. 添加自定义 CSS 文件
3. 在配置中指定自定义样式文件路径

**Q: 如何导出离线文档？**
A: 使用 Knife4j 的导出功能：

1. 访问 `http://localhost:8080/doc.html`
2. 点击右上角"导出"按钮
3. 选择导出格式（Markdown、Word、PDF、OpenAPI JSON/YAML）

### 5. 与 carlos-spring-boot-starter-core 集成

本模块使用 `carlos-spring-boot-starter-core` 模块的工具类获取应用地址和端口信息，确保文档链接的正确性。

## 版本要求

- JDK 17+
- Spring Boot 3.5.8+
- SpringDoc OpenAPI 2.x+
- Knife4j 4.x+

## 相关模块

- **carlos-spring-boot-starter-core**：基础工具类、常量定义、响应格式
- **carlos-oauth2**：OAuth2 认证授权，为文档提供认证支持
- **carlos-gateway**：API 网关，文档可能需要网关相关配置