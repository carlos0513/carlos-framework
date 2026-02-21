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
 * @author yunjin
 * @date 2021-12-28 15:26:57
 */
@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SysResourceTreeVO {

    @Schema(value = "菜单id")
    private String id;

    @Schema(value = "菜单标题")
    private String title;

    @Schema(value = "菜单名称")
    private String name;

    @Schema(value = "接口地址")
    private String path;

    @Schema(value = "资源列表")
    List<SysResourceBaseVO> resources;

    @Schema(value = "子菜单")
    List<SysResourceTreeVO> children;
}
