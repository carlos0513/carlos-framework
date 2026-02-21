package com.yunjin.org.pojo.param;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;
import java.util.Set;


/**
 * <p>
 * 用户角色 列表查询参数封装
 * </p>
 *
 * @author yunjin
 * @date 2022-11-11 19:21:46
 */
@Data
@Accessors(chain = true)
@Schema(value = "用户角色新增参数", description = "用户角色新增参数")
public class UserRoleCreateParam {

    @NotEmpty(message = "用户id不能为空")
    @Schema(value = "用户id列表")
    private Set<String> userIds;

    @NotEmpty(message = "角色id不能为空")
    @Schema(value = "角色id列表")
    private Set<String> roleIds;
}
