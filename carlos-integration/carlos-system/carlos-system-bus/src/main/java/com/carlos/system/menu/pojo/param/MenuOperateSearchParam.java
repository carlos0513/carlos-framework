package com.carlos.system.menu.pojo.param;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;


/**
 * <p>
 * 菜单操作 列表查询参数封装
 * </p>
 *
 * @author carlos
 * @date 2023-7-7 14:19:55
 */
@Data
@Accessors(chain = true)
@Schema(description = "菜单操作列表查询参数")
public class MenuOperateSearchParam {
    @Schema(description = "关键字")
    private String keyword;
}
