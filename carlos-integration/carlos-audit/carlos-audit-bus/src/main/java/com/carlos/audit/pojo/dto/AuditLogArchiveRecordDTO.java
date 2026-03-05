package com.carlos.audit.pojo.dto;


import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * <p>
 * 审计日志归档记录 数据传输对象，service和manager向外传输对象
 * </p>
 *
 * @author Carlos
 * @date 2026年3月5日 下午11:36:54
 */
@Data
@Accessors(chain = true)
public class AuditLogArchiveRecordDTO {
    /**  */
    private Long id;
    /** 归档批次ID */
    private String archiveId;
    /** 归档日期 */
    private LocalDate archiveDate;
    /** 归档数据起始时间 */
    private LocalDateTime startTime;
    /** 归档数据结束时间 */
    private LocalDateTime endTime;
    /** 归档记录数 */
    private Long recordCount;
    /** 归档文件大小 */
    private Long fileSizeBytes;
    /** 存储路径 */
    private String storagePath;
    /** 存储类型: OSS/S3/LOCAL */
    private String storageType;
    /** 校验和 */
    private String verifyChecksum;
    /** 状态 */
    private String state;
    /**  */
    private LocalDateTime createdTime;
}
