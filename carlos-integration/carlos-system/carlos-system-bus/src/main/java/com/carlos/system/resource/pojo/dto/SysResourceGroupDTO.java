package com.carlos.system.resource.pojo.dto;


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
public class SysResourceGroupDTO {

    @Schema(description = "菜单分类")
    private String name;

    @Schema(description = "资源列表")
    List<SysResourceDTO> resources;

}
