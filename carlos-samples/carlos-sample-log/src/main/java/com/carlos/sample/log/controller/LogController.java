package com.carlos.sample.log.controller;

import com.carlos.log.annotation.Log;
import com.carlos.log.enums.BusinessType;
import com.carlos.log.enums.OperatorType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 日志演示控制器
 * <p>
 * 演示使用 @Log 注解记录操作日志的各种场景
 *
 * @author carlos
 */
@Slf4j
@RestController
@RequestMapping("/log")
public class LogController {

    /**
     * 简单日志记录 - 查询操作
     */
    @Log(title = "用户管理", businessType = BusinessType.QUERY)
    @GetMapping("/list")
    public Map<String, Object> list() {
        log.info("执行查询操作");
        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("message", "查询成功");
        result.put("data", "用户列表数据");
        return result;
    }

    /**
     * 记录新增操作日志
     */
    @Log(title = "用户管理", businessType = BusinessType.INSERT)
    @PostMapping("/save")
    public Map<String, Object> save(@RequestBody Map<String, Object> params) {
        log.info("执行新增操作，参数：{}", params);
        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("message", "新增成功");
        result.put("data", params);
        return result;
    }

    /**
     * 记录更新操作日志
     */
    @Log(title = "用户管理", businessType = BusinessType.UPDATE)
    @PutMapping("/update/{id}")
    public Map<String, Object> update(@PathVariable Long id, @RequestBody Map<String, Object> params) {
        log.info("执行更新操作，ID：{}，参数：{}", id, params);
        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("message", "更新成功");
        result.put("id", id);
        result.put("data", params);
        return result;
    }

    /**
     * 记录删除操作日志
     */
    @Log(title = "用户管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/delete/{id}")
    public Map<String, Object> delete(@PathVariable Long id) {
        log.info("执行删除操作，ID：{}", id);
        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("message", "删除成功");
        result.put("id", id);
        return result;
    }

    /**
     * 记录导出操作日志
     */
    @Log(title = "用户管理", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public Map<String, Object> export() {
        log.info("执行导出操作");
        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("message", "导出成功");
        result.put("fileName", "用户数据.xlsx");
        return result;
    }

    /**
     * 记录登录操作日志（指定操作者类型为其他）
     */
    @Log(title = "登录认证", businessType = BusinessType.OTHER, operatorType = OperatorType.OTHER)
    @PostMapping("/login")
    public Map<String, Object> login(@RequestParam String username, @RequestParam String password) {
        log.info("执行登录操作，用户名：{}", username);
        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("message", "登录成功");
        result.put("username", username);
        return result;
    }

    /**
     * 禁用日志记录（用于对比）
     */
    @Log(title = "测试功能", businessType = BusinessType.QUERY)
    @GetMapping("/no-log")
    public Map<String, Object> noLog() {
        log.info("此操作不会被记录到操作日志");
        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("message", "操作成功（无日志记录）");
        return result;
    }

    /**
     * 模拟异常操作（演示错误日志记录）
     */
    @Log(title = "异常测试", businessType = BusinessType.QUERY)
    @GetMapping("/error")
    public Map<String, Object> error() {
        log.error("模拟异常操作");
        throw new RuntimeException("模拟业务异常");
    }
}
