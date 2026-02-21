package com.yunjin.org.pojo.param;


import com.yunjin.core.param.ParamPage;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;


/**
 * <p>
 * 用户角色 列表查询参数封装
 * </p>
 *
 * @author yunjin
 * @date 2022-11-11 19:21:46
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@Schema(value = "用户角色列表查询参数", description = "用户角色列表查询参数")
public class UserRolePageParam extends ParamPage {

    @Schema(value = "用户id")
    private String userId;
    @Schema(value = "角色id")
    private String roleId;
    @Schema(value = "租户id")
    private String tenantId;
    @Schema("开始时间")
    private LocalDateTime start;

    @Schema("结束时间")
    private LocalDateTime end;
}
