package com.yunjin.resource.pojo.param;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;

/**
 * <p>
 * 资源组 更新参数封装
 * </p>
 *
 * @author yunjin
 * @date 2024-8-22 10:59:20
 */
@Data
@Accessors(chain = true)
@Schema(value = "资源组修改参数", description = "资源组修改参数")
public class ResourceGroupUpdateParam {
        @NotBlank(message = "主键不能为空")
        @Schema(value = "主键")
        private String id;
        @Schema(value = "资源组code")
        private String groupCode;
        @Schema(value = "资源组名称")
        private String groupName;
        @Schema(value = "资源说明")
        private String description;
        @Schema(value = "是否启用 1启用 0禁用")
        private Boolean state;
}
