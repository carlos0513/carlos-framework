package com.yunjin.org.pojo.entity;


import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 数据源对象
 * </p>
 *
 * @author yunjin
 * @date 2024-9-23 16:01:35
 */
@Data
@Accessors(chain = true)
@TableName("org_complaint_report")
@JsonInclude
public class OrgComplaintReport extends Model<OrgComplaintReport> implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_UUID, value = "id")
    private String id;
    /**
     * 投诉来源
     */
    @TableField(value = "complaint_source")
    private Integer complaintSource;
    /**
     * 任务来源
     */
    @TableField(value = "task_source")
    private Integer taskSource;
    /**
     * 任务来源系统
     */
    @TableField(value = "task_sys")
    private String taskSys;
    /**
     * 投诉任务
     */
    @TableField(value = "complaint_task")
    private String complaintTask;
    /**
     * 投诉表单
     */
    @TableField(value = "complaint_form")
    private String complaintForm;
    /**
     * 投诉类型
     */
    @TableField(value = "complaint_type")
    private Integer complaintType;
    /**
     * 投诉原因
     */
    @TableField(value = "reason")
    private String reason;
    /**
     * 投诉部门
     */
    @TableField(value = "form_dept")
    private String formDept;
    /**
     * 投诉状态
     */
    @TableField(value = "status")
    private Integer status;
    /**
     * 投诉反馈
     */
    @TableField(value = "reply")
    private String reply;
    /**
     * 处理层级
     */
    @TableField(value = "handle_level")
    private Integer handleLevel;
    /**
     * 投诉截图
     */
    @TableField(value = "pictures")
    private String pictures;
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
    @TableField(value = "update_time", fill = FieldFill.INSERT)
    private LocalDateTime updateTime;


}
