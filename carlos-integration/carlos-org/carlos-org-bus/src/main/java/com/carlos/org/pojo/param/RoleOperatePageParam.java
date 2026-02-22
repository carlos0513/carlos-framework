package com.carlos.org.pojo.param;


import com.carlos.core.param.ParamPage;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;


/**
 * <p>
 * 角色菜单操作表 列表查询参数封装
 * </p>
 *
 * @author yunjin
 * @date 2023-7-7 14:19:55
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@Schema(description = "角色菜单操作表列表查询参数")
public class RoleOperatePageParam extends ParamPage {
    @Schema(description = "角色id")
    private String roleId;
    @Schema(description = "菜单操作id")
    private String operateId;
    @Schema(description = "开始时间")
    private LocalDateTime start;

    @Schema(description = "结束时间")
    private LocalDateTime end;
}
