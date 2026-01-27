# yunjin-redisson

## 模块简介

`yunjin-redisson` 是 YunJin 框架的 Redisson 分布式锁和缓存模块，基于 Redisson 3.51.0 构建。该模块提供了注解驱动的分布式锁、手动锁管理、Spring Data Redis 集成等功能，支持单机、哨兵、集群等多种 Redis 部署模式。

## 主要功能

### 1. 注解式分布式锁

**@RedisLock 注解**提供声明式分布式锁：

```java
@Service
public class OrderService {

    // 基础用法
    @RedisLock(name = "order", key = "#orderId")
    public void processOrder(Long orderId) {
        // 业务逻辑
        // 自动加锁和释放锁
    }

    // 自定义过期时间
    @RedisLock(
        name = "payment",
        key = "#orderId",
        expire = 10000,
        timeUnit = TimeUnit.MILLISECONDS
    )
    public void processPayment(Long orderId) {
        // 10 秒后自动释放锁
    }

    // SpEL 表达式支持
    @RedisLock(name = "user", key = "#user.id")
    public void updateUser(User user) {
        // 使用对象属性作为锁键
    }

    // 复杂 SpEL 表达式
    @RedisLock(name = "order", key = "#order.userId + ':' + #order.id")
    public void createOrder(Order order) {
        // 组合多个属性作为锁键
    }
}
```

**注解参数**：

| 参数         | 类型       | 默认值          | 说明              |
|------------|----------|--------------|-----------------|
| `name`     | String   | ""           | 锁名称（业务标识）       |
| `key`      | String   | ""           | 锁键（支持 SpEL 表达式） |
| `expire`   | int      | 5000         | 锁过期时间           |
| `timeUnit` | TimeUnit | MILLISECONDS | 时间单位            |

**锁键格式**：

```
{prefix}{name}:{key}
```

**示例**：

```java
@RedisLock(name = "order", key = "#orderId")
// 实际锁键: redisson_lock:order:123
```

### 2. 手动锁管理

**RedissonLockUtil** 提供编程式锁管理：

```java
@Service
public class InventoryService {

    public void deductStock(Long productId, Integer quantity) {
        String lockKey = "stock:" + productId;

        // 尝试获取锁
        boolean locked = RedissonLockUtil.addLock(lockKey, 5000, TimeUnit.MILLISECONDS);

        if (!locked) {
            throw new ServiceException("获取锁失败，请稍后重试");
        }

        try {
            // 业务逻辑
            Product product = productService.getById(productId);
            if (product.getStock() < quantity) {
                throw new ServiceException("库存不足");
            }
            product.setStock(product.getStock() - quantity);
            productService.updateById(product);
        } finally {
            // 释放锁
            RedissonLockUtil.releaseLock(lockKey);
        }
    }
}
```

**API 方法**：

```java
// 获取锁
boolean success = RedissonLockUtil.addLock(
    "lockName",           // 锁名称
    5000,                 // 过期时间
    TimeUnit.MILLISECONDS // 时间单位
);

// 释放锁
boolean released = RedissonLockUtil.releaseLock("lockName");
```

### 3. SpEL 表达式支持

**支持的 SpEL 表达式**：

```java
// 1. 简单属性访问
@RedisLock(name = "user", key = "#userId")
public void method(Long userId) { }

// 2. 对象属性访问
@RedisLock(name = "order", key = "#order.id")
public void method(Order order) { }

// 3. 嵌套属性访问
@RedisLock(name = "order", key = "#order.user.id")
public void method(Order order) { }

// 4. 字符串拼接
@RedisLock(name = "order", key = "#userId + ':' + #orderId")
public void method(Long userId, Long orderId) { }

// 5. 方法调用
@RedisLock(name = "user", key = "#user.getId()")
public void method(User user) { }

// 6. 条件表达式
@RedisLock(name = "order", key = "#order.type == 'VIP' ? 'vip:' + #order.id : 'normal:' + #order.id")
public void method(Order order) { }

// 7. 集合操作
@RedisLock(name = "batch", key = "#ids.size() + ':' + #ids[0]")
public void method(List<Long> ids) { }
```

### 4. 分布式锁场景

#### 场景 1：防止重复提交

```java
@RestController
@RequestMapping("/order")
public class OrderController {

    @PostMapping("/create")
    @RedisLock(name = "order:create", key = "#userId", expire = 3000)
    public Result<Order> createOrder(@RequestParam Long userId, @RequestBody OrderDTO dto) {
        // 3 秒内同一用户只能提交一次订单
        Order order = orderService.create(userId, dto);
        return Result.ok(order);
    }
}
```

#### 场景 2：库存扣减

```java
@Service
public class InventoryService {

    @RedisLock(name = "stock", key = "#productId", expire = 5000)
    public void deductStock(Long productId, Integer quantity) {
        Product product = productService.getById(productId);
        if (product.getStock() < quantity) {
            throw new ServiceException("库存不足");
        }
        product.setStock(product.getStock() - quantity);
        productService.updateById(product);
    }
}
```

#### 场景 3：定时任务防止重复执行

```java
@Component
public class DataSyncTask {

    @Scheduled(cron = "0 0 * * * ?")
    @RedisLock(name = "task:sync", key = "'data'", expire = 3600000)
    public void syncData() {
        // 1 小时内只执行一次
        // 即使多个实例同时触发，也只有一个能获取锁
        dataService.sync();
    }
}
```

#### 场景 4：分布式事务

```java
@Service
public class TransferService {

    @RedisLock(
        name = "transfer",
        key = "#fromAccount + ':' + #toAccount",
        expire = 10000
    )
    @Transactional
    public void transfer(Long fromAccount, Long toAccount, BigDecimal amount) {
        // 转账操作加锁，防止并发问题
        accountService.deduct(fromAccount, amount);
        accountService.add(toAccount, amount);
    }
}
```

### 5. 锁的特性

**可重入锁**：

- Redisson 的 RLock 是可重入锁
- 同一线程可以多次获取同一把锁
- 释放次数必须与获取次数相同

**自动续期**：

- Redisson 的看门狗机制（Watchdog）
- 默认每 10 秒自动续期
- 防止业务执行时间过长导致锁过期

**公平锁**：

```java
// 如需公平锁，可以直接使用 Redisson API
RLock fairLock = redissonClient.getFairLock("myLock");
fairLock.lock();
try {
    // 业务逻辑
} finally {
    fairLock.unlock();
}
```

**读写锁**：

```java
// 读写锁支持
RReadWriteLock rwLock = redissonClient.getReadWriteLock("myLock");

// 读锁
RLock readLock = rwLock.readLock();
readLock.lock();
try {
    // 读操作
} finally {
    readLock.unlock();
}

// 写锁
RLock writeLock = rwLock.writeLock();
writeLock.lock();
try {
    // 写操作
} finally {
    writeLock.unlock();
}
```

## 配置说明

### 基础配置

```yaml
spring:
  redis:
    redisson:
      enabled: true                    # 启用分布式锁
      prefix: "redisson_lock:"         # 锁键前缀
      is-use-system-clock: false       # 使用系统时钟
      config: classpath:redisson-single.yml  # Redisson 配置文件
```

### 单机模式配置

**redisson-single.yml**：

```yaml
singleServerConfig:
  address: redis://localhost:6379
  database: 0
  password: password
  idleConnectionTimeout: 10000
  connectTimeout: 10000
  timeout: 3000
  retryAttempts: 3
  retryInterval: 1500
  connectionPoolSize: 20
  connectionMinimumIdleSize: 4
  subscriptionConnectionPoolSize: 10
  subscriptionConnectionMinimumIdleSize: 1
  subscriptionsPerConnection: 5
  dnsMonitoringInterval: 5000

threads: 0  # 0 表示自动检测 CPU 核心数
nettyThreads: 0
codec: !<org.redisson.codec.JsonJacksonCodec> {}
transportMode: NIO
```

### 哨兵模式配置

**redisson-sentinel.yml**：

```yaml
sentinelServersConfig:
  masterName: mymaster
  sentinelAddresses:
    - redis://192.168.1.10:26379
    - redis://192.168.1.11:26379
    - redis://192.168.1.12:26379
  database: 0
  password: password
  masterConnectionPoolSize: 20
  slaveConnectionPoolSize: 20
  idleConnectionTimeout: 10000
  connectTimeout: 10000
  timeout: 3000
  retryAttempts: 3
  retryInterval: 1500

threads: 0
nettyThreads: 0
codec: !<org.redisson.codec.JsonJacksonCodec> {}
transportMode: NIO
```

### 集群模式配置

**redisson-cluster.yml**：

```yaml
clusterServersConfig:
  nodeAddresses:
    - redis://192.168.1.10:6379
    - redis://192.168.1.11:6379
    - redis://192.168.1.12:6379
    - redis://192.168.1.13:6379
    - redis://192.168.1.14:6379
    - redis://192.168.1.15:6379
  password: password
  masterConnectionPoolSize: 20
  slaveConnectionPoolSize: 20
  idleConnectionTimeout: 10000
  connectTimeout: 10000
  timeout: 3000
  retryAttempts: 3
  retryInterval: 1500
  scanInterval: 1000

threads: 0
nettyThreads: 0
codec: !<org.redisson.codec.JsonJacksonCodec> {}
transportMode: NIO
```

## 依赖引入

```xml
<dependency>
    <groupId>com.yunjin</groupId>
    <artifactId>yunjin-redisson</artifactId>
    <version>${yunjin.version}</version>
</dependency>
```

## 依赖项

- **Redisson Spring Boot Starter**: 3.51.0
- **yunjin-core**: 核心基础模块（SpEL 工具、异常）
- **Hutool**: 工具库（字符串工具）
- **AspectJ**: AOP 支持
- **Spring Boot**: 自动配置

## 使用示例

### 完整示例

```java
// 1. 配置文件
// application.yml
spring:
  redis:
    redisson:
      enabled: true
      prefix: "myapp:lock:"
      config: classpath:redisson-single.yml

// redisson-single.yml
singleServerConfig:
  address: redis://localhost:6379
  database: 0
  password: password

// 2. 服务类
@Service
public class OrderService {

    // 注解式锁
    @RedisLock(name = "order", key = "#orderId", expire = 5000)
    public void processOrder(Long orderId) {
        // 业务逻辑
        Order order = orderMapper.selectById(orderId);
        order.setStatus(OrderStatus.PROCESSING);
        orderMapper.updateById(order);
    }

    // 手动锁
    public void cancelOrder(Long orderId) {
        String lockKey = "order:cancel:" + orderId;
        boolean locked = RedissonLockUtil.addLock(lockKey, 3000, TimeUnit.MILLISECONDS);

        if (!locked) {
            throw new ServiceException("操作频繁，请稍后重试");
        }

        try {
            Order order = orderMapper.selectById(orderId);
            if (order.getStatus() != OrderStatus.PENDING) {
                throw new ServiceException("订单状态不允许取消");
            }
            order.setStatus(OrderStatus.CANCELLED);
            orderMapper.updateById(order);
        } finally {
            RedissonLockUtil.releaseLock(lockKey);
        }
    }
}

// 3. 控制器
@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/process/{id}")
    public Result<Void> processOrder(@PathVariable Long id) {
        orderService.processOrder(id);
        return Result.ok();
    }

    @PostMapping("/cancel/{id}")
    public Result<Void> cancelOrder(@PathVariable Long id) {
        orderService.cancelOrder(id);
        return Result.ok();
    }
}
```

## 注意事项

1. **锁过期时间**: 设置合理的过期时间，避免死锁
2. **业务执行时间**: 确保业务执行时间小于锁过期时间
3. **异常处理**: 使用 try-finally 确保锁一定被释放
4. **锁粒度**: 锁粒度越小，并发性能越好
5. **SpEL 表达式**: 确保表达式能正确解析，避免运行时错误
6. **可重入性**: 注意可重入锁的特性，避免死锁
7. **Redis 可用性**: 确保 Redis 高可用，避免锁服务不可用
8. **锁前缀**: 多应用共享 Redis 时配置不同前缀
9. **看门狗**: Redisson 自动续期，长时间业务无需担心锁过期
10. **集群模式**: 集群模式下注意 RedLock 算法的使用

## 性能优化

1. **锁粒度**: 尽量减小锁的范围，提高并发性能
2. **锁超时**: 合理设置超时时间，避免长时间等待
3. **连接池**: 配置合理的连接池大小
4. **网络延迟**: 使用本地 Redis 或低延迟网络
5. **批量操作**: 避免在锁内执行批量操作

## 常见问题

### 1. 锁获取失败

**原因**：

- 锁已被其他线程持有
- Redis 连接失败
- 网络超时

**解决**：

```java
boolean locked = RedissonLockUtil.addLock(lockKey, 5000, TimeUnit.MILLISECONDS);
if (!locked) {
    throw new ServiceException("系统繁忙，请稍后重试");
}
```

### 2. 锁未释放

**原因**：

- 业务代码抛出异常，未执行 finally
- 应用崩溃

**解决**：

- 使用 try-finally 确保释放
- 设置合理的过期时间，自动释放

### 3. 死锁

**原因**：

- 多个锁的获取顺序不一致
- 锁未释放

**解决**：

- 统一锁的获取顺序
- 使用超时机制
- 设置锁过期时间

## 版本要求

- JDK 17+
- Spring Boot 3.5.8+
- Redis 5.0+
- Redisson 3.51.0+
- Maven 3.8+

## 相关模块

- `yunjin-core`: 核心基础模块
- `yunjin-redis`: Redis 缓存模块
