package com.carlos.org.pojo;

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
 * @author carlos
 * @date 2024-1104 10:10:28
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@Schema(description = "当前部门执行人列表查询参数")
public class CurSubPageDto extends ParamPage {

    @Schema(description = "组织机构id")
    private Long id;

    @Schema(description = "当前任务id")
    private String taskId;

    @Schema(description = "模糊匹配用户名/账号/手机号")
    private String userName;

}
