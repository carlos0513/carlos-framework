package com.carlos.org.pojo.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 部门角色 显示层对象，向页面传输的对象
 * </p>
 *
 * @author yunjin
 * @date 2022-11-11 19:21:46
 */
@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.ALWAYS)
public class DepartmentRoleVO implements Serializable {

    private static final long serialVersionUID = 1L;
    @Schema(description = "主键")
    private String id;
    @Schema(description = "部门id")
    private String departmentId;
    @Schema(description = "角色id")
    private String roleId;
    @Schema(description = "创建者")
    private String createBy;
    @Schema(description = "创建时间")
    private LocalDateTime createTime;
    @Schema(description = "修改时间")
    private LocalDateTime updateTime;
    @Schema(description = "租户id")
    private String tenantId;

}
