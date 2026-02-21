package com.yunjin.org.pojo.excel;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @Description: Excel错误信息对象
 * @Date: 2023/2/21 10:44
 */
@Data
@Accessors(chain = true)
public class ExcelResultMessage {

    /**
     * 导入总条数
     */
    @Schema(value = "导入总条数")
    private Integer totalCount;

    /**
     * 成功条数
     */
    @Schema(value = "成功条数")
    private Integer successCount;

    /**
     * 失败条数
     */
    @Schema(value = "失败条数")
    private Integer failedCount;

    /**
     * 解析失败信息
     */
    @Schema(value = "失败信息")
    private List<ExcelErrorMessage> failedMessage;
}
