package com.carlos.msg.base.pojo.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 消息类型 显示层对象，向页面传输的对象
 * </p>
 *
 * @author Carlos
 * @date 2025-3-10 10:53:04
 */
@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MsgMessageTypeVO implements Serializable {
    private static final long serialVersionUID = 1L;
    @Schema(value = "主键ID")
    private Long id;
    @Schema(value = "类型编码")
    private String typeCode;
    @Schema(value = "类型名称")
    private String typeName;
    @Schema(value = "是否启用")
    private Boolean enabled;
    @Schema(value = "创建者编号")
    private String createBy;
    @Schema(value = "创建时间")
    private LocalDateTime createTime;
    @Schema(value = "更新者编号")
    private String updateBy;
    @Schema(value = "更新时间")
    private LocalDateTime updateTime;

}
