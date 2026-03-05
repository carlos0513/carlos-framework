package com.carlos.audit.pojo.dto;


import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * <p>
 * 审计日志-数据变更详情 数据传输对象，service和manager向外传输对象
 * </p>
 *
 * @author Carlos
 * @date 2026年3月5日 下午11:36:54
 */
@Data
@Accessors(chain = true)
public class AuditLogDataChangeDTO {
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
