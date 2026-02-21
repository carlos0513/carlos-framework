package com.carlos.system.news.pojo.dto;


import com.carlos.system.enums.UserMessageType;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 系统-通知公告 数据传输对象，service和manager向外传输对象
 * </p>
 *
 * @author yunjin
 * @date 2022-11-14 23:48:53
 */
@Data
@Accessors(chain = true)
public class SysNewsDTO {

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
     * 标题图片
     */
    private String image;
    /**
     * 发送日期
     */
    private LocalDateTime sendDate;
    /**
     * 内容
     */
    private String content;
    /**
     *
     */
    private Boolean enabled;
    /**
     *
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
     * 租户id
     */
    private String tenantId;

    /**
     *
     */
    private List<String> imageUrlList;
}
