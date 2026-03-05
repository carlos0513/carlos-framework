package com.carlos.audit.pojo.dto;


import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * <p>
 * 审计日志-字段级变更明细 数据传输对象，service和manager向外传输对象
 * </p>
 *
 * @author Carlos
 * @date 2026年3月5日 下午11:36:54
 */
@Data
@Accessors(chain = true)
public class AuditLogFieldChangeDTO {
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
