package com.carlos.audit.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.carlos.audit.pojo.enums.AuditLogArchiveStateEnum;
import com.carlos.audit.pojo.enums.AuditLogStorageTypeEnum;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * <p>
 * 审计日志归档记录（管理冷数据归档） 数据源对象
 * </p>
 *
 * @author Carlos
 * @date 2026年3月6日 下午9:31:12
 */
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("audit_log_archive_record")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AuditLogArchiveRecord extends Model<AuditLogArchiveRecord> implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID, value = "id")
    private Long id;
    /**
     * 归档批次ID，UUID
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
     * 归档文件大小，字节
     */
    @TableField(value = "file_size_bytes")
    private Long fileSizeBytes;
    /**
     * 存储路径
     */
    @TableField(value = "storage_path")
    private String storagePath;
    /**
     * 存储类型：OSS/S3/LOCAL
     */
    @TableField(value = "storage_type")
    private AuditLogStorageTypeEnum storageType;
    /**
     * 校验和，MD5或SHA256
     */
    @TableField(value = "verify_checksum")
    private String verifyChecksum;
    /**
     * 状态：SUCCESS-成功/FAILED-失败/PROCESSING-处理中
     */
    @TableField(value = "state")
    private AuditLogArchiveStateEnum state;
    /**
     * 创建时间
     */
    @TableField(value = "created_time")
    private LocalDateTime createdTime;

    // Getter methods
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getArchiveId() {
        return archiveId;
    }

    public void setArchiveId(String archiveId) {
        this.archiveId = archiveId;
    }

    public LocalDate getArchiveDate() {
        return archiveDate;
    }

    public void setArchiveDate(LocalDate archiveDate) {
        this.archiveDate = archiveDate;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public Long getRecordCount() {
        return recordCount;
    }

    public void setRecordCount(Long recordCount) {
        this.recordCount = recordCount;
    }

    public Long getFileSizeBytes() {
        return fileSizeBytes;
    }

    public void setFileSizeBytes(Long fileSizeBytes) {
        this.fileSizeBytes = fileSizeBytes;
    }

    public String getStoragePath() {
        return storagePath;
    }

    public void setStoragePath(String storagePath) {
        this.storagePath = storagePath;
    }

    public AuditLogStorageTypeEnum getStorageType() {
        return storageType;
    }

    public void setStorageType(AuditLogStorageTypeEnum storageType) {
        this.storageType = storageType;
    }

    public String getVerifyChecksum() {
        return verifyChecksum;
    }

    public void setVerifyChecksum(String verifyChecksum) {
        this.verifyChecksum = verifyChecksum;
    }

    public AuditLogArchiveStateEnum getState() {
        return state;
    }

    public void setState(AuditLogArchiveStateEnum state) {
        this.state = state;
    }

    public LocalDateTime getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(LocalDateTime createdTime) {
        this.createdTime = createdTime;
    }
}
