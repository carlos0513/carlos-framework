package com.carlos.msg.base.pojo.param;


import com.carlos.core.param.ParamPage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
@ApiModel(value = "消息模板列表查询参数", description = "消息模板列表查询参数")
public class MsgMessageTemplatePageParam extends ParamPage {
    @ApiModelProperty(value = "消息类型")
    private Long typeId;
    @ApiModelProperty(value = "模板编码")
    private String templateCode;
    @ApiModelProperty(value = "模板内容(含变量占位符)")
    private String templateContent;
    @ApiModelProperty(value = "渠道特殊配置(如短信模板ID),配置对应渠道编码")
    private String channelConfig;
    @ApiModelProperty(value = "是否启用")
    private Boolean active;
    @ApiModelProperty("开始时间")
    private LocalDateTime start;

    @ApiModelProperty("结束时间")
    private LocalDateTime end;
}
