package com.carlos.org.pojo.param;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;

/**
* <p>
    * 用户菜单收藏表 更新参数封装
    * </p>
*
 * @author carlos
* @date    2024-2-28 11:10:01
*/
@Data
@Accessors(chain = true)
@Schema(description = "用户菜单收藏表修改参数")
public class OrgUserMenuUpdateParam {
        @NotBlank(message = "主键不能为空")
        @Schema(description = "主键")
        private String id;
    @Schema(description = "用户id")
        private String userId;
    @Schema(description = "菜单id")
        private String menuId;
}
