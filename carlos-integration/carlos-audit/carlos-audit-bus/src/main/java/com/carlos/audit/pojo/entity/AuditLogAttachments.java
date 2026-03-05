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
import java.time.LocalDateTime;

/**
 * <p>
 * 审计日志-附件引用 数据源对象
 * </p>
 *
 * @author Carlos
 * @date 2026年3月5日 下午11:36:54
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("audit_log_attachments")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AuditLogAttachments extends Model<AuditLogAttachments> implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     *
     */
    @TableId(type = IdType.ASSIGN_ID, value = "id")
    private Long id;
    /**
     *
     */
    @TableField(value = "audit_log_id")
    private Long auditLogId;
    /**
     * 类型: IMAGE/VIDEO/FILE/SNAPSHOT
     */
    @TableField(value = "attachment_type")
    private String attachmentType;
    /**
     * 存储: OSS/S3/MINIO/GRIDFS
     */
    @TableField(value = "storage_type")
    private String storageType;
    /**
     * 存储桶
     */
    @TableField(value = "bucket_name")
    private String bucketName;
    /**
     * 对象键
     */
    @TableField(value = "object_key")
    private String objectKey;
    /**
     * 原始文件名
     */
    @TableField(value = "file_name")
    private String fileName;
    /**
     * 文件大小(字节)
     */
    @TableField(value = "file_size")
    private Long fileSize;
    /**
     * MIME类型
     */
    @TableField(value = "content_type")
    private String contentType;
    /**
     *
     */
    @TableField(value = "created_time")
    private LocalDateTime createdTime;

}
