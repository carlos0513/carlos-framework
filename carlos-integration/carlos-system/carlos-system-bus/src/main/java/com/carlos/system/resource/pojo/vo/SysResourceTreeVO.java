package com.carlos.system.resource.pojo.vo;


import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * <p>
 * 菜单树形数据
 * </p>
 *
 * @author carlos
 * @date 2021-12-28 15:26:57
 */
@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SysResourceTreeVO {

    @Schema(description = "菜单id")
    private String id;

    @Schema(description = "菜单标题")
    private String title;

    @Schema(description = "菜单名称")
    private String name;

    @Schema(description = "接口地址")
    private String path;

    @Schema(description = "资源列表")
    List<SysResourceBaseVO> resources;

    @Schema(description = "子菜单")
    List<SysResourceTreeVO> children;
}
