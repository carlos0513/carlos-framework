package com.carlos.system.upload.pojo.entity;


import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 文件上传记录 数据源对象
 * </p>
 *
 * @author Carlos
 * @date 2022-2-7 15:22:31
 */
@Data
@Accessors(chain = true)
@TableName("sys_upload_record")
@JsonInclude(JsonInclude.Include.ALWAYS)
public class UploadRecord implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID, value = "id")
    private Long id;
    /**
     * 文件分组
     */
    @TableField(value = "group_id")
    private String groupId;
    /**
     * 文件库名称
     */
    @TableField(value = "repository_name")
    private String repositoryName;
    /**
     * 文件库地址
     */
    @TableField(value = "repository_url")
    private String repositoryUrl;
    /**
     * 源文件名
     */
    @TableField(value = "original_name")
    private String originalName;
    /**
     * 文件用途
     */
    @TableField(value = "uses")
    private String uses;
    /**
     * 上传状态 成功 失败
     */
    @TableField(value = "state")
    private String state;
    /**
     * 上传人
     */
    @TableField(value = "create_by", fill = FieldFill.INSERT)
    private Long createBy;
    /**
     * 上传时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

}
