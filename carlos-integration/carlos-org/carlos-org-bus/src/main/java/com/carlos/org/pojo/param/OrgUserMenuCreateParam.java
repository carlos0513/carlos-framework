package com.carlos.org.pojo.param;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;


/**
 * <p>
 * 用户菜单收藏表 新增参数封装
 * </p>
 *
 * @author  yunjin
 * @date    2024-2-28 11:10:01
 */
@Data
@Accessors(chain = true)
@Schema(description = "用户菜单收藏表新增参数")
public class OrgUserMenuCreateParam {
        @NotBlank(message = "用户id不能为空")
        @Schema(description = "用户id")
        private String userId;
        @NotBlank(message = "菜单id不能为空")
        @Schema(description = "菜单id")
        private String menuId;
}
