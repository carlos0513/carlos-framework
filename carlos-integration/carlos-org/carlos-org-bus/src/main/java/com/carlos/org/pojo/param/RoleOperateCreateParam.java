package com.carlos.org.pojo.param;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;


/**
 * <p>
 * 角色菜单操作表 新增参数封装
 * </p>
 *
 * @author yunjin
 * @date 2023-7-7 14:19:55
 */
@Data
@Accessors(chain = true)
@Schema(description = "角色菜单操作表新增参数")
public class RoleOperateCreateParam {
    @Schema(description = "角色id")
        private String roleId;
    @Schema(description = "菜单操作id")
        private String operateId;
}
