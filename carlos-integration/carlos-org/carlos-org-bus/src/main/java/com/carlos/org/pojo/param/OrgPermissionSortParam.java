package com.carlos.org.pojo.param;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;


/**
 * <p>
 * 权限排序参数
 * </p>
 * <p>PM-005 权限排序</p>
 *
 * @author Carlos
 * @date 2026年2月28日 下午1:25:36
 */
@Data
public class OrgPermissionSortParam {

    /**
     * 权限id
     */
    @NotNull(message = "权限id不能为空")
    @Schema(description = "权限id", requiredMode = Schema.RequiredMode.REQUIRED)
    private Serializable id;

    /**
     * 排序值
     */
    @NotNull(message = "排序值不能为空")
    @Schema(description = "排序值", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer sort;

}
