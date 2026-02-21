package com.yunjin.resource.pojo.entity;


import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 系统指标管理 数据源对象
 * </p>
 *
 * @author yunjin
 * @date 2024-8-22 10:59:20
 */
@Data
@Accessors(chain = true)
@TableName("sys_metrics_management")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MetricsManagement implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_UUID, value = "id")
    private String id;
    /**
     * 指标编码
     */
    @TableField(value = "metrics_code")
    private String metricsCode;
    /**
     * 指标名称
     */
    @TableField(value = "metrics_name")
    private String metricsName;
    /**
     * 指标类型(可扩展)：0 首页指标
     */
    @TableField(value = "metrics_type")
    private Integer metricsType;
    /**
     * 是否启用 1启用 0禁用
     */
    @TableField(value = "state")
    private Boolean state;
    /**
     * 指标描述
     */
    @TableField(value = "description")
    private String description;
    /**
     * 逻辑删除，0：未删除，1：已删除
     */
    @TableLogic
    @TableField(value = "is_deleted")
    private Boolean deleted;
    /**
     * 创建者编号
     */
    @TableField(value = "create_by", fill = FieldFill.INSERT)
    private String createBy;
    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    /**
     * 更新者编号
     */
    @TableField(value = "update_by", fill = FieldFill.UPDATE)
    private String updateBy;
    /**
     * 修改时间
     */
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

}
