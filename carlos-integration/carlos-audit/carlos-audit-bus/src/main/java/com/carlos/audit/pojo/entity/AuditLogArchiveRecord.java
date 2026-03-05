package com.carlos.audit.pojo.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * <p>
 * 审计日志归档记录 数据源对象
 * </p>
 *
 * @author Carlos
 * @date 2026年3月5日 下午11:36:54
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("audit_log_archive_record")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AuditLogArchiveRecord extends Model<AuditLogArchiveRecord> implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     *
     */
    @TableId(type = IdType.ASSIGN_ID, value = "id")
    private Long id;
    /**
     * 归档批次ID
     */
    @TableField(value = "archive_id")
    private String archiveId;
    /**
     * 归档日期
     */
    @TableField(value = "archive_date")
    private LocalDate archiveDate;
    /**
     * 归档数据起始时间
     */
    @TableField(value = "start_time")
    private LocalDateTime startTime;
    /**
     * 归档数据结束时间
     */
    @TableField(value = "end_time")
    private LocalDateTime endTime;
    /**
     * 归档记录数
     */
    @TableField(value = "record_count")
    private Long recordCount;
    /**
     * 归档文件大小
     */
    @TableField(value = "file_size_bytes")
    private Long fileSizeBytes;
    /**
     * 存储路径
     */
    @TableField(value = "storage_path")
    private String storagePath;
    /**
     * 存储类型: OSS/S3/LOCAL
     */
    @TableField(value = "storage_type")
    private String storageType;
    /**
     * 校验和
     */
    @TableField(value = "verify_checksum")
    private String verifyChecksum;
    /**
     * 状态
     */
    @TableField(value = "state")
    private String state;
    /**
     *
     */
    @TableField(value = "created_time")
    private LocalDateTime createdTime;

}
