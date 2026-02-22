package com.carlos.org.pojo.param;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Set;


/**
 * <p>
 * 角色菜单 新增参数封装
 * </p>
 *
 * @author carlos
 * @date 2022-11-11 19:21:46
 */
@Data
@Accessors(chain = true)
@Schema(description = "角色菜单新增参数")
public class RoleMenuCreateParam {

    @NotEmpty(message = "角色id不能为空")
    @Schema(description = "角色id")
    private Set<String> ids;

    @NotEmpty(message = "菜单id不能为空")
    @Schema(description = "菜单id")
    private Set<String> menuIds;

}
