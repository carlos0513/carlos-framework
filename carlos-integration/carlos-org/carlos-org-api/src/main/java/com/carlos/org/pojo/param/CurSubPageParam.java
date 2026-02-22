package com.carlos.org.pojo.param;

import com.carlos.core.param.ParamPage;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

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
@Schema(description = "当前部门执行人列表查询参数")
public class CurSubPageParam extends ParamPage {

    @Schema(description = "任务id")
    private String taskId;
    @Schema(description = "任务名称")
    private String taskName;
    @Schema(description = "组织机构code")
    private String deptCode;
    @Schema(description = "下派用户账号")
    private String account;
    @Schema(description = "下派用户姓名")
    private String userName;
    @Schema(description = "任务创建人")
    private String taskCreateBy;
    @Schema(description = "当前用户")
    private String myself;
    @Schema(description = "关键字（账号/姓名/手机号模糊匹配）")
    private String keyword;


}
