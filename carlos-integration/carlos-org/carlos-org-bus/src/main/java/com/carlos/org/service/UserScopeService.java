package com.carlos.org.service;

import cn.hutool.core.collection.CollectionUtil;
import com.carlos.boot.util.ExtendInfoUtil;
import com.carlos.core.exception.ServiceException;
import com.carlos.core.response.Result;
import com.carlos.org.manager.UserManager;
import com.carlos.org.manager.UserRoleManager;
import com.carlos.org.pojo.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;


/**
 * <p>
 * 用户数据权限服务
 * </p>
 *
 * @author Carlos
 * @date 2022/11/23 12:32
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserScopeService {

    private final UserRoleManager userRoleManager;
    private final DepartmentService departmentService;
    private final UserDepartmentService userDepartmentService;
    private final UserManager userManager;


    /**
     * 获取当前角色的所有用户id
     *
     * @return java.util.Set<java.io.Serializable>
     * @author Carlos
     * @date 2022/11/23 12:50
     */
    public Set<Serializable> getCurrentRoleUserId() {
        Serializable roleId = ExtendInfoUtil.getRoleId();
        return this.userRoleManager.listUserIdByRole(roleId);
    }


    /**
     * 获取当前部门下所有用户id
     *
     * @return java.util.Set<java.io.Serializable>
     * @author Carlos
     * @date 2022/11/23 12:51
     */
    public Set<Serializable> getCurrentDeptUserId() {
        Serializable departmentId = ExtendInfoUtil.getDepartmentId();
        Set<String> userIds = this.userDepartmentService.getUserIdByDepartmentId(String.valueOf(departmentId));
        if (CollectionUtil.isNotEmpty(userIds)) {
            return new HashSet<>(userIds);
        }
        return null;
    }


    /**
     * 获取当前部门及子部门所有用户id
     *
     * @return java.util.Set<java.io.Serializable>
     * @author Carlos
     * @date 2022/11/23 13:39
     */
    public Set<Serializable> getCurrentDeptAllUserId(Serializable departmentId) {
        if (departmentId == null) {
            departmentId = ExtendInfoUtil.getDepartmentId();
        }
        Set<Serializable> deptIds = this.departmentService.getAllSubDepartmentId(departmentId);
        deptIds.add(departmentId);
        return this.userDepartmentService.getUserIdByDepartmentId(deptIds);
    }


    /**
     * 获取当前用户部门及子部门id
     *
     * @return java.util.Set<java.io.Serializable>
     * @author Carlos
     * @date 2022/12/13 15:30
     */
    public Set<Serializable> getCurrentDeptTreeIds(Serializable departmentId) {
        if (departmentId == null) {
            departmentId = ExtendInfoUtil.getDepartmentId();
        }
        Set<Serializable> deptIds = this.departmentService.getAllSubDepartmentId(departmentId);
        deptIds.add(departmentId);
        return deptIds;
    }

    /**
     * 获取用户当前区域及所有子区域的区域编码
     *
     * @return java.util.Set<java.io.Serializable>
     * @author Carlos
     * @date 2022/12/13 15:34
     */
    public Set<String> getCurrentRegionTreeIds() {
        Serializable userId = ExtendInfoUtil.getUserId();
        UserDTO user = this.userManager.getDtoById(userId);
        // String regionCode = user.getDepartmentInfo().getRegionCode();
        // if (StrUtil.isBlank(regionCode)) {
        //     return null;
        // }
        // Result<Set<String>> result = this.feignRegion.getSubRegionCodes(regionCode);
        Result<Set<String>> result = null;
        if (Boolean.FALSE.equals(result.getSuccess())) {
            log.error("Feign request failed, message: {}, detail message:{}", result.getMessage(), result.getStack());
            throw new ServiceException(result.getMessage());
        }
        return result.getData();

    }
}
