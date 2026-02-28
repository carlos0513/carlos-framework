package com.carlos.org.service;

import com.carlos.org.manager.OrgRolePermissionManager;
import com.carlos.org.pojo.dto.OrgRolePermissionDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Set;


/**
 * <p>
 * 角色权限 业务
 * </p>
 *
 * @author Carlos
 * @date 2026年2月28日 下午1:25:36
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class OrgRolePermissionService {

    private final OrgRolePermissionManager rolePermissionManager;

    /**
     * 新增角色权限
     *
     * @param dto 角色权限数据
     * @author Carlos
     * @date 2026年2月28日 下午1:25:36
     */
    public void addOrgRolePermission(OrgRolePermissionDTO dto) {
        boolean success = rolePermissionManager.add(dto);
        if (!success) {
            // 保存失败的应对措施
            return;
        }
        Serializable id = dto.getId();
        log.info("Insert 'OrgRolePermission' data: id:{}", id);
        // 保存完成的后续业务
    }

    /**
     * 删除角色权限
     *
     * @param ids 角色权限id
     * @author Carlos
     * @date 2026年2月28日 下午1:25:36
     */
    public void deleteOrgRolePermission(Set<Serializable> ids) {
        for (Serializable id : ids) {
            boolean success = rolePermissionManager.delete(id);
            if (!success) {
                // 删除失败的措施
                continue;
            }
            // 删除成功的后续业务
        }
    }

    /**
     * 修改角色权限信息
     *
     * @param dto 对象信息
     * @author Carlos
     * @date 2026年2月28日 下午1:25:36
     */
    public void updateOrgRolePermission(OrgRolePermissionDTO dto) {
        boolean success = rolePermissionManager.modify(dto);
        if (!success) {
            // 修改失败操作
            return;
        }
        // 修改成功的后续操作
        log.info("Update 'OrgRolePermission' data: id:{}", dto.getId());
    }

}
