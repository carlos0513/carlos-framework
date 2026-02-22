package com.carlos.org.pojo.param;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 绵阳定开 参数扩展
 * </p>
 *
 * @author carlos
 * @date 2024-1104 10:10:28
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@Schema(description = "任务执行人列表查询参数")
public class TaskExecutorPageMianYangParam extends CurSubExecutorPageParam {

    @Schema(description = "部门id")
    @NotNull(message = "部门id不能为空！")
    private String id;

    @Schema(description = "是否包含下级部门")
    @NotNull(message = "是否包含下级部门不能为空！")
    private Boolean isLower;

    @Schema(description = "是否是管理员")
    private Boolean isAdmin;

}
