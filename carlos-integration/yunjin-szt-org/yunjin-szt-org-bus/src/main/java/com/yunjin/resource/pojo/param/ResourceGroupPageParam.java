package com.yunjin.resource.pojo.param;


import com.yunjin.core.param.ParamPage;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;


/**
 * <p>
 * 资源组 列表查询参数封装
 * </p>
 *
 * @author yunjin
 * @date 2024-8-22 10:59:20
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@Schema(value = "资源组列表查询参数", description = "资源组列表查询参数")
public class ResourceGroupPageParam extends ParamPage {
    @Schema(value = "资源组code")
    private String groupCode;
    @Schema(value = "资源组名称")
    private String groupName;
    @Schema(value = "资源说明")
    private String description;
    @Schema(value = "是否启用 1启用 0禁用")
    private Boolean state;
    @Schema("开始时间")
    private LocalDateTime start;

    @Schema("结束时间")
    private LocalDateTime end;
}
