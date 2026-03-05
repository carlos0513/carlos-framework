package com.carlos.audit.pojo.dto;


import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * <p>
 * 审计日志-动态标签 数据传输对象，service和manager向外传输对象
 * </p>
 *
 * @author Carlos
 * @date 2026年3月5日 下午11:36:54
 */
@Data
@Accessors(chain = true)
public class AuditLogTagsDTO {
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
