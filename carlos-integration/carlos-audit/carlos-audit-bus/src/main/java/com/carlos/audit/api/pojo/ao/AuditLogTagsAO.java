package com.carlos.audit.api.pojo.ao;


import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 审计日志-动态标签 API提供的对象(API Object)
 * </p>
 *
 * @author Carlos
 * @date 2026年3月5日 下午11:36:54
 */
@Data
public class AuditLogTagsAO implements Serializable {
    /**  */
    private Long id;
    /**  */
    private Long auditLogId;
    /** 标签键 */
    private String tagKey;
    /** 标签值 */
    private String tagValue;
    /**  */
    private LocalDateTime createdTime;
}
