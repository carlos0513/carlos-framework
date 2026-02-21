package com.yunjin.board.pojo.dto;


import com.yunjin.board.pojo.enums.CustomConfigType;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * <p>
 * 看板自定义配置 数据传输对象，service和manager向外传输对象
 * </p>
 *
 * @author Carlos
 * @date 2025-5-13 11:07:57
 */
@Data
@Accessors(chain = true)
public class BoardCustomConfigDTO {
    /**
     * 主键
     */
    private Long id;
    /**
     * 用户id
     */
    private String configKey;
    /**
     * 配置信息
     */
    private String configJson;
    /**
     * 配置类型
     */
    private CustomConfigType configType;
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
