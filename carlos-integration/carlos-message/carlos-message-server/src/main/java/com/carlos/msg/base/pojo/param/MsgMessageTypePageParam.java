package com.carlos.msg.base.pojo.param;


import com.carlos.core.param.ParamPage;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;


/**
 * <p>
 * 消息类型 列表查询参数封装
 * </p>
 *
 * @author Carlos
 * @date 2025-3-10 10:53:04
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@Schema(description = "消息类型列表查询参数")
public class MsgMessageTypePageParam extends ParamPage {
    @Schema(description = "类型编码")
    private String typeCode;
    @Schema(description = "类型名称")
    private String typeName;
    @Schema(description = "是否启用")
    private Boolean enabled;
    @Schema(description = "开始时间")
    private LocalDateTime start;

    @Schema(description = "结束时间")
    private LocalDateTime end;
}
