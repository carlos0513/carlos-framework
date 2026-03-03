package com.carlos.org.pojo.entity;


import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 岗位表
 * </p>
 *
 * @author carlos
 * @date 2026-03-03
 */
@Data
@Accessors(chain = true)
@TableName("org_position")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrgPosition extends Model<OrgPosition> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID, value = "id")
    private Long id;

    /**
     * 岗位编码
     */
    @TableField(value = "position_code")
    private String positionCode;

    /**
     * 岗位名称
     */
    @TableField(value = "position_name")
    private String positionName;

    /**
     * 所属职系ID
     */
    @TableField(value = "category_id")
    private Long categoryId;

    /**
     * 职级ID
     */
    @TableField(value = "level_id")
    private Long levelId;

    /**
     * 所属部门ID
     */
    @TableField(value = "department_id")
    private Long departmentId;

    /**
     * 岗位类型：1标准岗，2虚拟岗，3兼职岗
     */
    @TableField(value = "position_type")
    private Integer positionType;

    /**
     * 状态：0禁用，1启用
     */
    @TableField(value = "state")
    private Integer state;

    /**
     * 版本
     */
    @Version
    @TableField(value = "version")
    private Integer version;

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
