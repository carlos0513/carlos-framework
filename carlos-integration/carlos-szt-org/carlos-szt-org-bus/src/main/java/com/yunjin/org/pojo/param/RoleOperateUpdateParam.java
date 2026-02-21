package com.yunjin.org.pojo.param;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;

/**
 * <p>
 * 角色菜单操作表 更新参数封装
 * </p>
 *
 * @author yunjin
 * @date 2023-7-7 14:19:55
 */
@Data
@Accessors(chain = true)
@Schema(value = "角色菜单操作表修改参数", description = "角色菜单操作表修改参数")
public class RoleOperateUpdateParam {
        @NotBlank(message = "主键不能为空")
        @Schema(value = "主键")
        private String id;
        @Schema(value = "角色id")
        private String roleId;
        @Schema(value = "菜单操作id")
        private String operateId;
}
