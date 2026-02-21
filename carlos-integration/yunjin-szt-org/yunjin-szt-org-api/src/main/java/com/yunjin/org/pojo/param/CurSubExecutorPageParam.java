package com.yunjin.org.pojo.param;

import com.yunjin.core.param.ParamPage;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * <p>
 * 当前以及下级部门执行人 列表查询参数封装
 * </p>
 *
 * @author yunjin
 * @date 2024-1104 10:10:28
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@Schema(value = "当前以及下级部门执行人列表查询参数", description = "当前以及下级部门执行人列表查询参数")
public class CurSubExecutorPageParam extends ParamPage {

    @Schema(value = "任务id")
    private String taskId;
    @Schema(value = "任务名称")
    private String taskName;
    @Schema(value = "组织机构code")
    private String deptCode;
    @Schema(value = "组织机构id集合")
    private List<String> deptIds;
    @Schema("下派用户账号")
    private String account;
    @Schema("下派用户姓名")
    private String userName;
    @Schema("任务创建人")
    private String taskCreateBy;
    @Schema("当前用户")
    private String myself;

}
