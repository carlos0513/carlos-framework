# carlos-spring-boot-starter-redis 集成指南

## 概述

`carlos-spring-boot-starter-redis` 是一个功能完整的 Redis 集成模块，已整合以下功能：

- **Redis 基础操作**：基于 Lettuce 的 Redis 客户端，支持单机、哨兵、集群模式
- **Redisson 分布式锁**：基于 Redisson 3.51.0 的分布式锁实现
- **Caffeine 本地缓存**：高性能本地缓存，支持多种缓存策略
- **多级缓存**：Caffeine（L1）+ Redis（L2）两级缓存架构

## 快速开始

### 1. 添加依赖

```xml
<dependency>
    <groupId>com.carlos</groupId>
    <artifactId>carlos-spring-boot-starter-redis</artifactId>
    <version>${carlos.version}</version>
</dependency>
```

### 2. 完整配置示例

```yaml
spring:
  # Redis 基础配置
  redis:
    # 单机模式
    host: localhost
    port: 6379
    password: your_password
    database: 0
    timeout: 3000ms

    # Lettuce 连接池配置
    lettuce:
      pool:
        max-active: 50      # 最大活跃连接数
        max-idle: 100       # 最大空闲连接数
        min-idle: 10        # 最小空闲连接数
        max-wait: 30s       # 最大等待时间
      shutdown-timeout: 100ms

  # Spring Cache 配置
  cache:
    type: redis
    redis:
      time-to-live: 3600000  # 缓存 TTL（毫秒）

# Carlos 框架配置
carlos:
  # Redis 缓存配置
  cache:
    user-prefix: true           # 启用键前缀
    key-prefix: "myapp:cache:"  # 键前缀

  redis:
    # Redisson 分布式锁配置
    lock:
      enabled: true             # 是否启用分布式锁，默认true
      prefix: "redis:lock:"     # 锁前缀，默认 redis:lock:

    # Caffeine 本地缓存配置
    caffeine:
      enabled: true             # 是否启用Caffeine，默认true
      initial-capacity: 100     # 初始容量，默认100
      maximum-size: 10000       # 最大容量，默认10000
      expire-after-write: 300   # 写入后过期时间（秒），默认300秒
      expire-after-access: 600  # 访问后过期时间（秒），默认不启用
      refresh-after-write: 60   # 刷新时间（秒），默认不启用
      record-stats: true        # 是否记录统计信息，默认false
```

### 3. 哨兵模式配置

```yaml
spring:
  redis:
    sentinel:
      master: mymaster
      nodes:
        - 192.168.1.10:26379
        - 192.168.1.11:26379
        - 192.168.1.12:26379
    password: your_password
```

### 4. 集群模式配置

```yaml
spring:
  redis:
    cluster:
      nodes:
        - 192.168.1.10:6379
        - 192.168.1.11:6379
        - 192.168.1.12:6379
        - 192.168.1.13:6379
        - 192.168.1.14:6379
        - 192.168.1.15:6379
      max-redirects: 3
    password: your_password
```

## 使用示例

### Redis 基础操作

```java
import com.carlos.redis.util.RedisUtil;

@Service
public class UserService {

    // 设置缓存
    public void cacheUser(User user) {
        RedisUtil.setValue("user:" + user.getId(), user, 3600);
    }

    // 获取缓存
    public User getUser(Long id) {
        return RedisUtil.getValue("user:" + id);
    }

    // 删除缓存
    public void deleteUser(Long id) {
        RedisUtil.delete("user:" + id);
    }
}
```

### Redisson 分布式锁

#### 注解式使用

```java
import com.carlos.redis.lock.RedisLock;

@Service
public class OrderService {

    @RedisLock(name = "order", key = "#orderId", expire = 30000)
    public void processOrder(Long orderId) {
        // 业务逻辑，自动加锁和释放锁
    }
}
```

#### 编程式使用

```java
import com.carlos.redis.lock.RedissonLockUtil;
import java.util.concurrent.TimeUnit;

@Service
public class PaymentService {

    public void processPayment(String paymentId) {
        String lockKey = "payment:" + paymentId;

        // 尝试获取锁
        if (RedissonLockUtil.tryLock(lockKey, 5, 30, TimeUnit.SECONDS)) {
            try {
                // 业务逻辑
                doPayment(paymentId);
            } finally {
                // 释放锁
                RedissonLockUtil.unlock(lockKey);
            }
        } else {
            throw new RuntimeException("获取锁失败");
        }
    }
}
```

### Caffeine 本地缓存

```java
import com.carlos.redis.caffeine.CaffeineUtil;

@Service
public class ConfigService {

    // 设置本地缓存
    public void cacheConfig(String key, Object value) {
        CaffeineUtil.put(key, value);
    }

    // 获取本地缓存
    public Object getConfig(String key) {
        return CaffeineUtil.get(key);
    }

    // 获取缓存统计信息
    public void printStats() {
        String stats = CaffeineUtil.stats();
        System.out.println("缓存统计: " + stats);
    }
}
```

### 多级缓存

```java
import com.carlos.redis.multilevel.MultiLevelCacheUtil;
import java.util.concurrent.TimeUnit;

@Service
public class ProductService {

    @Autowired
    private ProductMapper productMapper;

    // 使用多级缓存
    public Product getProduct(Long id) {
        String key = "product:" + id;

        // 先从L1（Caffeine）获取，再从L2（Redis）获取，都没有则从数据库加载
        return (Product) MultiLevelCacheUtil.get(key, () -> {
            return productMapper.selectById(id);
        }, 3600, TimeUnit.SECONDS);
    }

    // 更新缓存
    public void updateProduct(Product product) {
        productMapper.updateById(product);

        // 更新两级缓存
        String key = "product:" + product.getId();
        MultiLevelCacheUtil.put(key, product, 3600, TimeUnit.SECONDS);
    }

    // 删除缓存
    public void deleteProduct(Long id) {
        productMapper.deleteById(id);

        // 删除两级缓存
        String key = "product:" + id;
        MultiLevelCacheUtil.evict(key);
    }
}
```

### Spring Cache 注解

```java
import org.springframework.cache.annotation.*;

@Service
public class UserService {

    @Cacheable(value = "users", key = "#id")
    public User getById(Long id) {
        return userMapper.selectById(id);
    }

    @CachePut(value = "users", key = "#user.id")
    public User update(User user) {
        userMapper.updateById(user);
        return user;
    }

    @CacheEvict(value = "users", key = "#id")
    public void delete(Long id) {
        userMapper.deleteById(id);
    }

    @CacheEvict(value = "users", allEntries = true)
    public void deleteAll() {
        userMapper.delete(null);
    }
}
```

## 最佳实践

### 1. 缓存键命名规范

```java
// 推荐：使用冒号分隔，层次清晰
String key = "user:profile:" + userId;
String key = "order:detail:" + orderId;

// 不推荐：使用下划线或无分隔符
String key = "user_profile_" + userId;
String key = "orderdetail" + orderId;
```

### 2. 合理设置过期时间

```java
// 热点数据：短期缓存
RedisUtil.setValue("hot:product:" + id, product, 300);  // 5分钟

// 常规数据：中期缓存
RedisUtil.setValue("user:" + id, user, 3600);  // 1小时

// 配置数据：长期缓存
RedisUtil.setValue("config:" + key, value, 86400);  // 1天
```

### 3. 使用多级缓存提升性能

```java
// 对于频繁访问的数据，使用多级缓存
public User getUser(Long id) {
    return (User) MultiLevelCacheUtil.get("user:" + id,
        () -> userMapper.selectById(id),
        3600, TimeUnit.SECONDS
    );
}
```

### 4. 分布式锁的正确使用

```java
// 推荐：使用 try-finally 确保锁释放
@RedisLock(name = "order", key = "#orderId", expire = 30000)
public void processOrder(Long orderId) {
    // 业务逻辑
}

// 或者编程式
String lockKey = "order:" + orderId;
if (RedissonLockUtil.tryLock(lockKey, 5, 30, TimeUnit.SECONDS)) {
    try {
        // 业务逻辑
    } finally {
        RedissonLockUtil.unlock(lockKey);
    }
}
```

### 5. 批量操作优化

```java
// 批量设置缓存
Map<String, User> userMap = new HashMap<>();
userMap.put("user:1", user1);
userMap.put("user:2", user2);
userMap.put("user:3", user3);
RedisUtil.setValueList(userMap, Duration.ofHours(1));

// 批量获取缓存
List<String> keys = Arrays.asList("user:1", "user:2", "user:3");
List<User> users = RedisUtil.getValueList(keys);
```

## 注意事项

1. **键前缀**：多应用共享 Redis 时务必配置 `carlos.cache.key-prefix`
2. **连接池**：根据并发量合理配置连接池大小
3. **过期时间**：合理设置 TTL，避免内存溢出
4. **分布式锁**：注意锁的粒度和过期时间，避免死锁
5. **本地缓存**：Caffeine 缓存仅在单个 JVM 内有效，不跨节点
6. **多级缓存**：注意缓存一致性问题，更新时需同时更新两级缓存
7. **序列化**：Long 类型会序列化为 String，注意类型转换
8. **主从延迟**：读写分离时注意主从同步延迟

## 迁移指南

### 从独立的 redisson 模块迁移

如果您之前使用的是独立的 `carlos-spring-boot-starter-redisson` 模块，请按以下步骤迁移：

1. **更新依赖**

```xml
<!-- 删除 -->
<dependency>
    <groupId>com.carlos</groupId>
    <artifactId>carlos-spring-boot-starter-redisson</artifactId>
</dependency>

<!-- 保留或添加 -->
<dependency>
    <groupId>com.carlos</groupId>
    <artifactId>carlos-spring-boot-starter-redis</artifactId>
</dependency>
```

2. **更新配置**

```yaml
# 旧配置（carlos.redisson）
carlos:
  redisson:
    enabled: true
    prefix: "redisson_lock:"

# 新配置（carlos.redis.lock）
carlos:
  redis:
    lock:
      enabled: true
      prefix: "redis:lock:"
```

3. **更新导入语句**

```java
// 旧导入
import com.carlos.redisson.RedisLock;
import com.carlos.redisson.RedissonLockUtil;

// 新导入
import com.carlos.redis.lock.RedisLock;
import com.carlos.redis.lock.RedissonLockUtil;
```

4. **代码无需修改**

注解和方法调用保持不变，只需更新导入语句即可。

## 故障排查

### 1. 连接失败

```
Could not get a resource from the pool
```

**解决方案**：

- 检查 Redis 服务是否启动
- 检查网络连接和防火墙
- 增加连接池大小：`spring.redis.lettuce.pool.max-active`

### 2. 序列化错误

```
Cannot deserialize
```

**解决方案**：

- 确保实体类实现 Serializable
- 检查 JSON 序列化配置
- 清空 Redis 中的旧数据

### 3. 分布式锁超时

```
Lock acquisition timeout
```

**解决方案**：

- 增加等待时间：`@RedisLock(expire = 60000)`
- 检查业务逻辑是否耗时过长
- 确保锁正确释放

## 性能优化建议

1. **使用多级缓存**：热点数据使用 Caffeine + Redis 两级缓存
2. **批量操作**：使用 Pipeline 批量读写
3. **连接复用**：Lettuce 基于 Netty，连接复用效率高
4. **读写分离**：配置哨兵模式，从库优先读取
5. **Lua 脚本**：复杂操作使用 Lua 脚本减少网络往返

## 版本要求

- JDK 17+
- Spring Boot 3.5.9+
- Redis 5.0+
- Maven 3.8+

## 相关文档

- [Redis 官方文档](https://redis.io/documentation)
- [Redisson 官方文档](https://github.com/redisson/redisson/wiki)
- [Caffeine 官方文档](https://github.com/ben-manes/caffeine/wiki)
- [Spring Data Redis 文档](https://spring.io/projects/spring-data-redis)
