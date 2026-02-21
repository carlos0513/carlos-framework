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
 * 投诉建议处理节点日志 数据源对象
 * </p>
 *
 * @author yunjin
 * @date 2024-9-23 16:01:35
 */
@Data
@Accessors(chain = true)
@TableName("org_complaint_log")
@JsonInclude
public class OrgComplaintLog extends Model<OrgComplaintLog> implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_UUID, value = "id")
    private String id;
    /**
     * 投诉id
     */
    @TableField(value = "complaint_id")
    private String complaintId;
    /**
     * 处理类型
     */
    @TableField(value = "handle_type")
    private Integer handleType;
    /**
     * 处理备注
     */
    @TableField(value = "remark")
    private String remark;
    /**
     * 当前处理部门code
     */
    @TableField(value = "dept_code")
    private String deptCode;
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

}
