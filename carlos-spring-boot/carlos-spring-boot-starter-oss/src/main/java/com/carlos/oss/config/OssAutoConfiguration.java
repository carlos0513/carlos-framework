package com.carlos.oss.config;

import cn.hutool.core.util.StrUtil;
import com.carlos.oss.adapter.S3OssTemplate;
import com.carlos.oss.core.OssTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3ClientBuilder;
import software.amazon.awssdk.services.s3.S3Configuration;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

import java.net.URI;
import java.time.Duration;

/**
 * OSS 自动配置类
 * 基于 AWS S3 SDK，统一支持所有兼容 S3 协议的对象存储服务
 *
 * @author carlos
 * @date 2026-02-01
 */
@Slf4j
@Configuration
@EnableConfigurationProperties(OssProperties.class)
@ConditionalOnProperty(prefix = "carlos.oss", name = "enabled", havingValue = "true", matchIfMissing = true)
public class OssAutoConfiguration {

    /**
     * 创建 S3 客户端
     */
    @Bean
    @ConditionalOnMissingBean
    public S3Client s3Client(OssProperties properties) {
        log.info("Initializing S3 client for OSS type: {}", properties.getType());

        // 获取配置
        String endpoint = properties.getActualEndpoint();
        String accessKey = properties.getAccessKey();
        String secretKey = properties.getSecretKey();
        String region = properties.getRegion();

        if (StrUtil.isBlank(accessKey) || StrUtil.isBlank(secretKey)) {
            throw new IllegalStateException("Access key and secret key must be configured");
        }

        // 创建凭证
        AwsBasicCredentials credentials = AwsBasicCredentials.create(accessKey, secretKey);

        // 创建 S3 配置
        S3Configuration s3Config = S3Configuration.builder()
            .pathStyleAccessEnabled(properties.isPathStyleAccess())
            .build();

        // 创建 S3 客户端构建器
        S3ClientBuilder builder = S3Client.builder()
            .credentialsProvider(StaticCredentialsProvider.create(credentials))
            .serviceConfiguration(s3Config)
            .region(Region.of(region));

        // 设置自定义 endpoint
        if (StrUtil.isNotBlank(endpoint)) {
            builder.endpointOverride(URI.create(endpoint));
            log.info("Using custom endpoint: {}", endpoint);
        }

        // 设置超时时间
        builder.overrideConfiguration(config -> config
            .apiCallTimeout(Duration.ofMillis(properties.getSocketTimeout()))
            .apiCallAttemptTimeout(Duration.ofMillis(properties.getConnectionTimeout()))
        );

        S3Client s3Client = builder.build();
        log.info("S3 client initialized successfully for region: {}", region);

        return s3Client;
    }

    /**
     * 创建 S3 预签名器
     */
    @Bean
    @ConditionalOnMissingBean
    public S3Presigner s3Presigner(OssProperties properties) {
        log.info("Initializing S3 presigner");

        String endpoint = properties.getActualEndpoint();
        String accessKey = properties.getAccessKey();
        String secretKey = properties.getSecretKey();
        String region = properties.getRegion();

        AwsBasicCredentials credentials = AwsBasicCredentials.create(accessKey, secretKey);

        S3Presigner.Builder builder = S3Presigner.builder()
            .credentialsProvider(StaticCredentialsProvider.create(credentials))
            .region(Region.of(region));

        if (StrUtil.isNotBlank(endpoint)) {
            builder.endpointOverride(URI.create(endpoint));
        }

        return builder.build();
    }

    /**
     * 创建 OSS 模板
     */
    @Bean
    @ConditionalOnMissingBean(OssTemplate.class)
    public OssTemplate ossTemplate(OssProperties properties, S3Client s3Client, S3Presigner s3Presigner) {
        log.info("Creating OSS template with S3 protocol");
        return new S3OssTemplate(properties, s3Client, s3Presigner);
    }
}
