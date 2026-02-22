package com.carlos.msg.base.pojo.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 消息模板 显示层对象，向页面传输的对象
 * </p>
 *
 * @author Carlos
 * @date 2025-3-10 10:53:04
 */
@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MsgMessageTemplateVO implements Serializable {
    private static final long serialVersionUID = 1L;
    @Schema(description = "主键ID")
    private Long id;
    @Schema(description = "消息类型")
    private Long typeId;
    @Schema(description = "模板编码")
    private String templateCode;
    @Schema(description = "模板内容(含变量占位符)")
    private String templateContent;
    @Schema(description = "渠道特殊配置(如短信模板ID),配置对应渠道编码")
    private String channelConfig;
    @Schema(description = "是否启用")
    private Boolean active;
    @Schema(description = "创建者编号")
    private String createBy;
    @Schema(description = "创建时间")
    private LocalDateTime createTime;
    @Schema(description = "更新者编号")
    private String updateBy;
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;

}
