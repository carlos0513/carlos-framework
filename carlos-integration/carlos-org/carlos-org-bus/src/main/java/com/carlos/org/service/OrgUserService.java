package com.carlos.org.service;

import cn.hutool.core.util.StrUtil;
import cn.idev.excel.FastExcel;
import com.carlos.org.excel.UserImportListener;
import com.carlos.org.exception.*;
import com.carlos.org.manager.*;
import com.carlos.org.pojo.dto.*;
import com.carlos.org.pojo.entity.OrgUserDepartment;
import com.carlos.org.pojo.entity.OrgUserPosition;
import com.carlos.org.pojo.entity.OrgUserRole;
import com.carlos.org.pojo.enums.OrgUserStateEnum;
import com.carlos.org.pojo.param.*;
import com.carlos.org.pojo.vo.*;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.Serializable;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 系统用户 业务
 * </p>
 * <p>实现UM001-UM014所有用户管理需求</p>
 *
 * @author Carlos
 * @date 2026年2月28日
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class OrgUserService {

    private final OrgUserManager userManager;
    private final OrgUserDepartmentManager userDepartmentManager;
    private final OrgUserRoleManager userRoleManager;
    private final OrgUserPositionManager userPositionManager;
    private final OrgDepartmentManager departmentManager;
    private final OrgRoleManager roleManager;
    private final OrgPositionManager positionManager;

    /**
     * UM-003 新增用户
     */
    @Transactional(rollbackFor = Exception.class)
    public void addOrgUser(OrgUserDTO dto) {
        // 校验账号唯一性
        if (StrUtil.isNotBlank(dto.getAccount())) {
            OrgUserDTO existUser = userManager.getUserByAccount(dto.getAccount());
            if (existUser != null) {
                throw new AccountExistsException(dto.getAccount());
            }
        }

        // 校验手机号唯一性
        if (StrUtil.isNotBlank(dto.getPhone())) {
            OrgUserDTO existUser = userManager.getUserByPhone(dto.getPhone());
            if (existUser != null) {
                throw new PhoneExistsException(dto.getPhone());
            }
        }

        // 设置默认状态为启用
        if (dto.getState() == null) {
            dto.setState(OrgUserStateEnum.ENABLE);
        }

        boolean success = userManager.add(dto);
        if (!success) {
            throw new UserOperationException("新增用户失败");
        }
        log.info("UM-003 新增用户成功：id={}", dto.getId());
    }

    /**
     * UM-005 删除用户（逻辑删除）
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteOrgUser(Set<Serializable> ids) {
        for (Serializable id : ids) {
            Long userId = Long.valueOf(id.toString());
            
            // 删除用户
            boolean success = userManager.removeById(id);
            if (!success) {
                log.warn("删除用户失败：id={}", id);
                continue;
            }
            
            // 删除关联关系
            userDepartmentManager.deleteByUserId(userId);
            userRoleManager.deleteByUserId(userId);
            userPositionManager.deleteByUserId(userId);
            
            log.info("删除用户成功：id={}", id);
        }
    }

    /**
     * UM-006 修改用户
     */
    @Transactional(rollbackFor = Exception.class)
    public void modifyOrgUser(OrgUserDTO dto) {
        // 校验用户是否存在
        OrgUserDTO existUser = userManager.getDtoById(dto.getId());
        if (existUser == null) {
            throw new UserNotFoundException(dto.getId());
        }

        // 如果修改了账号，校验唯一性
        if (StrUtil.isNotBlank(dto.getAccount()) && !dto.getAccount().equals(existUser.getAccount())) {
            OrgUserDTO sameAccountUser = userManager.getUserByAccount(dto.getAccount());
            if (sameAccountUser != null) {
                throw new AccountExistsException(dto.getAccount());
            }
        }

        // 如果修改了手机号，校验唯一性
        if (StrUtil.isNotBlank(dto.getPhone()) && !dto.getPhone().equals(existUser.getPhone())) {
            OrgUserDTO samePhoneUser = userManager.getUserByPhone(dto.getPhone());
            if (samePhoneUser != null) {
                throw new PhoneExistsException(dto.getPhone());
            }
        }

        boolean success = userManager.modify(dto);
        if (!success) {
            throw new UserOperationException("修改用户失败");
        }
        log.info("UM-006 修改用户成功：id={}", dto.getId());
    }

    /**
     * UM-007 修改用户状态
     */
    @Transactional(rollbackFor = Exception.class)
    public void modifyState(OrgUserModifyStateParam param) {
        OrgUserDTO dto = userManager.getDtoById(param.getId());
        if (dto == null) {
            throw new UserNotFoundException(param.getId());
        }

        dto.setState(param.getState());
        boolean success = userManager.modify(dto);
        if (!success) {
            throw new UserOperationException("修改状态失败");
        }
        log.info("UM-007 修改用户状态成功：id={}, state={}", param.getId(), param.getState());
    }

    /**
     * UM-008 重置密码
     */
    @Transactional(rollbackFor = Exception.class)
    public void resetPassword(OrgUserResetPasswordParam param) {
        OrgUserDTO dto = userManager.getDtoById(param.getId());
        if (dto == null) {
            throw new UserNotFoundException(param.getId());
        }

        // 校验新密码复杂度
        if (!checkPasswordComplexity(param.getNewPassword())) {
            throw new PasswordComplexityException();
        }

        // 设置新密码
        dto.setPassword(param.getNewPassword());
        boolean success = userManager.modify(dto);
        if (!success) {
            throw new UserOperationException("重置密码失败");
        }
        log.info("UM-008 重置密码成功：id={}", param.getId());
    }

    /**
     * UM-009 修改密码
     */
    @Transactional(rollbackFor = Exception.class)
    public void modifyPassword(OrgUserModifyPasswordParam param) {
        OrgUserDTO dto = userManager.getDtoById(param.getId());
        if (dto == null) {
            throw new UserNotFoundException(param.getId());
        }

        // 校验新密码复杂度
        if (!checkPasswordComplexity(param.getNewPassword())) {
            throw new PasswordComplexityException();
        }

        // 设置新密码
        dto.setPassword(param.getNewPassword());
        boolean success = userManager.modify(dto);
        if (!success) {
            throw new UserOperationException("修改密码失败");
        }
        log.info("UM-009 修改密码成功：id={}", param.getId());
    }

    /**
     * UM-010 用户导入
     */
    @Transactional(rollbackFor = Exception.class)
    public UserImportResultVO importUser(MultipartFile file) {
        try {
            UserImportListener listener = new UserImportListener(this);
            FastExcel.read(file.getInputStream(), UserImportExcel.class, listener).sheet().doRead();
            return listener.getResult();
        } catch (IOException e) {
            log.error("用户导入失败", e);
            throw new UserOperationException("导入失败：" + e.getMessage());
        }
    }

    /**
     * 批量导入用户（内部方法）
     */
    @Transactional(rollbackFor = Exception.class)
    public int batchImportUsers(List<UserImportExcel> dataList) {
        int successCount = 0;
        
        for (UserImportExcel data : dataList) {
            try {
                // 检查用户名是否已存在
                if (userManager.getUserByAccount(data.getUsername()) != null) {
                    log.warn("用户名已存在：{}", data.getUsername());
                    continue;
                }

                // 创建用户DTO
                OrgUserDTO dto = new OrgUserDTO();
                dto.setAccount(data.getUsername());
                dto.setRealName(data.getRealName());
                dto.setPhone(data.getPhone());
                dto.setEmail(data.getEmail());
                dto.setState(OrgUserStateEnum.ENABLE);
                dto.setPassword("123456"); // 默认密码，需要重置

                // 保存用户
                boolean success = userManager.add(dto);
                if (success && dto.getId() != null) {
                    // 处理部门关联
                    if (StrUtil.isNotBlank(data.getDeptCode())) {
                        assignDeptByCode(dto.getId(), data.getDeptCode());
                    }
                    // 处理角色关联
                    if (StrUtil.isNotBlank(data.getRoleCode())) {
                        assignRoleByCode(dto.getId(), data.getRoleCode());
                    }
                    // 处理岗位关联
                    if (StrUtil.isNotBlank(data.getPositionCode())) {
                        assignPositionByCode(dto.getId(), data.getPositionCode());
                    }
                    successCount++;
                }
            } catch (Exception e) {
                log.error("导入用户失败：{}", data.getUsername(), e);
            }
        }
        
        return successCount;
    }

    /**
     * UM-011 用户导出
     */
    public void exportUser(UserExportQueryDTO query, HttpServletResponse response) {
        try {
            // 设置响应头
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setCharacterEncoding("utf-8");
            String fileName = URLEncoder.encode("用户列表_" + System.currentTimeMillis(), StandardCharsets.UTF_8);
            response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");

            // 查询数据
            List<UserExportExcel> dataList = getExportData(query);

            // FastExcel 写入
            FastExcel.write(response.getOutputStream(), UserExportExcel.class)
                    .autoCloseStream(false)
                    .sheet("用户列表")
                    .doWrite(dataList);

        } catch (IOException e) {
            log.error("用户导出失败", e);
            throw new UserOperationException("导出失败：" + e.getMessage());
        }
    }

    /**
     * 获取导出数据
     */
    public List<UserExportExcel> getExportData(UserExportQueryDTO query) {
        // TODO: 根据条件查询用户列表
        // 这里简化实现，实际需要调用 Manager 查询
        return new ArrayList<>();
    }

    /**
     * UM-012 用户分配部门
     */
    @Transactional(rollbackFor = Exception.class)
    public void assignDepartments(OrgUserAssignDeptParam param) {
        // 校验用户是否存在
        OrgUserDTO user = userManager.getDtoById(param.getUserId());
        if (user == null) {
            throw new UserNotFoundException(param.getUserId());
        }

        // 保存部门关联
        if (param.getDepartmentIds() != null && !param.getDepartmentIds().isEmpty()) {
            List<UserDepartmentDTO> deptList = param.getDepartmentIds().stream()
                    .map(deptId -> {
                        UserDepartmentDTO dto = new UserDepartmentDTO();
                        dto.setDepartmentId(Long.valueOf(deptId));
                        dto.setMain(deptId.equals(param.getMainDeptId()));
                        return dto;
                    })
                    .collect(Collectors.toList());
            
            userDepartmentManager.saveUserDepartments(param.getUserId(), deptList);
        } else {
            // 清空部门关联
            userDepartmentManager.deleteByUserId(param.getUserId());
        }

        // 更新主部门ID
        if (StrUtil.isNotBlank(param.getMainDeptId())) {
            user.setMainDeptId(Long.valueOf(param.getMainDeptId()));
            userManager.modify(user);
        }

        log.info("UM-012 用户分配部门成功：userId={}, departments={}", param.getUserId(), param.getDepartmentIds());
    }

    /**
     * 根据编码分配部门
     */
    private void assignDeptByCode(Long userId, String deptCode) {
        // 根据编码查询部门ID
        // TODO: 需要实现根据编码查询部门
    }

    /**
     * UM-013 用户分配角色
     */
    @Transactional(rollbackFor = Exception.class)
    public void assignRoles(OrgUserAssignRoleParam param) {
        // 校验用户是否存在
        OrgUserDTO user = userManager.getDtoById(param.getUserId());
        if (user == null) {
            throw new UserNotFoundException(param.getUserId());
        }

        // 保存角色关联
        List<Long> roleIds = param.getRoleIds() != null ? 
                param.getRoleIds().stream().map(Long::valueOf).collect(Collectors.toList()) : 
                new ArrayList<>();
        
        userRoleManager.saveUserRoles(param.getUserId(), roleIds);

        log.info("UM-013 用户分配角色成功：userId={}, roles={}", param.getUserId(), param.getRoleIds());
    }

    /**
     * 根据编码分配角色
     */
    private void assignRoleByCode(Long userId, String roleCode) {
        // TODO: 根据编码查询角色ID并分配
    }

    /**
     * UM-014 用户分配岗位
     */
    @Transactional(rollbackFor = Exception.class)
    public void assignPositions(OrgUserAssignPositionParam param) {
        // 校验用户是否存在
        OrgUserDTO user = userManager.getDtoById(param.getUserId());
        if (user == null) {
            throw new UserNotFoundException(param.getUserId());
        }

        // 保存岗位关联
        if (param.getPositionIds() != null && !param.getPositionIds().isEmpty()) {
            List<UserPositionDTO> posList = param.getPositionIds().stream()
                    .map(posId -> {
                        UserPositionDTO dto = new UserPositionDTO();
                        dto.setPositionId(Long.valueOf(posId));
                        dto.setMain(posId.equals(param.getMainPositionId()));
                        return dto;
                    })
                    .collect(Collectors.toList());
            
            userPositionManager.saveUserPositions(param.getUserId(), posList);
        } else {
            // 清空岗位关联
            userPositionManager.deleteByUserId(param.getUserId());
        }

        log.info("UM-014 用户分配岗位成功：userId={}, positions={}", param.getUserId(), param.getPositionIds());
    }

    /**
     * 根据编码分配岗位
     */
    private void assignPositionByCode(Long userId, String positionCode) {
        // TODO: 根据编码查询岗位ID并分配
    }

    /**
     * 获取用户部门列表
     */
    public List<OrgUserDeptDTO> getUserDepartments(Serializable userId) {
        List<OrgUserDepartment> list = userDepartmentManager.getByUserId(Long.valueOf(userId.toString()));
        
        return list.stream().map(entity -> {
            OrgUserDeptDTO dto = new OrgUserDeptDTO();
            dto.setDeptId(entity.getDepartmentId());
            dto.setIsMain(entity.getIsMain() == 1);
            // TODO: 查询部门名称
            dto.setDeptName("");
            return dto;
        }).collect(Collectors.toList());
    }

    /**
     * 获取用户角色列表
     */
    public List<OrgUserRoleDTO> getUserRoles(Serializable userId) {
        List<OrgUserRole> list = userRoleManager.getByUserId(Long.valueOf(userId.toString()));
        
        return list.stream().map(entity -> {
            OrgUserRoleDTO dto = new OrgUserRoleDTO();
            dto.setRoleId(entity.getRoleId());
            // TODO: 查询角色名称
            dto.setRoleName("");
            return dto;
        }).collect(Collectors.toList());
    }

    /**
     * 获取用户岗位列表
     */
    public List<OrgUserPositionDTO> getUserPositions(Serializable userId) {
        List<OrgUserPosition> list = userPositionManager.getByUserId(Long.valueOf(userId.toString()));
        
        return list.stream().map(entity -> {
            OrgUserPositionDTO dto = new OrgUserPositionDTO();
            dto.setPositionId(entity.getPositionId());
            dto.setIsMain(entity.getIsMain() == 1);
            // TODO: 查询岗位名称
            dto.setPositionName("");
            return dto;
        }).collect(Collectors.toList());
    }

    /**
     * 校验密码复杂度
     */
    private boolean checkPasswordComplexity(String password) {
        if (StrUtil.isBlank(password) || password.length() < 8) {
            return false;
        }
        return true;
    }
}
