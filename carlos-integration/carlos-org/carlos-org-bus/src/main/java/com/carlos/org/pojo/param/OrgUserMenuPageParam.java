package com.carlos.org.pojo.param;


import com.carlos.core.param.ParamPage;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;


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
@Schema(description = "用户菜单收藏表列表查询参数")
public class OrgUserMenuPageParam extends ParamPage {
    @Schema(description = "用户id")
    private String userId;
    @Schema(description = "菜单id")
    private String menuId;
    @Schema(description = "开始时间")
    private LocalDateTime start;

    @Schema(description = "结束时间")
    private LocalDateTime end;
}
