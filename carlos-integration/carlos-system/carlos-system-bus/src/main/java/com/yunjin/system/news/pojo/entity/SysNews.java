package com.carlos.system.news.pojo.entity;


import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.carlos.system.enums.UserMessageType;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 系统-通知公告 数据源对象
 * </p>
 *
 * @author yunjin
 * @date 2022-11-14 23:48:53
 */
@Data
@Accessors(chain = true)
@TableName("sys_news")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SysNews implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_UUID, value = "id")
    private String id;
    /**
     * 标题
     */
    @TableField(value = "title")
    private String title;
    /**
     * 消息类型
     */
    @TableField(value = "type")
    private UserMessageType type;
    /**
     * 来源
     */
    @TableField(value = "source")
    private String source;
    /**
     * 标题图片
     */
    @TableField(value = "image")
    private String image;
    /**
     * 发送日期
     */
    @TableField(value = "send_date")
    private LocalDateTime sendDate;
    /**
     * 内容
     */
    @TableField(value = "content")
    private String content;
    /**
     *
     */
    @TableField(value = "enabled")
    private Boolean enabled;
    /**
     *
     */
    @TableField(value = "introducing")
    private String introducing;
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
    @TableField(value = "update_by", fill = FieldFill.UPDATE)
    private String updateBy;
    /**
     * 修改时间
     */
    @TableField(value = "update_time", fill = FieldFill.UPDATE)
    private LocalDateTime updateTime;
    /**
     * 租户id
     */
    @TableField(value = "tenant_id")
    private String tenantId;

}
