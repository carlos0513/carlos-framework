package com.yunjin.org.pojo.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 显示层对象，向页面传输的对象
 * </p>
 *
 * @author yunjin
 * @date 2024-9-23 16:01:35
 */
@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrgComplaintReportVO implements Serializable {
    private static final long serialVersionUID = 1L;
    @Schema(value = "主键")
    private String id;
    @Schema(value = "投诉来源")
    private Integer complaintSource;
    @Schema(value = "投诉来源中文")
    private String complaintSourceName;
    @Schema(value = "任务来源")
    private Integer taskSource;
    @Schema(value = "任务来源名称")
    private String taskSourceName;
    @Schema(value = "任务来源系统")
    private String taskSys;
    @Schema(value = "投诉任务")
    private String complaintTask;
    @Schema(value = "投诉任务名称")
    private String complaintTaskName;
    @Schema(value = "投诉表单")
    private String complaintForm;
    @Schema(value = "投诉表单中文")
    private String complaintFormName;
    @Schema(value = "投诉类型")
    private Integer complaintType;
    @Schema(value = "投诉类型中文")
    private String complaintTypeName;
    @Schema(value = "投诉原因")
    private String reason;
    @Schema(value = "投诉部门")
    private String formDept;
    @Schema(value = "投诉父部门")
    private String parFormDept;
    @Schema(value = "投诉状态")
    private Integer status;
    @Schema(value = "投诉状态中文")
    private String statusName;
    @Schema(value = "投诉反馈")
    private String reply;
    @Schema(value = "创建者")
    private String createBy;
    @Schema(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;
    @Schema(value = "修改者")
    private String updateBy;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Schema(value = "修改时间")
    private LocalDateTime updateTime;
    @Schema(value = "处理层级")
    private Integer handleLevel;
    @Schema(value = "投诉截图")
    private String pictures;

}
