package com.yunjin.board.pojo.dto;


import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * <p>
 * 工作台卡片信息 数据传输对象，service和manager向外传输对象
 * </p>
 *
 * @author Carlos
 * @date 2025-5-13 11:07:57
 */
@Data
@Accessors(chain = true)
public class BoardCardInfoDTO {
    /**
     * 主键
     */
    private Long id;
    /**
     * 卡片名称
     */
    private String cardName;
    /**
     * 卡片编码
     */
    private String cardCode;
    /**
     * 组件名称
     */
    private String component;
    /**
     * 缩略图
     */
    private String thumbnail;
    /**
     * 描述
     */
    private String description;
    /**
     * 创建者编号
     */
    private String createBy;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    /**
     * 更新者编号
     */
    private String updateBy;
    /**
     * 修改时间
     */
    private LocalDateTime updateTime;
}
