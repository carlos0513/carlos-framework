package com.yunjin.org.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.google.common.collect.Sets;
import com.yunjin.boot.request.RequestInfo;
import com.yunjin.boot.request.RequestUtil;
import com.yunjin.core.auth.UserContext;
import com.yunjin.core.exception.ServiceException;
import com.yunjin.core.pagination.Paging;
import com.yunjin.core.response.Result;
import com.yunjin.org.UserUtil;
import com.yunjin.org.manager.DepartmentRoleManager;
import com.yunjin.org.manager.RoleManager;
import com.yunjin.org.manager.UserDepartmentManager;
import com.yunjin.org.pojo.ao.UserLoginAO;
import com.yunjin.org.pojo.dto.*;
import com.yunjin.org.pojo.entity.UserDepartment;
import com.yunjin.org.pojo.param.RolePageParam;
import com.yunjin.org.pojo.param.UserDeptRoleDTO;
import com.yunjin.org.pojo.vo.RoleDetailVO;
import com.yunjin.org.pojo.vo.RolePageVO;
import com.yunjin.org.pojo.vo.UserRoleVO;
import com.yunjin.org.service.*;
import com.yunjin.resource.service.ResourceGroupService;
import com.yunjin.system.api.ApiMenu;
import com.yunjin.system.enums.MenuType;
import com.yunjin.system.pojo.ao.MenuAO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;


/**
 * <p>
 * 系统角色 业务接口实现类
 * </p>
 *
 * @author yunjin
 * @date 2022-11-11 18:19:17
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    public static final String ROLE_CODE_PREFIX = "ROLE_";
    private final RoleManager roleManager;
    private final RoleMenuService roleMenuService;
    private final DepartmentRoleService departmentRoleService;
    private final DepartmentRoleManager departmentRoleManager;
    private final DepartmentService departmentService;
    private final RoleResourceGroupRefService roleResourceGroupRefService;
    private final UserDepartmentService userDepartmentService;
    private final ResourceGroupService resourceGroupService;
    private final UserDepartmentManager userDepartmentManager;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean addRole(RoleDTO dto) {
        //if(StringUtils.isEmpty(dto.getResourceGroupId())){
        //    dto.setResourceGroupId("2024082314481815640073190785028");
        //}
        RoleDTO role;
        // 检查角色是否已经存在
        role = roleManager.getByName(dto.getName());
        if (role != null) {
            throw new ServiceException("角色名称已存在");
        }
        // 获取角色编码
        String code = getRoleNextCode();
        dto.setCode(code);
        boolean success = roleManager.add(dto);
        if (!success) {
            // 保存失败的应对措施
            return false;
        }
        String id = dto.getId();

//        RequestInfo requestInfo = RequestUtil.getRequestInfo();
//        UserContext userContext = requestInfo.getUserContext();
//        Set<Serializable> departmentIds = userContext.getDepartmentIds();
//        if (CollectionUtil.isEmpty(departmentIds)) {
//            throw new ServiceException("当前用户信息不存在组织机构！");
//        }
//        ArrayList<Serializable> deptIds = new ArrayList<>(departmentIds);
//        Serializable departmentId = deptIds.get(0);
//        DepartmentRoleDTO departmentRole = new DepartmentRoleDTO();
//        departmentRole.setDepartmentId(departmentId.toString());
//        departmentRole.setRoleId(id);
//        departmentRole.setCreateBy(userContext.getUserId().toString());
//        departmentRole.setCreateTime(LocalDateTime.now());
//        departmentRoleService.addDepartmentRole(departmentRole);

        // 保存角色机构信息
        this.batchAddDepartmentRole(dto.getDepartmentTypes(), dto.getId());

        // 保存角色用户信息
        if (CollectionUtil.isNotEmpty(dto.getUserDeptRoles())) {
            userDepartmentService.addRelationByRoleId(id, dto.getUserDeptRoles());
            log.info("角色用户信息保存成功， roleId={}  userId:{}", dto.getId(), dto.getUserDeptRoles());
        }

        // 保存角色菜单信息
        roleMenuService.addRoleMenu(id, dto.getMenuIds());

        // 保存角色资源
        RoleResourceGroupRefDTO resourceGroupRef = new RoleResourceGroupRefDTO();
        resourceGroupRef.setRoleId(id);
        resourceGroupRef.setResourceGroupId(dto.getResourceGroupId());
        roleResourceGroupRefService.addRoleResourceGroupRef(resourceGroupRef);
        return true;
    }

    private void batchAddDepartmentRole(Set<String> departmentTypes, String roleId) {
        if (CollectionUtil.isNotEmpty(departmentTypes)) {
            RequestInfo requestInfo = RequestUtil.getRequestInfo();
            UserContext userContext = requestInfo.getUserContext();
            List<DepartmentRoleDTO> departmentRoleList = new ArrayList<>();
            LocalDateTime now = LocalDateTime.now();
            for (String type : departmentTypes) {
                DepartmentRoleDTO departmentRole = new DepartmentRoleDTO();
                departmentRole.setDepartmentType(type);
                departmentRole.setRoleId(roleId);
                departmentRole.setCreateBy(userContext.getUserId().toString());
                departmentRole.setCreateTime(now);
                departmentRoleList.add(departmentRole);
            }
            departmentRoleService.batchAddDepartmentRole(departmentRoleList);
            log.info("角色机构信息保存成功， roleId={}  departmentId:{}", roleId, departmentTypes);
        }
    }

    @Override
    public String getRoleNextCode() {
        RoleDTO role = roleManager.getLatestOne();
        String code = role.getCode();
        String num = StrUtil.subAfter(code, StrUtil.UNDERLINE, true);
        if (NumberUtil.isNumber(num)) {
            int integer = Integer.parseInt(num);
            return ROLE_CODE_PREFIX + (integer + 1);
        }
        throw new ServiceException("角色编码不合法");
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteRole(Set<String> ids) {
        if (CollectionUtil.isEmpty(ids)) {
            throw new ServiceException("删除角色id的集合为空，请重试！");
        }
        for (String id : ids) {
            // 判断角色和用户关联信息
            List<UserDepartmentDTO> refs = userDepartmentService.getByRoleIds(Collections.singleton(id));
            if (CollectionUtil.isNotEmpty(refs)) {
                throw new ServiceException("角色已关联用户，无法删除");
            }

            boolean success = roleManager.delete(id);
            if (!success) {
                // 删除失败的措施
                continue;
            }

            //删除角色菜单表中的角色信息
            roleMenuService.deleteRoleMenu(id, null);

            // 删除角色机构信息
            departmentRoleService.deleteByRoleId(id);
        }
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateRole(RoleDTO dto) {
        //if(StringUtils.isEmpty(dto.getResourceGroupId())){
        //    dto.setResourceGroupId("2024082314481815640073190785028");
        //}

        // 根据id获取角色信息
        String id = dto.getId();
        RoleDTO role = roleManager.getDtoById(id);
        if (role == null) {
            throw new ServiceException("角色不存在");
        }
        // 保存角色机构信息
        departmentRoleService.deleteByRoleId(dto.getId());
        this.batchAddDepartmentRole(dto.getDepartmentTypes(), dto.getId());

        // 保存角色用户信息
        // 更新角色优化 取消一次性添加角色与用户关系 改为单个用户与角色关系的新增与删除
//        userDepartmentService.deleteByRoleId(id);
//        if (CollectionUtil.isNotEmpty(dto.getUserDeptRoles())) {
//            userDepartmentService.addRelationByRoleId(id, dto.getUserDeptRoles());
//            log.info("角色用户信息保存成功， roleId={}  userId:{}", dto.getId(), dto.getUserDeptRoles());
//        }

        // 保存角色菜单信息
        roleMenuService.addRoleMenu(id, dto.getMenuIds());

        // 保存角色资源组关联信息
        RoleResourceGroupRefDTO resourceGroupRef = new RoleResourceGroupRefDTO();
        resourceGroupRef.setRoleId(id);
        resourceGroupRef.setResourceGroupId(dto.getResourceGroupId());
        roleResourceGroupRefService.addRoleResourceGroupRef(resourceGroupRef);
        return true;
    }

    @Override
    public RoleDTO getById(Serializable roleId) {
        return roleManager.getDtoById(roleId);
    }

    @Override
    public List<RoleDTO> getRoleByIds(Set<String> roleIds) {
        return roleManager.getDtoByIds(roleIds);
    }

    @Override
    public List<RoleDTO> getAll(String name) {
        List<RoleDTO> all = roleManager.listAll(name);
        //查询当前用户本级及下级的
        UserLoginAO.Department department = UserUtil.getDepartment();
        if (Objects.isNull(department)) {
            throw new ServiceException("当前用户信息不存在组织机构！");
        }
        Set<String> currentUserDepartmentIds = departmentService.getCurrentAndAllSubDepartmentId(department.getDeptCode());
        Set<String> roleIds = departmentRoleManager.listRoleIdByDeptIds(currentUserDepartmentIds);
        List<RoleDTO> roles = roleManager.getDtoByIds(roleIds);
        if (StrUtil.isNotBlank(name)) {
            roles.retainAll(all);
        }
        roles.forEach(role -> role.setDepartmentTypes(departmentRoleService.listDepartmentTypeByRoleId(Collections.singleton(role.getId()))));
        return CollectionUtil.isNotEmpty(roleIds) ? roles : Collections.emptyList();
    }

    @Override
    public List<RoleDTO> listAll(String keyword) {
        List<RoleDTO> all = roleManager.listAll(keyword);
        return all;
    }

    @Override
    public void removeUserRole(UserDeptRoleDTO param) {
        // 校验当前移除用户是否有且只有一个部门角色
        Long count = userDepartmentManager.lambdaQuery().eq(UserDepartment::getUserId, param.getUserId()).count();
        if (count == 1) {
            throw new ServiceException("禁止删除：当前用户仅有一个角色，删除将导致该用户无法登录！");
        }
        //移除用户角色,保留用户部门信息
        userDepartmentService.removeUserRole(param);
    }

    @Override
    public UserRoleVO checkUserDeptRole(String userId, String deptId) {
        List<UserDepartmentDTO> res = userDepartmentService.getByUserIdAndDeptId(userId, deptId);
        log.info("用户:{},部门:{}查询角色信息：{}", userId, deptId, res);
        if (CollectionUtil.isEmpty(res)) {
            return new UserRoleVO().setCanAdd(true);
        }
        return new UserRoleVO().setCanAdd(false)
                .setUserId(userId)
                .setDeptId(deptId)
                .setRoleId(res.get(0).getRoleId())
                .setRoleName(roleManager.getDtoById(res.get(0).getRoleId()).getName())
                .setDeptName(departmentService.getDepartmentById(deptId).getDeptName())
                .setDeptId(deptId);
    }

    @Override
    public RoleDTO getDetail(String id) {
        RoleDTO role = roleManager.getDtoById(id);
        if (role == null) {
            log.error("role id not exist");
            throw new ServiceException("角色id错误，获取详情信息失败！");
        }
        // 获取角色用户ids
        Set<String> roleId = Sets.newHashSet();
        roleId.add(id);
//        // 获取用户信息
//        UserService userService = SpringUtil.getBean(UserService.class);
//        List<UserDTO> users = userService.getUserByRoleIds(roleId);
//        Map<String, UserDTO> userMap = users.stream().collect(Collectors.toMap(UserDTO::getId, Function.identity()));
//        List<RoleDTO.UserInfo> userList = new ArrayList<>();
//        List<UserDepartmentDTO> byRoleIds = userDepartmentService.getByRoleIds(roleId);
//        for (UserDepartmentDTO userDepartmentDTO : byRoleIds) {
//            UserDTO userDTO = userMap.get(userDepartmentDTO.getUserId());
//            if (userDTO != null) {
//                RoleDTO.UserInfo userInfo = RoleConvert.INSTANCE.userDTOToInfo(userDTO);
//                userInfo.setDepartmentId(userDepartmentDTO.getDepartmentId());
//                userInfo.setDepartmentLevelCode(userDepartmentDTO.getDepartmentLevelCode());
//                userInfo.setDepartmentName(departmentService.getDepartmentById(userDepartmentDTO.getDepartmentId()).getDeptName());
//                userList.add(userInfo);
//            }
//        }
//        role.setUserList(userList);

        // 获取机构信息
//        Set<String> departmentIds = departmentRoleService.listDepartmentIdByRoleId(roleId);
//        if (CollectionUtil.isNotEmpty(departmentIds)) {
//            role.setDepartmentList(departmentService.listByIds(departmentIds));
//        }
        role.setDepartmentTypes(departmentRoleService.listDepartmentTypeByRoleId(roleId));

        // 获取资源组信息
        Set<String> roleIds = new HashSet<>();
        roleIds.add(id);
        List<RoleResourceGroupDTO> resourceGroupDTOS = roleResourceGroupRefService.getByRoleIds(roleIds);
        if (CollUtil.isNotEmpty(resourceGroupDTOS)) {
            // 默认返回第一个
            RoleResourceGroupDTO roleResourceGroupDTO = resourceGroupDTOS.get(0);
            role.setResourceGroupId(roleResourceGroupDTO.getGroupId());
        }
        return role;
    }

    @Override
    public Paging<RoleDetailVO.UserInfo> roleUserPage(String id, Integer current, Integer size, String keyWord) {
        // 优化获取角色关联的用户列表
        List<RoleDetailVO.UserInfo> userInfoList = new ArrayList<>();
        Paging<UserDepartmentDTO> userDepartmentDTOPaging = userDepartmentManager.getUserDeptInfoByRoleIdPage(id, current, size, keyWord);
        userDepartmentDTOPaging.getRecords().forEach(user -> {
            RoleDetailVO.UserInfo userInfo = new RoleDetailVO.UserInfo();
            userInfo.setId(user.getUserId());
            userInfo.setAccount(user.getAccount());
            userInfo.setRealname(user.getRealname());
            userInfo.setPhone(user.getPhone());
            userInfo.setCreateTime(user.getCreateTime());
            userInfo.setDepartmentName(user.getDepartmentName());
            userInfo.setDepartmentId(user.getDepartmentId());
            userInfo.setDepartmentLevelCode(user.getDepartmentLevelCode());
            userInfo.setCreateBy(user.getCreateBy());
            userInfoList.add(userInfo);
        });
        return new Paging<RoleDetailVO.UserInfo>()
                .setTotal(userDepartmentDTOPaging.getTotal())
                .setCurrent(userDepartmentDTOPaging.getCurrent())
                .setSize(userDepartmentDTOPaging.getSize())
                .setPages(userDepartmentDTOPaging.getPages())
                .setRecords(userInfoList);

    }

    @Override
    public void addUserRole(UserDeptRoleDTO param) {
        // 查询用户与部门关系是否存在
        UserDepartment userDepartment = userDepartmentManager.lambdaQuery().eq(UserDepartment::getUserId, param.getUserId())
                .eq(UserDepartment::getDepartmentId, param.getDepartmentId()).one();
        if (userDepartment == null) {
            // 添加用户与部门关系
            throw new ServiceException("用户与部门关系不存在!");
        }
        if (ObjectUtil.equal(userDepartment.getRoleId(), param.getRoleId())) {
            throw new ServiceException("该用户在该组织机构下已存在相同角色!");
        }
        // 为当前部门用户添加角色
        userDepartment.setRoleId(param.getRoleId());
        userDepartmentManager.updateById(userDepartment);

    }

    @Override
    public void initRoleMenu() {
        List<RoleDTO> roles = roleManager.listAll(null);
        if (CollUtil.isEmpty(roles)) {
            return;
        }

        ApiMenu api = SpringUtil.getBean(ApiMenu.class);
        Result<List<MenuAO>> result = api.allMenu();
        if (!result.getSuccess()) {
            log.error("Api request failed, message: {}, detail message:{}", result.getMessage(), result.getStack());
            throw new ServiceException(result.getMessage());
        }
        List<MenuAO> menus = result.getData();
        if (CollUtil.isEmpty(menus)) {
            return;
        }
        roleMenuService.batchAddRoleMenu(roles.stream().map(RoleDTO::getId).collect(Collectors.toSet()), menus.stream().map(MenuAO::getId).collect(
                Collectors.toSet()));
    }

    @Override
    public RoleDTO getOrAdd(RoleDTO dto) {
        RoleDTO role;
        // 检查角色是否已经存在
        role = roleManager.getByName(dto.getName());
        if (role != null) {
            return role;
        }
        // 获取角色编码
        String code = getRoleNextCode();
        dto.setCode(code);
        boolean success = roleManager.add(dto);
        if (!success) {
            log.error("角色保存失败：{}", dto);
        }

        return dto;
    }

    @Override
    public Paging<RolePageVO> getPage(RolePageParam param) {
        Paging<RolePageVO> page = this.roleManager.getPage(param);
        if (page == null || page.getRecords() == null || page.getRecords().isEmpty()) {
            return page;
        }
        page.getRecords().forEach(role -> {
            Set<String> departmentTypes = departmentRoleService.listDepartmentTypeByRoleId(Collections.singleton(role.getId()));
            role.setDepartmentTypes(departmentTypes);
        });
        return page;
    }

    @Override
    public void initMobileRoleMenu() {
        List<RoleDTO> roles = roleManager.listAll(null);
        if (CollUtil.isEmpty(roles)) {
            return;
        }

        ApiMenu api = SpringUtil.getBean(ApiMenu.class);
        Result<List<MenuAO>> result = api.allMenu();
        if (!result.getSuccess()) {
            log.error("Api request failed, message: {}, detail message:{}", result.getMessage(), result.getStack());
            throw new ServiceException(result.getMessage());
        }
        List<MenuAO> menus = result.getData();
        if (CollUtil.isEmpty(menus)) {
            return;
        }
        menus = menus.stream().filter(menu -> MenuType.MOBILE == menu.getMenuType()).collect(Collectors.toList());
        roleMenuService.batchAddMobileRoleMenu(roles.stream().map(RoleDTO::getId).collect(Collectors.toSet()), menus.stream().map(MenuAO::getId).collect(
                Collectors.toSet()));
    }

    @Override
    public void specifiedMenuForAllRole(List<String> menuIds) {
        List<RoleDTO> roles = roleManager.listAll(null);
        if (CollUtil.isEmpty(roles)) {
            return;
        }
        if (CollUtil.isEmpty(menuIds)) {
            return;
        }
        ApiMenu api = SpringUtil.getBean(ApiMenu.class);
        Result<List<MenuAO>> result = api.listMenus(new HashSet<>(menuIds));
        if (!result.getSuccess()) {
            log.error("Api request failed, message: {}, detail message:{}", result.getMessage(), result.getStack());
            throw new ServiceException(result.getMessage());
        }
        List<MenuAO> menus = result.getData();
        if (CollUtil.isEmpty(menus)) {
            return;
        }
        roleMenuService.batchAddMobileRoleMenu(roles.stream().map(RoleDTO::getId).collect(Collectors.toSet()), menus.stream().map(MenuAO::getId).collect(
                Collectors.toSet()));
    }
}
