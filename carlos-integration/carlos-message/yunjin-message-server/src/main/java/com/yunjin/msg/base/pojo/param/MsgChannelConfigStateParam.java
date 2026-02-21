package com.carlos.msg.base.pojo.param;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Set;


/**
 * <p>
 * 消息渠道配置 新增参数封装
 * </p>
 *
 * @author Carlos
 * @date 2025-3-10 10:53:04
 */
@Data
@Accessors(chain = true)
@Schema(value = "消息渠道配置新增参数", description = "消息渠道配置新增参数")
public class MsgChannelConfigStateParam {
    @NotEmpty(message = "主键不能为空")
    @Schema(value = "主键")
    private Set<Serializable> ids;

    @NotNull(message = "状态不能为空")
    @Schema(value = "启用状态")
    private Boolean enabled;
}
