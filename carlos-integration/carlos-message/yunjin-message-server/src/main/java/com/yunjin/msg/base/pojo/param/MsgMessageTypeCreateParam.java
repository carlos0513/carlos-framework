package com.carlos.msg.base.pojo.param;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;


/**
 * <p>
 * 消息类型 新增参数封装
 * </p>
 *
 * @author Carlos
 * @date 2025-3-10 10:53:04
 */
@Data
@Accessors(chain = true)
@Schema(value = "消息类型新增参数", description = "消息类型新增参数")
public class MsgMessageTypeCreateParam {
    @NotBlank(message = "类型编码不能为空")
    @Schema(value = "类型编码")
    private String typeCode;
    @NotBlank(message = "类型名称不能为空")
    @Schema(value = "类型名称")
    private String typeName;
    @NotNull(message = "是否启用不能为空")
    @Schema(value = "是否启用")
    private Boolean enabled;
}
