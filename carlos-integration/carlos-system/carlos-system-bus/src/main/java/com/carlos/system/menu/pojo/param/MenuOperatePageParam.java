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
@Schema(description = "菜单操作分页列表查询参数")
public class MenuOperatePageParam extends ParamPage {
    @Schema(description = "资源名称")
    private String operateName;
    @Schema(description = "资源编码")
    private String operateCode;
    @Schema(description = "接口路径")
    private String path;
    @Schema(description = "请求方式")
    private String operateMethod;
    @Schema(description = "图标")
    private String icon;
    @Schema(description = "资源类型，按钮")
    private String operateType;
    @Schema(description = "状态，0：禁用，1：启用")
    private String state;
    @Schema(description = "显示和隐藏，0：显示，1：隐藏")
    private Boolean hidden;
    @Schema(description = "资源描述")
    private String description;
    @Schema(description = "开始时间")
    private LocalDateTime start;

    @Schema(description = "结束时间")
    private LocalDateTime end;
}
