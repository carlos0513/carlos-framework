package com.carlos.message.pojo.param;


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
 * @date 2026年3月12日 上午11:17:05
 */
@Data
@Accessors(chain = true)
@Schema(description = "消息模板新增参数")
public class MessageTemplateCreateParam {
    @NotNull(message = "消息类型ID不能为空")
    @Schema(description = "消息类型ID")
    private Long typeId;
    @NotBlank(message = "模板编码不能为空")
    @Schema(description = "模板编码")
    private String templateCode;
    @NotBlank(message = "模板名称不能为空")
    @Schema(description = "模板名称")
    private String templateName;
    @Schema(description = "标题模板")
    private String titleTemplate;
    @NotBlank(message = "模板内容不能为空")
    @Schema(description = "模板内容")
    private String contentTemplate;
    @Schema(description = "参数定义JSON")
    private String paramSchema;
    @Schema(description = "渠道特殊配置")
    private String channelConfig;
    @NotNull(message = "是否启用: 0-禁用 1-启用 2-草稿不能为空")
    @Schema(description = "是否启用: 0-禁用 1-启用 2-草稿")
    private Boolean enabled;
}
