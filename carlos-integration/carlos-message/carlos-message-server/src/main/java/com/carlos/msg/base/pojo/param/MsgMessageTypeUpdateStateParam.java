package com.carlos.msg.base.pojo.param;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * 消息类型 更新参数封装
 * </p>
 *
 * @author Carlos
 * @date 2025-3-10 10:53:04
 */
@Data
@Accessors(chain = true)
@Schema(description = "消息类型状态修改参数")
public class MsgMessageTypeUpdateStateParam {
    @NotNull(message = "主键ID不能为空")
    @Schema(description = "主键ID")
    private Long id;

    @NotNull(message = "状态不能为空")
    @Schema(description = "是否启用")
    private Boolean enabled;
}
