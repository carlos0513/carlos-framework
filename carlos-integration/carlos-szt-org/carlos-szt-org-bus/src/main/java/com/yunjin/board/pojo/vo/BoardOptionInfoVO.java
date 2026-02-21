package com.yunjin.board.pojo.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 工作台卡片选项信息 显示层对象，向页面传输的对象
 * </p>
 *
 * @author Carlos
 * @date 2025-5-13 11:07:57
 */
@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BoardOptionInfoVO implements Serializable {
    private static final long serialVersionUID = 1L;
    @Schema(value = "主键")
    private Long id;
    @Schema(value = "按钮名称")
    private String optionId;
    @Schema(value = "选项类型 标签 按钮 超链接")
    private String optionType;
    @Schema(value = "描述")
    private String description;
    @Schema(value = "创建者编号")
    private String createBy;
    @Schema(value = "创建时间")
    private LocalDateTime createTime;
    @Schema(value = "更新者编号")
    private String updateBy;
    @Schema(value = "修改时间")
    private LocalDateTime updateTime;

}
