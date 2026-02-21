package com.yunjin.org.pojo.param;


import com.yunjin.org.enums.LabelTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
* <p>
    * 标签分类 更新参数封装
    * </p>
*
* @author  yunjin
* @date    2024-3-22 15:07:09
*/
@Data
@Accessors(chain = true)
@Schema(value = "标签分类修改参数", description = "标签分类修改参数")
public class LabelTypeUpdateParam {
        @NotBlank(message = "主键ID不能为空")
        @Schema(value = "主键ID")
        private String id;
        @Schema(value = "类型名称")
        @Size(max = 100, message = "名称过长")
        private String name;
        @Schema(value = "父级id")
        private String parentId;
        @Schema(value = "排序")
        private Integer sort;
        @Schema(value = "是否隐藏")
        private Boolean hidden;
        @NotNull(message = "标签类别不能为空")
        @Schema(value = "标签类别")
        private LabelTypeEnum labelType;
}
