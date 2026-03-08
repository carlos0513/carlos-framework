# carlos-mongodb

## 模块简介

`carlos-mongodb` 是 Carlos 框架的 MongoDB 数据库集成模块，基于 Spring Data MongoDB 提供对 MongoDB 数据库的访问支持。该模块提供了与
`carlos-spring-boot-starter-mybatis` 类似的基础设施，包括通用的 Service 基类、字段自动填充、分页支持等，使 MongoDB 开发体验与
MyBatis 保持一致性。

## 主要功能

### 1. Spring Data MongoDB 集成

基于 Spring Boot Starter Data MongoDB 提供完整的 MongoDB 访问支持：

```java
// 实体类定义
@Document(collection = "users")
@Data
public class User {
    @Id
    private Long id;

    @Indexed(unique = true)
    private String username;

    private String email;
    private String phone;

    @Field("create_time")
    private LocalDateTime createTime;

    @Field("update_time")
    private LocalDateTime updateTime;
}

// Repository 接口
@Repository
public interface UserRepository extends MongoRepository<User, String> {

    // 自定义查询方法
    User findByUsername(String username);

    List<User> findByEmailContaining(String email);

    @Query("{'createTime': {$gte: ?0}}")
    List<User> findAfterCreateTime(LocalDateTime time);
}
```

### 2. 通用 Service 基类

提供与 MyBatis-Plus 风格一致的 Service 基类：

```java
// 基础 Service 接口
public interface BaseService<T> {

    T save(T entity);

    T update(T entity);

    T getById(String id);

    List<T> list();

    Page<T> page(PageParam param);

    boolean removeById(String id);
}

// 基础 Service 实现
@Service
public abstract class BaseServiceImpl<T> implements BaseService<T> {

    @Autowired
    protected MongoTemplate mongoTemplate;

    @Override
    public T save(T entity) {
        if (entity instanceof AbstractMybatisCommonField) {
            AbstractMybatisCommonField commonField = (AbstractMybatisCommonField) entity;
            if (commonField.getCreateTime() == null) {
                commonField.setCreateTime(LocalDateTime.now());
            }
            commonField.setUpdateTime(LocalDateTime.now());
        }

        return mongoTemplate.save(entity);
    }

    @Override
    public Page<T> page(PageParam param) {
        // MongoDB 分页实现
        Query query = new Query();
        long total = mongoTemplate.count(query, getEntityClass());

        query.with(PageRequest.of(param.getCurrent() - 1, param.getSize()));
        List<T> records = mongoTemplate.find(query, getEntityClass());

        return new Page<>(param.getCurrent(), param.getSize(), total, records);
    }

    protected abstract Class<T> getEntityClass();
}
```

### 3. 字段自动填充

支持与 MyBatis-Plus 类似的字段自动填充机制：

```java
// 字段填充策略枚举
public enum FieldFill {
    DEFAULT,        // 默认不处理
    INSERT,         // 插入时填充
    UPDATE,         // 更新时填充
    INSERT_UPDATE   // 插入和更新时填充
}

// 字段填充注解
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface TableField {

    String value() default "";

    FieldFill fill() default FieldFill.DEFAULT;
}

// 元对象处理器接口
public interface MetaObjectHandler {

    void insertFill(MetaObject metaObject);

    void updateFill(MetaObject metaObject);
}
```

### 4. MongoDB 分页支持

提供专门为 MongoDB 设计的分页工具：

```java
// MongoDB 分页对象
@Data
public class MongoPage<T> {

    private long current;    // 当前页
    private long size;       // 每页大小
    private long total;      // 总记录数
    private List<T> records; // 当前页数据

    // 分页参数转换
    public static <T> MongoPage<T> of(PageParam param, List<T> records, long total) {
        MongoPage<T> page = new MongoPage<>();
        page.setCurrent(param.getCurrent());
        page.setSize(param.getSize());
        page.setTotal(total);
        page.setRecords(records);
        return page;
    }

    // 转换为通用分页对象
    public Page<T> toPage() {
        return new Page<>(current, size, total, records);
    }
}
```

### 5. 通用字段抽象

提供与 MyBatis 实体一致的通用字段抽象：

```java
// 通用字段接口
public interface MybatisCommonField {

    LocalDateTime getCreateTime();

    void setCreateTime(LocalDateTime createTime);

    LocalDateTime getUpdateTime();

    void setUpdateTime(LocalDateTime updateTime);

    String getCreateBy();

    void setCreateBy(String createBy);

    String getUpdateBy();

    void setUpdateBy(String updateBy);
}

// 抽象实现
@Data
public abstract class AbstractMybatisCommonField implements MybatisCommonField {

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableField(fill = FieldFill.INSERT)
    private Long createBy;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateBy;
}
```

## 快速开始

### 1. 添加依赖

```xml
<dependency>
    <groupId>com.carlos</groupId>
    <artifactId>carlos-mongodb</artifactId>
    <version>${carlos.version}</version>
</dependency>
```

### 2. MongoDB 配置

在 `application.yml` 中配置 MongoDB 连接：

```yaml
spring:
  data:
    mongodb:
      # 单机模式
      uri: mongodb://username:password@localhost:27017/database

      # 或者分开配置
      host: localhost
      port: 27017
      database: carlos
      username: admin
      password: password

      # 连接池配置
      auto-index-creation: true

  # 可选：配置多个数据源
  mongodb:
    primary:
      uri: mongodb://localhost:27017/primary
    secondary:
      uri: mongodb://localhost:27017/secondary
```

### 3. 基本使用示例

#### 实体类定义：

```java
@Document(collection = "products")
@Data
@EqualsAndHashCode(callSuper = true)
public class Product extends AbstractMybatisCommonField {

    @Id
    private Long id;

    @Indexed
    private String productCode;

    private String productName;

    private BigDecimal price;

    private Integer stock;

    @Field("category_id")
    private String categoryId;

    private List<String> tags;

    private Map<String, Object> attributes;

    // 地理位置字段（用于地理查询）
    private GeoJsonPoint location;
}
```

#### Repository 定义：

```java
@Repository
public interface ProductRepository extends MongoRepository<Product, String> {

    // 基本查询方法
    List<Product> findByProductNameContaining(String name);

    List<Product> findByPriceBetween(BigDecimal min, BigDecimal max);

    List<Product> findByTagsIn(List<String> tags);

    // 地理查询
    List<Product> findByLocationNear(Point point, Distance distance);

    // 使用 @Query 注解
    @Query("{'price': {$gte: ?0, $lte: ?1}}")
    List<Product> findProductsByPriceRange(BigDecimal minPrice, BigDecimal maxPrice);

    // 聚合查询
    @Aggregation(pipeline = {
        "{'$match': {'categoryId': ?0}}",
        "{'$group': {'_id': '$categoryId', 'total': {'$sum': '$stock'}}}"
    })
    List<CategoryStock> getStockByCategory(String categoryId);
}
```

#### Service 实现：

```java
@Service
public class ProductServiceImpl extends BaseServiceImpl<Product> implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Override
    public Product saveProduct(Product product) {
        // 业务逻辑
        if (product.getStock() < 0) {
            throw new BusinessException("库存不能为负数");
        }

        return save(product);
    }

    @Override
    public Page<Product> searchProducts(ProductQueryParam param) {
        Query query = new Query();

        // 构建查询条件
        if (StringUtils.hasText(param.getKeyword())) {
            Criteria keywordCriteria = new Criteria().orOperator(
                Criteria.where("productName").regex(param.getKeyword(), "i"),
                Criteria.where("productCode").regex(param.getKeyword(), "i")
            );
            query.addCriteria(keywordCriteria);
        }

        if (param.getMinPrice() != null && param.getMaxPrice() != null) {
            query.addCriteria(Criteria.where("price")
                .gte(param.getMinPrice()).lte(param.getMaxPrice()));
        }

        if (param.getCategoryId() != null) {
            query.addCriteria(Criteria.where("categoryId").is(param.getCategoryId()));
        }

        // 执行分页查询
        return page(query, param);
    }

    @Override
    protected Class<Product> getEntityClass() {
        return Product.class;
    }
}
```

#### Controller 使用：

```java
@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping
    public Result<Product> createProduct(@RequestBody @Valid CreateProductRequest request) {
        Product product = convertToEntity(request);
        Product saved = productService.saveProduct(product);
        return Result.ok(saved);
    }

    @GetMapping("/search")
    public Result<Page<Product>> searchProducts(ProductQueryParam param) {
        Page<Product> page = productService.searchProducts(param);
        return Result.ok(page);
    }

    @GetMapping("/nearby")
    public Result<List<Product>> findNearbyProducts(
            @RequestParam double longitude,
            @RequestParam double latitude,
            @RequestParam(defaultValue = "1000") double distance) {

        Point point = new Point(longitude, latitude);
        List<Product> products = productService.findNearby(point, distance);
        return Result.ok(products);
    }
}
```

### 4. 字段自动填充配置

实现自定义的元对象处理器：

```java
@Component
public class CustomMetaObjectHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        if (metaObject.getOriginalObject() instanceof AbstractMybatisCommonField) {
            AbstractMybatisCommonField entity = (AbstractMybatisCommonField) metaObject.getOriginalObject();

            // 设置创建时间
            if (entity.getCreateTime() == null) {
                entity.setCreateTime(LocalDateTime.now());
            }

            // 设置更新时间
            entity.setUpdateTime(LocalDateTime.now());

            // 设置创建人（从用户上下文获取）
            UserContext userContext = CurrentUser.getUserContext();
            if (userContext != null) {
                if (entity.getCreateBy() == null) {
                    entity.setCreateBy(userContext.getAccount());
                }
                entity.setUpdateBy(userContext.getAccount());
            }
        }
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        if (metaObject.getOriginalObject() instanceof AbstractMybatisCommonField) {
            AbstractMybatisCommonField entity = (AbstractMybatisCommonField) metaObject.getOriginalObject();

            // 设置更新时间
            entity.setUpdateTime(LocalDateTime.now());

            // 设置更新人
            UserContext userContext = CurrentUser.getUserContext();
            if (userContext != null) {
                entity.setUpdateBy(userContext.getAccount());
            }
        }
    }
}
```

## 详细功能说明

### 1. 多数据源支持

支持配置多个 MongoDB 数据源：

```java
@Configuration
public class MultipleMongoConfig {

    @Bean(name = "primaryMongoTemplate")
    public MongoTemplate primaryMongoTemplate() {
        return new MongoTemplate(primaryMongoDbFactory());
    }

    @Bean(name = "secondaryMongoTemplate")
    public MongoTemplate secondaryMongoTemplate() {
        return new MongoTemplate(secondaryMongoDbFactory());
    }

    @Primary
    @Bean
    public MongoDbFactory primaryMongoDbFactory() {
        return new SimpleMongoClientDbFactory("mongodb://localhost:27017/primary");
    }

    @Bean
    public MongoDbFactory secondaryMongoDbFactory() {
        return new SimpleMongoClientDbFactory("mongodb://localhost:27017/secondary");
    }
}
```

### 2. 事务支持

MongoDB 4.0+ 支持事务，需要在配置中启用：

```yaml
spring:
  data:
    mongodb:
      # 副本集或分片集群才支持事务
      uri: mongodb://localhost:27017,localhost:27018,localhost:27019/database?replicaSet=rs0
```

使用事务：

```java
@Service
public class OrderService {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Transactional
    public void createOrder(Order order) {
        // 保存订单
        mongoTemplate.save(order);

        // 扣减库存
        Query query = new Query(Criteria.where("productId").is(order.getProductId()));
        Update update = new Update().inc("stock", -order.getQuantity());
        mongoTemplate.updateFirst(query, update, Product.class);

        // 记录日志
        OrderLog log = new OrderLog(order.getId(), "CREATED");
        mongoTemplate.save(log);
    }
}
```

### 3. 索引管理

支持自动创建索引和手动管理索引：

```java
@Component
public class IndexManager implements CommandLineRunner {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public void run(String... args) {
        // 创建复合索引
        mongoTemplate.indexOps(Product.class).ensureIndex(
            new Index().on("categoryId", Sort.Direction.ASC)
                      .on("price", Sort.Direction.ASC)
                      .named("category_price_idx")
        );

        // 创建文本索引
        mongoTemplate.indexOps(Product.class).ensureIndex(
            new Index().on("productName", Sort.Direction.ASC)
                      .on("description", Sort.Direction.ASC)
                      .named("text_search_idx")
        );

        // 创建地理空间索引
        mongoTemplate.indexOps(Product.class).ensureIndex(
            new Index().on("location", Sort.Direction.ASC).named("location_idx")
        );
    }
}
```

### 4. 聚合框架支持

支持 MongoDB 强大的聚合框架：

```java
@Service
public class ReportService {

    @Autowired
    private MongoTemplate mongoTemplate;

    public SalesReport getSalesReport(LocalDate startDate, LocalDate endDate) {
        Aggregation aggregation = Aggregation.newAggregation(
            Aggregation.match(Criteria.where("orderDate")
                .gte(startDate).lte(endDate)),
            Aggregation.group("productId")
                .sum("quantity").as("totalQuantity")
                .sum(ConvertOperators.Multiply.multiply("$price", "$quantity")).as("totalAmount")
                .avg("price").as("avgPrice"),
            Aggregation.sort(Sort.Direction.DESC, "totalAmount"),
            Aggregation.limit(10)
        );

        AggregationResults<SalesData> results =
            mongoTemplate.aggregate(aggregation, "orders", SalesData.class);

        return new SalesReport(results.getMappedResults());
    }
}
```

### 5. 地理空间查询

支持地理空间数据存储和查询：

```java
@Service
public class LocationService {

    @Autowired
    private MongoTemplate mongoTemplate;

    public List<Store> findNearbyStores(double longitude, double latitude, double maxDistance) {
        Point point = new Point(longitude, latitude);
        Distance distance = new Distance(maxDistance, Metrics.KILOMETERS);

        NearQuery nearQuery = NearQuery.near(point)
            .maxDistance(distance)
            .spherical(true);

        GeoResults<Store> results = mongoTemplate.geoNear(nearQuery, Store.class);

        return results.getContent().stream()
            .map(GeoResult::getContent)
            .collect(Collectors.toList());
    }

    public boolean isWithinArea(double longitude, double latitude, Polygon area) {
        Point point = new Point(longitude, latitude);
        Query query = Query.query(Criteria.where("location").within(area));

        return mongoTemplate.exists(query, "locations");
    }
}
```

## 配置项说明

| 配置项                                           | 类型      | 默认值         | 说明             |
|-----------------------------------------------|---------|-------------|----------------|
| `spring.data.mongodb.uri`                     | String  | -           | MongoDB 连接 URI |
| `spring.data.mongodb.host`                    | String  | `localhost` | 主机地址           |
| `spring.data.mongodb.port`                    | int     | `27017`     | 端口号            |
| `spring.data.mongodb.database`                | String  | -           | 数据库名           |
| `spring.data.mongodb.username`                | String  | -           | 用户名            |
| `spring.data.mongodb.password`                | String  | -           | 密码             |
| `spring.data.mongodb.authentication-database` | String  | `admin`     | 认证数据库          |
| `spring.data.mongodb.auto-index-creation`     | boolean | `true`      | 是否自动创建索引       |

## 依赖项

- `carlos-spring-boot-core`：基础工具类、通用字段抽象
- `spring-boot-starter-data-mongodb`：Spring Data MongoDB 核心依赖
- `spring-boot-starter`：Spring Boot 基础依赖

## 注意事项

### 1. 性能考虑

- MongoDB 适合文档型数据存储，不适合强事务场景
- 合理设计文档结构，避免嵌套过深
- 为常用查询字段创建索引
- 使用投影（Projection）减少返回数据量

### 2. 数据建模建议

- 根据查询模式设计文档结构
- 适当使用嵌入文档减少 JOIN 操作
- 控制文档大小，避免单个文档过大
- 考虑数据增长和分片策略

### 3. 使用建议

- 优先使用 Repository 接口的声明式查询
- 复杂查询使用 MongoTemplate
- 批量操作使用 BulkOperations
- 定期优化索引和查询性能

### 4. 与 MyBatis 对比

- **MongoDB**：适合文档型、非结构化数据，读写性能高
- **MySQL + MyBatis**：适合关系型、结构化数据，事务支持好
- 根据业务需求选择合适的存储方案

## 版本要求

- JDK 17+
- Spring Boot 3.5.8+
- Spring Data MongoDB 4.x+
- MongoDB Server 4.4+（建议 5.0+）

## 相关模块

- **carlos-spring-boot-core**：基础工具类、通用字段抽象
- **carlos-spring-boot-starter-mybatis**：关系型数据库访问，可对比参考
- **carlos-spring-boot-starter-redis**：缓存支持，可与 MongoDB 配合使用
