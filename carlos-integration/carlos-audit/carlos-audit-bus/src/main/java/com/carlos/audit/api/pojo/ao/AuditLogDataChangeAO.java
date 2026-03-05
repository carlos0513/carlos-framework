package com.carlos.audit.api.pojo.ao;


import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 审计日志-数据变更详情 API提供的对象(API Object)
 * </p>
 *
 * @author Carlos
 * @date 2026年3月5日 下午11:36:53
 */
@Data
public class AuditLogDataChangeAO implements Serializable {
    /**  */
    private Long id;
    /** 关联主表ID */
    private Long auditLogId;
    /** 变更前数据(JSON) */
    private String oldData;
    /** 是否压缩 */
    private Boolean oldDataCompressed;
    /** 变更后数据(JSON) */
    private String newData;
    /** 是否压缩 */
    private Boolean newDataCompressed;
    /** 变更字段数量 */
    private Integer changedFieldCount;
    /**  */
    private LocalDateTime createdTime;
}
