package com.carlos.system.menu.pojo.param;


import com.carlos.core.param.ParamPage;
import com.carlos.system.enums.MenuType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;


/**
 * <p>
 * 系统菜单 列表查询参数封装
 * </p>
 *
 * @author yunjin
 * @date 2021-12-28 15:26:57
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@Schema(description = "系统菜单列表查询参数")
public class MenuPageParam extends ParamPage {

    private static final long serialVersionUID = 2559512989423712792L;
    @Schema(description = "前端名称")
    private String keyword;

    @Schema(description = "父级id")
    private String parentId;

    @Schema(description = "显示和隐藏，0：显示，1：隐藏")
    private Boolean hidden;

    @Schema(description = "开始时间")
    private LocalDateTime start;

    @Schema(description = "结束时间")
    private LocalDateTime end;

    @Schema(description = "菜单类型，PC：pc端菜单，MOBILE：移动端菜单")
    private MenuType menuType = MenuType.PC;
}
