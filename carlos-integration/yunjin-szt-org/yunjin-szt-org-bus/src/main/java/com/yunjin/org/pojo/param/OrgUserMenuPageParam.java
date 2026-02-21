package com.yunjin.org.pojo.param;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import com.yunjin.core.param.ParamPage;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

    import java.lang.Boolean;
    import java.lang.String;


/**
 * <p>
 * 用户菜单收藏表 列表查询参数封装
 * </p>
 *
 * @author  yunjin
 * @date    2024-2-28 11:10:01
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@Schema(value = "用户菜单收藏表列表查询参数", description = "用户菜单收藏表列表查询参数")
public class OrgUserMenuPageParam extends ParamPage {
    @Schema(value = "用户id")
    private String userId;
    @Schema(value = "菜单id")
    private String menuId;
    @Schema("开始时间")
    private LocalDateTime start;

    @Schema("结束时间")
    private LocalDateTime end;
}
