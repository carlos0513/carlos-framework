# carlos-redis

## 模块简介

`carlos-redis` 是 Carlos 框架的 Redis 缓存集成模块，提供了基于 Lettuce 客户端的 Redis 连接管理、Spring Cache 抽象支持、高级 Redis 操作工具、Lua 脚本执行、多数据源支持等功能。该模块支持主从读写分离、批量操作、管道优化等企业级特性。

## 主要功能

### 1. Redis 基础操作

**RedisUtil** 工具类提供全面的 Redis 操作：

#### 通用操作

```java
// 设置过期时间
RedisUtil.setExpire("key", 3600, TimeUnit.SECONDS);

// 获取剩余 TTL
Long ttl = RedisUtil.getExpire("key");

// 检查键是否存在
Boolean exists = RedisUtil.hasKey("key");

// 删除键
RedisUtil.delete("key");

// 批量删除
RedisUtil.delete(Arrays.asList("key1", "key2", "key3"));

// 模式匹配查找键
Set<String> keys = RedisUtil.keys("user:*");

// 安全扫描键（避免阻塞）
Set<String> keys = RedisUtil.scanKeys("user:*");
```

#### String 操作

```java
// 设置值
RedisUtil.setValue("user:1", userObject);

// 设置值并指定过期时间
RedisUtil.setValue("user:1", userObject, 3600);

// 获取值
User user = RedisUtil.getValue("user:1");

// 条件设置（不存在时才设置）
Boolean success = RedisUtil.setIfAbsent("lock:order:1", "locked", 30);

// 原子递增
Long count = RedisUtil.incrementValue("counter", 1L);

// 原子递减
Long count = RedisUtil.decrementValue("counter", 1L);

// 追加字符串
RedisUtil.append("log", "new content");

// 获取子串
String substr = RedisUtil.getValueRang("content", 0, 10);

// 位操作
RedisUtil.setValueBit("bitmap", 100, true);
Boolean bit = RedisUtil.getValueBit("bitmap", 100);
```

#### Hash 操作

```java
// 设置 Hash 字段
Map<String, Object> userMap = new HashMap<>();
userMap.put("name", "张三");
userMap.put("age", 30);
RedisUtil.putHash("user:profile:1", userMap);

// 设置 Hash 字段并指定过期时间
RedisUtil.putHash("user:profile:1", userMap, 7200);

// 获取单个字段
String name = RedisUtil.getHash("user:profile:1", "name");

// 获取整个 Hash
Map<Object, Object> profile = RedisUtil.getHash("user:profile:1");

// 删除 Hash 字段
RedisUtil.deleteHash("user:profile:1", "age");

// 检查字段是否存在
Boolean exists = RedisUtil.hasKeyHash("user:profile:1", "name");

// Hash 字段递增
RedisUtil.incrementHash("user:stats:1", "loginCount", 1L);

// 批量获取多个 Hash
List<Long> userIds = Arrays.asList(1L, 2L, 3L);
Map<Long, User> users = RedisUtil.hashMultiGetAll(userIds, User.class);
```

#### List 操作

```java
// 右侧推入
RedisUtil.pushList("queue:tasks", task);

// 左侧推入
RedisUtil.leftPushList("queue:tasks", task);

// 获取列表范围
List<Object> tasks = RedisUtil.getList("queue:tasks", 0, 10);

// 获取整个列表
List<Object> allTasks = RedisUtil.getList("queue:tasks");

// 获取指定索引元素
Object task = RedisUtil.getListIndex("queue:tasks", 0);

// 修剪列表
RedisUtil.listTrim("queue:tasks", 0, 100);

// 获取列表长度
Long size = RedisUtil.getListSize("queue:tasks");

// 更新指定索引元素
RedisUtil.updateListIndex("queue:tasks", 0, newTask);

// 删除元素
RedisUtil.removeList("queue:tasks", 1, task);
```

#### Set 操作

```java
// 添加成员
RedisUtil.addSet("tags:1", "java", "spring", "redis");

// 获取所有成员
Set<Object> tags = RedisUtil.getSet("tags:1");

// 检查成员是否存在
Boolean exists = RedisUtil.hasKeySet("tags:1", "java");

// 获取集合大小
Long size = RedisUtil.getSetSize("tags:1");

// 删除成员
RedisUtil.removeSet("tags:1", "redis");
```

### 2. 批量操作

**批量写入**（使用管道优化）：

```java
// 批量设置值
Map<String, Object> valueMap = new HashMap<>();
valueMap.put("user:1", user1);
valueMap.put("user:2", user2);
valueMap.put("user:3", user3);
RedisUtil.setValueList(valueMap, Duration.ofHours(1));

// 批量获取值（并行处理）
List<String> keys = Arrays.asList("user:1", "user:2", "user:3");
List<Object> values = RedisUtil.getValueList(keys);

// 多键获取
List<Object> values = RedisUtil.mGet(keys);
```

**特性**：

- 使用 Pipeline 减少网络往返
- 大批量自动分批处理（默认 5000 条/批）
- 并行处理提升性能（使用 ForkJoinPool）

### 3. Lua 脚本支持

**加载和执行 Lua 脚本**：

```java
// 加载脚本并获取 SHA
String luaScript = "return redis.call('get', KEYS[1])";
String sha = RedisUtil.loadScripts(luaScript);

// 检查脚本是否存在（支持集群）
Boolean exists = RedisUtil.scriptExists(sha);

// 执行 Lua 脚本
Object result = RedisUtil.lua(
    luaScript,
    ReturnType.VALUE,
    1,
    "key".getBytes()
);

// 通过 SHA 执行预加载的脚本
Object result = RedisUtil.evalSha(
    sha,
    ReturnType.VALUE,
    1,
    "key".getBytes()
);
```

**自定义 Lua 脚本加载器**：

```java
@Component
public class MyLuaScripts extends AbstractLuaScriptHolder {

    private String rateLimitScript;

    @Override
    public void load() {
        // 从 classpath 加载脚本
        rateLimitScript = readClasspathScript("lua/rate_limit.lua");

        // 预加载到 Redis
        RedisUtil.loadScripts(rateLimitScript);
    }

    public String getRateLimitScript() {
        return rateLimitScript;
    }
}
```

### 4. 主从读写分离

**配置主从模式**：

```yaml
spring:
  redis:
    # 哨兵模式
    sentinel:
      master: mymaster
      nodes:
        - 192.168.1.10:26379
        - 192.168.1.11:26379
        - 192.168.1.12:26379
    password: password
    lettuce:
      pool:
        max-active: 50
        max-idle: 100
        max-wait: 30s
        min-idle: 0
```

**使用不同的 RedisTemplate**：

```java
@Service
public class UserService {

    @Autowired
    @Qualifier("redisTemplate")
    private RedisTemplate<String, Object> redisTemplate;  // 从库优先读

    @Autowired
    @Qualifier("onlyMasterTemplate")
    private RedisTemplate<String, Object> masterTemplate;  // 仅主库

    public User getUser(Long id) {
        // 从库优先读取（读写分离）
        return (User) redisTemplate.opsForValue().get("user:" + id);
    }

    public void saveUser(User user) {
        // 强制写入主库
        masterTemplate.opsForValue().set("user:" + user.getId(), user);
    }
}
```

### 5. 键前缀支持

**配置统一前缀**：

```yaml
yunjin:
  cache:
    user-prefix: true
    key-prefix: "myapp:cache:"
```

**效果**：

```java
// 代码中使用
RedisUtil.setValue("user:1", user);

// 实际存储的键
// myapp:cache:user:1
```

**优势**：

- 多应用共享 Redis 时避免键冲突
- 便于按前缀批量清理
- 支持环境隔离（dev、test、prod）

### 6. Spring Cache 集成

**启用缓存**：

```java
@Configuration
@EnableCaching
public class CacheConfig {
}
```

**使用注解**：

```java
@Service
public class UserService {

    // 缓存查询结果
    @Cacheable(value = "users", key = "#id")
    public User getById(Long id) {
        return userMapper.selectById(id);
    }

    // 更新缓存
    @CachePut(value = "users", key = "#user.id")
    public User update(User user) {
        userMapper.updateById(user);
        return user;
    }

    // 删除缓存
    @CacheEvict(value = "users", key = "#id")
    public void delete(Long id) {
        userMapper.deleteById(id);
    }

    // 清空所有缓存
    @CacheEvict(value = "users", allEntries = true)
    public void deleteAll() {
        userMapper.delete(null);
    }
}
```

**配置缓存 TTL**：

```yaml
spring:
  cache:
    redis:
      time-to-live: 3600000  # 1 小时（毫秒）
```

### 7. 缓存管理接口

**实现 ICacheManager 接口**：

```java
@Component
public class UserCacheManager implements ICacheManager<Long, User> {

    @Override
    public void initCache() {
        // 预加载缓存
        List<User> users = userService.list();
        users.forEach(user -> putCache(user.getId(), user));
    }

    @Override
    public void clearCache() {
        // 清空缓存
        RedisUtil.deleteSpace("user:");
    }

    @Override
    public void putCache(Long key, User value) {
        RedisUtil.setValue(generateKey(key), value, 3600);
    }

    @Override
    public User getCache(Long key) {
        return RedisUtil.getValue(generateKey(key));
    }

    @Override
    public void deleteCache(Long key) {
        RedisUtil.delete(generateKey(key));
    }

    @Override
    public String generateKey(Long key) {
        return "user:" + key;
    }

    @Override
    public EvictionPolicy policy() {
        return EvictionPolicy.CacheAside;
    }

    @Override
    public CacheStats stats() {
        // 返回缓存统计信息
        return CacheStats.empty();
    }
}
```

### 8. 数据库操作

**清理操作**（谨慎使用）：

```java
// 清空当前数据库
RedisUtil.flushDb();

// 清空所有数据库
RedisUtil.flushAll();

// 按前缀批量删除（分批处理，避免阻塞）
RedisUtil.deleteSpace("temp:");
```

### 9. 序列化配置

**自动序列化特性**：

- **Long/BigInteger → String**: 防止 JavaScript 精度丢失
- **日期时间支持**: LocalDateTime、LocalDate、LocalTime
- **自定义日期格式**: 使用 Hutool DatePattern
- **JSON 序列化**: 使用 Jackson2JsonRedisSerializer

**示例**：

```java
// Java 对象
public class User {
    private Long id;  // 序列化为字符串 "123456789012345678"
    private LocalDateTime createTime;  // 序列化为 "2026-01-25 14:30:00"
    private BigDecimal amount;  // 保持精度
}

// 存储
RedisUtil.setValue("user:1", user);

// 读取（自动反序列化）
User user = RedisUtil.getValue("user:1");
```

## 配置说明

### 完整配置示例

```yaml
spring:
  cache:
    type: redis
    redis:
      time-to-live: 3600000  # 缓存 TTL（毫秒）

  redis:
    # 单机模式
    database: 0
    host: localhost
    port: 6379
    password: password
    timeout: 3000ms

    # 哨兵模式
    sentinel:
      master: mymaster
      nodes:
        - 192.168.1.10:26379
        - 192.168.1.11:26379
        - 192.168.1.12:26379

    # 集群模式
    cluster:
      nodes:
        - 192.168.1.10:6379
        - 192.168.1.11:6379
        - 192.168.1.12:6379
      max-redirects: 3

    # Lettuce 连接池配置
    lettuce:
      pool:
        max-active: 50      # 最大活跃连接数
        max-idle: 100       # 最大空闲连接数
        min-idle: 0         # 最小空闲连接数
        max-wait: 30s       # 最大等待时间
      shutdown-timeout: 100ms

# Carlos 缓存配置
yunjin:
  cache:
    user-prefix: true           # 启用键前缀
    key-prefix: "myapp:cache:"  # 键前缀
```

### 连接模式

#### 单机模式

```yaml
spring:
  redis:
    host: localhost
    port: 6379
    password: password
```

#### 哨兵模式

```yaml
spring:
  redis:
    sentinel:
      master: mymaster
      nodes:
        - 192.168.1.10:26379
        - 192.168.1.11:26379
        - 192.168.1.12:26379
    password: password
```

#### 集群模式

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
    password: password
```

## 依赖引入

```xml
<dependency>
    <groupId>com.carlos</groupId>
    <artifactId>carlos-redis</artifactId>
    <version>${carlos.version}</version>
</dependency>
```

## 依赖项

- **Spring Boot Data Redis**: Redis 集成
- **Lettuce**: Redis 客户端
- **Apache Commons Pool2**: 连接池
- **Fastjson**: JSON 序列化（2.0.60）
- **Hutool**: 工具库
- **Guava**: 缓存工具
- **carlos-core**: 核心基础模块
- **carlos-json**: JSON 工具

## 使用场景

1. **数据缓存**: 热点数据缓存，减轻数据库压力
2. **会话管理**: 分布式会话存储
3. **分布式锁**: 使用 SETNX 实现分布式锁
4. **计数器**: 点赞数、浏览量等实时计数
5. **排行榜**: 使用 ZSet 实现排行榜
6. **消息队列**: 使用 List 实现简单队列
7. **限流**: 使用 Lua 脚本实现限流
8. **去重**: 使用 Set 实现去重

## 注意事项

1. **键命名规范**: 建议使用冒号分隔，如 `user:profile:1`
2. **过期时间**: 合理设置 TTL，避免内存溢出
3. **批量操作**: 大批量操作会自动分批，避免阻塞
4. **主从延迟**: 读写分离时注意主从同步延迟
5. **Lua 脚本**: 复杂逻辑使用 Lua 脚本保证原子性
6. **键前缀**: 多应用共享 Redis 时务必配置前缀
7. **连接池**: 根据并发量合理配置连接池大小
8. **序列化**: Long 类型会序列化为 String，注意类型转换
9. **清理操作**: `flushAll` 和 `flushDb` 谨慎使用
10. **扫描操作**: 使用 `scanKeys` 而非 `keys` 避免阻塞

## 性能优化

1. **管道操作**: 批量操作自动使用 Pipeline
2. **并行处理**: 大批量读取使用 ForkJoinPool 并行处理
3. **连接复用**: Lettuce 基于 Netty，连接复用效率高
4. **读写分离**: 从库优先读取，减轻主库压力
5. **批量限制**: 自动分批处理，避免单次操作过大
6. **Lua 脚本**: 复杂操作使用 Lua 减少网络往返

## 版本要求

- JDK 17+
- Spring Boot 3.5.8+
- Redis 5.0+
- Maven 3.8+

## 相关模块

- `carlos-core`: 核心基础模块
- `carlos-json`: JSON 工具
- `carlos-redisson`: Redisson 分布式锁和缓存
