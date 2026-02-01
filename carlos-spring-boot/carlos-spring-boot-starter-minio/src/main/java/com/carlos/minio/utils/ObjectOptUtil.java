package com.carlos.minio.utils;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import com.carlos.minio.bean.ObjectInfo;
import com.carlos.minio.exception.MinioException;
import io.minio.*;
import io.minio.http.Method;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 对象相关操作
 * </p>
 *
 * @author carlos
 * @date 2021/6/10 14:19
 */
@Slf4j
@Component
public class ObjectOptUtil {

    // region----------------------  获取对象 start  ------------------------

    /**
     * 获取对象
     *
     * @param bucket 桶名称
     * @param object 对象地址和名称
     * @return java.io.InputStream
     * @author carlos
     * @date 2021/6/10 17:25
     */
    public static InputStream getObject(String bucket, String object) {
        try {
            return MinioUtil.getClient().getObject(
                    GetObjectArgs.builder()
                            .bucket(bucket)
                            .object(object)
                            .build()
            );
        } catch (Exception e) {
            throw new MinioException(e.getMessage(), e);
        }
    }

    /**
     * 获取对象
     *
     * @param object 对象地址和名称
     * @return java.io.InputStream
     * @author carlos
     * @date 2021/6/10 17:28
     */
    public static InputStream getObject(String object) {
        return getObject(MinioUtil.getDefaultBucket(), object);
    }

    /**
     * 获取对象的访问链接
     *
     * @param bucketName bucket名称
     * @param objectName ⽂件名称
     * @param expires    过期时间 <=7  天
     * @return java.lang.String
     * @author carlos
     * @date 2021/6/11 10:35
     */
    public static String getObjectUrl(String bucketName, String objectName, Integer expires) {
        if (!objectExist(bucketName, objectName)) {
            throw new MinioException("Can't find obj '" + objectName + "' in bucket " + bucketName);
        }
        try {
            return MinioUtil.getClient().getPresignedObjectUrl(GetPresignedObjectUrlArgs.builder()
                    .bucket(bucketName)
                    .expiry(expires, TimeUnit.DAYS)
                    .method(Method.GET)
                    .object(objectName).build()
            );
        } catch (Exception e) {
            throw new MinioException(e.getMessage(), e);
        }
    }


    /**
     * 获取对象的访问链接 默认过期时间7天
     *
     * @param bucketName ⽂桶名
     * @param objectName ⽂件名称
     * @return java.lang.String
     * @author carlos
     * @date 2021/6/11 10:35
     */
    public static String getPresignedObjectPublicUrl(String bucketName, String objectName) {
        return getPresignedObjectPublicUrl(bucketName, objectName, 7);
    }

    /**
     * 获取对象的访问链接
     *
     * @param bucketName bucket名称
     * @param objectName ⽂件名称
     * @param expires    过期时间 <=7  天
     * @return java.lang.String
     * @author carlos
     * @date 2021/6/11 10:35
     */
    public static String getPresignedObjectPublicUrl(String bucketName, String objectName, Integer expires) {
        if (!objectExist(bucketName, objectName)) {
            throw new MinioException("Can't find obj '" + objectName + "' in bucket " + bucketName);
        }
        try {
            return MinioUtil.getAsyncClient().getPresignedObjectPublicUrl(GetPresignedObjectUrlArgs.builder()
                    .bucket(bucketName)
                    .expiry(expires, TimeUnit.DAYS)
                    .method(Method.GET)
                    .object(objectName).build()
            );
        } catch (Exception e) {
            throw new MinioException(e.getMessage(), e);
        }
    }

    /**
     * 获取对象的访问链接
     *
     * @param objectName ⽂件名称
     * @return java.lang.String
     * @author carlos
     * @date 2021/6/11 10:35
     */
    public static String getObjectUrl(String objectName) {
        return getObjectUrl(objectName, 7);
    }

    /**
     * 获取对象的访问链接
     *
     * @param objectName ⽂件名称
     * @return java.lang.String
     * @author carlos
     * @date 2021/6/11 10:35
     */
    public static String getPublicObjectUrl(String bucket, String objectName) {
        return MinioUtil.getPublicEndpoint() + StrUtil.SLASH + bucket + StrUtil.SLASH + objectName;
    }

    /**
     * 获取对象的访问链接
     *
     * @param objectName ⽂件名称
     * @return java.lang.String
     * @author carlos
     * @date 2021/6/11 10:35
     */
    public static String getPublicObjectUrl(String objectName) {
        return MinioUtil.getPublicEndpoint() + StrUtil.SLASH + MinioUtil.getDefaultBucket() + StrUtil.SLASH + objectName;
    }

    /**
     * 获取对象的访问链接
     *
     * @param objectName ⽂件名称
     * @param expires    过期时间 <=7  天
     * @return java.lang.String
     * @author carlos
     * @date 2021/6/11 10:35
     */
    public static String getObjectUrl(String objectName, Integer expires) {
        return getObjectUrl(MinioUtil.getDefaultBucket(), objectName, expires);
    }

    /**
     * 获取对象的访问链接 默认过期时间7天
     *
     * @param bucketName ⽂桶名
     * @param objectName ⽂件名称
     * @return java.lang.String
     * @author carlos
     * @date 2021/6/11 10:35
     */
    public static String getObjectUrl(String bucketName, String objectName) {
        return getObjectUrl(bucketName, objectName, 7);
    }

    /**
     * 判断对象是否存在
     *
     * @param bucketName 桶名称
     * @param objectName 对象名称
     * @return boolean
     * @author carlos
     * @date 2021/6/11 10:47
     */
    public static boolean objectExist(String bucketName, String objectName) {
        StatObjectResponse objectInfo;
        try {
            objectInfo = objectInfo(bucketName, objectName);
        } catch (Exception e) {
            return false;
        }
        return objectInfo != null;
    }

    /**
     * 获取对象信息
     *
     * @param bucketName bucket名称
     * @param objectName ⽂件名称
     * @return java.lang.String
     * @author carlos
     * @date 2021/6/11 10:35
     */
    public static StatObjectResponse objectInfo(String bucketName, String objectName) {
        StatObjectResponse object;
        try {
            object = MinioUtil.getClient().statObject(StatObjectArgs.builder()
                    .bucket(bucketName)
                    .object(objectName).build()
            );
        } catch (Exception e) {
            throw new MinioException(e.getMessage(), e);
        }
        return object;
    }


    // endregion----------------------   获取对象 end   ------------------------

    // region----------------------  upload start  ------------------------

    /**
     * 上传文件到默认bucket
     *
     * @param filename 文件名称
     * @param object   对象名称
     * @return 文件信息
     * @author carlos
     * @date 2021/6/11 13:47
     */
    public static ObjectInfo uploadObject(String filename, String object) {
        return uploadObject(MinioUtil.getDefaultBucket(), filename, object);
    }

    /**
     * 上传文件
     *
     * @param bucket   桶名称
     * @param filename 文件名称
     * @param object   对象名称
     * @return 上传文件信息
     * @author carlos
     * @date 2021/6/11 13:47
     */

    public static ObjectInfo uploadObject(String bucket, String filename, String object) {
        MinioUtil.checkBucket(bucket);
        try {
            MinioUtil.getClient().uploadObject(
                    UploadObjectArgs.builder()
                            .bucket(bucket)
                            .filename(filename)
                            .object(object)
                            .build()
            );
            return new ObjectInfo(bucket, filename, object);
        } catch (Exception e) {
            throw new MinioException(e.getMessage(), e);
        }
    }

    /**
     * 上传文件到默认桶
     *
     * @param object 对象信息
     * @param file   文件对象
     * @author carlos
     * @date 2021/6/11 13:48
     */
    public static ObjectInfo uploadObject(String object, File file) {
        return uploadObject(MinioUtil.getDefaultBucket(), file, object);
    }

    /**
     * 上传文件
     *
     * @param bucket 桶名称
     * @param file   文件
     * @param object 对象
     * @author carlos
     * @date 2021/6/11 13:49
     */
    public static ObjectInfo uploadObject(String bucket, File file, String object) {
        return uploadObject(bucket, file.getAbsolutePath(), object);
    }

    public static ObjectInfo uploadObject(Path path, String object) {
        return uploadObject(MinioUtil.getDefaultBucket(), path, object);
    }

    public static ObjectInfo uploadObject(String bucket, Path path, String object) {
        return uploadObject(bucket, path.toAbsolutePath().toString(), object);
    }
    // endregion----------------------   upload end   ------------------------

    // region----------------------  上传文件 start  ------------------------

    /**
     * 保存文件对象
     *
     * @param bucket 桶名
     * @param object 对象名
     * @param file   文件对象
     * @return com.carlos.minio.bean.ObjectInfo
     * @author carlos
     * @date 2022/2/9 13:50
     */
    public static ObjectInfo putObject(String bucket, String object, MultipartFile file) {
        try {
            putObject(bucket, object, file.getContentType(), file.getInputStream(), 0, -1);
            return new ObjectInfo(bucket, file.getOriginalFilename(), object);
        } catch (IOException e) {
            throw new MinioException(e);
        }
    }

    /**
     * 保存文件流
     *
     * @param bucket 桶名
     * @param object 对象名
     * @param stream 文件流
     * @return com.carlos.minio.bean.ObjectInfo
     * @author carlos
     * @date 2022/2/9 13:51
     */
    public static ObjectInfo putObject(String bucket, String object, InputStream stream) {
        try {

            putObject(bucket, object, null, stream, stream.available(), -1);
            return new ObjectInfo(bucket, null, object);
        } catch (IOException e) {
            throw new MinioException(e);
        }
    }

    /**
     * 保存文件流
     *
     * @param object 对象名
     * @param stream 文件流
     * @return com.carlos.minio.bean.ObjectInfo
     * @author carlos
     * @date 2022/2/9 13:51
     */
    public static ObjectInfo putObject(String object, InputStream stream) {
        String bucket = MinioUtil.getDefaultBucket();
        try {

            putObject(bucket, object, null, stream, stream.available(), -1);
            return new ObjectInfo(bucket, null, object);
        } catch (IOException e) {
            throw new MinioException(e);
        }
    }

    /**
     * 保存文件流
     *
     * @param bucket 桶名
     * @param object 对象名
     * @param url    文件地址
     * @return com.carlos.minio.bean.ObjectInfo
     * @author carlos
     * @date 2022/2/9 13:51
     */
    public static ObjectInfo putObject(String bucket, String object, String url) {
        byte[] bytes;
        try {
            bytes = HttpUtil.downloadBytes(url);
        } catch (Exception e) {
            throw new MinioException("Can't download file from url:" + url, e);
        }
        ByteArrayInputStream stream = IoUtil.toStream(bytes);
        try {
            putObject(bucket, object, null, stream, stream.available(), -1);
            return new ObjectInfo(bucket, null, object);
        } catch (Exception e) {
            throw new MinioException(e);
        }
    }

    /**
     * 保存文件到默认桶下
     *
     * @param object 对象名称
     * @param file   上传文件
     * @return com.carlos.minio.bean.ObjectInfo
     * @author carlos
     * @date 2021/11/10 17:13
     */
    public static ObjectInfo putObject(String object, MultipartFile file) {
        return putObject(MinioUtil.getDefaultBucket(), object, file);
    }

    public static void putObject(String bucket, String object, String contentType, InputStream stream, long objectSize,
                                 long partSize) {
        if (objectSize == 0) {
            try {
                objectSize = stream.available();
            } catch (IOException e) {
                throw new MinioException(e);
            }
        }
        MinioUtil.checkBucket(bucket);
        PutObjectArgs.Builder builder = PutObjectArgs.builder()
                .bucket(bucket)
                .object(object)
                .stream(stream, objectSize, partSize);
        if (StrUtil.isNotBlank(contentType)) {
            builder.contentType(contentType);
        }
        try {
            ObjectWriteResponse response = MinioUtil.getClient().putObject(builder.build());
        } catch (Exception e) {
            throw new MinioException(e);
        }
    }
    // endregion----------------------   上传文件 end   ------------------------

    // region----------------------  删除对象 start  ------------------------

    /**
     * 删除对象
     *
     * @param bucket 桶名称
     * @param object 对象名称
     * @author carlos
     * @date 2021/6/10 17:39
     */
    public static void deleteObject(String bucket, String object) {
        try {
            MinioUtil.getClient().removeObject(
                    RemoveObjectArgs.builder()
                            .bucket(bucket)
                            .object(object)
                            .build()
            );
        } catch (Exception e) {
            throw new MinioException(e.getMessage(), e);
        }
    }

    /**
     * 删除默认桶中的对象
     *
     * @param object 对象名称
     * @author carlos
     * @date 2021/6/10 17:41
     */
    public static void deleteObject(String object) {
        deleteObject(MinioUtil.getDefaultBucket(), object);
    }
    // endregion----------------------   删除对象 end   ------------------------
}
