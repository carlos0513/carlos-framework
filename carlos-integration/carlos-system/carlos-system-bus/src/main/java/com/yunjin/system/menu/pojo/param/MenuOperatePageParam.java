package com.carlos.system.menu.pojo.param;


import com.carlos.core.param.ParamPage;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;


/**
 * <p>
 * 菜单操作 分页列表查询参数
 * </p>
 *
 * @author yunjin
 * @date 2023-7-7 14:19:55
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@Schema(value = "菜单操作分页列表查询参数", description = "菜单操作分页列表查询参数")
public class MenuOperatePageParam extends ParamPage {
    @Schema(value = "资源名称")
    private String operateName;
    @Schema(value = "资源编码")
    private String operateCode;
    @Schema(value = "接口路径")
    private String path;
    @Schema(value = "请求方式")
    private String operateMethod;
    @Schema(value = "图标")
    private String icon;
    @Schema(value = "资源类型，按钮")
    private String operateType;
    @Schema(value = "状态，0：禁用，1：启用")
    private String state;
    @Schema(value = "显示和隐藏，0：显示，1：隐藏")
    private Boolean hidden;
    @Schema(value = "资源描述")
    private String description;
    @Schema("开始时间")
    private LocalDateTime start;

    @Schema("结束时间")
    private LocalDateTime end;
}
