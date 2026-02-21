package com.carlos.system.news.pojo.dto;


import java.time.LocalDateTime;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * 浏览记录 数据传输对象，service和manager向外传输对象
 * </p>
 *
 * @author yunjin
 * @date 2023-1-13 16:31:50
 */
@Data
@Accessors(chain = true)
public class SysViewRecordDTO {

    /**
     *
     */
    private String id;
    /**
     * 记录类型(0 通知公告,1 消息提醒)
     */
    private Integer type;
    /**
     * 关联id
     */
    private String referenceId;
    /**
     * 用户id
     */
    private String userId;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}
