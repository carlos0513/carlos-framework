package com.carlos.org.pojo.param;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * <p>
 * 部门 更新参数封装
 * </p>
 *
 * @author carlos
 */
@Data
@Accessors(chain = true)
@Schema(description = "部门修改参数")
public class DepartmentUpdateParam {

    @NotBlank(message = "主键不能为空")
    @Schema(description = "主键")
    private String id;
    @Schema(description = "上级机构")
    private String parentId;
    @NotBlank(message = "部门名称不能为空")
    @Schema(description = "部门名称")
    private String deptName;
    @Schema(description = "机构地址")
    private String address;
    @Schema(description = "部门人员")
    private List<DepartmentUserListParam> users;
}
