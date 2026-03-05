package com.carlos.audit.pojo.param;


import com.carlos.core.param.ParamPage;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;


/**
 * <p>
 * 审计日志-附件引用 列表查询参数封装
 * </p>
 *
 * @author Carlos
 * @date 2026年3月5日 下午11:36:54
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@Schema(description = "审计日志-附件引用列表查询参数")
public class AuditLogAttachmentsPageParam extends ParamPage {
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
    @Schema(description = "开始时间")
    private LocalDateTime start;

    @Schema(description = "结束时间")
    private LocalDateTime end;
}
