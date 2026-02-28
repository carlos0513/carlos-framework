package com.carlos.org.service;

import com.carlos.org.manager.OrgRoleManager;
import com.carlos.org.pojo.dto.OrgRoleDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Set;


/**
 * <p>
 * 系统角色 业务
 * </p>
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
     * 新增系统角色
     *
     * @param dto 系统角色数据
     * @author Carlos
     * @date 2026年2月28日 下午1:25:36
     */
    public void addOrgRole(OrgRoleDTO dto) {
        boolean success = roleManager.add(dto);
        if (!success) {
            // 保存失败的应对措施
            return;
        }
        Serializable id = dto.getId();
        log.info("Insert 'OrgRole' data: id:{}", id);
        // 保存完成的后续业务
    }

    /**
     * 删除系统角色
     *
     * @param ids 系统角色id
     * @author Carlos
     * @date 2026年2月28日 下午1:25:36
     */
    public void deleteOrgRole(Set<Serializable> ids) {
        for (Serializable id : ids) {
            boolean success = roleManager.delete(id);
            if (!success) {
                // 删除失败的措施
                continue;
            }
            // 删除成功的后续业务
        }
    }

    /**
     * 修改系统角色信息
     *
     * @param dto 对象信息
     * @author Carlos
     * @date 2026年2月28日 下午1:25:36
     */
    public void updateOrgRole(OrgRoleDTO dto) {
        boolean success = roleManager.modify(dto);
        if (!success) {
            // 修改失败操作
            return;
        }
        // 修改成功的后续操作
        log.info("Update 'OrgRole' data: id:{}", dto.getId());
    }

}
