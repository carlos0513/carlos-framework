package com.carlos.msg.base.pojo.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 消息模板 显示层对象，向页面传输的对象
 * </p>
 *
 * @author Carlos
 * @date 2025-3-10 10:53:04
 */
@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MsgMessageTemplateExcel implements Serializable {


    @ExcelProperty(value = "消息类型")
    private String typeName;
    @ExcelProperty(value = "模板编码")
    private String templateCode;
    @ExcelProperty(value = "模板内容(含变量占位符)")
    private String templateContent;
    @ExcelProperty(value = "渠道特殊配置")
    private String channelConfig;
    @ExcelProperty(value = "开启状态")
    private Boolean active;


}
