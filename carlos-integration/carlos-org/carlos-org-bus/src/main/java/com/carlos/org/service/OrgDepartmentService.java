package com.carlos.org.service;

import com.carlos.org.manager.OrgDepartmentManager;
import com.carlos.org.pojo.dto.OrgDepartmentDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Set;


/**
 * <p>
 * 部门 业务
 * </p>
 *
 * @author Carlos
 * @date 2026年2月28日 下午1:25:36
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class OrgDepartmentService {

    private final OrgDepartmentManager departmentManager;

    /**
     * 新增部门
     *
     * @param dto 部门数据
     * @author Carlos
     * @date 2026年2月28日 下午1:25:36
     */
    public void addOrgDepartment(OrgDepartmentDTO dto) {
        boolean success = departmentManager.add(dto);
        if (!success) {
            // 保存失败的应对措施
            return;
        }
        Serializable id = dto.getId();
        log.info("Insert 'OrgDepartment' data: id:{}", id);
        // 保存完成的后续业务
    }

    /**
     * 删除部门
     *
     * @param ids 部门id
     * @author Carlos
     * @date 2026年2月28日 下午1:25:36
     */
    public void deleteOrgDepartment(Set<Serializable> ids) {
        for (Serializable id : ids) {
            boolean success = departmentManager.delete(id);
            if (!success) {
                // 删除失败的措施
                continue;
            }
            // 删除成功的后续业务
        }
    }

    /**
     * 修改部门信息
     *
     * @param dto 对象信息
     * @author Carlos
     * @date 2026年2月28日 下午1:25:36
     */
    public void updateOrgDepartment(OrgDepartmentDTO dto) {
        boolean success = departmentManager.modify(dto);
        if (!success) {
            // 修改失败操作
            return;
        }
        // 修改成功的后续操作
        log.info("Update 'OrgDepartment' data: id:{}", dto.getId());
    }

}
