package com.yunjin.org.pojo.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.yunjin.org.pojo.emuns.HelpFileEnum;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;
@Data
@Accessors(chain = true)
@TableName("bbt_help_file")
@JsonInclude(JsonInclude.Include.ALWAYS)
public class HelpFile extends Model<HelpFile> implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_UUID, value = "id")
    private String id;
    /**
     * 逻辑删除，0：未删除，1：已删除
     */
    @TableLogic
    @TableField(value = "is_deleted")
    private Boolean deleted;

    /**
     * 文件MinioId
     */
    @TableField(value = "file_content", fill = FieldFill.INSERT)
    private String fileContent;
    /**
     * 文件名
     */
    @TableField(value = "file_name", fill = FieldFill.INSERT)
    private String fileName;
    /**
     * 文件类型
     */
    @TableField(value = "file_type", fill = FieldFill.INSERT)
    private HelpFileEnum fileType;
    /**
     * 排序
     */
    @TableField(value = "sort", fill = FieldFill.INSERT)
    private String sort;
    /**
     * 创建者
     */
    @TableField(value = "create_by", fill = FieldFill.INSERT)
    private String createBy;
    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    /**
     * 修改者
     */
    @TableField(value = "update_by", fill = FieldFill.INSERT_UPDATE)
    private String updateBy;
    /**
     * 修改时间
     */
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    /**
     * 文件样例
     */
    @TableField(value = "file_sample", fill = FieldFill.INSERT_UPDATE)
    private String fileSample;
}
