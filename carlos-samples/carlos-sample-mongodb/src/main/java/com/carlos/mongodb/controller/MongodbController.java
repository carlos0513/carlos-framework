package com.carlos.mongodb.controller;

import com.carlos.core.response.Result;
import com.carlos.mongodb.entity.LogDocument;
import com.carlos.mongodb.service.LogDocumentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * MongoDB CRUD 接口演示
 *
 * @author Carlos
 * @date 2026/3/15
 */
@RestController
@RequestMapping("/mongo/log")
@RequiredArgsConstructor
@Tag(name = "MongoDB 日志管理", description = "MongoDB 文档操作示例")
public class MongodbController {

    private final LogDocumentService logDocumentService;

    // ==================== 新增操作 ====================

    @PostMapping("/save")
    @Operation(summary = "保存日志", description = "新增或更新日志文档")
    public Result<LogDocument> save(@RequestBody LogDocument document) {
        if (document.getCreateTime() == null) {
            document.setCreateTime(LocalDateTime.now());
        }
        LogDocument saved = logDocumentService.save(document);
        return Result.success(saved);
    }

    @PostMapping("/saveBatch")
    @Operation(summary = "批量保存日志", description = "批量新增日志文档")
    public Result<List<LogDocument>> saveBatch(@RequestBody List<LogDocument> documents) {
        documents.forEach(doc -> {
            if (doc.getCreateTime() == null) {
                doc.setCreateTime(LocalDateTime.now());
            }
        });
        List<LogDocument> saved = logDocumentService.saveBatch(documents);
        return Result.success(saved);
    }

    // ==================== 查询操作 ====================

    @GetMapping("/{id}")
    @Operation(summary = "根据ID查询", description = "根据文档ID查询日志")
    @Parameter(name = "id", description = "文档ID", required = true)
    public Result<LogDocument> getById(@PathVariable String id) {
        Optional<LogDocument> document = logDocumentService.getById(id);
        return document.map(Result::success).orElseGet(() -> Result.error("文档不存在"));
    }

    @GetMapping("/list")
    @Operation(summary = "查询所有", description = "查询所有日志文档")
    public Result<List<LogDocument>> list() {
        List<LogDocument> list = logDocumentService.list();
        return Result.success(list);
    }

    @GetMapping("/page")
    @Operation(summary = "分页查询", description = "分页查询日志文档")
    public Result<Page<LogDocument>> page(
        @RequestParam(defaultValue = "1") Integer pageNum,
        @RequestParam(defaultValue = "10") Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNum - 1, pageSize, Sort.by(Sort.Direction.DESC, "createTime"));
        Page<LogDocument> page = logDocumentService.page(pageable);
        return Result.success(page);
    }

    @GetMapping("/byLevel")
    @Operation(summary = "根据级别查询", description = "根据日志级别查询")
    @Parameter(name = "level", description = "日志级别(INFO/WARN/ERROR)", required = true)
    public Result<List<LogDocument>> findByLevel(@RequestParam String level) {
        List<LogDocument> list = logDocumentService.findByLevel(level);
        return Result.success(list);
    }

    @GetMapping("/byModule")
    @Operation(summary = "根据模块查询", description = "根据模块名称模糊查询")
    @Parameter(name = "module", description = "模块名称", required = true)
    public Result<List<LogDocument>> findByModuleLike(@RequestParam String module) {
        List<LogDocument> list = logDocumentService.findByModuleLike(module);
        return Result.success(list);
    }

    @GetMapping("/byUserId")
    @Operation(summary = "根据用户查询", description = "根据用户ID查询")
    @Parameter(name = "userId", description = "用户ID", required = true)
    public Result<List<LogDocument>> findByUserId(@RequestParam Long userId) {
        List<LogDocument> list = logDocumentService.findByUserId(userId);
        return Result.success(list);
    }

    // ==================== 删除操作 ====================

    @DeleteMapping("/{id}")
    @Operation(summary = "根据ID删除", description = "根据文档ID删除日志")
    @Parameter(name = "id", description = "文档ID", required = true)
    public Result<Void> removeById(@PathVariable String id) {
        logDocumentService.removeById(id);
        return Result.success();
    }

    @DeleteMapping("/batch")
    @Operation(summary = "批量删除", description = "批量删除日志文档")
    public Result<Void> removeBatch(@RequestBody List<LogDocument> documents) {
        logDocumentService.removeBatch(documents);
        return Result.success();
    }

    @DeleteMapping("/all")
    @Operation(summary = "删除所有", description = "清空所有日志文档(谨慎使用)")
    public Result<Void> removeAll() {
        logDocumentService.removeAll();
        return Result.success();
    }

    // ==================== 统计操作 ====================

    @GetMapping("/count")
    @Operation(summary = "统计数量", description = "统计日志总数")
    public Result<Long> count() {
        long count = logDocumentService.count();
        return Result.success(count);
    }

    @GetMapping("/exists/{id}")
    @Operation(summary = "判断存在", description = "判断文档是否存在")
    @Parameter(name = "id", description = "文档ID", required = true)
    public Result<Boolean> existsById(@PathVariable String id) {
        boolean exists = logDocumentService.existsById(id);
        return Result.success(exists);
    }
}
