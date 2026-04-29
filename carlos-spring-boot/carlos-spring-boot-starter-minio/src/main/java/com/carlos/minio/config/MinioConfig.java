package com.carlos.minio.config;


import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.carlos.minio.utils.MinioUtil;
import io.minio.MinioAsyncClient;
import io.minio.MinioClient;
import io.minio.http.HttpUtils;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <p>
 * minio核心配置
 * </p>
 *
 * @author carlos
 * @date 2021/6/10 14:14
 */
@Slf4j
@RequiredArgsConstructor
@Configuration
@EnableConfigurationProperties(MinioProperties.class)
@ConditionalOnProperty(prefix = "carlos.minio", name = "enabled", havingValue = "true", matchIfMissing = true)
public class MinioConfig {

    private final MinioClient minioClient;

    private final IMinioAsyncClient minioAsyncClient;

    private final MinioProperties minioProperties;

    /**
     * 初始化 MinioUtil 静态字段
     */
    @PostConstruct
    public void init() {
        MinioUtil.init(minioClient, minioAsyncClient, minioProperties);
    }

    @Bean
    @ConditionalOnMissingBean
    MinioClient minioClientFactory(MinioProperties properties) {
        log.debug("Minio config info:{}", JSONUtil.toJsonPrettyStr(properties));
        final MinioClient.Builder builder = MinioClient.builder();
        builder.endpoint(properties.getEndpoint());
        builder.credentials(properties.getAccessKey(), properties.getSecretKey());

        OkHttpClient okHttpClient = HttpUtils
            .newDefaultHttpClient(properties.getConnectTimeout().toMillis(),
                properties.getWriteTimeout().toMillis(),
                properties.getReadTimeout().toMillis());
        builder.httpClient(okHttpClient);
        MinioClient client = builder.build();
        return client;
    }


    /**
     * 解决公网地址问题
     */
    @Bean
    IMinioAsyncClient minioAsyncClient(MinioProperties properties) {
        OkHttpClient okHttpClient = HttpUtils
            .newDefaultHttpClient(properties.getConnectTimeout().toMillis(),
                properties.getWriteTimeout().toMillis(),
                properties.getReadTimeout().toMillis());
        MinioAsyncClient client = MinioAsyncClient.builder()
            .endpoint(properties.getEndpoint())
            .credentials(properties.getAccessKey(), properties.getSecretKey())
            .httpClient(okHttpClient)
            .build();
        IMinioAsyncClient asyncClient = new IMinioAsyncClient(client);
        String publicEndpoint = properties.getPublicEndpoint();
        if (!StrUtil.endWith(publicEndpoint, StrUtil.SLASH)) {
            publicEndpoint += StrUtil.SLASH;
        }
        asyncClient.setPublicBaseUrl(publicEndpoint);
        return asyncClient;
    }

}
