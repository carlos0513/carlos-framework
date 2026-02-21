package com.yunjin.org.pojo.param;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
    import java.lang.Boolean;
    import java.lang.String;

/**
* <p>
    * 用户菜单收藏表 更新参数封装
    * </p>
*
* @author  yunjin
* @date    2024-2-28 11:10:01
*/
@Data
@Accessors(chain = true)
@Schema(value = "用户菜单收藏表修改参数", description = "用户菜单收藏表修改参数")
public class OrgUserMenuUpdateParam {
        @NotBlank(message = "主键不能为空")
        @Schema(value = "主键")
        private String id;
        @Schema(value = "用户id")
        private String userId;
        @Schema(value = "菜单id")
        private String menuId;
}
