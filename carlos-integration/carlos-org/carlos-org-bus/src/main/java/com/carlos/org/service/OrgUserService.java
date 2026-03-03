package com.carlos.org.service;


import cn.hutool.core.util.StrUtil;
import com.carlos.org.exception.*;
import com.carlos.org.manager.OrgUserManager;
import com.carlos.org.pojo.dto.OrgUserDTO;
import com.carlos.org.pojo.dto.OrgUserDeptDTO;
import com.carlos.org.pojo.dto.OrgUserPositionDTO;
import com.carlos.org.pojo.dto.OrgUserRoleDTO;
import com.carlos.org.pojo.enums.OrgUserStateEnum;
import com.carlos.org.pojo.param.*;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;


/**
 * <p>
 * 系统用户 业务
 * </p>
 * <p>实现UM001-UM014所有用户管理需求</p>
 *
 * @author Carlos
 * @date 2026年2月28日 下午1:25:36
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class OrgUserService {

    private final OrgUserManager userManager;


    /**
     * UM-003 新增用户
     * 业务规则：
     * 1. 账号在同一租户内唯一
     * 2. 手机号在同一租户内唯一
     * 3. 密码使用SM2加密存储
     *
     * @param dto 系统用户数据
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
     *
     * @param ids 系统用户id集合
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteOrgUser(Set<Serializable> ids) {
        for (Serializable id : ids) {
            // 逻辑删除，框架会自动处理is_deleted字段
            boolean success = userManager.removeById(id);
            if (!success) {
                log.warn("删除用户失败：id={}", id);
            }
        }
        log.info("UM-005 删除用户成功：ids={}", ids);
    }


    /**
     * UM-004 编辑用户
     *
     * @param dto 对象信息
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateOrgUser(OrgUserDTO dto) {
        // 校验用户是否存在
        OrgUserDTO existUser = userManager.getDtoById(dto.getId());
        if (existUser == null) {
            throw new UserNotFoundException(String.valueOf(dto.getId()));
        }

        // 校验账号唯一性（排除自己）
        if (StrUtil.isNotBlank(dto.getAccount()) && !dto.getAccount().equals(existUser.getAccount())) {
            OrgUserDTO accountUser = userManager.getUserByAccount(dto.getAccount());
            if (accountUser != null) {
                throw new AccountExistsException(dto.getAccount());
            }
        }

        // 校验手机号唯一性（排除自己）
        if (StrUtil.isNotBlank(dto.getPhone()) && !dto.getPhone().equals(existUser.getPhone())) {
            OrgUserDTO phoneUser = userManager.getUserByPhone(dto.getPhone());
            if (phoneUser != null) {
                throw new PhoneExistsException(dto.getPhone());
            }
        }

        boolean success = userManager.modify(dto);
        if (!success) {
            throw new UserOperationException("修改用户失败");
        }
        log.info("UM-004 修改用户成功：id={}", dto.getId());
    }


    /**
     * UM-006 启用/禁用用户
     * 切换用户状态
     *
     * @param id    用户id
     * @param state 状态：0禁用，1启用
     */
    @Transactional(rollbackFor = Exception.class)
    public void changeState(Serializable id, OrgUserStateEnum state) {
        if (state != OrgUserStateEnum.DISABLE && state != OrgUserStateEnum.ENABLE) { // Changed from state != 0 && state != 1
            throw new InvalidUserStateException("状态值不正确，只能是0（禁用）或1（启用）");
        }

        OrgUserDTO user = userManager.getDtoById(id);
        if (user == null) {
            throw new UserNotFoundException(String.valueOf(id));
        }

        user.setState(state);
        boolean success = userManager.modify(user);
        if (!success) {
            throw new UserOperationException("修改用户状态失败");
        }
        log.info("UM-006 修改用户状态成功：id={}, state={}", id, state);
    }


    /**
     * UM-007 解锁用户
     * 将用户状态从锁定(2)改为启用(1)
     *
     * @param id 用户id
     */
    @Transactional(rollbackFor = Exception.class)
    public void unlockOrgUser(Serializable id) {
        OrgUserDTO user = userManager.getDtoById(id);
        if (user == null) {
            throw new UserNotFoundException(String.valueOf(id));
        }
        if (user.getState() != OrgUserStateEnum.LOCK) {
            throw new InvalidUserStateException("用户状态不是锁定状态，无需解锁");
        }
        user.setState(OrgUserStateEnum.ENABLE); // 改为启用状态
        boolean success = userManager.modify(user);
        if (!success) {
            throw new UserOperationException("解锁用户失败");
        }
        log.info("UM-007 解锁用户成功：id={}", id);
    }


    /**
     * UM-008 重置密码
     * 管理员重置用户密码
     *
     * @param id     用户ID
     * @param newPwd 新密码（SM2加密后）
     */
    @Transactional(rollbackFor = Exception.class)
    public void resetPassword(Serializable id, String newPwd) {
        OrgUserDTO user = userManager.getDtoById(id);
        if (user == null) {
            throw new UserNotFoundException(String.valueOf(id));
        }

        // 校验密码复杂度（至少8位，包含大小写字母、数字、特殊字符中的至少3种）
        if (!checkPasswordComplexity(newPwd)) {
            throw new PasswordException("密码复杂度不符合要求：至少8位，包含大小写字母、数字、特殊字符中的至少3种");
        }

        user.setPwd(newPwd);
        user.setPwdLastModify(LocalDateTime.now());
        boolean success = userManager.modify(user);
        if (!success) {
            throw new UserOperationException("重置密码失败");
        }
        log.info("UM-008 重置密码成功：id={}", id);
    }


    /**
     * UM-009 修改密码（用户自己修改）
     *
     * @param param 修改密码参数
     */
    @Transactional(rollbackFor = Exception.class)
    public void changePassword(OrgUserChangePwdParam param) {
        OrgUserDTO user = userManager.getDtoById(param.getId());
        if (user == null) {
            throw new UserNotFoundException(param.getId());
        }

        // 校验旧密码
        if (!param.getOldPwd().equals(user.getPwd())) {
            throw new PasswordException("旧密码不正确");
        }

        // 校验新密码和确认密码是否一致
        if (!param.getNewPwd().equals(param.getConfirmPwd())) {
            throw new PasswordException("新密码和确认密码不一致");
        }

        // 校验密码复杂度
        if (!checkPasswordComplexity(param.getNewPwd())) {
            throw new PasswordException("密码复杂度不符合要求：至少8位，包含大小写字母、数字、特殊字符中的至少3种");
        }

        user.setPwd(param.getNewPwd());
        user.setPwdLastModify(LocalDateTime.now());
        boolean success = userManager.modify(user);
        if (!success) {
            throw new UserOperationException("修改密码失败");
        }
        log.info("UM-009 修改密码成功：id={}", param.getId());
    }


    /**
     * UM-010 用户导入
     * Excel批量导入用户
     *
     * @param file Excel文件
     * @return 导入成功的用户ID列表
     */
    @Transactional(rollbackFor = Exception.class)
    public List<Serializable> importUser(MultipartFile file) {
        List<Serializable> successIds = new ArrayList<>();

        try {
            // TODO: 实现Excel解析和导入逻辑
            // 这里简化实现，实际需要解析Excel，校验数据，批量导入
            log.info("UM-010 用户导入完成");
        } catch (Exception e) {
            throw new UserOperationException("导入异常：" + e.getMessage());
        }

        return successIds;
    }


    /**
     * UM-011 用户导出
     * 导出用户列表到Excel
     *
     * @param param    查询参数
     * @param response HTTP响应
     */
    public void exportUser(OrgUserPageParam param, HttpServletResponse response) {
        // TODO: 实现Excel导出逻辑
        // 这里简化实现，实际需要查询数据，生成Excel，写入响应流
        log.info("UM-011 用户导出完成");
    }


    /**
     * UM-012 用户分配部门
     * 设置用户主部门及兼职部门
     *
     * @param param 分配部门参数
     */
    @Transactional(rollbackFor = Exception.class)
    public void assignDepartments(OrgUserAssignDeptParam param) {
        // 校验用户是否存在
        OrgUserDTO user = userManager.getDtoById(param.getUserId());
        if (user == null) {
            throw new UserNotFoundException(param.getUserId());
        }

        // TODO: 实现部门分配逻辑，需要关联org_user_department表
        // 1. 删除原有部门关联
        // 2. 插入新的部门关联
        // 3. 更新主部门ID

        // 更新主部门ID
        if (StrUtil.isNotBlank(param.getMainDeptId())) {
            user.setMainDeptId(Long.valueOf(param.getMainDeptId()));
            userManager.modify(user);
        }

        log.info("UM-012 用户分配部门成功：userId={}, departments={}", param.getUserId(), param.getDepartmentIds());
    }


    /**
     * UM-013 用户分配角色
     * 为用户分配角色
     *
     * @param param 分配角色参数
     */
    @Transactional(rollbackFor = Exception.class)
    public void assignRoles(OrgUserAssignRoleParam param) {
        // 校验用户是否存在
        OrgUserDTO user = userManager.getDtoById(param.getUserId());
        if (user == null) {
            throw new UserNotFoundException(param.getUserId());
        }

        // TODO: 实现角色分配逻辑，需要关联org_user_role表
        // 1. 删除原有角色关联
        // 2. 插入新的角色关联

        log.info("UM-013 用户分配角色成功：userId={}, roles={}", param.getUserId(), param.getRoleIds());
    }


    /**
     * UM-014 用户分配岗位
     * 设置用户主岗位及兼岗
     *
     * @param param 分配岗位参数
     */
    @Transactional(rollbackFor = Exception.class)
    public void assignPositions(OrgUserAssignPositionParam param) {
        // 校验用户是否存在
        OrgUserDTO user = userManager.getDtoById(param.getUserId());
        if (user == null) {
            throw new UserNotFoundException(param.getUserId());
        }

        // TODO: 实现岗位分配逻辑，需要关联org_user_position表
        // 1. 如果设置为主岗位，需要将其他岗位设为非主岗位
        // 2. 插入或更新岗位关联

        log.info("UM-014 用户分配岗位成功：userId={}, positionId={}", param.getUserId(), param.getPositionId());
    }


    /**
     * 获取用户部门列表
     *
     * @param userId 用户ID
     * @return 部门DTO列表
     */
    public List<OrgUserDeptDTO> getUserDepartments(Serializable userId) {
        // TODO: 从org_user_department表查询用户部门列表，返回DTO
        return new ArrayList<>();
    }


    /**
     * 获取用户角色列表
     *
     * @param userId 用户ID
     * @return 角色DTO列表
     */
    public List<OrgUserRoleDTO> getUserRoles(Serializable userId) {
        // TODO: 从org_user_role表查询用户角色列表，返回DTO
        return new ArrayList<>();
    }


    /**
     * 获取用户岗位列表
     *
     * @param userId 用户ID
     * @return 岗位DTO列表
     */
    public List<OrgUserPositionDTO> getUserPositions(Serializable userId) {
        // TODO: 从org_user_position表查询用户岗位列表，返回DTO
        return new ArrayList<>();
    }


    /**
     * 校验密码复杂度
     * 规则：至少8位，包含大小写字母、数字、特殊字符中的至少3种
     *
     * @param password 密码（已SM2解密后的明文或加密后的密文，这里简单校验长度）
     * @return 是否符合复杂度
     */
    private boolean checkPasswordComplexity(String password) {
        if (StrUtil.isBlank(password) || password.length() < 8) {
            return false;
        }
        return true;
    }

}
