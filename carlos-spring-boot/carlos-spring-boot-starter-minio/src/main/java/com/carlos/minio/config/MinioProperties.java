package com.carlos.minio.config;

import lombok.Data;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.util.Assert;

import java.time.Duration;

/**
 * <p>
 * minio配置项
 * </p>
 *
 * @author carlos
 * @date 2021/6/10 13:21
 */
@Data
@ConfigurationProperties(prefix = "carlos.minio")
public class MinioProperties implements InitializingBean {

    private static final String DEFAULT_BUCKET = "default";

    /**
     * 是否启用 默认弃用
     */
    private boolean enabled = true;

    /**
     * 访问地址
     */
    private String endpoint;

    /**
     * 公网地址
     */
    private String publicEndpoint;

    /**
     * 账号
     */
    private String accessKey;

    /**
     * 密码
     */
    private String secretKey;

    /**
     * 默认bucket,应用启动时进行初始化 以下规则适用于在 Amazon S3 中命名存储桶：
     * <p>
     * 存储桶名称的长度必须介于 3 到 63 个字符之间。
     * <p>
     * 存储桶名称只能由小写字母、数字、点 (.) 和连字符 (-) 组成。
     * <p>
     * 存储桶名称必须以字母或数字开头和结尾。
     * <p>
     * 存储桶名称不得格式化为 IP 地址（例如，192.168.5.4）。
     * <p>
     * 存储桶名称不得以前缀 开头xn--。
     * <p>
     * 存储桶名称不得以后缀 结尾-s3alias。此后缀是为接入点别名保留的。有关更多信息，请参阅 为访问点使用存储桶式别名。
     * <p>
     * Bucket 名称在一个分区内必须是唯一的。分区是一组 Region。AWS 目前有三个分区：（aws标准区域）、 aws-cn（中国区域）和aws-us-gov（AWS GovCloud [美国] 区域）。
     */
    private String bucket = DEFAULT_BUCKET;

    /**
     * 客户端连接超时时间
     */
    private Duration connectTimeout = Duration.ofSeconds(10);

    /**
     * 写入超时时间
     */
    private Duration writeTimeout = Duration.ofSeconds(60);

    /**
     * 读取超时时间
     */
    private Duration readTimeout = Duration.ofSeconds(10);

    /**
     * 检查bucket是否存在
     */
    private boolean checkBucket = true;

    /**
     * 自动创建不存在的bucket
     */
    private boolean createBucket = true;


    /**
     * 默认创建public桶
     */
    private boolean defaultPublic = true;


    @Override
    public void afterPropertiesSet() {
        if (enabled) {
            Assert.hasText(this.endpoint, "'minio.endpoint' must not be blank.");
            Assert.hasText(this.accessKey, "'minio.access-key' must not be blank.");
            Assert.hasText(this.secretKey, "'minio.secret-key' must not be blank.");
            Assert.hasText(this.bucket, " 'minio.bucket' must not be blank.");
        }
    }

}
