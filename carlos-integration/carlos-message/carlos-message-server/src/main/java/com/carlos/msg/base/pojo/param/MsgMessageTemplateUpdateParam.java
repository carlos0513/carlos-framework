package com.carlos.msg.base.pojo.param;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * 消息模板 更新参数封装
 * </p>
 *
 * @author Carlos
 * @date 2025-3-10 10:53:04
 */
@Data
@Accessors(chain = true)
@Schema(description = "消息模板修改参数")
public class MsgMessageTemplateUpdateParam {
    @NotNull(message = "主键ID不能为空")
    @Schema(description = "主键ID")
    private Long id;
    @Schema(description = "消息类型")
    private Long typeId;
    @Schema(description = "模板内容(含变量占位符)")
    private String templateContent;
    @Schema(description = "渠道特殊配置(如短信模板ID),配置对应渠道编码")
    private String channelConfig;
    @Schema(description = "是否启用")
    private Boolean active;
}
