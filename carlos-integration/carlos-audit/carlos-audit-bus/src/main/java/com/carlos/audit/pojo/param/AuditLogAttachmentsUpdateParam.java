package com.carlos.audit.pojo.param;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * <p>
 * 审计日志-附件引用 更新参数封装
 * </p>
 *
 * @author Carlos
 * @date 2026年3月5日 下午11:36:54
 */
@Data
@Accessors(chain = true)
@Schema(description = "审计日志-附件引用修改参数")
public class AuditLogAttachmentsUpdateParam {
    @NotNull(message = "不能为空")
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
