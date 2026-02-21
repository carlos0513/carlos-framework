package com.yunjin.org.pojo.param;


import com.yunjin.core.param.ParamPage;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;


/**
 * <p>
 * 列表查询参数封装
 * </p>
 *
 * @author yunjin
 * @date 2024-9-23 16:01:35
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@Schema(value = "列表查询参数", description = "列表查询参数")
public class OrgComplaintReportPageParam extends ParamPage {
    @Schema(value = "投诉来源")
    private Integer complaintSource;
    @Schema(value = "任务来源")
    private Integer taskSource;
    @Schema(value = "任务来源系统")
    private String taskSys;
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
    @Schema("开始时间")
    private LocalDateTime start;

    @Schema("结束时间")
    private LocalDateTime end;
    @Schema("用户id")
    private String userId;
}
