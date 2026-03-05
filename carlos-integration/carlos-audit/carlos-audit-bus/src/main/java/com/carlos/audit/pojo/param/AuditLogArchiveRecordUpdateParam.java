package com.carlos.audit.pojo.param;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * <p>
 * 审计日志归档记录 更新参数封装
 * </p>
 *
 * @author Carlos
 * @date 2026年3月5日 下午11:36:54
 */
@Data
@Accessors(chain = true)
@Schema(description = "审计日志归档记录修改参数")
public class AuditLogArchiveRecordUpdateParam {
    @NotNull(message = "不能为空")
    @Schema(description = "")
    private Long id;
    @Schema(description = "归档批次ID")
    private String archiveId;
    @Schema(description = "归档日期")
    private LocalDate archiveDate;
    @Schema(description = "归档数据起始时间")
    private LocalDateTime startTime;
    @Schema(description = "归档数据结束时间")
    private LocalDateTime endTime;
    @Schema(description = "归档记录数")
    private Long recordCount;
    @Schema(description = "归档文件大小")
    private Long fileSizeBytes;
    @Schema(description = "存储路径")
    private String storagePath;
    @Schema(description = "存储类型: OSS/S3/LOCAL")
    private String storageType;
    @Schema(description = "校验和")
    private String verifyChecksum;
    @Schema(description = "状态")
    private String state;
    @Schema(description = "")
    private LocalDateTime createdTime;
}
