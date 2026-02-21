package com.yunjin.org.pojo.param;

import com.yunjin.core.param.ParamPage;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

/**
 * <p>
 * 绵阳定开 参数扩展
 * </p>
 *
 * @author yunjin
 * @date 2024-1104 10:10:28
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@Schema(value = "任务执行人列表查询参数", description = "任务执行人列表查询参数")
public class TaskExecutorPageMianYangParam extends CurSubExecutorPageParam {

    @Schema(value = "部门id")
    @NotNull(message = "部门id不能为空！")
    private String id;

    @Schema(value = "是否包含下级部门")
    @NotNull(message = "是否包含下级部门不能为空！")
    private Boolean isLower;

    @Schema(value = "是否是管理员")
    private Boolean isAdmin;

}
