package com.yunjin.board.pojo.entity;


import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 工作台卡片信息 数据源对象
 * </p>
 *
 * @author Carlos
 * @date 2025-5-13 11:07:57
 */
@Data
@Accessors(chain = true)
@TableName("board_card_info")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BoardCardInfo implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID, value = "id")
    private Long id;
    /**
     * 卡片名称
     */
    @TableField(value = "card_name")
    private String cardName;
    /**
     * 卡片编码
     */
    @TableField(value = "card_code")
    private String cardCode;
    /**
     * 组件名称
     */
    @TableField(value = "component")
    private String component;
    /**
     * 缩略图
     */
    @TableField(value = "thumbnail")
    private String thumbnail;
    /**
     * 描述
     */
    @TableField(value = "description")
    private String description;
    /**
     * 创建者编号
     */
    @TableField(value = "create_by", fill = FieldFill.INSERT)
    private String createBy;
    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    /**
     * 更新者编号
     */
    @TableField(value = "update_by", fill = FieldFill.UPDATE)
    private String updateBy;
    /**
     * 修改时间
     */
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

}
