package com.carlos.org.pojo.vo;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * <p>
 * 用户岗位VO
 * </p>
 *
 * @author carlos
 * @date 2026-03-03
 */
@Data
@Accessors(chain = true)
@Schema(description = "用户岗位VO")
public class OrgUserPositionVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "岗位ID")
    private Long positionId;

    @Schema(description = "岗位名称")
    private String positionName;

    @Schema(description = "岗位编码")
    private String positionCode;

    @Schema(description = "职级ID")
    private Long levelId;

    @Schema(description = "职级名称")
    private String levelName;

    @Schema(description = "部门ID")
    private Long departmentId;

    @Schema(description = "部门名称")
    private String departmentName;

    @Schema(description = "是否主岗位")
    private Boolean isMain;

    @Schema(description = "任职状态：1在职，2试用期，3待入职，4待离职，5已卸任")
    private Integer positionStatus;

    @Schema(description = "任职日期")
    private LocalDate appointDate;

}
