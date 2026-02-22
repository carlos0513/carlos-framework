package com.carlos.msg.base.pojo.param;


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
 * @date 2025-3-10 10:53:04
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@Schema(description = "消息模板列表查询参数")
public class MsgMessageTemplatePageParam extends ParamPage {
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
    @Schema(description = "开始时间")
    private LocalDateTime start;

    @Schema(description = "结束时间")
    private LocalDateTime end;
}
