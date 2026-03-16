package com.carlos.sample.oss.controller;

import com.carlos.boot.pojo.R;
import com.carlos.boot.oss.OssTemplate;
import com.carlos.boot.oss.pojo.OssFile;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * OSS 文件操作示例 Controller
 *
 * @author carlos
 */
@Slf4j
@RestController
@RequestMapping("/oss")
@RequiredArgsConstructor
@Tag(name = "OSS 文件管理", description = "阿里云/腾讯云 OSS 文件上传下载示例接口")
public class OssController {

    private final OssTemplate ossTemplate;

    /**
     * 上传文件
     *
     * @param file 文件
     * @return 文件信息
     */
    @PostMapping("/upload")
    @Operation(summary = "上传文件", description = "上传文件到 OSS")
    public Result<OssFile> upload(
            @Parameter(description = "文件", required = true)
            @RequestParam("file") MultipartFile file) throws IOException {
        OssFile ossFile = ossTemplate.upload(file.getOriginalFilename(), file.getInputStream());
        log.info("文件上传成功: {}", ossFile.getUrl());
        return Result.ok(ossFile);
    }

    /**
     * 上传文件到指定目录
     *
     * @param file 文件
     * @param dir  目录
     * @return 文件信息
     */
    @PostMapping("/upload/{dir}")
    @Operation(summary = "上传文件到指定目录", description = "上传文件到 OSS 指定目录下")
    public Result<OssFile> uploadToDir(
            @Parameter(description = "文件", required = true)
            @RequestParam("file") MultipartFile file,
            @Parameter(description = "目录名称", required = true)
            @PathVariable("dir") String dir) throws IOException {
        String fileName = dir + "/" + file.getOriginalFilename();
        OssFile ossFile = ossTemplate.upload(fileName, file.getInputStream());
        log.info("文件上传到目录 [{}] 成功: {}", dir, ossFile.getUrl());
        return Result.ok(ossFile);
    }

    /**
     * 获取文件 URL
     *
     * @param fileName 文件名
     * @return 文件 URL
     */
    @GetMapping("/url")
    @Operation(summary = "获取文件 URL", description = "获取文件的访问 URL")
    public Result<String> getUrl(
            @Parameter(description = "文件名", required = true)
            @RequestParam("fileName") String fileName) {
        String url = ossTemplate.getFileUrl(fileName);
        return Result.ok(url);
    }

    /**
     * 下载文件
     *
     * @param fileName 文件名
     * @param response HTTP 响应
     */
    @GetMapping("/download")
    @Operation(summary = "下载文件", description = "从 OSS 下载文件")
    public void download(
            @Parameter(description = "文件名", required = true)
            @RequestParam("fileName") String fileName,
            HttpServletResponse response) throws IOException {
        ossTemplate.download(fileName, response.getOutputStream());
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
    }

    /**
     * 删除文件
     *
     * @param fileName 文件名
     * @return 操作结果
     */
    @DeleteMapping("/delete")
    @Operation(summary = "删除文件", description = "从 OSS 删除文件")
    public Result<Boolean> delete(
            @Parameter(description = "文件名", required = true)
            @RequestParam("fileName") String fileName) {
        boolean result = ossTemplate.delete(fileName);
        log.info("文件删除结果: {}, fileName: {}", result, fileName);
        return Result.ok(result);
    }

    /**
     * 批量删除文件
     *
     * @param fileNames 文件名列表
     * @return 操作结果
     */
    @DeleteMapping("/delete/batch")
    @Operation(summary = "批量删除文件", description = "批量从 OSS 删除文件")
    public Result<Boolean> deleteBatch(
            @Parameter(description = "文件名列表", required = true)
            @RequestBody List<String> fileNames) {
        boolean result = ossTemplate.delete(fileNames);
        log.info("批量文件删除结果: {}, count: {}", result, fileNames.size());
        return Result.ok(result);
    }

    /**
     * 检查文件是否存在
     *
     * @param fileName 文件名
     * @return 是否存在
     */
    @GetMapping("/exists")
    @Operation(summary = "检查文件是否存在", description = "检查 OSS 中是否存在指定文件")
    public Result<Boolean> exists(
            @Parameter(description = "文件名", required = true)
            @RequestParam("fileName") String fileName) {
        boolean exists = ossTemplate.doesFileExist(fileName);
        return Result.ok(exists);
    }

    /**
     * 获取文件列表
     *
     * @param prefix 前缀（目录）
     * @return 文件列表
     */
    @GetMapping("/list")
    @Operation(summary = "获取文件列表", description = "获取 OSS 中的文件列表")
    public Result<List<OssFile>> list(
            @Parameter(description = "前缀（目录）")
            @RequestParam(value = "prefix", required = false) String prefix) {
        List<OssFile> files = ossTemplate.listFiles(prefix);
        return Result.ok(files);
    }

    /**
     * 复制文件
     *
     * @param sourceFileName 源文件名
     * @param targetFileName 目标文件名
     * @return 操作结果
     */
    @PostMapping("/copy")
    @Operation(summary = "复制文件", description = "在 OSS 内部复制文件")
    public Result<Boolean> copy(
            @Parameter(description = "源文件名", required = true)
            @RequestParam("sourceFileName") String sourceFileName,
            @Parameter(description = "目标文件名", required = true)
            @RequestParam("targetFileName") String targetFileName) {
        boolean result = ossTemplate.copy(sourceFileName, targetFileName);
        log.info("文件复制结果: {}, source: {}, target: {}", result, sourceFileName, targetFileName);
        return Result.ok(result);
    }

    /**
     * 移动文件
     *
     * @param sourceFileName 源文件名
     * @param targetFileName 目标文件名
     * @return 操作结果
     */
    @PostMapping("/move")
    @Operation(summary = "移动文件", description = "在 OSS 内部移动文件")
    public Result<Boolean> move(
            @Parameter(description = "源文件名", required = true)
            @RequestParam("sourceFileName") String sourceFileName,
            @Parameter(description = "目标文件名", required = true)
            @RequestParam("targetFileName") String targetFileName) {
        boolean result = ossTemplate.move(sourceFileName, targetFileName);
        log.info("文件移动结果: {}, source: {}, target: {}", result, sourceFileName, targetFileName);
        return Result.ok(result);
    }

}
