package com.carlos.org.login.pojo;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 登录用户角色信息
 * </p>
 *
 * @author Carlos
 * @date 2022/11/17 9:45
 */
@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.ALWAYS)
public class CurrentUserRoleVO implements Serializable {

    private static final long serialVersionUID = 1L;
    @Schema(description = "主键")
    private Long id;

    @Schema(description = "角色名称")
    private String name;

    @Schema(description = "角色唯一编码")
    private String code;

    @Schema(description = "部门id")
    private String departmentId;

    @Schema(description = "部门名称")
    private String departmentName;
}
