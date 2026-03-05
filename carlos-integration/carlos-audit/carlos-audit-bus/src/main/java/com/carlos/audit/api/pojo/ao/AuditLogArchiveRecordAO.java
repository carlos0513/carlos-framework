package com.carlos.audit.api.pojo.ao;


import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * <p>
 * 审计日志归档记录 API提供的对象(API Object)
 * </p>
 *
 * @author Carlos
 * @date 2026年3月5日 下午11:36:53
 */
@Data
public class AuditLogArchiveRecordAO implements Serializable {
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
