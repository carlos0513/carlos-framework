package com.carlos.msg.base.pojo.param;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;


/**
 * <p>
 * 消息模板 新增参数封装
 * </p>
 *
 * @author Carlos
 * @date 2025-3-10 10:53:04
 */
@Data
@Accessors(chain = true)
@Schema(description = "消息模板新增参数")
public class MsgMessageTemplateCreateParam {
    @NotNull(message = "消息类型不能为空")
    @Schema(description = "消息分类")
    private Long typeId;
    @NotBlank(message = "模板编码不能为空")
    @Schema(description = "模板编码")
    private String templateCode;
    @NotBlank(message = "模板内容(含变量占位符)不能为空")
    @Schema(description = "模板内容(含变量占位符)")
    private String templateContent;
    @Schema(description = "渠道特殊配置(如短信模板ID),配置对应渠道编码")
    private String channelConfig;
    @NotNull(message = "是否启用不能为空")
    @Schema(description = "是否启用")
    private Boolean active;
}
