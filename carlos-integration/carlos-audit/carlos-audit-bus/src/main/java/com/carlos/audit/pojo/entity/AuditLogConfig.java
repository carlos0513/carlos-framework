package com.carlos.audit.pojo.entity;


import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 审计日志配置 数据源对象
 * </p>
 *
 * @author Carlos
 * @date 2026年3月5日 下午11:36:54
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("audit_log_config")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AuditLogConfig extends Model<AuditLogConfig> implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     *
     */
    @TableId(type = IdType.ASSIGN_ID, value = "id")
    private Long id;
    /**
     * 日志类型
     */
    @TableField(value = "log_type")
    private String logType;
    /**
     * 保留天数
     */
    @TableField(value = "retention_days")
    private Integer retentionDays;
    /**
     * 采样率 0.00-1.00
     */
    @TableField(value = "sampling_rate")
    private BigDecimal samplingRate;
    /**
     * 是否异步写入
     */
    @TableField(value = "async_write")
    private Boolean asyncWrite;
    /**
     * 是否存储数据变更
     */
    @TableField(value = "store_data_change")
    private Boolean storeDataChange;
    /**
     * 是否存储技术上下文
     */
    @TableField(value = "store_technical")
    private Boolean storeTechnical;
    /**
     * 逻辑删除，0：未删除，1：已删除
     */
    @TableLogic
    @TableField(value = "is_deleted")
    private Boolean deleted;
    /**
     * 创建者编号
     */
    @TableField(value = "create_by")
    private Long createBy;
    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    private LocalDateTime createTime;
    /**
     * 更新者编号
     */
    @TableField(value = "update_by")
    private Long updateBy;
    /**
     * 更新时间
     */
    @TableField(value = "update_time")
    private LocalDateTime updateTime;
    /**
     * 租户ID，0表示系统级
     */
    @TableField(value = "tenant_id")
    private String tenantId;

}
