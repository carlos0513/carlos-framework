package com.yunjin.resource.pojo.param;


import com.yunjin.core.param.ParamPage;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;


/**
 * <p>
 * 资源组详情项 列表查询参数封装
 * </p>
 *
 * @author yunjin
 * @date 2024-8-22 10:59:20
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@Schema(value = "资源组详情项列表查询参数", description = "资源组详情项列表查询参数")
public class ResourceGroupItemPageParam extends ParamPage {
    @Schema(value = "资源组id")
    private String groupId;
    @Schema(value = "资源类型(可扩展):0按钮, 1指标 ")
    private Integer resourceType;
    @Schema(value = "资源key")
    private String resourceKey;
    @Schema("开始时间")
    private LocalDateTime start;

    @Schema("结束时间")
    private LocalDateTime end;
}
