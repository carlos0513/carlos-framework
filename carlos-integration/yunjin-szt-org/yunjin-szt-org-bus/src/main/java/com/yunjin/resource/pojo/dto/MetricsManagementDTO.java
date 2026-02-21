package com.yunjin.resource.pojo.dto;


import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * <p>
 * 系统指标管理 数据传输对象，service和manager向外传输对象
 * </p>
 *
 * @author yunjin
 * @date 2024-8-22 10:59:20
 */
@Data
@Accessors(chain = true)
public class MetricsManagementDTO {
    /**
     * 主键
     */
    private String id;
    /**
     * 指标编码
     */
    private String metricsCode;
    /**
     * 指标名称
     */
    private String metricsName;
    /**
     * 指标类型(可扩展)：0 首页指标
     */
    private Integer metricsType;
    /**
     * 是否启用 1启用 0禁用
     */
    private Boolean state;
    /**
     * 指标描述
     */
    private String description;
    /**
     * 创建者编号
     */
    private String createBy;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    /**
     * 更新者编号
     */
    private String updateBy;
    /**
     * 修改时间
     */
    private LocalDateTime updateTime;
}
