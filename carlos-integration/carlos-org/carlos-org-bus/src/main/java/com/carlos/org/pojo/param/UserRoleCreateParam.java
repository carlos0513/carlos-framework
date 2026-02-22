package com.carlos.org.pojo.param;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.experimental.Accessors;

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
@Schema(description = "用户角色新增参数")
public class UserRoleCreateParam {

    @NotEmpty(message = "用户id不能为空")
    @Schema(description = "用户id列表")
    private Set<String> userIds;

    @NotEmpty(message = "角色id不能为空")
    @Schema(description = "角色id列表")
    private Set<String> roleIds;
}
