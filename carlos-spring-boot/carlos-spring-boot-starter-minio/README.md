# carlos-minio

MinIO对象存储集成组件，提供文件上传、下载、管理等功能。

## 功能特性

- **文件上传**: 支持单文件和多文件上传
- **文件下载**: 支持文件下载和预览
- **桶管理**: 支持桶的创建、删除、列表查询
- **对象管理**: 支持对象的增删改查
- **预签名URL**: 支持生成临时访问URL
- **分片上传**: 支持大文件分片上传

## 快速开始

### Maven依赖

```xml
<dependency>
    <groupId>com.carlos</groupId>
    <artifactId>carlos-minio</artifactId>
    <version>${carlos.version}</version>
</dependency>
```

### 配置示例

```yaml
carlos:
  minio:
    enabled: true
    endpoint: http://minio.example.com:9000
    access-key: ${MINIO_ACCESS_KEY:minioadmin}
    secret-key: ${MINIO_SECRET_KEY:minioadmin}
    bucket-name: default-bucket
    # 是否自动创建桶
    auto-create-bucket: true
```

## 使用示例

### 文件上传

```java
@Autowired
private MinioUtil minioUtil;

public String uploadFile(MultipartFile file) {
    String objectName = UUID.randomUUID().toString() +
                       file.getOriginalFilename();

    minioUtil.uploadFile("my-bucket", objectName,
                        file.getInputStream(),
                        file.getContentType());

    return objectName;
}
```

### 文件下载

```java
public void downloadFile(String objectName, HttpServletResponse response) {
    InputStream stream = minioUtil.getObject("my-bucket", objectName);

    response.setContentType("application/octet-stream");
    response.setHeader("Content-Disposition",
                      "attachment; filename=" + objectName);

    IOUtils.copy(stream, response.getOutputStream());
}
```

### 生成预签名URL

```java
public String getPresignedUrl(String objectName) {
    // 生成7天有效的下载链接
    return minioUtil.getPresignedObjectUrl(
        "my-bucket",
        objectName,
        7,
        TimeUnit.DAYS
    );
}
```

### 桶操作

```java
// 创建桶
minioUtil.createBucket("new-bucket");

// 检查桶是否存在
boolean exists = minioUtil.bucketExists("my-bucket");

// 列出所有桶
List<String> buckets = minioUtil.listBuckets();

// 删除桶
minioUtil.removeBucket("old-bucket");
```

### 对象操作

```java
// 列出对象
List<String> objects = minioUtil.listObjects("my-bucket");

// 删除对象
minioUtil.removeObject("my-bucket", "file.txt");

// 复制对象
minioUtil.copyObject("source-bucket", "file.txt",
                    "target-bucket", "copy-file.txt");

// 获取对象信息
ObjectStat stat = minioUtil.statObject("my-bucket", "file.txt");
```

## 工具类说明

| 工具类           | 说明          |
|---------------|-------------|
| MinioUtil     | 通用MinIO操作工具 |
| BucketOptUtil | 桶操作专用工具     |
| ObjectOptUtil | 对象操作专用工具    |

## 依赖模块

- **carlos-spring-boot-starter-core**: 核心基础功能
- **MinIO Java SDK**: MinIO官方SDK

## 注意事项

- 生产环境必须修改默认的access-key和secret-key
- 建议使用HTTPS协议保证传输安全
- 大文件上传建议使用分片上传
- 注意设置合理的桶访问策略
- 定期清理过期的临时文件

## 版本要求

- JDK 17+
- Spring Boot 3.x
- MinIO Server 2023+
