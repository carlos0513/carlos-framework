package com.carlos.system.resource.pojo.param;


import com.carlos.core.param.ParamPage;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;


/**
 * <p>
 * 系统资源 列表查询参数封装
 * </p>
 *
 * @author yunjin
 * @date 2021-12-28 15:26:57
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@Schema(description = "系统资源列表查询参数")
public class SysResourcePageParam extends ParamPage {

    @Schema(description = "分类id")
    private Long categoryId;
    @Schema(description = "资源名称")
    private String name;
    @Schema(description = "接口路径")
    private String path;
    @Schema(description = "请求方式")
    private String method;
    @Schema(description = "图标")
    private String icon;
    @Schema(description = "资源类型，按钮")
    private String type;
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
