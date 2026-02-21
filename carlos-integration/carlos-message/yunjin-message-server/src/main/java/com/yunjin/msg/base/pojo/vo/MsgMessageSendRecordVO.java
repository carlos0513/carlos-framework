package com.carlos.msg.base.pojo.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 消息发送记录 显示层对象，向页面传输的对象
 * </p>
 *
 * @author Carlos
 * @date 2025-3-10 10:53:04
 */
@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MsgMessageSendRecordVO implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "主键")
    private Long id;
    @ApiModelProperty(value = "消息id")
    private Long messageId;
    @ApiModelProperty(value = "重试次数")
    private Integer retryCount;
    @ApiModelProperty(value = "发送时间")
    private LocalDateTime sendTime;
    @ApiModelProperty(value = "原始请求参数")
    private String requestParam;
    @ApiModelProperty(value = "渠道返回数据")
    private String responseData;
    @ApiModelProperty(value = "推送渠道(短信、站内信、钉钉等)")
    private String pushChannel;
    @ApiModelProperty(value = "是否发送成功 0 失败 1 成功")
    private Boolean success;
    @ApiModelProperty(value = "创建人")
    private String createBy;
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

}
