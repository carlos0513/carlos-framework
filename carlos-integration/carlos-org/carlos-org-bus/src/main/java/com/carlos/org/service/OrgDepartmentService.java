package com.carlos.org.service;


import cn.hutool.core.util.StrUtil;
import com.carlos.core.pagination.Paging;
import com.carlos.org.exception.*;
import com.carlos.org.manager.OrgDepartmentManager;
import com.carlos.org.manager.OrgUserManager;
import com.carlos.org.pojo.dto.OrgDepartmentDTO;
import com.carlos.org.pojo.dto.OrgDepartmentUserDTO;
import com.carlos.org.pojo.dto.OrgUserDTO;
import com.carlos.org.pojo.enums.OrgDepartmentStateEnum;
import com.carlos.org.pojo.param.OrgDepartmentMoveParam;
import com.carlos.org.pojo.param.OrgDepartmentPageParam;
import com.carlos.org.pojo.param.OrgDepartmentSetLeaderParam;
import com.carlos.org.pojo.param.OrgDepartmentSortParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Comparator;
import java.util.List;
import java.util.Set;


/**
 * <p>
 * 部门 业务
 * </p>
 * <p>实现DM001-DM010所有部门管理需求</p>
 *
 * @author Carlos
 * @date 2026年2月28日 下午1:25:36
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class OrgDepartmentService {

    private final OrgDepartmentManager departmentManager;

    private final OrgUserManager userManager;


    /**
     * DM-003 新增部门
     *
     * @param dto 部门数据
     */
    @Transactional(rollbackFor = Exception.class)
    public void addOrgDepartment(OrgDepartmentDTO dto) {
        // 校验部门编号唯一性
        if (StrUtil.isNotBlank(dto.getDeptCode())) {
            OrgDepartmentDTO existDept = departmentManager.getByDeptCode(dto.getDeptCode());
            if (existDept != null) {
                throw new DepartmentCodeExistsException(dto.getDeptCode());
            }
        }

        // 设置默认状态为启用
        if (dto.getState() == null) {
            dto.setState(OrgDepartmentStateEnum.ENABLE);
        }

        // 如果没有设置排序，默认为0
        if (dto.getSort() == null) {
            dto.setSort(0);
        }

        // 设置层级和路径
        if (dto.getParentId() == null || dto.getParentId() == 0) {
            // 根部门
            dto.setParentId(0L);
            dto.setLevel(1);
            dto.setPath("/");
        } else {
            // 子部门
            OrgDepartmentDTO parentDept = departmentManager.getDtoById(dto.getParentId());
            if (parentDept == null) {
                throw new DepartmentNotFoundException(String.valueOf(dto.getParentId()));
            }
            dto.setLevel(parentDept.getLevel() + 1);
            dto.setPath(parentDept.getPath() + dto.getParentId() + "/");
        }

        boolean success = departmentManager.add(dto);
        if (!success) {
            throw new DepartmentOperationException("新增部门失败");
        }
        log.info("DM-003 新增部门成功：id={}", dto.getId());
    }


    /**
     * DM-005 删除部门
     * 删除前校验：1. 无子部门 2. 无用户
     *
     * @param ids 部门id集合
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteOrgDepartment(Set<Serializable> ids) {
        for (Serializable id : ids) {
            // 校验部门是否存在
            OrgDepartmentDTO dept = departmentManager.getDtoById(id);
            if (dept == null) {
                throw new DepartmentNotFoundException(String.valueOf(id));
            }

            // 校验是否有子部门
            List<OrgDepartmentDTO> children = departmentManager.getChildrenByParentId(id);
            if (!children.isEmpty()) {
                throw new DepartmentHasChildrenException(String.valueOf(id));
            }

            // 校验是否有用户
            List<OrgUserDTO> users = departmentManager.getUsersByDeptId(id);
            if (!users.isEmpty()) {
                throw new DepartmentHasUsersException(String.valueOf(id));
            }

            // 逻辑删除
            boolean success = departmentManager.removeById(id);
            if (!success) {
                log.warn("删除部门失败：id={}", id);
            }
        }
        log.info("DM-005 删除部门成功：ids={}", ids);
    }


    /**
     * DM-004 编辑部门
     *
     * @param dto 对象信息
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateOrgDepartment(OrgDepartmentDTO dto) {
        // 校验部门是否存在
        OrgDepartmentDTO existDept = departmentManager.getDtoById(dto.getId());
        if (existDept == null) {
            throw new DepartmentNotFoundException(String.valueOf(dto.getId()));
        }

        // 校验部门编号唯一性（排除自己）
        if (StrUtil.isNotBlank(dto.getDeptCode()) && !dto.getDeptCode().equals(existDept.getDeptCode())) {
            OrgDepartmentDTO codeDept = departmentManager.getByDeptCode(dto.getDeptCode());
            if (codeDept != null) {
                throw new DepartmentCodeExistsException(dto.getDeptCode());
            }
        }

        boolean success = departmentManager.modify(dto);
        if (!success) {
            throw new DepartmentOperationException("修改部门失败");
        }
        log.info("DM-004 修改部门成功：id={}", dto.getId());
    }


    /**
     * DM-001 获取部门树（平铺列表，由前端或Convert构建树）
     *
     * @return 部门列表
     */
    public List<OrgDepartmentDTO> getDepartmentTree() {
        // 获取所有部门
        List<OrgDepartmentDTO> allDepts = departmentManager.listAll();

        // 按排序排序
        allDepts.sort(Comparator.comparing(OrgDepartmentDTO::getSort, Comparator.nullsLast(Comparator.naturalOrder())));

        return allDepts;
    }


    /**
     * DM-006 移动部门
     *
     * @param param 移动参数
     */
    @Transactional(rollbackFor = Exception.class)
    public void moveDepartment(OrgDepartmentMoveParam param) {
        // 校验部门是否存在
        OrgDepartmentDTO dept = departmentManager.getDtoById(param.getId());
        if (dept == null) {
            throw new DepartmentNotFoundException(param.getId());
        }

        // 校验目标父部门是否存在（0表示根部门，不需要校验）
        Long parentId = Long.valueOf(param.getParentId());
        if (parentId != 0) {
            OrgDepartmentDTO parentDept = departmentManager.getDtoById(parentId);
            if (parentDept == null) {
                throw new DepartmentNotFoundException(param.getParentId());
            }

            // 校验不能移动到自己的子部门下
            if (isChildDepartment(dept.getId(), parentId)) {
                throw new DepartmentOperationException("不能移动到自己的子部门下");
            }
        }

        // 更新父ID、层级和路径
        dept.setParentId(parentId);
        if (parentId == 0) {
            dept.setLevel(1);
            dept.setPath("/");
        } else {
            OrgDepartmentDTO parentDept = departmentManager.getDtoById(parentId);
            dept.setLevel(parentDept.getLevel() + 1);
            dept.setPath(parentDept.getPath() + parentId + "/");
        }

        // 更新子部门的path
        updateChildrenPath(dept);

        boolean success = departmentManager.modify(dept);
        if (!success) {
            throw new DepartmentOperationException("移动部门失败");
        }
        log.info("DM-006 移动部门成功：id={}, parentId={}", param.getId(), parentId);
    }


    /**
     * 判断是否为自己的子部门
     */
    private boolean isChildDepartment(Long deptId, Long targetParentId) {
        if (deptId.equals(targetParentId)) {
            return true;
        }
        List<OrgDepartmentDTO> children = departmentManager.getChildrenByParentId(deptId);
        for (OrgDepartmentDTO child : children) {
            if (isChildDepartment(child.getId(), targetParentId)) {
                return true;
            }
        }
        return false;
    }


    /**
     * 更新子部门的path
     */
    private void updateChildrenPath(OrgDepartmentDTO parentDept) {
        List<OrgDepartmentDTO> children = departmentManager.getChildrenByParentId(parentDept.getId());
        for (OrgDepartmentDTO child : children) {
            child.setLevel(parentDept.getLevel() + 1);
            child.setPath(parentDept.getPath() + parentDept.getId() + "/");
            departmentManager.modify(child);
            updateChildrenPath(child);
        }
    }


    /**
     * DM-007 部门排序
     *
     * @param param 排序参数
     */
    @Transactional(rollbackFor = Exception.class)
    public void sortDepartment(OrgDepartmentSortParam param) {
        // 校验部门是否存在
        OrgDepartmentDTO dept = departmentManager.getDtoById(param.getId());
        if (dept == null) {
            throw new DepartmentNotFoundException(param.getId());
        }

        dept.setSort(param.getSort());
        boolean success = departmentManager.modify(dept);
        if (!success) {
            throw new DepartmentOperationException("部门排序失败");
        }
        log.info("DM-007 部门排序成功：id={}, sort={}", param.getId(), param.getSort());
    }


    /**
     * DM-008 获取部门人员列表
     *
     * @param deptId 部门ID
     * @param param  分页参数
     * @return 人员分页列表
     */
    public Paging<OrgDepartmentUserDTO> getDepartmentUsers(Serializable deptId, OrgDepartmentPageParam param) {
        // 校验部门是否存在
        OrgDepartmentDTO dept = departmentManager.getDtoById(deptId);
        if (dept == null) {
            throw new DepartmentNotFoundException(String.valueOf(deptId));
        }

        return departmentManager.getUsersByDeptId(deptId, param);
    }


    /**
     * DM-009 设置部门负责人
     *
     * @param param 设置负责人参数
     */
    @Transactional(rollbackFor = Exception.class)
    public void setDepartmentLeader(OrgDepartmentSetLeaderParam param) {
        // 校验部门是否存在
        OrgDepartmentDTO dept = departmentManager.getDtoById(param.getDeptId());
        if (dept == null) {
            throw new DepartmentNotFoundException(param.getDeptId());
        }

        // 校验负责人是否存在
        OrgUserDTO user = userManager.getDtoById(param.getLeaderId());
        if (user == null) {
            throw new UserNotFoundException(param.getLeaderId());
        }

        dept.setLeaderId(Long.valueOf(param.getLeaderId()));
        boolean success = departmentManager.modify(dept);
        if (!success) {
            throw new DepartmentOperationException("设置部门负责人失败");
        }
        log.info("DM-009 设置部门负责人成功：deptId={}, leaderId={}", param.getDeptId(), param.getLeaderId());
    }


    /**
     * DM-010 部门角色配置（预留）
     *
     * @param deptId 部门ID
     * @param roleId 角色ID
     */
    @Transactional(rollbackFor = Exception.class)
    public void setDepartmentRole(Serializable deptId, Serializable roleId) {
        // 校验部门是否存在
        OrgDepartmentDTO dept = departmentManager.getDtoById(deptId);
        if (dept == null) {
            throw new DepartmentNotFoundException(String.valueOf(deptId));
        }

        // TODO: 实现部门角色关联逻辑
        log.info("DM-010 部门角色配置：deptId={}, roleId={}", deptId, roleId);
    }


    /**
     * 启用/禁用部门
     *
     * @param id    部门ID
     * @param state 状态：0禁用，1启用
     */
    @Transactional(rollbackFor = Exception.class)
    public void changeState(Serializable id, Integer state) {
        if (state != 0 && state != 1) {
            throw new DepartmentOperationException("状态值不正确，只能是0（禁用）或1（启用）");
        }

        OrgDepartmentDTO dept = departmentManager.getDtoById(id);
        if (dept == null) {
            throw new DepartmentNotFoundException(String.valueOf(id));
        }

        dept.setState(state == 1 ? OrgDepartmentStateEnum.ENABLE : OrgDepartmentStateEnum.DISABLE);
        boolean success = departmentManager.modify(dept);
        if (!success) {
            throw new DepartmentOperationException("修改部门状态失败");
        }
        log.info("修改部门状态成功：id={}, state={}", id, state);
    }

}
