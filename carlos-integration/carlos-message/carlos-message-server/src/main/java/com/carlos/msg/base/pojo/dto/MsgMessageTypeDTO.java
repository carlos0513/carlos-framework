package com.carlos.msg.base.pojo.dto;


import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * <p>
 * 消息类型 数据传输对象，service和manager向外传输对象
 * </p>
 *
 * @author Carlos
 * @date 2025-3-10 10:53:03
 */
@Data
@Accessors(chain = true)
public class MsgMessageTypeDTO {
    /** 主键ID */
    private Long id;
    /** 类型编码 */
    private String typeCode;
    /** 类型名称 */
    private String typeName;
    /** 是否启用 */
    private Boolean enabled;
    /** 创建者编号 */
    private String createBy;
    /** 创建时间 */
    private LocalDateTime createTime;
    /** 更新者编号 */
    private String updateBy;
    /** 更新时间 */
    private LocalDateTime updateTime;
}
