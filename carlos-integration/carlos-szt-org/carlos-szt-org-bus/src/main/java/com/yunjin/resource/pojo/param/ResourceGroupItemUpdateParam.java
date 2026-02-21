package com.yunjin.resource.pojo.param;


import com.yunjin.org.enums.ResourceTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;

/**
 * <p>
 * 资源组详情项 更新参数封装
 * </p>
 *
 * @author yunjin
 * @date 2024-8-22 10:59:20
 */
@Data
@Accessors(chain = true)
@Schema(value = "资源组详情项修改参数", description = "资源组详情项修改参数")
public class ResourceGroupItemUpdateParam {
    @NotBlank(message = "主键不能为空")
    @Schema(value = "主键")
    private String id;
    @Schema(value = "资源组id")
    private String groupId;
    @Schema(value = "资源类型(可扩展):0按钮, 1指标 ")
    private ResourceTypeEnum resourceType;
    @Schema(value = "资源key")
    private String resourceKey;
}
