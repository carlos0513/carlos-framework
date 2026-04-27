# Carlos Security Starter

Security Starter，支持接口级别权限控制（@PreAuthorize）。

## 特性

- 从网关Header提取用户信息
- 支持 Spring Security @PreAuthorize 注解
- 多级缓存权限加载（Caffeine + Redis）
- 可扩展的权限提供器架构

## 快速开始

### 1. 添加依赖

```xml
<dependency>
    <groupId>com.carlos</groupId>
    <artifactId>carlos-spring-boot-starter-security</artifactId>
</dependency>
```

### 2. 配置文件

```yaml
carlos:
  resource-server:
    enabled: true
    whitelist:
      - /public/**
      - /api/v1/open/**
    permission:
      provider: redis  # redis / database
      cache:
        enabled: true
        local-ttl: 300  # 本地缓存5分钟
        max-size: 5000
```

### 3. 使用注解

```java
@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public List<User> listAll() {
        return userService.list();
    }

    @PreAuthorize("hasPermission('user', 'read')")
    @GetMapping("/{id}")
    public User getById(@PathVariable Long id) {
        return userService.getById(id);
    }

    @PreAuthorize("@permissionEvaluator.isOwner(#id, 'user')")
    @GetMapping("/{id}/detail")
    public UserDetail getDetail(@PathVariable Long id) {
        return userService.getDetail(id);
    }
}
```

## 获取当前用户信息

```java
// 1. 使用 SecurityUtils 工具类
Long userId = SecurityUtils.getCurrentUserId();
String username = SecurityUtils.getCurrentUsername();
UserContext user = SecurityUtils.getCurrentUser();

// 2. 检查权限
if (SecurityUtils.hasPermission("user:update")) {
    // 执行业务逻辑
}

// 3. 获取当前用户权限和角色
Set<String> permissions = SecurityUtils.getCurrentUserPermissions();
Set<Long> roles = SecurityUtils.getCurrentUserRoles();
```

## 权限提供器

### 默认实现

- **RedisPermissionProvider**：从Redis读取权限（默认）
- **CachedPermissionProvider**：带本地缓存的装饰器

### 自定义实现

```java
@Bean
@Primary
public PermissionProvider customPermissionProvider() {
    return new CustomPermissionProvider();
}

public class CustomPermissionProvider implements PermissionProvider {
    
    @Override
    public Set<String> getPermissions(Long roleId) {
        // 自定义权限获取逻辑
        return myPermissionService.getByRoleId(roleId);
    }
}
```

## 权限数据初始化

```java
@Service
public class PermissionInitService {
    
    @Autowired
    private PermissionService permissionService;
    
    public void initRolePermissions(Long roleId, Set<String> permissions) {
        permissionService.updateRolePermissions(roleId, permissions, true);
    }
}
```

## Redis 数据结构

```
Key: auth:permissions:{roleId}
Value: Set<String>
TTL: 7天（可配置）

示例：
auth:permissions:1
  → ["user:read", "user:write", "order:read"]
```

## 缓存同步

当权限变更时，使用 `PermissionService` 更新权限并同步到其他服务实例：

```java
@Service
public class PermissionSyncService {
    
    @Autowired
    private PermissionService permissionService;
    
    public void updatePermissions(Long roleId, Set<String> newPermissions) {
        // 1. 更新数据库（业务逻辑）
        rolePermissionMapper.update(roleId, newPermissions);
        
        // 2. 更新缓存并同步到其他实例
        permissionService.updateRolePermissions(roleId, newPermissions, true);
    }
}
```
