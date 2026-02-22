package com.carlos.msg.base.pojo.dto;


import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * <p>
 * 消息模板 数据传输对象，service和manager向外传输对象
 * </p>
 *
 * @author Carlos
 * @date 2025-3-10 10:53:03
 */
@Data
@Accessors(chain = true)
public class MsgMessageTemplateDTO {
    /** 主键ID */
    private Long id;
    /** 消息类型 */
    private Long typeId;
    /** 消息类型名称 */
    private Long typeName;
    /** 模板编码 */
    private String templateCode;
    /** 模板内容(含变量占位符) */
    private String templateContent;
    /** 渠道特殊配置(如短信模板ID),配置对应渠道编码 */
    private String channelConfig;
    /** 是否启用 */
    private Boolean active;
    /** 创建者编号 */
    private String createBy;
    /** 创建时间 */
    private LocalDateTime createTime;
    /** 更新者编号 */
    private String updateBy;
    /** 更新时间 */
    private LocalDateTime updateTime;
}
