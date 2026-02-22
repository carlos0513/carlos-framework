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
 * 部门 数据源对象
 * </p>
 *
 * @author yunjin
 * @date 2022-11-11 18:19:17
 */
@Data
@Accessors(chain = true)
@TableName("org_department")
@JsonInclude(JsonInclude.Include.ALWAYS)
public class Department extends Model<Department> implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_UUID, value = "id")
    private String id;
    /**
     * 部门名称
     */
    @TableField(value = "dept_name")
    private String deptName;
    /**
     * 部门编号
     */
    @TableField(value = "dept_code")
    private String deptCode;
    /**
     * 行政区域编码
     */
    @TableField(value = "region_code")
    private String regionCode;
    /**
     * 详细地址
     */
    @TableField(value = "address")
    private String address;
    /**
     * 详细地址
     */
    @TableField(value = "admin_id")
    private String adminId;
    /**
     * 联系方式
     */
    @TableField(value = "tel")
    private String tel;
    /**
     * 父id
     */
    @TableField(value = "parent_id")
    private String parentId;
    /**
     * 部门层级
     */
    @TableField(value = "level")
    private Long level;
    /**
     * 状态，0：禁用，1：启用
     */
    @TableField(value = "state")
    private String state;
    /**
     * 排序
     */
    @TableField(value = "sort")
    private Integer sort;
    /**
     * 备注
     */
    @TableField(value = "description")
    private String description;
    /**
     * 版本
     */
    @Version
    @TableField(value = "version")
    private Long version;
    /**
     * 逻辑删除，0：未删除，1：已删除
     */
    @TableLogic
    @TableField(value = "is_deleted")
    private Boolean deleted;
    /**
     * 创建者
     */
    @TableField(value = "create_by", fill = FieldFill.INSERT)
    private String createBy;
    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    /**
     * 修改者
     */
    @TableField(value = "update_by", fill = FieldFill.INSERT_UPDATE)
    private String updateBy;
    /**
     * 修改时间
     */
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    /**
     * 租户id
     */
    @TableField(value = "tenant_id")
    private String tenantId;
    /**
     * 组织机构类型
     */
    @TableField(value = "department_type")
    private String departmentType;
    /**
     * 对应层级
     */
    @TableField(value = "department_level_code")
    private String departmentLevelCode;

}
