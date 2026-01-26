# yunjin-excel

## 模块简介

`yunjin-excel` 是 YunJin 框架的 Excel 导入导出模块，基于 EasyExcel 3.1.3 和 Apache POI 5.2.5 构建。该模块提供了高性能的 Excel 读写功能、批量导入处理、数据验证、LuckySheet 在线表格支持等功能。

## 主要功能

### 1. Excel 导出

#### 基础导出（注解方式）

```java
// 1. 定义数据模型
@Data
public class UserExportData {
    @ExcelProperty("用户ID")
    private Long id;

    @ExcelProperty("用户名")
    private String name;

    @ExcelProperty("邮箱")
    private String email;

    @ExcelProperty("创建时间")
    private LocalDateTime createTime;
}

// 2. 控制器导出
@RestController
@RequestMapping("/user")
public class UserController {

    @GetMapping("/export")
    public void export(HttpServletResponse response) {
        List<UserExportData> dataList = userService.listExportData();
        ExcelUtil.download(response, "用户列表", UserExportData.class, dataList);
    }
}
```

#### 自定义表头导出

```java
@GetMapping("/export-custom")
public void exportCustom(HttpServletResponse response) {
    // 自定义表头
    List<List<String>> headers = Arrays.asList(
        Arrays.asList("用户ID"),
        Arrays.asList("用户名"),
        Arrays.asList("邮箱"),
        Arrays.asList("创建时间")
    );

    // 数据列表（每行是一个 List）
    List<List<Object>> dataList = new ArrayList<>();
    dataList.add(Arrays.asList(1L, "张三", "zhangsan@example.com", "2026-01-25"));
    dataList.add(Arrays.asList(2L, "李四", "lisi@example.com", "2026-01-24"));

    ExcelUtil.download(response, "用户列表", headers, dataList);
}
```

#### 复杂表头导出

```java
@Data
public class SalesReportData {
    @ExcelProperty({"销售数据", "基本信息", "日期"})
    private String date;

    @ExcelProperty({"销售数据", "基本信息", "区域"})
    private String region;

    @ExcelProperty({"销售数据", "销售额", "实际销售额"})
    private BigDecimal actualSales;

    @ExcelProperty({"销售数据", "销售额", "目标销售额"})
    private BigDecimal targetSales;

    @ExcelProperty({"销售数据", "完成率"})
    private String completionRate;
}
```

### 2. Excel 导入

#### 定义数据模型

```java
@Data
public class UserImportData implements ExcelData {

    @ExcelProperty("*用户名")
    @NotBlank(message = "用户名不能为空")
    private String name;

    @ExcelProperty("*邮箱")
    @NotBlank(message = "邮箱不能为空")
    @Email(message = "邮箱格式不正确")
    private String email;

    @ExcelProperty("*手机号")
    @NotBlank(message = "手机号不能为空")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String phone;

    @ExcelProperty("年龄")
    @Min(value = 0, message = "年龄不能小于0")
    @Max(value = 150, message = "年龄不能大于150")
    private Integer age;

    @ExcelProperty("错误信息")
    private String errorInfo;
}
```

#### 实现导入执行器

```java
@Slf4j
@Component
public class UserImportExecutor implements ExcelImportExecutor<UserImportData> {

    @Autowired
    private UserService userService;

    @Override
    public void check(UserImportData row,
                      List<UserImportData> cacheDataList,
                      List<UserImportData> errorDataList) {
        // 1. 数据验证
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        Validator validator = validatorFactory.getValidator();
        Set<ConstraintViolation<UserImportData>> violations = validator.validate(row);

        if (CollUtil.isNotEmpty(violations)) {
            // 收集验证错误信息
            List<String> errors = violations.stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.toList());
            String errorMessage = String.join(", ", errors);
            row.setErrorInfo(errorMessage);
            errorDataList.add(row);
            return;
        }

        // 2. 业务验证
        if (userService.existsByEmail(row.getEmail())) {
            row.setErrorInfo("邮箱已存在");
            errorDataList.add(row);
            return;
        }

        // 3. 验证通过，加入缓存列表
        cacheDataList.add(row);
    }

    @Override
    public boolean saveBatch(List<UserImportData> cacheDataList) {
        // 批量保存到数据库
        List<User> users = cacheDataList.stream()
            .map(data -> {
                User user = new User();
                BeanUtils.copyProperties(data, user);
                return user;
            })
            .collect(Collectors.toList());

        return userService.saveBatch(users);
    }

    @Override
    public void errorDataResponse(List<UserImportData> errorDataList) {
        if (CollUtil.isEmpty(errorDataList)) {
            log.info("导入成功，无错误数据");
            return;
        }

        // 导出错误数据
        log.warn("导入失败，错误数据数量: {}", errorDataList.size());
        // 可以将错误数据导出为 Excel 文件供用户下载
    }
}
```

#### 控制器导入

```java
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserImportExecutor userImportExecutor;

    @PostMapping("/import")
    public Result<Void> importUsers(@RequestParam("file") MultipartFile file) {
        try {
            // 验证文件格式
            ExcelUtil.checkExcel(file.getOriginalFilename());

            // 创建监听器
            ExcelDataImportListener<UserImportData> listener =
                new ExcelDataImportListener<>(userImportExecutor);

            // 读取 Excel
            EasyExcel.read(file.getInputStream(), UserImportData.class, listener)
                .sheet()
                .doRead();

            return Result.ok();
        } catch (IOException e) {
            throw new ExcelException("文件读取失败", e);
        }
    }
}
```

### 3. 批量导入处理

**批量处理特性**：

```java
@Slf4j
public class ExcelDataImportListener<T extends ExcelData> implements ReadListener<T> {

    // 批量大小：100 条
    private static final int BATCH_COUNT = 100;

    private List<T> rightRows = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);
    private List<T> errorRows = ListUtils.newArrayList();

    @Override
    public void invoke(T row, AnalysisContext context) {
        // 每读取一行数据，进行验证
        executor.check(row, rightRows, errorRows);

        // 达到批量大小，执行保存
        if (rightRows.size() >= BATCH_COUNT) {
            saveData();
            rightRows = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        // 保存剩余数据
        saveData();
        // 处理错误数据
        executor.errorDataResponse(errorRows);
    }

    private void saveData() {
        if (CollUtil.isNotEmpty(rightRows)) {
            executor.saveBatch(rightRows);
        }
    }
}
```

**优势**：

- 内存占用小：每次只处理 100 条数据
- 性能高：批量插入数据库
- 错误隔离：错误数据不影响正确数据

### 4. 数据验证

**支持的验证注解**：

```java
@Data
public class ProductImportData implements ExcelData {

    @ExcelProperty("*产品名称")
    @NotBlank(message = "产品名称不能为空")
    @Size(min = 2, max = 50, message = "产品名称长度必须在2-50之间")
    private String name;

    @ExcelProperty("*产品价格")
    @NotNull(message = "产品价格不能为空")
    @DecimalMin(value = "0.01", message = "产品价格必须大于0")
    @DecimalMax(value = "999999.99", message = "产品价格不能超过999999.99")
    private BigDecimal price;

    @ExcelProperty("*库存数量")
    @NotNull(message = "库存数量不能为空")
    @Min(value = 0, message = "库存数量不能小于0")
    private Integer stock;

    @ExcelProperty("产品描述")
    @Size(max = 500, message = "产品描述不能超过500字")
    private String description;

    @ExcelProperty("*产品分类")
    @NotBlank(message = "产品分类不能为空")
    private String category;

    @ExcelProperty("错误信息")
    private String errorInfo;
}
```

### 5. LuckySheet 支持

**LuckySheet** 是一个在线表格编辑器，模块提供了完整的数据模型支持。

#### 单元格数据模型

```java
@Data
public class CellData {
    private Integer r;  // 行号
    private Integer c;  // 列号
    private V v;        // 单元格值对象

    @Data
    public static class V {
        private Ct ct;           // 单元格格式
        private String v;        // 原始值
        private String m;        // 显示值
        private String bg;       // 背景色
        private Integer ff;      // 字体
        private String fc;       // 字体颜色
        private Integer bl;      // 粗体
        private Integer it;      // 斜体
        private Integer fs;      // 字体大小
        private Integer cl;      // 删除线
        private Integer ht;      // 水平对齐
        private Integer vt;      // 垂直对齐
        private Integer tr;      // 文本旋转
        private Integer tb;      // 文本换行
        private Ps ps;           // 批注
        private String f;        // 公式
    }
}
```

#### 工作表数据模型

```java
@Data
public class ExcelSheet {
    private String name;                    // 工作表名称
    private String color;                   // 标签颜色
    private Integer index;                  // 索引
    private Integer status;                 // 状态
    private Integer order;                  // 排序
    private Integer hide;                   // 是否隐藏
    private Integer row;                    // 行数
    private Integer column;                 // 列数
    private Integer defaultRowHeight;       // 默认行高
    private Integer defaultColWidth;        // 默认列宽
    private List<CellData> celldata;        // 单元格数据
    private Config config;                  // 配置
    private Frozen frozen;                  // 冻结
    private List<?> chart;                  // 图表
    private DataVerification dataVerification;  // 数据验证
    // ... 更多配置
}
```

### 6. 高级特性

#### 动态列导出

```java
@GetMapping("/export-dynamic")
public void exportDynamic(HttpServletResponse response) {
    // 动态构建表头
    List<List<String>> headers = new ArrayList<>();
    headers.add(Arrays.asList("姓名"));
    headers.add(Arrays.asList("年龄"));

    // 动态添加列
    for (int i = 1; i <= 12; i++) {
        headers.add(Arrays.asList(i + "月销售额"));
    }

    // 动态构建数据
    List<List<Object>> dataList = new ArrayList<>();
    // ... 填充数据

    ExcelUtil.download(response, "销售报表", headers, dataList);
}
```

#### 样式定制

```java
@Data
public class StyledData {
    @ExcelProperty("姓名")
    @ColumnWidth(20)  // 列宽
    private String name;

    @ExcelProperty("金额")
    @ColumnWidth(15)
    @NumberFormat("#,##0.00")  // 数字格式
    private BigDecimal amount;

    @ExcelProperty("日期")
    @ColumnWidth(20)
    @DateTimeFormat("yyyy-MM-dd HH:mm:ss")  // 日期格式
    private LocalDateTime date;

    @ExcelProperty("状态")
    @ColumnWidth(10)
    private String status;
}
```

#### 下拉选择

```java
@Data
public class DataWithSelect {
    @ExcelProperty("姓名")
    private String name;

    @ExcelProperty("性别")
    @ExcelIgnoreUnannotated
    private String gender;  // 下拉选择：男/女

    @ExcelProperty("部门")
    private String department;  // 下拉选择：研发部/销售部/财务部
}

// 导出时添加下拉选择
EasyExcel.write(outputStream, DataWithSelect.class)
    .registerWriteHandler(new CustomSelectHandler())
    .sheet("数据")
    .doWrite(dataList);
```

## 配置说明

### 依赖配置

```xml
<dependency>
    <groupId>com.yunjin</groupId>
    <artifactId>yunjin-excel</artifactId>
    <version>${yunjin.version}</version>
</dependency>
```

### 文件上传配置

```yaml
spring:
  servlet:
    multipart:
      enabled: true
      max-file-size: 10MB      # 单个文件最大大小
      max-request-size: 50MB   # 请求最大大小
```

## 依赖项

- **EasyExcel**: 3.1.3（高性能 Excel 读写）
- **Apache POI**: 5.2.5（Excel 格式支持）
- **Jakarta Validation**: 参数验证
- **Hutool**: 工具库
- **yunjin-core**: 核心基础模块

## 使用示例

### 完整导入导出示例

```java
// 1. 数据模型
@Data
public class EmployeeData implements ExcelData {
    @ExcelProperty("*工号")
    @NotBlank(message = "工号不能为空")
    private String employeeNo;

    @ExcelProperty("*姓名")
    @NotBlank(message = "姓名不能为空")
    private String name;

    @ExcelProperty("*部门")
    @NotBlank(message = "部门不能为空")
    private String department;

    @ExcelProperty("*职位")
    @NotBlank(message = "职位不能为空")
    private String position;

    @ExcelProperty("*入职日期")
    @NotNull(message = "入职日期不能为空")
    private LocalDate hireDate;

    @ExcelProperty("薪资")
    @DecimalMin(value = "0", message = "薪资不能小于0")
    private BigDecimal salary;

    @ExcelProperty("错误信息")
    private String errorInfo;
}

// 2. 导入执行器
@Component
public class EmployeeImportExecutor implements ExcelImportExecutor<EmployeeData> {

    @Autowired
    private EmployeeService employeeService;

    @Override
    public void check(EmployeeData row,
                      List<EmployeeData> cacheDataList,
                      List<EmployeeData> errorDataList) {
        // 验证
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<EmployeeData>> violations = validator.validate(row);

        if (!violations.isEmpty()) {
            String errors = violations.stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining(", "));
            row.setErrorInfo(errors);
            errorDataList.add(row);
            return;
        }

        // 业务验证
        if (employeeService.existsByEmployeeNo(row.getEmployeeNo())) {
            row.setErrorInfo("工号已存在");
            errorDataList.add(row);
            return;
        }

        cacheDataList.add(row);
    }

    @Override
    public boolean saveBatch(List<EmployeeData> cacheDataList) {
        List<Employee> employees = cacheDataList.stream()
            .map(data -> {
                Employee employee = new Employee();
                BeanUtils.copyProperties(data, employee);
                return employee;
            })
            .collect(Collectors.toList());
        return employeeService.saveBatch(employees);
    }

    @Override
    public void errorDataResponse(List<EmployeeData> errorDataList) {
        if (CollUtil.isEmpty(errorDataList)) {
            return;
        }
        log.error("导入失败，错误数据: {}", errorDataList.size());
    }
}

// 3. 控制器
@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private EmployeeImportExecutor importExecutor;

    // 导出
    @GetMapping("/export")
    public void export(HttpServletResponse response) {
        List<EmployeeData> dataList = employeeService.listExportData();
        ExcelUtil.download(response, "员工列表", EmployeeData.class, dataList);
    }

    // 导入
    @PostMapping("/import")
    public Result<Void> importData(@RequestParam("file") MultipartFile file) {
        try {
            ExcelUtil.checkExcel(file.getOriginalFilename());

            ExcelDataImportListener<EmployeeData> listener =
                new ExcelDataImportListener<>(importExecutor);

            EasyExcel.read(file.getInputStream(), EmployeeData.class, listener)
                .sheet()
                .doRead();

            return Result.ok();
        } catch (IOException e) {
            throw new ExcelException("文件读取失败", e);
        }
    }

    // 下载模板
    @GetMapping("/template")
    public void downloadTemplate(HttpServletResponse response) {
        List<EmployeeData> emptyList = Collections.emptyList();
        ExcelUtil.download(response, "员工导入模板", EmployeeData.class, emptyList);
    }
}
```

## 注意事项

1. **文件大小**: 配置合理的文件上传大小限制
2. **内存管理**: 大文件导入使用批量处理，避免内存溢出
3. **数据验证**: 使用 Jakarta Validation 进行数据验证
4. **错误处理**: 错误数据应导出供用户修正
5. **文件格式**: 仅支持 .xls 和 .xlsx 格式
6. **中文乱码**: 使用 UTF-8 编码，文件名需 URL 编码
7. **日期格式**: 注意日期格式的统一
8. **数字精度**: 使用 BigDecimal 处理金额
9. **模板下载**: 提供模板下载功能方便用户
10. **异步处理**: 大文件导入建议使用异步处理

## 性能优化

1. **批量处理**: 默认 100 条/批，可根据实际情况调整
2. **流式读取**: EasyExcel 使用流式读取，内存占用小
3. **并行处理**: 可使用线程池并行处理批次数据
4. **索引优化**: 导入前临时禁用索引，导入后重建
5. **事务控制**: 批量插入使用事务，提高性能

## 常见问题

### 1. 中文文件名乱码

**解决**：

```java
String fileName = URLEncoder.encode("文件名", "UTF-8")
    .replaceAll("\\+", "%20");
response.setHeader("Content-disposition",
    "attachment;filename*=utf-8''" + fileName + ".xlsx");
```

### 2. 日期格式错误

**解决**：

```java
@ExcelProperty("日期")
@DateTimeFormat("yyyy-MM-dd")
private LocalDate date;
```

### 3. 数字精度丢失

**解决**：

```java
@ExcelProperty("金额")
@NumberFormat("#.##")
private BigDecimal amount;
```

### 4. 内存溢出

**解决**：

- 减小批量大小
- 使用流式读取
- 增加 JVM 堆内存

## 版本要求

- JDK 17+
- Spring Boot 3.5.8+
- EasyExcel 3.1.3+
- Apache POI 5.2.5+
- Maven 3.8+

## 相关模块

- `yunjin-core`: 核心基础模块
- `yunjin-utils`: 工具模块
- `yunjin-mybatis`: 数据库操作
