package com.carlos.msg.base.pojo.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
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
    @ApiModelProperty(value = "主键ID")
    private Long id;
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
    @ApiModelProperty(value = "创建者编号")
    private String createBy;
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;
    @ApiModelProperty(value = "更新者编号")
    private String updateBy;
    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

}
