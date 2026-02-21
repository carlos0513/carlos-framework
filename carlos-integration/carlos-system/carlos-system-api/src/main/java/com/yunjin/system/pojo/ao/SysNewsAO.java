package com.carlos.system.pojo.ao;

import com.carlos.system.enums.UserMessageType;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 系统-通知公告 显示层对象，向页面传输的对象
 * </p>
 *
 * @author yunjin
 * @date 2022-11-14 23:48:53
 */
@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SysNewsAO implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 主键
     */
    private String id;
    /**
     * 标题
     */
    private String title;
    /**
     * 消息类型
     */
    private UserMessageType type;
    /**
     * 来源
     */
    private String source;
    /**
     * 发送日期
     */
    private LocalDateTime sendDate;
    /**
     * 内容
     */
    private String content;
    /**
     * 简介
     */
    private String introducing;
    /**
     * 创建者
     */
    private String createBy;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    /**
     * 修改者
     */
    private String updateBy;
    /**
     * 修改时间
     */
    private LocalDateTime updateTime;
    /**
     *
     */
    private Boolean enabled;
}
