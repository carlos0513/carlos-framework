package com.yunjin.org.pojo.param;


import com.yunjin.core.param.ParamPage;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;


/**
 * <p>
 * 角色资源组关联表 列表查询参数封装
 * </p>
 *
 * @author yunjin
 * @date 2024-8-22 10:59:20
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@Schema(value = "角色资源组关联表列表查询参数", description = "角色资源组关联表列表查询参数")
public class RoleResourceGroupRefPageParam extends ParamPage {
    @Schema(value = "角色id")
    private String roleId;
    @Schema(value = "资源组id")
    private String resourceGroupId;
    @Schema("开始时间")
    private LocalDateTime start;

    @Schema("结束时间")
    private LocalDateTime end;
}
