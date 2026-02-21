package com.yunjin.org.pojo.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.yunjin.org.enums.LabelTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
* <p>
    * 标签分类 显示层对象，向页面传输的对象
    * </p>
*
* @author  yunjin
* @date    2024-3-22 15:07:09
*/
@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LabelTypeVO implements Serializable {
private static final long serialVersionUID = 1L;
        @Schema(value = "主键ID")
        private String id;
        @Schema(value = "类型名称")
        private String name;
        @Schema(value = "父级id")
        private String parentId;
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
        @Schema(value = "子类型列表")
        private List<LabelTypeVO> subTypes;
        @Schema(value = "标签列表")
        private List<LabelVO> labels;
        @Schema(value = "标签类别")
        private LabelTypeEnum labelType;

}
