package com.carlos.org.pojo.dto;


import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * <p>
 * 用户岗位DTO
 * </p>
 *
 * @author carlos
 * @date 2026-03-03
 */
@Data
@Accessors(chain = true)
public class OrgUserPositionDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 岗位ID
     */
    private Long positionId;

    /**
     * 岗位名称
     */
    private String positionName;

    /**
     * 岗位编码
     */
    private String positionCode;

    /**
     * 职级ID
     */
    private Long levelId;

    /**
     * 职级名称
     */
    private String levelName;

    /**
     * 部门ID
     */
    private Long departmentId;

    /**
     * 部门名称
     */
    private String departmentName;

    /**
     * 是否主岗位
     */
    private Boolean isMain;

    /**
     * 任职状态：1在职，2试用期，3待入职，4待离职，5已卸任
     */
    private Integer positionStatus;

    /**
     * 任职日期
     */
    private LocalDate appointDate;

}
