package com.yunjin.org.pojo.param;


import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import javax.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * 部门 更新参数封装
 * </p>
 *
 * @author yunjin
 */
@Data
@Accessors(chain = true)
@Schema(value = "部门修改参数", description = "部门修改参数")
public class DepartmentUpdateParam {

    @NotBlank(message = "主键不能为空")
    @Schema(value = "主键")
    private String id;
    @Schema(value = "上级机构")
    private String parentId;
    @NotBlank(message = "部门名称不能为空")
    @Schema(value = "部门名称")
    private String deptName;
    @Schema(value = "机构地址")
    private String address;
    @Schema(value = "部门人员")
    private List<DepartmentUserListParam> users;
}
