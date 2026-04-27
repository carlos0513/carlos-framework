package com.carlos.org.pojo.entity;


import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * <p>
 * 用户岗位关联表
 * </p>
 *
 * @author carlos
 * @date 2026-03-03
 */
@Data
@Accessors(chain = true)
@TableName("org_user_position")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrgUserPosition extends Model<OrgUserPosition> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID, value = "id")
    private Long id;

    /**
     * 用户ID
     */
    @TableField(value = "user_id")
    private Long userId;

    /**
     * 岗位ID
     */
    @TableField(value = "position_id")
    private Long positionId;

    /**
     * 职级ID
     */
    @TableField(value = "level_id")
    private Long levelId;

    /**
     * 任职部门ID
     */
    @TableField(value = "department_id")
    private Long departmentId;

    /**
     * 是否主岗位：1是，0否
     */
    @TableField(value = "is_main")
    private Boolean isMain;

    /**
     * 任职状态：1在职，2试用期，3待入职，4待离职，5已卸任
     */
    @TableField(value = "position_status")
    private Integer positionStatus;

    /**
     * 任职日期
     */
    @TableField(value = "appoint_date")
    private LocalDate appointDate;

    /**
     * 卸任日期
     */
    @TableField(value = "dimission_date")
    private LocalDate dimissionDate;

    /**
     * 逻辑删除
     */
    @TableLogic
    @TableField(value = "is_deleted")
    private Boolean deleted;

    /**
     * 创建者
     */
    @TableField(value = "create_by")
    private Long createBy;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    private LocalDateTime createTime;

    /**
     * 修改者
     */
    @TableField(value = "update_by")
    private Long updateBy;

    /**
     * 修改时间
     */
    @TableField(value = "update_time")
    private LocalDateTime updateTime;

    /**
     * 租户id
     */
    @TableField(value = "tenant_id")
    private Long tenantId;

}
