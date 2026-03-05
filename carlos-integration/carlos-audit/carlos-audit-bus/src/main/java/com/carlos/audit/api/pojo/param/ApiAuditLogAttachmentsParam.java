package com.carlos.audit.api.pojo.param;


import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 审计日志-附件引用 API提供的对象(API Object)
 * </p>
 *
 * @author Carlos
 * @date 2026年3月5日 下午11:36:54
 */
@Data
@Accessors(chain = true)
public class ApiAuditLogAttachmentsParam implements Serializable {
    /**  */
    private Long id;
    /**  */
    private Long auditLogId;
    /** 类型: IMAGE/VIDEO/FILE/SNAPSHOT */
    private String attachmentType;
    /** 存储: OSS/S3/MINIO/GRIDFS */
    private String storageType;
    /** 存储桶 */
    private String bucketName;
    /** 对象键 */
    private String objectKey;
    /** 原始文件名 */
    private String fileName;
    /** 文件大小(字节) */
    private Long fileSize;
    /** MIME类型 */
    private String contentType;
    /**  */
    private LocalDateTime createdTime;
}
