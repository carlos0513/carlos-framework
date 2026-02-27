package com.carlos.system.resource.pojo.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 系统资源基本信息
 * </p>
 *
 * @author carlos
 * @date 2021/12/27
 */
@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SysResourceBaseInfo implements Serializable {

    private static final long serialVersionUID = 1L;
    @Schema(description = "主键")
    private Long id;
    @Schema(description = "分类id")
    private Long categoryId;
    @Schema(description = "资源名称")
    private String name;
    @Schema(description = "接口路径")
    private String path;
    @Schema(description = "图标")
    private String icon;
    @Schema(description = "资源类型，按钮")
    private String type;
    @Schema(description = "描述")
    private String description;
}
