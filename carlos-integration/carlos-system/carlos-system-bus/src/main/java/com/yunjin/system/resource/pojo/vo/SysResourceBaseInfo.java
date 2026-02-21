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
 * @author yunjin
 * @date 2021/12/27
 */
@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SysResourceBaseInfo implements Serializable {

    private static final long serialVersionUID = 1L;
    @Schema(value = "主键")
    private String id;
    @Schema(value = "分类id")
    private Long categoryId;
    @Schema(value = "资源名称")
    private String name;
    @Schema(value = "接口路径")
    private String path;
    @Schema(value = "图标")
    private String icon;
    @Schema(value = "资源类型，按钮")
    private String type;
    @Schema(value = "描述")
    private String description;
}
