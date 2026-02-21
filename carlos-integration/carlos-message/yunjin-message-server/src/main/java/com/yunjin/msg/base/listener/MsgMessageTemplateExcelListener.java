package com.carlos.msg.base.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.exception.ExcelDataConvertException;
import com.alibaba.excel.metadata.data.CellData;
import com.alibaba.excel.read.listener.ReadListener;
import com.alibaba.excel.util.ListUtils;
import com.carlos.core.exception.ServiceException;
import com.carlos.msg.base.convert.MsgMessageTemplateConvert;
import com.carlos.msg.base.pojo.dto.MsgMessageTemplateDTO;
import com.carlos.msg.base.pojo.excel.MsgMessageTemplateExcel;
import com.carlos.msg.base.service.MsgMessageTemplateService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * <p>
 *   消息模板数据导入监听器
 * </p>
 *
 * @author Carlos
 * @date 2025-03-11 11:47
 */
@Slf4j
@Data
public class MsgMessageTemplateExcelListener implements ReadListener<MsgMessageTemplateExcel> {

    private final MsgMessageTemplateService service;


    private List<MsgMessageTemplateExcel> cachedDataList = ListUtils.newArrayList();

    public MsgMessageTemplateExcelListener(final MsgMessageTemplateService service) {
        this.service = service;
    }

    @Override
    public void invoke(final MsgMessageTemplateExcel user, final AnalysisContext context) {
        cachedDataList.add(user);
    }

    @Override
    public void doAfterAllAnalysed(final AnalysisContext context) {
        saveData();
    }

    /**
     * 在转换异常 获取其他异常下会调用本接口。抛出异常则停止读取。如果这里不抛出异常则 继续读取下一行。
     */
    @Override
    public void onException(final Exception exception, final AnalysisContext context) {
        // 如果是某一个单元格的转换异常 能获取到具体行号
        // 如果要获取头的信息 配合invokeHeadMap使用
        if (exception instanceof ExcelDataConvertException) {
            final ExcelDataConvertException excelDataConvertException = (ExcelDataConvertException) exception;
            final Integer rowIndex = excelDataConvertException.getRowIndex();
            final Integer columnIndex = excelDataConvertException.getColumnIndex();
            final CellData<?> cellData = excelDataConvertException.getCellData();
            log.error("第{}行，第{}列解析异常，数据为:{}", excelDataConvertException.getRowIndex(),
                    excelDataConvertException.getColumnIndex(), excelDataConvertException.getCellData());
            throw new ServiceException("第" + rowIndex + "行，第" + columnIndex + "列解析异常，数据为:" + cellData + "");
        }
    }

    /**
     * 存储数据库
     */
    private void saveData() {
        final List<MsgMessageTemplateDTO> templateDTOS = MsgMessageTemplateConvert.INSTANCE.excelToDTOS(cachedDataList);
        for (MsgMessageTemplateDTO item : templateDTOS) {
            // FIXME 2025-03-11 保存时，考虑校验重复数据，如果重复了，返回excel文档，对错误数据行进行标注
            service.addMsgMessageTemplate(item);
        }
    }

}