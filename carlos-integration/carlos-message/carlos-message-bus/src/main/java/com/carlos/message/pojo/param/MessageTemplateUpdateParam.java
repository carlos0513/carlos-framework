package com.carlos.message.pojo.param;


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
 * @date 2026年3月12日 上午11:17:05
 */
@Data
@Accessors(chain = true)
@Schema(description = "消息模板修改参数")
public class MessageTemplateUpdateParam {
    @NotNull(message = "主键ID不能为空")
    @Schema(description = "主键ID")
    private Long id;
    @Schema(description = "消息类型ID")
    private Long typeId;
    @Schema(description = "模板编码")
    private String templateCode;
    @Schema(description = "模板名称")
    private String templateName;
    @Schema(description = "标题模板")
    private String titleTemplate;
    @Schema(description = "模板内容")
    private String contentTemplate;
    @Schema(description = "参数定义JSON")
    private String paramSchema;
    @Schema(description = "渠道特殊配置")
    private String channelConfig;
    @Schema(description = "是否启用: 0-禁用 1-启用 2-草稿")
    private Boolean enabled;
}
