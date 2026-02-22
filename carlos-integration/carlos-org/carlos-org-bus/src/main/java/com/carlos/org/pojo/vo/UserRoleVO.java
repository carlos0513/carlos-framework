package com.carlos.org.pojo.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 用户角色 显示层对象，向页面传输的对象
 * </p>
 *
 * @author yunjin
 * @date 2022-11-11 19:21:46
 */
@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.ALWAYS)
public class UserRoleVO implements Serializable {

    private static final long serialVersionUID = 1L;
    @Schema(description = "用户id")
    private String userId;
    @Schema(description = "角色id")
    private String roleId;
    @Schema(description = "角色名称")
    private String roleName;

    @Schema(description = "角色code")
    private String roleCode;

    @Schema(description = "角色id")
    private String deptId;
    @Schema(description = "角色名称")
    private String deptName;

    @Schema(description = "是否可以新增角色")
    private boolean canAdd;
}
