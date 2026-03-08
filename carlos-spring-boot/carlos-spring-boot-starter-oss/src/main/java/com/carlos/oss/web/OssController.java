package com.carlos.oss.web;

import com.carlos.core.response.Result;
import com.carlos.oss.config.OssProperties;
import com.carlos.oss.core.OssTemplate;
import com.carlos.oss.model.OssClientInfo;
import com.carlos.oss.model.OssFile;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * OSS 接口控制器
 * 提供 OSS 相关信息查询和基础操作接口
 * </p>
 *
 * @author carlos
 * @date 2026-03-06
 */
@Slf4j
@RestController
@RequestMapping("/oss")
@Tag(name = "OSS 对象存储接口")
@RequiredArgsConstructor
public class OssController {

    private final OssProperties properties;
    private final OssTemplate ossTemplate;

    /**
     * 获取 OSS 客户端初始化信息
     */
    @GetMapping("/init")
    @Operation(summary = "获取 OSS 初始化信息")
    public Result<OssClientInfo> init() {
        return Result.ok(OssClientInfo.builder()
            .endpoint(properties.getEndpoint())
            .publicEndpoint(properties.getPublicEndpoint())
            .accessKey(properties.getAccessKey())
            .secretKey(properties.getSecretKey())
            .defaultBucket(properties.getBucketName())
            .region(properties.getRegion())
            .pathStyleAccess(properties.isPathStyleAccess())
            .type(properties.getType() != null ? properties.getType().name() : null)
            .build());
    }

    /**
     * 获取默认桶名称
     */
    @GetMapping("/bucket/default")
    @Operation(summary = "获取默认桶名称")
    public Result<String> getDefaultBucket() {
        return Result.ok(properties.getBucketName());
    }

    /**
     * 列出所有桶
     */
    @GetMapping("/bucket/list")
    @Operation(summary = "列出所有存储桶")
    public Result<List<String>> listBuckets() {
        return Result.ok(ossTemplate.listBuckets());
    }

    /**
     * 检查桶是否存在
     */
    @GetMapping("/bucket/exists")
    @Operation(summary = "检查存储桶是否存在")
    @Parameter(name = "bucketName", description = "桶名称")
    public Result<Boolean> bucketExists(@RequestParam String bucketName) {
        return Result.ok(ossTemplate.bucketExists(bucketName));
    }

    /**
     * 创建桶
     */
    @PostMapping("/bucket/create")
    @Operation(summary = "创建存储桶")
    @Parameter(name = "bucketName", description = "桶名称")
    public Result<Void> createBucket(@RequestParam String bucketName) {
        ossTemplate.createBucket(bucketName);
        return Result.ok();
    }

    /**
     * 删除桶
     */
    @PostMapping("/bucket/delete")
    @Operation(summary = "删除存储桶")
    @Parameter(name = "bucketName", description = "桶名称")
    public Result<Void> deleteBucket(@RequestParam String bucketName) {
        ossTemplate.deleteBucket(bucketName);
        return Result.ok();
    }

    /**
     * 上传文件
     */
    @PostMapping("/upload")
    @Operation(summary = "上传文件")
    @Parameter(name = "file", description = "文件")
    @Parameter(name = "objectName", description = "对象名称（可选，默认使用原文件名）")
    @Parameter(name = "bucketName", description = "桶名称（可选，默认使用配置桶）")
    public Result<OssFile> upload(
        @RequestParam("file") MultipartFile file,
        @RequestParam(required = false) String objectName,
        @RequestParam(required = false) String bucketName) {

        String targetBucket = bucketName != null ? bucketName : properties.getBucketName();
        String targetObject = objectName != null ? objectName : file.getOriginalFilename();

        try {
            OssFile ossFile = ossTemplate.putObject(targetBucket, targetObject,
                file.getInputStream(), file.getContentType());
            return Result.ok(ossFile);
        } catch (Exception e) {
            log.error("Failed to upload file", e);
            throw new RuntimeException("Failed to upload file: " + e.getMessage());
        }
    }

    /**
     * 获取文件信息
     */
    @GetMapping("/info")
    @Operation(summary = "获取文件信息")
    @Parameter(name = "objectName", description = "对象名称")
    @Parameter(name = "bucketName", description = "桶名称（可选）")
    public Result<OssFile> getObjectInfo(
        @RequestParam String objectName,
        @RequestParam(required = false) String bucketName) {
        String targetBucket = bucketName != null ? bucketName : properties.getBucketName();
        return Result.ok(ossTemplate.getObjectInfo(targetBucket, objectName));
    }

    /**
     * 检查文件是否存在
     */
    @GetMapping("/exists")
    @Operation(summary = "检查文件是否存在")
    @Parameter(name = "objectName", description = "对象名称")
    @Parameter(name = "bucketName", description = "桶名称（可选）")
    public Result<Boolean> objectExists(
        @RequestParam String objectName,
        @RequestParam(required = false) String bucketName) {
        String targetBucket = bucketName != null ? bucketName : properties.getBucketName();
        return Result.ok(ossTemplate.objectExists(targetBucket, objectName));
    }

    /**
     * 删除文件
     */
    @PostMapping("/delete")
    @Operation(summary = "删除文件")
    @Parameter(name = "objectName", description = "对象名称")
    @Parameter(name = "bucketName", description = "桶名称（可选）")
    public Result<Void> deleteObject(
        @RequestParam String objectName,
        @RequestParam(required = false) String bucketName) {
        String targetBucket = bucketName != null ? bucketName : properties.getBucketName();
        ossTemplate.deleteObject(targetBucket, objectName);
        return Result.ok();
    }

    /**
     * 获取文件访问URL
     */
    @GetMapping("/url")
    @Operation(summary = "获取文件访问URL")
    @Parameter(name = "objectName", description = "对象名称")
    @Parameter(name = "bucketName", description = "桶名称（可选）")
    public Result<String> getObjectUrl(
        @RequestParam String objectName,
        @RequestParam(required = false) String bucketName) {
        String targetBucket = bucketName != null ? bucketName : properties.getBucketName();
        return Result.ok(ossTemplate.getObjectUrl(targetBucket, objectName));
    }

    /**
     * 获取预签名URL
     */
    @GetMapping("/presigned-url")
    @Operation(summary = "获取预签名URL（临时访问链接）")
    @Parameter(name = "objectName", description = "对象名称")
    @Parameter(name = "bucketName", description = "桶名称（可选）")
    @Parameter(name = "expires", description = "过期时间（秒，默认3600）")
    public Result<String> getPresignedUrl(
        @RequestParam String objectName,
        @RequestParam(required = false) String bucketName,
        @RequestParam(required = false, defaultValue = "3600") int expires) {
        String targetBucket = bucketName != null ? bucketName : properties.getBucketName();
        return Result.ok(ossTemplate.getPresignedUrl(targetBucket, objectName, expires));
    }

    /**
     * 列出文件
     */
    @GetMapping("/list")
    @Operation(summary = "列出文件")
    @Parameter(name = "prefix", description = "前缀（可选）")
    @Parameter(name = "bucketName", description = "桶名称（可选）")
    public Result<List<OssFile>> listObjects(
        @RequestParam(required = false) String prefix,
        @RequestParam(required = false) String bucketName) {
        String targetBucket = bucketName != null ? bucketName : properties.getBucketName();
        return Result.ok(ossTemplate.listObjects(targetBucket, prefix));
    }
}
