package com.yunjin.org.pojo.param;


import com.yunjin.org.enums.LabelTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;


/**
 * <p>
 * 标签 新增参数封装
 * </p>
 *
 * @author  yunjin
 * @date    2024-3-23 12:31:52
 */
@Data
@Accessors(chain = true)
@Schema(value = "标签新增参数", description = "标签新增参数")
public class LabelCreateParam {
        @Schema(value = "标签名称")
        private String name;
    @Schema(value = "唯一编码")
    private String code;
    @Schema(value = "类型ID")
    private String typeId;
    @NotNull(message = "排序不能为空")
    @Schema(value = "排序")
    private Integer sort;
    @Schema(value = "是否隐藏")
    private Boolean hidden;
    @Schema(value = "标签释义")
    private String description;
    @NotNull(message = "标签类别不能为空")
    @Schema(value = "标签类别")
    private LabelTypeEnum labelType;
}
