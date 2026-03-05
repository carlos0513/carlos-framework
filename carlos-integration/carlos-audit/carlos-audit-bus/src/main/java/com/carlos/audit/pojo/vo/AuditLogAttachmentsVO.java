package com.carlos.audit.pojo.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 审计日志-附件引用 显示层对象，向页面传输的对象
 * </p>
 *
 * @author Carlos
 * @date 2026年3月5日 下午11:36:54
 */
@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AuditLogAttachmentsVO implements Serializable {
    private static final long serialVersionUID = 1L;
    @Schema(description = "")
    private Long id;
    @Schema(description = "")
    private Long auditLogId;
    @Schema(description = "类型: IMAGE/VIDEO/FILE/SNAPSHOT")
    private String attachmentType;
    @Schema(description = "存储: OSS/S3/MINIO/GRIDFS")
    private String storageType;
    @Schema(description = "存储桶")
    private String bucketName;
    @Schema(description = "对象键")
    private String objectKey;
    @Schema(description = "原始文件名")
    private String fileName;
    @Schema(description = "文件大小(字节)")
    private Long fileSize;
    @Schema(description = "MIME类型")
    private String contentType;
    @Schema(description = "")
    private LocalDateTime createdTime;

}
