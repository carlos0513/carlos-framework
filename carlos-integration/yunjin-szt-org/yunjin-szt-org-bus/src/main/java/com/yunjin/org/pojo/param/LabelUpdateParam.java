package com.yunjin.org.pojo.param;


import com.yunjin.org.enums.LabelTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;

/**
* <p>
    * 标签 更新参数封装
    * </p>
*
* @author  yunjin
* @date    2024-3-23 12:31:52
*/
@Data
@Accessors(chain = true)
@Schema(value = "标签修改参数", description = "标签修改参数")
public class LabelUpdateParam {
        @NotBlank(message = "主键ID不能为空")
        @Schema(value = "主键ID")
        private String id;
        @Schema(value = "标签名称")
        private String name;
        @Schema(value = "唯一编码")
        private String code;
        @Schema(value = "类型ID")
        private String typeId;
        @Schema(value = "排序")
        private Integer sort;
        @Schema(value = "是否隐藏")
        private Boolean hidden;
        @Schema(value = "标签释义")
        private String description;
        @Schema(value = "标签类别")
        private LabelTypeEnum labelType;
}
