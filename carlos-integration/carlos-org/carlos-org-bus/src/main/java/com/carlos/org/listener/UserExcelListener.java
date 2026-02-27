package com.carlos.org.listener;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.text.StrPool;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import cn.idev.excel.annotation.ExcelIgnore;
import cn.idev.excel.annotation.ExcelProperty;
import cn.idev.excel.context.AnalysisContext;
import cn.idev.excel.exception.ExcelDataConvertException;
import cn.idev.excel.metadata.data.CellData;
import cn.idev.excel.metadata.data.ReadCellData;
import cn.idev.excel.read.listener.ReadListener;
import cn.idev.excel.util.ListUtils;
import com.carlos.core.exception.ServiceException;
import com.carlos.core.response.Result;
import com.carlos.org.pojo.excel.UserImportExcel;
import com.carlos.org.service.UserService;
import com.carlos.system.api.ApiDict;
import com.carlos.system.api.ApiRegion;
import com.carlos.system.pojo.ao.DictItemAO;
import com.carlos.system.pojo.ao.SysRegionAO;
import com.google.common.collect.Sets;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Description: 用户导入数据--监听器
 * @Date: 2023/2/20 14:18
 */
@Slf4j
@Data
public class UserExcelListener<T> implements ReadListener<UserImportExcel> {

    private final UserService userService;
    private final Class<T> clazz;

    /**
     * 每隔5条存储数据库，实际使用中可以100条，然后清理list ，方便内存回收
     */


    private List<UserImportExcel> cachedDataList = ListUtils.newArrayList();
    private List<UserImportExcel> errorList = ListUtils.newArrayList();

    //// 临时存储本次导入中所有部门编码，用于快速查找上下级关系
    // private final Map<String, Department> codeMap = new HashMap<>();

    private Map<String, String> typeMap;

    private Map<String, String> regionMap;
    /**
     * 表头校验标识
     */
    private boolean headerChecked = false;

    public UserExcelListener(UserService userService, Class<T> clazz) {
        this.userService = userService;
        this.clazz = clazz;
    }

    @Override
    public void invoke(UserImportExcel userImport, AnalysisContext context) {
        // 参数校验

        StringBuilder errorMsg = new StringBuilder();
        if (StrUtil.isBlank(userImport.getPhone())) {
            errorMsg.append("手机号码缺失;");
            // throw new ServiceException("电话号码缺失，异常行信息：" + userImport);
        }
        if (StrUtil.isBlank(userImport.getId())) {
            errorMsg.append("id缺失;");
            // throw new ServiceException("id缺失，异常行信息：" + userImport);
        }
        if (StrUtil.isBlank(userImport.getRole())) {
            errorMsg.append("角色缺失;");
            // throw new ServiceException("角色缺失，异常行信息：" + userImport);
            // userImport.setRole("普通用户");
        }
        if (StrUtil.isBlank(userImport.getAccount())) {
            errorMsg.append("用户名缺失;");
            // throw new ServiceException("用户名缺失，异常行信息：" + userImport);
        }
        if (StrUtil.isBlank(userImport.getRealname())) {
            errorMsg.append("真实姓名缺失;");
            // throw new ServiceException("用户名缺失，异常行信息：" + userImport);
        }
        if (StrUtil.isBlank(userImport.getDepartment())) {
            errorMsg.append("部门名缺失（department）;");
            // throw new ServiceException("部门名缺失（department），异常行信息：" + userImport);
        }
        userImport.setErrorMsg(errorMsg.toString());
        if (CharSequenceUtil.isNotBlank(errorMsg)) {
            errorList.add(userImport);
        }
        // 全量数据
        cachedDataList.add(userImport);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        // saveData();
        log.info("所有数据解析完成！");
    }

    @Override
    public void invokeHead(Map<Integer, ReadCellData<?>> headMap, AnalysisContext context) {
        if (headerChecked) {
            return;
        }
        // 1. 期望列名
        List<String> expected = Arrays.stream(clazz.getDeclaredFields())
                .filter(f -> f.isAnnotationPresent(ExcelProperty.class))
                .filter(f -> !f.isAnnotationPresent(ExcelIgnore.class))
                .map(f -> f.getAnnotation(ExcelProperty.class).value()[0])
                // 手动过滤错误信息字段
                .filter(f -> !"错误信息".equals(f))
                .collect(Collectors.toList());
        // 2. 实际列名

        List<String> actual = headMap.values().stream().map(CellData::getStringValue).collect(Collectors.toList());
        Set<String> missingFields = Sets.newHashSet();
        expected.forEach(i -> {
            if (!actual.contains(i)) {
                missingFields.add(i);
            }
        });
        if (CollUtil.isNotEmpty(missingFields)) {
            log.error("template error, missing fields:{}:", missingFields);
            throw new ServiceException("模板错误，请使用正确的模板! 缺失字段：" + CharSequenceUtil.join(StrPool.COMMA, missingFields));
        }
        headerChecked = true;
    }

    /**
     * 在转换异常 获取其他异常下会调用本接口。抛出异常则停止读取。如果这里不抛出异常则 继续读取下一行。
     */
    @Override
    public void onException(Exception exception, AnalysisContext context) {
        // 如果是某一个单元格的转换异常 能获取到具体行号
        // 如果要获取头的信息 配合invokeHeadMap使用
        if (exception instanceof ExcelDataConvertException) {
            ExcelDataConvertException excelDataConvertException = (ExcelDataConvertException) exception;
            Integer rowIndex = excelDataConvertException.getRowIndex();
            Integer columnIndex = excelDataConvertException.getColumnIndex();
            CellData<?> cellData = excelDataConvertException.getCellData();
            log.error("第{}行，第{}列解析异常，数据为:{}", excelDataConvertException.getRowIndex(),
                    excelDataConvertException.getColumnIndex(), excelDataConvertException.getCellData());
            throw new ServiceException("第" + rowIndex + "行，第" + columnIndex + "列解析异常，数据为:" + cellData + "");
        }
    }


    private Map<String, String> getTypeMap() {
        if (MapUtil.isEmpty(this.typeMap)) {
            this.typeMap = buildTypeMap();
        }
        return typeMap;
    }

    private Map<String, String> buildTypeMap() {

        Map<String, String> map = new HashMap<>();
        ApiDict api = SpringUtil.getBean(ApiDict.class);
        Result<List<DictItemAO>> result = api.list("ORG_TYPE");

        if (!result.getSuccess()) {
            log.error("Api request failed, message: {}, detail message:{}", result.getMessage(), result.getStack());
            throw new ServiceException(result.getMessage());
        }
        Optional.ofNullable(result.getData()).ifPresent(dict -> {
            for (DictItemAO item : dict) {
                map.put(item.getItemName(), item.getItemCode());
            }
        });
        return map;
    }

    private Map<String, String> getRegionMap() {
        if (MapUtil.isEmpty(this.regionMap)) {
            this.regionMap = buildRegionMap();
        }
        return regionMap;
    }

    private Map<String, String> buildRegionMap() {
        Map<String, String> map = new HashMap<>();
        ApiRegion api = SpringUtil.getBean(ApiRegion.class);
        Result<List<SysRegionAO>> result = api.all();
        if (!result.getSuccess()) {
            log.error("Api request failed, message: {}, detail message:{}", result.getMessage(), result.getStack());
            throw new ServiceException(result.getMessage());
        }
        Optional.ofNullable(result.getData()).ifPresent(regions -> {
            for (SysRegionAO region : regions) {
                map.put(region.getRegionCode(), region.getRegionName());
            }
        });
        return map;
    }
}