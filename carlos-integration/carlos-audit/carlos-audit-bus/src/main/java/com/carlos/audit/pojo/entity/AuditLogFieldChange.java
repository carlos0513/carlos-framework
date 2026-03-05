package com.carlos.audit.pojo.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 审计日志-字段级变更明细 数据源对象
 * </p>
 *
 * @author Carlos
 * @date 2026年3月5日 下午11:36:54
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("audit_log_field_change")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AuditLogFieldChange extends Model<AuditLogFieldChange> implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     *
     */
    @TableId(type = IdType.ASSIGN_ID, value = "id")
    private Long id;
    /**
     *
     */
    @TableField(value = "audit_log_id")
    private Long auditLogId;
    /**
     *
     */
    @TableField(value = "data_change_id")
    private Long dataChangeId;
    /**
     * 字段名
     */
    @TableField(value = "field_name")
    private String fieldName;
    /**
     * 字段中文描述
     */
    @TableField(value = "field_desc")
    private String fieldDesc;
    /**
     * ADDED/MODIFIED/REMOVED
     */
    @TableField(value = "change_type")
    private Integer changeType;
    /**
     * 旧值
     */
    @TableField(value = "old_value")
    private String oldValue;
    /**
     * 值类型: STRING/NUMBER/JSON
     */
    @TableField(value = "old_value_type")
    private Integer oldValueType;
    /**
     * 新值
     */
    @TableField(value = "new_value")
    private String newValue;
    /**
     * 值类型
     */
    @TableField(value = "new_value_type")
    private Integer newValueType;
    /**
     * 是否敏感字段
     */
    @TableField(value = "is_sensitive")
    private Boolean sensitive;
    /**
     *
     */
    @TableField(value = "created_time")
    private LocalDateTime createdTime;

}
