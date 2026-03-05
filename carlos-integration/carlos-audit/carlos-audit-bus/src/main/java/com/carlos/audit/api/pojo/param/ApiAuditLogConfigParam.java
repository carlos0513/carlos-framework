package com.carlos.audit.api.pojo.param;


import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 审计日志配置 API提供的对象(API Object)
 * </p>
 *
 * @author Carlos
 * @date 2026年3月5日 下午11:36:54
 */
@Data
@Accessors(chain = true)
public class ApiAuditLogConfigParam implements Serializable {
    /**  */
    private Long id;
    /** 日志类型 */
    private String logType;
    /** 保留天数 */
    private Integer retentionDays;
    /** 采样率 0.00-1.00 */
    private BigDecimal samplingRate;
    /** 是否异步写入 */
    private Boolean asyncWrite;
    /** 是否存储数据变更 */
    private Boolean storeDataChange;
    /** 是否存储技术上下文 */
    private Boolean storeTechnical;
    /** 创建者编号 */
    private Long createBy;
    /** 创建时间 */
    private LocalDateTime createTime;
    /** 更新者编号 */
    private Long updateBy;
    /** 更新时间 */
    private LocalDateTime updateTime;
    /** 租户ID，0表示系统级 */
    private String tenantId;
}
