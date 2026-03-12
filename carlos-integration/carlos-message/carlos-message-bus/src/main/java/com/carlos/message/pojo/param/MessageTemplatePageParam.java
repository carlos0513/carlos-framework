package com.carlos.message.pojo.param;


import com.carlos.core.param.ParamPage;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;


/**
 * <p>
 * 消息模板 列表查询参数封装
 * </p>
 *
 * @author Carlos
 * @date 2026年3月12日 上午11:17:05
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@Schema(description = "消息模板列表查询参数")
public class MessageTemplatePageParam extends ParamPage {
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
    @Schema(description = "开始时间")
    private LocalDateTime start;

    @Schema(description = "结束时间")
    private LocalDateTime end;
}
