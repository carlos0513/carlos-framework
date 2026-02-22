package com.carlos.org.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.carlos.core.exception.ServiceException;
import com.carlos.core.pagination.Paging;
import com.carlos.datasource.pagination.PageInfo;
import com.carlos.org.convert.UserDepartmentConvert;
import com.carlos.org.manager.DepartmentManager;
import com.carlos.org.manager.UserDepartmentManager;
import com.carlos.org.pojo.dto.DepartmentDTO;
import com.carlos.org.pojo.dto.UserDepartmentDTO;
import com.carlos.org.pojo.entity.Department;
import com.carlos.org.pojo.entity.UserDepartment;
import com.carlos.org.pojo.param.CurDeptExecutorPageParam;
import com.carlos.org.pojo.param.CurSubExecutorPageParam;
import com.carlos.org.pojo.param.DepartmentUserListParam.DepartmentUserModify;
import com.carlos.org.pojo.param.UserDeptRoleDTO;
import com.carlos.org.pojo.param.UserPageParam;
import com.carlos.org.service.UserDepartmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;


/**
 * <p>
 * 用户部门 业务接口实现类
 * </p>
 *
 * @author carlos
 * @date 2022-11-11 19:21:46
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserDepartmentServiceImpl implements UserDepartmentService {

    private final UserDepartmentManager userDepartmentManager;
    private final DepartmentManager departmentManager;


    @Override
    public void addRelationByUserId(String userId, List<UserDeptRoleDTO> deptRoleDTOS) {
        if (StrUtil.isBlank(userId)) {
            throw new ServiceException("用户id不能为空");
        }
        if (deptRoleDTOS == null) {
            throw new ServiceException("部门-角色不能为空");
        }
        if (CollUtil.isEmpty(deptRoleDTOS)) {
            return;
        }

        List<UserDepartmentDTO> collect = deptRoleDTOS.stream().map(i -> new UserDepartmentDTO()
                        .setUserId(userId)
                        .setIsAdmin(false)
                        .setDepartmentId(i.getDepartmentId())
                        .setRoleId(i.getRoleId())
                        .setDepartmentLevelCode(i.getDepartmentType())
                )
                .collect(Collectors.toList());

        boolean success = userDepartmentManager.add(collect);

    }

    @Override
    public void addRelationByRoleId(String roleId, List<UserDeptRoleDTO> deptRoleDTOS) {
        if (StrUtil.isBlank(roleId)) {
            throw new ServiceException("角色id不能为空");
        }
        if (deptRoleDTOS == null) {
            throw new ServiceException("部门-角色不能为空");
        }
        if (CollUtil.isEmpty(deptRoleDTOS)) {
            return;
        }
        deptRoleDTOS.forEach(i -> {
            //先尝试更新
            boolean update = userDepartmentManager.update(new LambdaUpdateWrapper<UserDepartment>()
                    .eq(UserDepartment::getDepartmentId, i.getDepartmentId())
                    .eq(UserDepartment::getUserId, i.getUserId())
                    .set(UserDepartment::getRoleId, roleId)
                    .set(UserDepartment::getDepartmentLevelCode, i.getDepartmentType()));
            if (!update) {
                UserDepartmentDTO userDepartmentDTO = new UserDepartmentDTO()
                        .setUserId(i.getUserId())
                        .setIsAdmin(false)
                        .setDepartmentId(i.getDepartmentId())
                        .setRoleId(roleId)
                        .setDepartmentLevelCode(i.getDepartmentType());
                userDepartmentManager.add(Collections.singletonList(userDepartmentDTO));
            }
        });
    }

    @Override
    public void addDepartmentUser(String departmentId, List<DepartmentUserModify> users) {
        if (StrUtil.isBlank(departmentId)) {
            throw new ServiceException("部门id不能为空");
        }
        if (CollectionUtil.isEmpty(users)) {
            throw new ServiceException("用户不能为空");
        }
        Department byId = departmentManager.getById(departmentId);
        List<UserDepartmentDTO> collect = users.stream().map(i -> new UserDepartmentDTO()
                .setUserId(i.getId())
                .setIsAdmin(i.getAdmin())
                .setDepartmentLevelCode(byId.getDepartmentLevelCode())
                .setDepartmentId(departmentId)).collect(Collectors.toList());
        // 判断用户是否在组织机构中已存在
        List<UserDepartmentDTO> departmentUsers = userDepartmentManager.listByDepartmentId(departmentId);
        Set<String> userIds = departmentUsers.stream().map(UserDepartmentDTO::getUserId).collect(Collectors.toSet());
        for (DepartmentUserModify userDepartmentDTO : users) {
            if (userIds.contains(userDepartmentDTO.getId())) {
                throw new ServiceException("用户 '" + userDepartmentDTO.getAccount() + "' 已在该组织机构中！");
            }
        }
        boolean success = userDepartmentManager.add(collect);
        if (!success) {
            // 保存失败的应对措施
            return;
        }
    }

    @Override
    public void removeDepartmentUser(String departmentId, List<DepartmentUserModify> users) {
        if (StrUtil.isBlank(departmentId)) {
            throw new ServiceException("部门id不能为空");
        }
        if (CollectionUtil.isEmpty(users)) {
            throw new ServiceException("用户不能为空");
        }

        List<UserDepartmentDTO> collect = users.stream().map(i -> new UserDepartmentDTO()
                .setUserId(i.getId())
                .setIsAdmin(i.getAdmin())
                .setDepartmentId(departmentId)).collect(Collectors.toList());

        boolean success = userDepartmentManager.deleteBatch(collect);

    }

    @Override
    public void deleteByUserId(String userId) {
        userDepartmentManager.deleteByUserId(userId);
    }

    @Override
    public Set<String> getDepartmentIdByUserId(String userId) {
        return userDepartmentManager.getDepartmentIdByUserId(userId);
    }

    @Override
    public Set<String> getUserIdByDepartmentId(String departmentId) {
        if (StrUtil.isBlank(departmentId)) {
            return null;
        }
        return userDepartmentManager.getUserIdByDepartmentId(departmentId);
    }

    @Override
    public Set<Serializable> getUserIdByDepartmentId(Set<Serializable> departmentIds) {
        if (CollectionUtil.isEmpty(departmentIds)) {
            return null;
        }
        return userDepartmentManager.getUserIdByDepartmentId(departmentIds);
    }

    @Override
    public List<UserDepartmentDTO> getAllRef() {
        List<UserDepartment> list = userDepartmentManager.list();
        return UserDepartmentConvert.INSTANCE.toDTO(list);
    }

    @Override
    public List<UserDepartmentDTO> getByDepartmentId(String id) {
        if (StrUtil.isBlank(id)) {
            return Collections.emptyList();
        }
        return userDepartmentManager.listByDepartmentId(id);
    }

    @Override
    public PageInfo<UserDepartmentDTO> getDepartmentUserPage(String id, UserPageParam page) {
        if (StrUtil.isBlank(id)) {
            return new PageInfo<>(page);
        }
        return userDepartmentManager.listByDepartmentIdPage(id, page);
    }

    @Override
    public List<UserDepartmentDTO> getDepartmentAdmin(String id) {
        if (StrUtil.isBlank(id)) {
            return Collections.emptyList();
        }
        return userDepartmentManager.listAdminByDepartmentId(id);
    }

    @Override
    public List<String> getUserDepartmentName(String userId) {
        if (StrUtil.isEmpty(userId)) {
            return Collections.emptyList();
        }
        Set<String> departmentIds = userDepartmentManager.getDepartmentIdByUserId(userId);
        if (CollUtil.isEmpty(departmentIds)) {
            return Collections.emptyList();
        }
        List<DepartmentDTO> department = departmentManager.getByIds(departmentIds);
        if (CollUtil.isEmpty(department)) {
            return Collections.emptyList();
        }
        department.sort(Comparator.comparing(DepartmentDTO::getDepartmentLevelCode, Comparator.nullsLast(Comparator.naturalOrder())).thenComparing(DepartmentDTO::getSort)
                .thenComparing(DepartmentDTO::getCreateTime, Comparator.reverseOrder()));
        return department.stream().map(i -> {
            // 获取部门上级名称
            List<DepartmentDTO> dtos = this.departmentManager.getParentDepartment(i.getId(), 1);
            List<String> names = dtos.stream().map(DepartmentDTO::getDeptName).collect(Collectors.toList());
            return StrUtil.join(StrUtil.DASHED, names);
        }).collect(Collectors.toList());
    }

    @Override
    public List<UserDepartmentDTO> getByUserId(String userId) {
        if (StrUtil.isBlank(userId)) {
            return Collections.emptyList();
        }

        LambdaQueryWrapper<UserDepartment> queryWrapper = new LambdaQueryWrapper<>(UserDepartment.class).eq(UserDepartment::getUserId, userId);
        List<UserDepartmentDTO> userDepartmentDTOS = userDepartmentManager.list(queryWrapper).stream().map(UserDepartmentConvert.INSTANCE::toDTO).collect(Collectors.toList());

        Set<String> departmentIds = userDepartmentManager.getDepartmentIdByUserId(userId);
        if (CollUtil.isEmpty(departmentIds)) {
            return Collections.emptyList();
        }
        Map<String, DepartmentDTO> id2Map = departmentManager.getByIds(departmentIds).stream().collect(Collectors.toMap(DepartmentDTO::getId, i -> i, (k1, k2) -> k1));
        for (UserDepartmentDTO userDepartmentDTO : userDepartmentDTOS) {
            DepartmentDTO departmentDTO = id2Map.get(userDepartmentDTO.getDepartmentId());
            if (Objects.isNull(departmentDTO)) {
                continue;
            }
            userDepartmentDTO.setDepartmentName(departmentDTO.getDeptName());
            userDepartmentDTO.setDepartmentCode(departmentDTO.getDeptCode());
            userDepartmentDTO.setDeptCode(departmentDTO.getDeptCode());
        }

        return userDepartmentDTOS;
    }

    @Override
    public Paging<UserDepartmentDTO> getCurSubUser(CurSubExecutorPageParam param) {
        if (StrUtil.isBlank(param.getDeptCode())) {
            return new Paging<>();
        }
        return userDepartmentManager.listByDepartmentCode(param);
    }

    @Override
    public Paging<UserDepartmentDTO> getCurDeptUser(CurDeptExecutorPageParam param) {
        return userDepartmentManager.listCurDept(param);
    }

    @Override
    public List<UserDeptRoleDTO> getDeptRolesByUserId(String id) {
        List<UserDepartment> list = userDepartmentManager.list(new LambdaQueryWrapper<>(UserDepartment.class)
                .select(UserDepartment::getRoleId, UserDepartment::getDepartmentId, UserDepartment::getDepartmentLevelCode)
                .eq(UserDepartment::getUserId, id)
        );
        if (CollUtil.isEmpty(list)) {
            return Collections.emptyList();
        }
        return list.stream().map(i -> new UserDeptRoleDTO()
                .setRoleId(i.getRoleId())
                .setDepartmentId(i.getDepartmentId())
                .setDepartmentType(i.getDepartmentLevelCode())
        ).collect(Collectors.toList());
    }

    @Override
    public List<UserDepartmentDTO> listAll() {
        List<UserDepartment> list = userDepartmentManager.list(new LambdaQueryWrapper<>(UserDepartment.class)
                .select(UserDepartment::getId,
                        UserDepartment::getUserId,
                        UserDepartment::getDepartmentId,
                        UserDepartment::getRoleId,
                        UserDepartment::getDepartmentLevelCode));
        return UserDepartmentConvert.INSTANCE.toDTO(list);
    }

    @Override
    public Set<String> getRoleIdsByUserId(String userId) {
        return userDepartmentManager.getRoleIdsByUserId(userId);
    }

    @Override
    public List<DepartmentDTO> getDepartmentsByUserId(String id) {
        return userDepartmentManager.getDepartmentsByUserId(id);
    }

    @Override
    public List<UserDepartmentDTO> getByRoleIds(Set<String> roleIds) {
        List<UserDepartment> list = userDepartmentManager.list(new LambdaQueryWrapper<>(UserDepartment.class)
                .select(UserDepartment::getId,
                        UserDepartment::getUserId,
                        UserDepartment::getDepartmentId,
                        UserDepartment::getRoleId,
                        UserDepartment::getDepartmentLevelCode)
                .in(UserDepartment::getRoleId, roleIds));
        return UserDepartmentConvert.INSTANCE.toDTO(list);

    }

    @Override
    public void batchUpdateUserDepartment(List<UserDepartmentDTO> res) {
        userDepartmentManager.updateBatchById(UserDepartmentConvert.INSTANCE.toDO(res));
    }

    @Override
    public void deleteByRoleId(String roelId) {
        userDepartmentManager.deleteByRoleId(roelId);
    }

    @Override
    public void removeUserRole(UserDeptRoleDTO param) {
        userDepartmentManager.update(new LambdaUpdateWrapper<>(UserDepartment.class)
                .eq(UserDepartment::getUserId, param.getUserId())
                .eq(UserDepartment::getDepartmentId, param.getDepartmentId())
                .eq(UserDepartment::getRoleId, param.getRoleId())
                .set(UserDepartment::getRoleId, null));
    }

    @Override
    public List<UserDepartmentDTO> getByLevels(Set<String> deptLevels) {
        return userDepartmentManager.getByLevels(deptLevels);
    }

    @Override
    public List<UserDepartmentDTO> getByUserIdAndDeptId(String userId, String deptId) {
        return UserDepartmentConvert.INSTANCE.toDTO(userDepartmentManager.list(new LambdaQueryWrapper<>(UserDepartment.class)
                .eq(UserDepartment::getUserId, userId)
                .eq(UserDepartment::getDepartmentId, deptId)
                .isNotNull(UserDepartment::getRoleId)));
    }
}
