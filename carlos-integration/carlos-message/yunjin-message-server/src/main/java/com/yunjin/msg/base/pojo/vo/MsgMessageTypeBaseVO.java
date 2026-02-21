package com.carlos.msg.base.pojo.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

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
public class MsgMessageTypeBaseVO implements Serializable {
    private static final long serialVersionUID = 1L;
    @Schema(value = "主键ID")
    private Long id;
    @Schema(value = "类型编码")
    private String typeCode;
    @Schema(value = "类型名称")
    private String typeName;
}
