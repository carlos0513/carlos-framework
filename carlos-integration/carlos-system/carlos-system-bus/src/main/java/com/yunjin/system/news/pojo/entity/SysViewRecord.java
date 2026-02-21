package com.carlos.system.news.pojo.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * 浏览记录 数据源对象
 * </p>
 *
 * @author yunjin
 * @date 2023-1-13 16:31:50
 */
@Data
@Accessors(chain = true)
@TableName("sys_view_record")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SysViewRecord implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     *
     */
    @TableId(type = IdType.ASSIGN_UUID, value = "id")
    private String id;
    /**
     * 记录类型(0 通知公告,1 消息提醒)
     */
    @TableField(value = "record_type")
    private Integer type;
    /**
     * 关联id
     */
    @TableField(value = "reference_id")
    private String referenceId;
    /**
     * 用户id
     */
    @TableField(value = "user_id")
    private String userId;
    /**
     * 创建时间
     */
    @TableField(value = "createTime")
    private LocalDateTime createTime;

}
