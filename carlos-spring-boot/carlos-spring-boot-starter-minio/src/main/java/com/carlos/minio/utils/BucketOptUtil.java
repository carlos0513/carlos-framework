package com.carlos.minio.utils;

import com.carlos.minio.bean.BucketInfo;
import com.carlos.minio.exception.MinioException;
import io.minio.*;
import io.minio.messages.Bucket;
import io.minio.messages.Item;
import io.minio.messages.Upload;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 桶相关操作
 * </p>
 *
 * @author carlos
 * @date 2021/6/10 14:19
 */
@Slf4j
public class BucketOptUtil {

    /**
     * 默认桶是否存在
     *
     * @return boolean
     * @author carlos
     * @date 2021/6/10 14:24
     */
    public static boolean exist() {
        return exist(MinioUtil.getDefaultBucket());
    }

    /**
     * 桶是否存在
     *
     * @param bucket 桶名称
     * @return boolean
     * @author carlos
     * @date 2021/6/10 14:24
     */
    public static boolean exist(String bucket) {
        try {
            return MinioUtil.getClient().bucketExists(BucketExistsArgs.builder().bucket(bucket).build());
        } catch (Exception e) {
            throw new MinioException(e.getMessage(), e);
        }
    }

    /**
     * 获取桶列表
     *
     * @return java.util.List<com.carlos.minio.bean.BucketInfo>
     * @author carlos
     * @date 2021/11/10 15:24
     */
    public static List<BucketInfo> list() {
        try {
            List<Bucket> buckets = MinioUtil.getClient().listBuckets();
            if (CollectionUtils.isEmpty(buckets)) {
                return null;
            }
            return buckets.stream().map(bucket -> new BucketInfo(bucket.name(), bucket.creationDate().toLocalDateTime())).collect(
                Collectors.toList());
        } catch (Exception e) {
            throw new MinioException(e.getMessage(), e);
        }
    }

    /**
     * 创建默认的存储桶
     *
     * @author carlos
     * @date 2021/6/10 14:22
     */
    public static void make() {
        make(MinioUtil.getDefaultBucket());
    }

    /**
     * 创建一个新的存储桶
     *
     * @param bucket 桶名称
     * @author carlos
     * @date 2021/6/10 14:22
     */
    public static void make(String bucket) {
        try {
            if (!exist(bucket)) {
                MinioUtil.getClient().makeBucket(MakeBucketArgs.builder().bucket(bucket).build());
            }
            if (MinioUtil.isDefaultPublic()) {
                setBucketPublic(bucket);
            }
        } catch (Exception e) {
            throw new MinioException(e.getMessage(), e);
        }
    }

    /**
     * <p>
     * 设置桶为public
     * </p>
     *
     * @author Carlos
     * @date 2023/11/12 23:30
     */
    public static void setBucketPublic(String bucket) {
        String config =
            "{\n" +
                "\t\"Version\": \"2012-10-17\",\n" +
                "\t\"Statement\": [{\n" +
                "\t\t\"Effect\": \"Allow\",\n" +
                "\t\t\"Principal\": {\n" +
                "\t\t\t\"AWS\": [\"*\"]\n" +
                "\t\t},\n" +
                "\t\t\"Action\": [\"s3:GetBucketLocation\", \"s3:ListBucket\", \"s3:ListBucketMultipartUploads\"],\n" +
                "\t\t\"Resource\": [\"arn:aws:s3:::" + bucket + "\"]\n" +
                "\t}, {\n" +
                "\t\t\"Effect\": \"Allow\",\n" +
                "\t\t\"Principal\": {\n" +
                "\t\t\t\"AWS\": [\"*\"]\n" +
                "\t\t},\n" +
                "\t\t\"Action\": [\"s3:AbortMultipartUpload\", \"s3:DeleteObject\", \"s3:GetObject\", \"s3:ListMultipartUploadParts\", \"s3:PutObject\"],\n" +
                "\t\t\"Resource\": [\"arn:aws:s3:::" + bucket + "/*\"]\n" +
                "\t}]\n" +
                "}\n";
        setBucketPolicy(bucket, config);
    }

    /**
     * <p>
     * 设置桶为private
     * </p>
     *
     * @author Carlos
     * @date 2023/11/12 23:30
     */
    public static void setBucketPrivate(String bucket) {
        String config = "{\"Version\":\"2012-10-17\",\"Statement\":[]}";
        setBucketPolicy(bucket, config);
    }


    /**
     * 设置桶策略
     *
     * @param bucket 桶名称
     * @param policy 策略json
     * @author Carlos
     * @date 2023/11/28 8:50
     */
    public static void setBucketPolicy(String bucket, String policy) {
        try {
            MinioUtil.getClient().setBucketPolicy(SetBucketPolicyArgs.builder().bucket(bucket).config(policy).build());
        } catch (Exception e) {
            throw new MinioException(e.getMessage(), e);
        }
    }

    /**
     * 删除默认存储桶
     *
     * @author carlos
     * @date 2021/6/10 14:51
     */
    public static void remove() {
        remove(MinioUtil.getDefaultBucket());
    }

    /**
     * 删除指定的存储桶
     *
     * @param bucket 桶名称
     * @author carlos
     * @date 2021/6/10 14:51
     */
    public static void remove(String bucket) {
        try {
            MinioUtil.getClient().removeBucket(
                RemoveBucketArgs.builder()
                    .bucket(bucket)
                    .build()
            );
        } catch (Exception e) {
            throw new MinioException(e.getMessage(), e);
        }
    }

    /**
     * 查找桶下的文件
     *
     * @param bucketName  桶名称
     * @param prefix      对象名称的前缀
     * @param recursive   是否递归查找，如果是false,就模拟文件夹结构查找。
     * @param useVersion1 如果是true, 使用版本1 REST API
     * @return java.lang.Iterable<io.minio.Result < io.minio.messages.Item>>
     * @author carlos
     * @date 2021/6/10 14:54
     */
    public static Iterable<Result<Item>> listObjects(String bucketName, String prefix, boolean recursive, boolean useVersion1) {

        ListObjectsArgs.Builder builder = ListObjectsArgs.builder();
        if (StringUtils.isNoneBlank(bucketName)) {
            builder.bucket(bucketName);
        }
        MinioUtil.getClient().listObjects(null);
        return null;
    }

    /**
     * 列出存储桶中被部分上传的对象
     *
     * @param bucketName 存储桶名称
     * @param prefix     对象名称的前缀，列出有该前缀的对象
     * @param recursive  是否递归查找，如果是false,就模拟文件夹结构查找
     * @return java.lang.Iterable<io.minio.Result < io.minio.messages.Upload>>
     * @author carlos
     * @date 2021/6/10 14:56
     */
    public static Iterable<Result<Upload>> listIncompleteUploads(String bucketName, String prefix, boolean recursive) {
        return null;
    }

}
