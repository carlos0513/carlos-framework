package com.yunjin.org.pojo.param;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;


/**
 * <p>
 * 新增参数封装
 * </p>
 *
 * @author yunjin
 * @date 2024-9-23 16:01:35
 */
@Data
@Accessors(chain = true)
@Schema(value = "新增参数", description = "新增参数")
public class OrgComplaintReportCreateParam {
    @Schema(value = "投诉来源")
    private Integer complaintSource;
    @Schema(value = "任务来源")
    private Integer taskSource;
    @Schema(value = "任务来源系统")
    private String taskSys;
    @Schema(value = "投诉任务")
    private String complaintTask;
    @Schema(value = "投诉表单")
    private String complaintForm;
    @Schema(value = "投诉类型")
    private Integer complaintType;
    @Schema(value = "投诉原因")
    private String reason;
    @Schema(value = "投诉部门")
    private String formDept;
    @Schema(value = "投诉状态")
    private Integer status;
    @Schema(value = "投诉反馈")
    private String reply;
    @Schema(value = "处理层级")
    private Integer handleLevel;
    @Schema(value = "投诉截图")
    private String pictures;
    @Schema(value = "白名单标识")
    private String sgin;
}
