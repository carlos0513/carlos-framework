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
 * 审计日志-动态标签 数据源对象
 * </p>
 *
 * @author Carlos
 * @date 2026年3月5日 下午11:36:54
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("audit_log_tags")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AuditLogTags extends Model<AuditLogTags> implements Serializable {
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
     * 标签键
     */
    @TableField(value = "tag_key")
    private String tagKey;
    /**
     * 标签值
     */
    @TableField(value = "tag_value")
    private String tagValue;
    /**
     *
     */
    @TableField(value = "created_time")
    private LocalDateTime createdTime;

}
