package com.carlos.system.menu.pojo.vo;


import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.Data;
import lombok.experimental.Accessors;

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
public class MenuTreeVO {

    @Schema(value = "菜单id")
    private String id;

    @Schema(value = "菜单标题")
    private String title;

    @Schema(value = "菜单名称")
    private String name;

    @Schema(value = "子菜单")
    List<MenuTreeVO> children;
}
