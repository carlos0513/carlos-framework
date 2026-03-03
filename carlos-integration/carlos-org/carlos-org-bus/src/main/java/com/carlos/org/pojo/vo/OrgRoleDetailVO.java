package com.carlos.org.pojo.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * <p>
 * 角色详情 VO（包含权限信息）
 * </p>
 * <p>RM-002 角色详情</p>
 *
 * @author Carlos
 * @date 2026年2月28日 下午1:25:36
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrgRoleDetailVO extends OrgRoleVO {

    private static final long serialVersionUID = 1L;

    /**
     * 权限ID列表
     */
    @Schema(description = "权限ID列表")
    private List<Long> permissionIds;

    /**
     * 权限树
     */
    @Schema(description = "权限树")
    private List<OrgPermissionTreeVO> permissionTree;

}
