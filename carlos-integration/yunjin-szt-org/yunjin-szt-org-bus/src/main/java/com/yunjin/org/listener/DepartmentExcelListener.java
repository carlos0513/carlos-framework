package com.yunjin.org.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.exception.ExcelDataConvertException;
import com.alibaba.excel.metadata.data.CellData;
import com.alibaba.excel.read.listener.ReadListener;
import com.alibaba.excel.util.ListUtils;
import com.yunjin.core.exception.ServiceException;
import com.yunjin.org.pojo.excel.DepartmentExcel;
import com.yunjin.org.service.DepartmentService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @Description: 部门导入数据--监听器
 * @Date: 2023/2/21 15:18
 */
@Slf4j
@Data
public class DepartmentExcelListener implements ReadListener<DepartmentExcel> {

    private final DepartmentService departmentService;

    /**
     * 每隔5条存储数据库，实际使用中可以100条，然后清理list ，方便内存回收
     */
    //private static final int BATCH_COUNT = 50;

    private List<DepartmentExcel> cachedDataList = ListUtils.newArrayList();

    public DepartmentExcelListener(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @Override
    public void invoke(DepartmentExcel user, AnalysisContext context) {
        cachedDataList.add(user);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {

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


}