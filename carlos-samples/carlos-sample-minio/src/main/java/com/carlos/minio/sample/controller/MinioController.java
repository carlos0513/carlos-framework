package com.carlos.minio.sample.controller;

import com.carlos.boot.minio.service.MinioService;
import com.carlos.core.pojo.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * MinIO 文件操作控制器
 *
 * @author carlos
 */
@Slf4j
@RestController
@RequestMapping("/minio")
@RequiredArgsConstructor
@Tag(name = "MinIO 文件管理", description = "MinIO 文件上传、下载、删除等接口")
public class MinioController {

    private final MinioService minioService;

    /**
     * 文件上传
     *
     * @param file 上传的文件
     * @return 文件访问 URL
     */
    @PostMapping("/upload")
    @Operation(summary = "文件上传", description = "上传文件到 MinIO")
    public Response<String> upload(
        @Parameter(description = "待上传的文件", required = true)
        @RequestParam("file") MultipartFile file) {
        String fileName = minioService.upload(file);
        return Response.ok(fileName);
    }

    /**
     * 指定 bucket 上传文件
     *
     * @param bucket bucket 名称
     * @param file   上传的文件
     * @return 文件访问 URL
     */
    @PostMapping("/upload/{bucket}")
    @Operation(summary = "指定 Bucket 上传", description = "上传文件到指定 Bucket")
    public Response<String> uploadToBucket(
        @Parameter(description = "Bucket 名称", required = true)
        @PathVariable("bucket") String bucket,
        @Parameter(description = "待上传的文件", required = true)
        @RequestParam("file") MultipartFile file) {
        String fileName = minioService.upload(bucket, file);
        return Response.ok(fileName);
    }

    /**
     * 获取文件 URL
     *
     * @param fileName 文件名
     * @return 文件 URL
     */
    @GetMapping("/url")
    @Operation(summary = "获取文件 URL", description = "获取文件的访问 URL")
    public Response<String> getUrl(
        @Parameter(description = "文件名", required = true)
        @RequestParam("fileName") String fileName) {
        String url = minioService.getObjectUrl(fileName);
        return Response.ok(url);
    }

    /**
     * 文件下载
     *
     * @param fileName 文件名
     * @param response HTTP 响应
     */
    @SneakyThrows
    @GetMapping("/download")
    @Operation(summary = "文件下载", description = "下载指定文件")
    public void download(
        @Parameter(description = "文件名", required = true)
        @RequestParam("fileName") String fileName,
        HttpServletResponse response) {
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition",
            "attachment;filename=" + URLEncoder.encode(fileName, StandardCharsets.UTF_8));
        minioService.download(fileName, response.getOutputStream());
    }

    /**
     * 删除文件
     *
     * @param fileName 文件名
     * @return 操作结果
     */
    @DeleteMapping("/delete")
    @Operation(summary = "删除文件", description = "删除指定文件")
    public Response<Void> delete(
        @Parameter(description = "文件名", required = true)
        @RequestParam("fileName") String fileName) {
        minioService.delete(fileName);
        return Response.ok();
    }

    /**
     * 批量删除文件
     *
     * @param fileNames 文件名列表
     * @return 操作结果
     */
    @DeleteMapping("/delete/batch")
    @Operation(summary = "批量删除", description = "批量删除文件")
    public Response<Void> deleteBatch(
        @Parameter(description = "文件名列表", required = true)
        @RequestBody List<String> fileNames) {
        minioService.delete(fileNames);
        return Response.ok();
    }

    /**
     * 判断文件是否存在
     *
     * @param fileName 文件名
     * @return 是否存在
     */
    @GetMapping("/exists")
    @Operation(summary = "文件是否存在", description = "判断指定文件是否存在")
    public Response<Boolean> exists(
        @Parameter(description = "文件名", required = true)
        @RequestParam("fileName") String fileName) {
        boolean exists = minioService.doesObjectExist(fileName);
        return Response.ok(exists);
    }

    /**
     * 列出所有文件
     *
     * @return 文件列表
     */
    @GetMapping("/list")
    @Operation(summary = "文件列表", description = "列出当前 bucket 中的所有文件")
    public Response<List<String>> list() {
        List<String> list = minioService.listObjects();
        return Response.ok(list);
    }

    /**
     * 列出指定 bucket 的所有文件
     *
     * @param bucket bucket 名称
     * @return 文件列表
     */
    @GetMapping("/list/{bucket}")
    @Operation(summary = "指定 Bucket 文件列表", description = "列出指定 bucket 中的所有文件")
    public Response<List<String>> listByBucket(
        @Parameter(description = "Bucket 名称", required = true)
        @PathVariable("bucket") String bucket) {
        List<String> list = minioService.listObjects(bucket);
        return Response.ok(list);
    }

}
