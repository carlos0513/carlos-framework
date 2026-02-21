package com.carlos.system.resource.pojo.param;


import com.carlos.core.param.ParamPage;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;


/**
 * <p>
 * 资源分类 列表查询参数封装
 * </p>
 *
 * @author yunjin
 * @date 2022-1-5 17:23:27
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@Schema(value = "资源分类列表查询参数", description = "资源分类列表查询参数")
public class SysResourceCategoryPageParam extends ParamPage {

    @Schema(value = "父级ID")
    private Long parentId;
    @Schema(value = "类型名称")
    private String name;
    @Schema("开始时间")
    private LocalDateTime start;

    @Schema("结束时间")
    private LocalDateTime end;
}
