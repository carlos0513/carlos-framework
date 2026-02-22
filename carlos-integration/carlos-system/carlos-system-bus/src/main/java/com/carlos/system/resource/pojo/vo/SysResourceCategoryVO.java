package com.carlos.system.resource.pojo.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 资源分类 显示层对象，向页面传输的对象
 * </p>
 *
 * @author carlos
 * @date 2022-1-5 17:23:27
 */
@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SysResourceCategoryVO implements Serializable {

    private static final long serialVersionUID = 1L;
    @Schema(description = "主键")
    private String id;
    @Schema(description = "父级ID")
    private Long parentId;
    @Schema(description = "类型名称")
    private String name;
    @Schema(description = "创建时间")
    private LocalDateTime createTime;
    @Schema(description = "修改时间")
    private LocalDateTime updateTime;
    @Schema(description = "是否有子类型")
    Boolean haveChildren;

}
