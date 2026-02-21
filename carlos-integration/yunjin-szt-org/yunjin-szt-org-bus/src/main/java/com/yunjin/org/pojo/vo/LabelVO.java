package com.yunjin.org.pojo.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.yunjin.org.enums.LabelTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
* <p>
    * 标签 显示层对象，向页面传输的对象
    * </p>
*
* @author  yunjin
* @date    2024-3-23 12:31:52
*/
@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LabelVO implements Serializable {
private static final long serialVersionUID = 1L;
        @Schema(value = "主键ID")
        private String id;
        @Schema(value = "标签名称")
        private String name;
        @Schema(value = "唯一编码")
        private String code;
        @Schema(value = "类型ID")
        private String typeId;
        @Schema(value = "类型名称")
        private String typeName;
        @Schema(value = "排序")
        private Integer sort;
        @Schema(value = "是否隐藏")
        private Boolean hidden;
        @Schema(value = "创建者编号")
        private String createBy;
        @Schema(value = "创建时间")
        private LocalDateTime createTime;
        @Schema(value = "更新者编号")
        private String updateBy;
        @Schema(value = "更新时间")
        private LocalDateTime updateTime;
        @Schema(value = "标签描述")
        private String description;
        @Schema(value = "标签类别")
        private LabelTypeEnum labelType;
}
