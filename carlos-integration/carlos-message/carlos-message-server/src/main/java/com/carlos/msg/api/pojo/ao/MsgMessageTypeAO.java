package com.carlos.msg.api.pojo.ao;


import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 消息类型 API提供的对象(API Object)
 * </p>
 *
 * @author Carlos
 * @date 2025-3-10 10:53:03
 */
@Data
public class MsgMessageTypeAO implements Serializable {
    /** 主键ID */
    private Long id;
    /** 类型编码 */
    private String typeCode;
    /** 类型名称 */
    private String typeName;
    /** 是否启用 */
    private Boolean enabled;
    /** 创建者编号 */
    private Long createBy;
    /** 创建时间 */
    private LocalDateTime createTime;
    /** 更新者编号 */
    private Long updateBy;
    /** 更新时间 */
    private LocalDateTime updateTime;
}
