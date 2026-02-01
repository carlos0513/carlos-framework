package com.carlos.minio.utils;

import com.carlos.minio.config.IMinioAsyncClient;
import com.carlos.minio.config.MinioProperties;
import com.carlos.minio.exception.MinioException;
import io.minio.MinioClient;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * <p>
 * minio工具
 * </p>
 *
 * @author carlos
 * @date 2021/6/10 17:00
 */
@Component
public class MinioUtil {

    private static MinioClient client;

    private static IMinioAsyncClient asyncClient;

    private static MinioProperties properties;

    @Autowired
    private MinioClient clientBean;

    @Autowired
    private IMinioAsyncClient asyncClientBen;

    @Autowired
    private MinioProperties minioPropertiesBean;


    @PostConstruct
    private void init() {
        MinioUtil.client = clientBean;
        MinioUtil.asyncClient = asyncClientBen;
        MinioUtil.properties = minioPropertiesBean;
    }

    /**
     * 获取minio客户端
     *
     * @return io.minio.MinioClient
     * @author carlos
     * @date 2021/6/10 17:10
     */
    public static MinioClient getClient() {
        return client;
    }

    /**
     * 获取minio客户端
     *
     * @return io.minio.MinioClient
     * @author carlos
     * @date 2021/6/10 17:10
     */
    public static IMinioAsyncClient getAsyncClient() {
        return asyncClient;
    }

    /**
     * 获取默认桶名称
     *
     * @return java.lang.String
     * @author carlos
     * @date 2021/6/10 17:11
     */
    public static String getDefaultBucket() {
        return properties.getBucket();
    }

    /**
     * 是否默认bucket public
     *
     * @return boolean
     * @author Carlos
     * @date 2023/11/12 23:39
     */
    public static boolean isDefaultPublic() {
        return properties.isDefaultPublic();
    }

    /**
     * 获取公网地址
     *
     * @return java.lang.String
     * @author Carlos
     * @date 2022/11/29 11:16
     */
    public static String getPublicEndpoint() {
        return properties.getPublicEndpoint();
    }

    /**
     * 检查桶
     *
     * @author carlos
     * @date 2021/6/10 14:24
     */
    public static void checkBucket(String bucket) {
        if (properties.isCheckBucket()) {
            try {
                boolean b = BucketOptUtil.exist(bucket);
                if (!b) {
                    if (properties.isCreateBucket()) {
                        try {
                            BucketOptUtil.make(bucket);
                        } catch (Exception e) {
                            throw new MinioException("桶创建失败： " + bucket, e);
                        }
                    } else {
                        throw new MinioException("桶不存在: " + bucket);
                    }
                }
            } catch (Exception e) {
                throw new MinioException(e.getMessage(), e);
            }
        }
    }
}
