package com.carlos.org.service;


import cn.hutool.core.util.StrUtil;
import com.carlos.core.pagination.Paging;
import com.carlos.org.exception.*;
import com.carlos.org.manager.OrgRoleManager;
import com.carlos.org.pojo.dto.OrgRoleDTO;
import com.carlos.org.pojo.dto.OrgRoleUserDTO;
import com.carlos.org.pojo.enums.OrgDataScopeEnum;
import com.carlos.org.pojo.enums.OrgRoleStateEnum;
import com.carlos.org.pojo.enums.OrgRoleTypeEnum;
import com.carlos.org.pojo.param.OrgRoleAssignPermissionParam;
import com.carlos.org.pojo.param.OrgRoleCopyParam;
import com.carlos.org.pojo.param.OrgRolePageParam;
import com.carlos.org.pojo.param.OrgRoleSetDataScopeParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;
import java.util.Set;


/**
 * <p>
 * 角色 业务
 * </p>
 * <p>实现RM001-RM010所有角色管理需求</p>
 *
 * @author Carlos
 * @date 2026年2月28日 下午1:25:36
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class OrgRoleService {

    private final OrgRoleManager roleManager;


    /**
     * RM-003 新增角色
     *
     * @param dto 角色数据
     */
    @Transactional(rollbackFor = Exception.class)
    public void addOrgRole(OrgRoleDTO dto) {
        // 校验角色编码唯一性
        if (StrUtil.isNotBlank(dto.getRoleCode())) {
            OrgRoleDTO existRole = roleManager.getByRoleCode(dto.getRoleCode());
            if (existRole != null) {
                throw new RoleCodeExistsException(dto.getRoleCode());
            }
        }

        // 设置默认类型为自定义角色
        if (dto.getRoleType() == null) {
            dto.setRoleType(OrgRoleTypeEnum.CUSTOM);
        }

        // 设置默认状态为启用
        if (dto.getState() == null) {
            dto.setState(OrgRoleStateEnum.ENABLE);
        }

        // 设置默认数据范围为仅本人
        if (dto.getDataScope() == null) {
            dto.setDataScope(OrgDataScopeEnum.SELF_ONLY);
        }

        boolean success = roleManager.add(dto);
        if (!success) {
            throw new OrgModuleException("新增角色失败");
        }
        log.info("RM-003 新增角色成功：id={}, roleName={}", dto.getId(), dto.getRoleName());
    }


    /**
     * RM-005 删除角色
     * 删除前校验：角色未被使用
     *
     * @param ids 角色id集合
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteOrgRole(Set<Serializable> ids) {
        for (Serializable id : ids) {
            // 校验角色是否存在
            OrgRoleDTO role = roleManager.getDtoById(id);
            if (role == null) {
                throw new RoleNotFoundException(String.valueOf(id));
            }

            // 校验是否为系统角色
            if (role.getRoleType() == OrgRoleTypeEnum.SYSTEM) {
                throw new SystemRoleNotEditableException(String.valueOf(id));
            }

            // 校验角色是否被使用
            int userCount = roleManager.getUserCountByRoleId(id);
            if (userCount > 0) {
                throw new RoleInUseException(String.valueOf(id));
            }

            // 逻辑删除
            boolean success = roleManager.removeById(id);
            if (!success) {
                log.warn("删除角色失败：id={}", id);
            }
        }
        log.info("RM-005 删除角色成功：ids={}", ids);
    }


    /**
     * RM-004 编辑角色
     *
     * @param dto 对象信息
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateOrgRole(OrgRoleDTO dto) {
        // 校验角色是否存在
        OrgRoleDTO existRole = roleManager.getDtoById(dto.getId());
        if (existRole == null) {
            throw new RoleNotFoundException(String.valueOf(dto.getId()));
        }

        // 校验是否为系统角色
        if (existRole.getRoleType() == OrgRoleTypeEnum.SYSTEM) {
            throw new SystemRoleNotEditableException(String.valueOf(dto.getId()));
        }

        boolean success = roleManager.modify(dto);
        if (!success) {
            throw new OrgModuleException("修改角色失败");
        }
        log.info("RM-004 修改角色成功：id={}", dto.getId());
    }


    /**
     * RM-006 启用/禁用角色
     *
     * @param id    角色ID
     * @param state 状态：0禁用，1启用
     */
    @Transactional(rollbackFor = Exception.class)
    public void changeState(Serializable id, Integer state) {
        if (state == null || (state != 0 && state != 1)) {
            throw new OrgModuleException("状态值不正确，只能是0（禁用）或1（启用）");
        }

        OrgRoleDTO role = roleManager.getDtoById(id);
        if (role == null) {
            throw new RoleNotFoundException(String.valueOf(id));
        }

        // 校验是否为系统角色
        if (role.getRoleType() == OrgRoleTypeEnum.SYSTEM) {
            throw new SystemRoleNotEditableException(String.valueOf(id));
        }

        role.setState(state == 1 ? OrgRoleStateEnum.ENABLE : OrgRoleStateEnum.DISABLE);
        boolean success = roleManager.modify(role);
        if (!success) {
            throw new OrgModuleException("修改角色状态失败");
        }
        log.info("RM-006 修改角色状态成功：id={}, state={}", id, state);
    }


    /**
     * RM-008 配置数据权限
     *
     * @param param 数据权限参数
     */
    @Transactional(rollbackFor = Exception.class)
    public void setDataScope(OrgRoleSetDataScopeParam param) {
        // 校验角色是否存在
        OrgRoleDTO role = roleManager.getDtoById(param.getRoleId());
        if (role == null) {
            throw new RoleNotFoundException(String.valueOf(param.getRoleId()));
        }

        // 校验数据范围值
        OrgDataScopeEnum dataScope = getDataScopeByCode(param.getDataScope());
        if (dataScope == null) {
            throw new OrgModuleException("数据范围值不正确");
        }

        role.setDataScope(dataScope);
        boolean success = roleManager.modify(role);
        if (!success) {
            throw new OrgModuleException("设置数据权限失败");
        }
        log.info("RM-008 设置数据权限成功：roleId={}, dataScope={}", param.getRoleId(), param.getDataScope());
    }


    /**
     * 根据code获取数据范围枚举
     */
    private OrgDataScopeEnum getDataScopeByCode(Integer code) {
        for (OrgDataScopeEnum scope : OrgDataScopeEnum.values()) {
            if (scope.getCode().equals(code)) {
                return scope;
            }
        }
        return null;
    }


    /**
     * RM-007 配置角色权限
     *
     * @param param 权限配置参数
     */
    @Transactional(rollbackFor = Exception.class)
    public void assignPermissions(OrgRoleAssignPermissionParam param) {
        // 校验角色是否存在
        OrgRoleDTO role = roleManager.getDtoById(param.getRoleId());
        if (role == null) {
            throw new RoleNotFoundException(String.valueOf(param.getRoleId()));
        }

        boolean success = roleManager.assignPermissions(param.getRoleId(), param.getPermissionIds());
        if (!success) {
            throw new OrgModuleException("配置角色权限失败");
        }
        log.info("RM-007 配置角色权限成功：roleId={}, permissionCount={}",
                param.getRoleId(), param.getPermissionIds().size());
    }


    /**
     * RM-009 查看角色用户
     *
     * @param roleId 角色ID
     * @param param  分页参数
     * @return 用户分页列表
     */
    public Paging<OrgRoleUserDTO> getRoleUsers(Serializable roleId, OrgRolePageParam param) {
        // 校验角色是否存在
        OrgRoleDTO role = roleManager.getDtoById(roleId);
        if (role == null) {
            throw new RoleNotFoundException(String.valueOf(roleId));
        }

        return roleManager.getRoleUsers(roleId, param);
    }


    /**
     * RM-010 复制角色
     *
     * @param param 复制参数
     */
    @Transactional(rollbackFor = Exception.class)
    public void copyRole(OrgRoleCopyParam param) {
        // 校验源角色是否存在
        OrgRoleDTO sourceRole = roleManager.getDtoById(param.getSourceRoleId());
        if (sourceRole == null) {
            throw new RoleNotFoundException(String.valueOf(param.getSourceRoleId()));
        }

        // 校验新角色编码唯一性
        if (StrUtil.isNotBlank(param.getNewRoleCode())) {
            OrgRoleDTO existRole = roleManager.getByRoleCode(param.getNewRoleCode());
            if (existRole != null) {
                throw new RoleCodeExistsException(param.getNewRoleCode());
            }
        }

        // 创建新角色
        OrgRoleDTO newRole = new OrgRoleDTO();
        newRole.setRoleName(param.getNewRoleName());
        newRole.setRoleCode(param.getNewRoleCode());
        newRole.setRoleType(OrgRoleTypeEnum.CUSTOM); // 复制后的角色为自定义类型
        newRole.setDataScope(sourceRole.getDataScope());
        newRole.setState(OrgRoleStateEnum.ENABLE);
        newRole.setDescription(sourceRole.getDescription());

        boolean success = roleManager.add(newRole);
        if (!success) {
            throw new OrgModuleException("复制角色失败");
        }

        // 复制权限关联
        List<Long> permissionIds = roleManager.getPermissionIdsByRoleId(param.getSourceRoleId());
        if (permissionIds != null && !permissionIds.isEmpty()) {
            roleManager.assignPermissions(newRole.getId(),
                    permissionIds.stream().map(id -> (Serializable) id).collect(java.util.stream.Collectors.toList()));
        }

        log.info("RM-010 复制角色成功：sourceRoleId={}, newRoleId={}, newRoleName={}",
                param.getSourceRoleId(), newRole.getId(), param.getNewRoleName());
    }

}
