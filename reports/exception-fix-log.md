# 异常吞噬问题修复清单

## 发现的问题汇总

### 1. e.printStackTrace() 问题（高优先级）

| 序号 | 文件路径 | 行号 | 问题描述 | 修复方案 | 状态 |
|------|----------|------|----------|----------|------|
| 1 | `carlos-integration/carlos-license/carlos-license-core/src/main/java/com/carlos/license/CustomLicenseManager.java` | 174 | `e.printStackTrace()` 在 `load` 方法中 | 改用 `log.error` 记录异常 | ✅ 已修复 |
| 2 | `carlos-commons/carlos-utils/src/main/java/com/carlos/util/easyexcel/ExcelUtil.java` | 130 | `e.printStackTrace()` 在 `getReader` 方法中 | 改用 `log.error` 记录异常 | ✅ 已修复 |
| 3 | `carlos-commons/carlos-utils/src/main/java/com/carlos/util/easyexcel/ExcelUtil.java` | 158 | `e.printStackTrace()` 在 `getReaderNoHead` 方法中 | 改用 `log.error` 记录异常 | ✅ 已修复 |

---

## 修复详情

### 修复 1：CustomLicenseManager.java
**变更：**
```java
// 修复前
catch (UnsupportedEncodingException e) {
    e.printStackTrace();
}

// 修复后
catch (UnsupportedEncodingException e) {
    log.error("XML解析失败：不支持的编码", e);
}
```

### 修复 2 & 3：ExcelUtil.java
**变更：**
```java
// 修复前
catch (IOException e) {
    e.printStackTrace();
}

// 修复后
catch (IOException e) {
    log.error("读取Excel文件失败: {}", excel.getOriginalFilename(), e);
}
```

同时添加了 `@Slf4j` 注解以支持日志记录。

---

## 建议后续处理

### 2. 异常被静默吞噬问题（中优先级）

需要进一步扫描确认：
- [ ] 扫描所有 `catch` 块中仅有日志记录没有抛出或处理的场景
- [ ] 检查是否应该抛出运行时异常而非静默处理

### 3. 异常处理不规范问题（低优先级）

- [ ] `AuditLogStorage.java` - 异常被捕获返回 false，可能需要更详细的错误处理
- [ ] `ClickHouseHealthIndicator.java` - 健康检查异常处理较完善，但可考虑增加重试

---

## 修复统计

- **已修复问题数**: 3
- **涉及文件数**: 2
- **引入变更**: 添加 @Slf4j 注解 1 处

---

*修复时间: 2026-04-03*
*修复人: 泡泡*
