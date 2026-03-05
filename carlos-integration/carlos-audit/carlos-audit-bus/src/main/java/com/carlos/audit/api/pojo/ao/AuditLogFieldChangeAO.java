package com.carlos.audit.api.pojo.ao;


import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 审计日志-字段级变更明细 API提供的对象(API Object)
 * </p>
 *
 * @author Carlos
 * @date 2026年3月5日 下午11:36:53
 */
@Data
public class AuditLogFieldChangeAO implements Serializable {
    /**  */
    private Long id;
    /**  */
    private Long auditLogId;
    /**  */
    private Long dataChangeId;
    /** 字段名 */
    private String fieldName;
    /** 字段中文描述 */
    private String fieldDesc;
    /** ADDED/MODIFIED/REMOVED */
    private Integer changeType;
    /** 旧值 */
    private String oldValue;
    /** 值类型: STRING/NUMBER/JSON */
    private Integer oldValueType;
    /** 新值 */
    private String newValue;
    /** 值类型 */
    private Integer newValueType;
    /** 是否敏感字段 */
    private Boolean sensitive;
    /**  */
    private LocalDateTime createdTime;
}
