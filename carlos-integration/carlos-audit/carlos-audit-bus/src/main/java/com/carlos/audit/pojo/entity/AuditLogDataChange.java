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
 * 审计日志-数据变更详情 数据源对象
 * </p>
 *
 * @author Carlos
 * @date 2026年3月5日 下午11:36:54
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("audit_log_data_change")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AuditLogDataChange extends Model<AuditLogDataChange> implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     *
     */
    @TableId(type = IdType.ASSIGN_ID, value = "id")
    private Long id;
    /**
     * 关联主表ID
     */
    @TableField(value = "audit_log_id")
    private Long auditLogId;
    /**
     * 变更前数据(JSON)
     */
    @TableField(value = "old_data")
    private String oldData;
    /**
     * 是否压缩
     */
    @TableField(value = "old_data_compressed")
    private Boolean oldDataCompressed;
    /**
     * 变更后数据(JSON)
     */
    @TableField(value = "new_data")
    private String newData;
    /**
     * 是否压缩
     */
    @TableField(value = "new_data_compressed")
    private Boolean newDataCompressed;
    /**
     * 变更字段数量
     */
    @TableField(value = "changed_field_count")
    private Integer changedFieldCount;
    /**
     *
     */
    @TableField(value = "created_time")
    private LocalDateTime createdTime;

}
