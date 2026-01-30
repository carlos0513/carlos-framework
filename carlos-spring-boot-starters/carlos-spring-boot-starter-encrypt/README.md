# carlos-encrypt

## 模块简介

`carlos-encrypt` 是 Carlos 框架的加密工具模块，提供了基于中国国密算法的加密解密功能。该模块支持 SM2（非对称加密）和 SM4（对称加密）两种国密算法，基于 BouncyCastle 1.70 和 Hutool 5.8.40 实现。

## 主要功能

### 1. SM4 对称加密

**SM4** 是中国国家密码管理局发布的对称加密算法，密钥长度为 128 位。

#### 基础用法

```java
// 使用配置的默认模式加密（BASE64 或 HEX）
String encrypted = EncryptUtil.encrypt("敏感数据");

// 解密
String decrypted = EncryptUtil.decrypt(encrypted);
```

#### 指定输出格式

```java
// HEX 格式加密
String hexEncrypted = EncryptUtil.sm4EncryptHex("敏感数据");
// 结果: 3a5f8c2d...（十六进制字符串）

// BASE64 格式加密
String base64Encrypted = EncryptUtil.sm4EncryptBase64("敏感数据");
// 结果: Ol+MLd...（Base64 字符串）

// 解密（自动识别格式）
String decrypted = EncryptUtil.decrypt(hexEncrypted);
```

#### 加密模式

**CBC 模式**（推荐）：

```yaml
carlos:
  encrypt:
    sm4:
      enabled: true
      encrypt-mode: cbc
      key: "your-secret-key-16bytes"
      iv: "your-iv-16bytes"
      store-type: base64
```

**ECB 模式**：

```yaml
carlos:
  encrypt:
    sm4:
      enabled: true
      encrypt-mode: ecb
      key: "your-secret-key-16bytes"
      store-type: hex
```

### 2. SM2 非对称加密

**SM2** 是中国国家密码管理局发布的椭圆曲线公钥密码算法，基于 256 位椭圆曲线。

#### 生成密钥对

```java
// 生成 SM2 密钥对
Sm2KeyPair keyPair = EncryptUtil.generateSm2KeyPair();

// 公钥（130 位十六进制字符串，以 "04" 开头）
String publicKey = keyPair.getPublicKey();
// 示例: 0403c7a7250306531238d49c2e0146e48217f2d65b28ed1d61565bd40472c7516bc78574de0a89f9067328558d656ecdc2504f49bb2bd843a9cfa9b404463ed453

// 私钥（64 位十六进制字符串）
String privateKey = keyPair.getPrivateKey();
// 示例: 3134665e1e009bab370738fd5ff1dabc7f390b683885c321f1523f4f725e8aa1
```

#### 加密解密

```java
// 使用公钥加密
String encrypted = EncryptUtil.sm2Encrypt("敏感数据");

// 使用私钥解密
String decrypted = EncryptUtil.sm2Decrypt(encrypted);
```

#### 指定输出格式

```java
// HEX 格式加密
String hexEncrypted = EncryptUtil.sm2EncryptHex("敏感数据");

// BASE64 格式加密
String base64Encrypted = EncryptUtil.sm2EncryptBase64("敏感数据");

// 解密（自动识别格式）
String decrypted = EncryptUtil.sm2Decrypt(hexEncrypted);
```

### 3. 实际应用场景

#### 场景 1：敏感数据存储

```java
@Service
public class UserService {

    public void saveUser(User user) {
        // 加密身份证号
        String encryptedIdCard = EncryptUtil.encrypt(user.getIdCard());
        user.setIdCard(encryptedIdCard);

        // 加密手机号
        String encryptedPhone = EncryptUtil.encrypt(user.getPhone());
        user.setPhone(encryptedPhone);

        userMapper.insert(user);
    }

    public User getUser(Long id) {
        User user = userMapper.selectById(id);

        // 解密身份证号
        String idCard = EncryptUtil.decrypt(user.getIdCard());
        user.setIdCard(idCard);

        // 解密手机号
        String phone = EncryptUtil.decrypt(user.getPhone());
        user.setPhone(phone);

        return user;
    }
}
```

#### 场景 2：API 数据加密传输

```java
@RestController
@RequestMapping("/api")
public class ApiController {

    // 接收加密数据
    @PostMapping("/submit")
    public Result<Void> submit(@RequestBody EncryptedRequest request) {
        // 解密请求数据
        String decryptedData = EncryptUtil.sm2Decrypt(request.getData());

        // 处理业务逻辑
        processData(decryptedData);

        return Result.ok();
    }

    // 返回加密数据
    @GetMapping("/data")
    public Result<EncryptedResponse> getData() {
        String sensitiveData = getSensitiveData();

        // 加密响应数据
        String encryptedData = EncryptUtil.sm2Encrypt(sensitiveData);

        EncryptedResponse response = new EncryptedResponse();
        response.setData(encryptedData);

        return Result.ok(response);
    }
}
```

#### 场景 3：配置文件敏感信息加密

```java
@Component
public class ConfigLoader {

    @Value("${database.password}")
    private String encryptedPassword;

    @PostConstruct
    public void init() {
        // 解密数据库密码
        String password = EncryptUtil.decrypt(encryptedPassword);

        // 使用解密后的密码
        dataSource.setPassword(password);
    }
}
```

#### 场景 4：日志脱敏

```java
@Aspect
@Component
public class LogAspect {

    @Around("@annotation(com.carlos.core.annotation.Log)")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();

        // 加密敏感参数
        for (int i = 0; i < args.length; i++) {
            if (args[i] instanceof SensitiveData) {
                SensitiveData data = (SensitiveData) args[i];
                data.setIdCard(EncryptUtil.encrypt(data.getIdCard()));
                data.setPhone(EncryptUtil.encrypt(data.getPhone()));
            }
        }

        // 记录日志
        log.info("Method: {}, Args: {}", joinPoint.getSignature(), args);

        return joinPoint.proceed();
    }
}
```

### 4. MyBatis 字段加密

**自定义 TypeHandler**：

```java
@MappedTypes(String.class)
public class EncryptTypeHandler extends BaseTypeHandler<String> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, String parameter, JdbcType jdbcType) throws SQLException {
        // 加密后存储
        String encrypted = EncryptUtil.encrypt(parameter);
        ps.setString(i, encrypted);
    }

    @Override
    public String getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String encrypted = rs.getString(columnName);
        // 解密后返回
        return EncryptUtil.decrypt(encrypted);
    }

    @Override
    public String getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String encrypted = rs.getString(columnIndex);
        return EncryptUtil.decrypt(encrypted);
    }

    @Override
    public String getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String encrypted = cs.getString(columnIndex);
        return EncryptUtil.decrypt(encrypted);
    }
}
```

**使用 TypeHandler**：

```java
@Data
@TableName("sys_user")
public class User {
    @TableId
    private Long id;

    private String name;

    // 自动加密解密
    @TableField(typeHandler = EncryptTypeHandler.class)
    private String idCard;

    @TableField(typeHandler = EncryptTypeHandler.class)
    private String phone;
}
```

## 配置说明

### 完整配置示例

```yaml
carlos:
  encrypt:
    # SM4 对称加密配置
    sm4:
      enabled: true                      # 启用 SM4 加密
      encrypt-mode: cbc                  # 加密模式：cbc 或 ecb
      key: "asdfghjklmnbvcx"            # 密钥（16 字节，不足会自动 MD5 处理）
      iv: "mnbvcxzpoiuytre1"            # 初始化向量（CBC 模式必需，16 字节）
      store-type: base64                 # 存储格式：base64 或 hex

    # SM2 非对称加密配置
    sm2:
      enabled: true                      # 启用 SM2 加密
      public-key: "0403c7a7250306531238d49c2e0146e48217f2d65b28ed1d61565bd40472c7516bc78574de0a89f9067328558d656ecdc2504f49bb2bd843a9cfa9b404463ed453"
      private-key: "3134665e1e009bab370738fd5ff1dabc7f390b683885c321f1523f4f725e8aa1"
      store-type: base64                 # 存储格式：base64 或 hex
```

### 配置项说明

#### SM4 配置

| 配置项            | 类型      | 默认值    | 说明                   |
|----------------|---------|--------|----------------------|
| `enabled`      | Boolean | true   | 是否启用 SM4 加密          |
| `encrypt-mode` | String  | cbc    | 加密模式（cbc/ecb）        |
| `key`          | String  | -      | 密钥（16 字节，不足会 MD5 处理） |
| `iv`           | String  | -      | 初始化向量（CBC 模式必需）      |
| `store-type`   | String  | base64 | 存储格式（base64/hex）     |

#### SM2 配置

| 配置项           | 类型      | 默认值    | 说明               |
|---------------|---------|--------|------------------|
| `enabled`     | Boolean | false  | 是否启用 SM2 加密      |
| `public-key`  | String  | -      | 公钥（130 位十六进制）    |
| `private-key` | String  | -      | 私钥（64 位十六进制）     |
| `store-type`  | String  | base64 | 存储格式（base64/hex） |

### 加密模式对比

| 特性    | CBC 模式 | ECB 模式 |
|-------|--------|--------|
| 安全性   | 高      | 低      |
| 需要 IV | 是      | 否      |
| 并行加密  | 否      | 是      |
| 并行解密  | 是      | 是      |
| 推荐使用  | ✓      | ✗      |

**推荐使用 CBC 模式**，因为 ECB 模式对相同的明文块会产生相同的密文块，存在安全隐患。

### 存储格式对比

| 格式     | 长度 | 可读性 | 存储空间 |
|--------|----|-----|------|
| HEX    | 较长 | 较好  | 较大   |
| BASE64 | 较短 | 一般  | 较小   |

**推荐使用 BASE64 格式**，存储空间更小。

## 依赖引入

```xml
<dependency>
    <groupId>com.carlos</groupId>
    <artifactId>carlos-encrypt</artifactId>
    <version>${carlos.version}</version>
</dependency>
```

## 依赖项

- **BouncyCastle**: 1.70（密码学提供者）
- **Hutool**: 5.8.40（SM2/SM4 工具封装）
- **carlos-core**: 核心基础模块
- **Spring Boot**: 3.5.8+（自动配置）

## 算法特性

### SM4 算法

- **标准**: GB/T 32907-2016
- **密钥长度**: 128 位（16 字节）
- **分组长度**: 128 位（16 字节）
- **轮数**: 32 轮
- **模式**: CBC、ECB、CTR、OFB、CFB
- **填充**: PKCS5Padding、PKCS7Padding
- **性能**: 优于 AES-128

### SM2 算法

- **标准**: GB/T 32918-2016
- **曲线**: SM2P256V1（256 位椭圆曲线）
- **密钥长度**: 256 位
- **公钥长度**: 130 字符（未压缩点编码）
- **私钥长度**: 64 字符（32 字节）
- **安全性**: 等同于 RSA-3072
- **性能**: 优于 RSA

## 注意事项

1. **密钥管理**: 密钥应妥善保管，不要硬编码在代码中
2. **密钥长度**: SM4 密钥必须为 16 字节，不足会自动 MD5 处理
3. **IV 向量**: CBC 模式必须配置 IV，且 IV 应随机生成
4. **公私钥**: SM2 公钥以 "04" 开头，长度 130 字符；私钥长度 64 字符
5. **加密模式**: 推荐使用 CBC 模式，ECB 模式不安全
6. **存储格式**: 推荐使用 BASE64 格式，节省存储空间
7. **性能考虑**: SM4 适合大量数据加密，SM2 适合密钥交换和数字签名
8. **异常处理**: 加密失败会抛出 `EncryptException`，需要捕获处理
9. **配置验证**: 启动时会验证密钥配置，缺失会抛出异常
10. **国密合规**: 符合中国国家密码管理局要求

## 性能优化

1. **批量加密**: 使用 SM4 对称加密，性能更好
2. **密钥缓存**: 密钥在启动时加载，避免重复解析
3. **线程安全**: 工具类方法是线程安全的
4. **对象复用**: BouncyCastle 对象会被复用，减少创建开销

## 安全建议

1. **密钥轮换**: 定期更换密钥，降低泄露风险
2. **密钥分离**: 不同环境使用不同密钥
3. **密钥加密**: 配置文件中的密钥应加密存储
4. **传输安全**: 使用 HTTPS 传输加密数据
5. **日志脱敏**: 日志中不要输出明文敏感数据
6. **权限控制**: 限制密钥文件的访问权限
7. **审计日志**: 记录加密解密操作的审计日志

## 常见问题

### 1. 密钥长度不正确

**问题**：

```
java.security.InvalidKeyException: Wrong key size
```

**解决**：

- SM4 密钥必须为 16 字节
- 配置的密钥会自动 MD5 处理为 16 字节

### 2. 解密失败

**问题**：

```
EncryptException: Decryption failed
```

**原因**：

- 密钥不匹配
- 加密模式不匹配
- 数据已损坏

**解决**：

- 检查密钥配置
- 确认加密解密使用相同模式
- 验证数据完整性

### 3. SM2 公钥格式错误

**问题**：

```
IllegalArgumentException: Invalid public key format
```

**解决**：

- 公钥必须以 "04" 开头
- 公钥长度必须为 130 字符
- 使用 `generateSm2KeyPair()` 生成标准密钥对

## 版本要求

- JDK 17+
- Spring Boot 3.5.8+
- BouncyCastle 1.70+
- Hutool 5.8.40+
- Maven 3.8+

## 相关模块

- `carlos-core`: 核心基础模块
- `carlos-tools`: 加密工具 GUI
- `carlos-test`: 加密功能测试
