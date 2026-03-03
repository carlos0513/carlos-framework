package com.carlos.org.pojo.param;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * <p>
 * 用户分配部门参数
 * </p>
 *
 * @author carlos
 * @date 2026-03-03
 */
@Data
@Accessors(chain = true)
@Schema(description = "用户分配部门参数-UM012")
public class OrgUserAssignDeptParam {

    @NotBlank(message = "用户id不能为空")
    @Schema(description = "用户ID")
    private String userId;

    @NotEmpty(message = "部门列表不能为空")
    @Schema(description = "部门ID列表")
    private List<String> departmentIds;

    @Schema(description = "主部门ID")
    private String mainDeptId;

}
