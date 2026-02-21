package com.yunjin.org;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.alibaba.ttl.TransmittableThreadLocal;
import com.yunjin.core.exception.ServiceException;
import com.yunjin.core.response.Result;
import com.yunjin.org.api.ApiDepartment;
import com.yunjin.org.api.ApiResourceGroup;
import com.yunjin.org.api.ApiUser;
import com.yunjin.org.enums.RoleResourceGroupEnum;
import com.yunjin.org.param.ApiResourceGroupParam;
import com.yunjin.org.pojo.ao.DepartmentAO;
import com.yunjin.org.pojo.ao.UserLoginAO;
import com.yunjin.org.pojo.ao.UserLoginAO.Department;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * 用户便捷工具类
 * </p>
 *
 * @author Carlos
 * @date 2023/7/16 23:45
 */
@Component
@Slf4j
public class UserUtil {
    private static final TransmittableThreadLocal<UserLoginAO> LOCAL = new TransmittableThreadLocal<> ();
    private static ApiUser apiUser;
    //    private static ApiDepartment apiDepartment;
    private static ApiResourceGroup apiResourceGroup;

    public UserUtil (@Nullable ApiUser apiUser, @Nullable ApiDepartment apiDepartment, @Nullable ApiResourceGroup apiResourceGroup) {
        UserUtil.apiUser = apiUser;
//        UserUtil.apiDepartment = apiDepartment;
        UserUtil.apiResourceGroup = apiResourceGroup;
    }


    /**
     * 获取用户id
     *
     * @return java.lang.String
     * @author Carlos
     * @date 2023/7/16 23:42
     */
    public static String getId () {
        UserLoginAO user = getUser ();
        return user.getId ();
    }

    /**
     * 获取账号
     *
     * @return java.lang.String
     * @author Carlos
     * @date 2023/7/16 23:42
     */
    public static String getAccount () {
        UserLoginAO user = getUser ();
        return user.getAccount ();
    }

    /**
     * 获取电话号
     *
     * @return java.lang.String
     * @author Carlos
     * @date 2023/7/16 23:42
     */
    public static String getPhone () {
        UserLoginAO user = getUser ();
        return user.getPhone ();
    }

    /**
     * 获取用户真实姓名
     */
    public static String getRealName () {
        UserLoginAO user = getUser ();
        return user.getRealname ();
    }

    /**
     * 获取部门Code
     *
     * @return java.lang.String
     * @author Carlos
     * @date 2023/7/16 23:42
     */
    public static Department getDepartment () {
        UserLoginAO user = getUser ();
        return user.getDepartment ();
    }

    /**
     * 获取区域Code
     *
     * @return java.lang.String
     * @author Carlos
     * @date 2023/7/16 23:42
     */
    public static String getRegionCode () {
        UserLoginAO user = getUser ();
        return user.getRegionCode ();
    }

    /**
     * 获取角色id
     *
     * @return java.util.Set<java.lang.String>
     * @author Carlos
     * @date 2023/7/16 23:41
     */
    public static Set<String> getRoleId () {
        UserLoginAO user = getUser ();
        return user.getRoleIds ();
    }

    /**
     * 获取当前用户信息
     *
     * @return com.yunjin.org.pojo.ao.UserAO
     * @author Carlos
     * @date 2023/7/16 23:40
     */
    public static UserLoginAO getUser () {
        Result<UserLoginAO> result = apiUser.getCurrentUser ();

        if (!result.getSuccess ()) {
            log.error ("Api request failed, message: {}, detail message:{}", result.getMessage (), result.getStack ());
        }
        UserLoginAO data = result.getData ();
        if (data == null) {
            data = LOCAL.get ();
        }
        if (data == null) {
            throw new ServiceException(result.getMessage());
        }
        return data;
    }

    /**
     * @Title: isAdmin
     * @Description: 是否为超级管理员
     * @Date: 2023/4/13 18:13
     * @Parameters: []
     * @Return boolean
     */
    public static boolean isAdmin () {
        UserLoginAO user = getUser ();
        return user.getAdmin ();
    }

    /**
     * @Description: 白名单接口获取当前用户信息
     * @Date: 2023/12/21 18:13
     */

    public static UserLoginAO getUserByWhiteList () {
        Result<UserLoginAO> result = apiUser.getCurrentUserByWhiteList ();

        if (!result.getSuccess ()) {
            log.error ("Api request failed, message: {}, detail message:{}", result.getMessage (), result.getStack ());
            throw new ServiceException (result.getMessage ());
        }
        return result.getData ();
    }

    /**
     * 获取用户id
     *
     * @return java.lang.String
     * @author Carlos
     * @date 2023/7/16 23:42
     */
    public static String getIdByWhiteList () {
        UserLoginAO user = getUserByWhiteList ();
        return user.getId ();
    }

    /**
     * 根据部门id获取所有子级部门code
     *
     * @return
     */
    public static Set<String> allSubDepartmentCodeByParentId (String deptId, boolean addSelf) {
        ApiDepartment apiDepartment = SpringUtil.getBean (ApiDepartment.class);
        Result<Set<String>> result = apiDepartment.allSubDepartmentCode (deptId, addSelf);
        if (!result.getSuccess ()) {
            log.error ("Api request failed, message: {}, detail message:{}", result.getMessage (), result.getStack ());
            throw new ServiceException (result.getMessage ());
        }
        return result.getData ();

    }

    /**
     * 获取当前用户所属顶级机构及其下级部门code集合
     * 系统管理员使用此方法获取顶级机构及其所有下级部门的编码
     *
     * @return 顶级机构及其下级部门编码集合
     */
    public static Set<String> getTopDeptAndSubCodes() {
        UserLoginAO.Department currentDept = getDepartment();
        ApiDepartment apiDepartment = SpringUtil.getBean(ApiDepartment.class);

        // 获取当前部门的顶级部门
        Result<DepartmentAO> result = apiDepartment.parentDepartment(currentDept.getDeptCode());
        if (!result.getSuccess()) {
            log.error("获取顶级部门失败: {}", result.getMessage());
            return Collections.emptySet();
        }

        DepartmentAO topDept = result.getData();
        if (topDept == null) {
            return Collections.emptySet();
        }

        // 获取顶级部门的所有下级部门编码（包括自己）
        return allSubDepartmentCodeByParentId(topDept.getId(), true);
    }

    /**
     * 获取当前用户角色组
     *
     * @return List<RoleResourceGroupEnum>
     */
    public static List<RoleResourceGroupEnum> getCurrentUserRoleResourceGroup () {
        UserLoginAO user = UserUtil.getUser ();
        String roleId = user.getRoleId ();
        Result<List<RoleResourceGroupEnum>> result = apiResourceGroup.getResourceGroupByRoleIds (new ApiResourceGroupParam ().setRoleIds (Collections.singleton (roleId)));
        if (result.getSuccess () && CollUtil.isNotEmpty (result.getData ())) {
            return result.getData ();
        } else {
            log.warn ("获取当前用户角色权限组失败，请检查是否配置角色权限组");
            return null;
        }
    }

    /**
     * 是否为系统管理员角色组
     *
     * @return true
     */
    public static boolean isSysAdminGroup () {
        List<RoleResourceGroupEnum> groups = getCurrentUserRoleResourceGroup ();
        return CollUtil.isNotEmpty (groups) && groups.stream ().anyMatch (RoleResourceGroupEnum.SYSTEM_ADMIN::equals);
    }

    /**
     * 是否为部门管理员角色组
     *
     * @return true
     */
    public static boolean isDeptAdminGroup () {
        List<RoleResourceGroupEnum> groups = getCurrentUserRoleResourceGroup ();
        return CollUtil.isNotEmpty (groups) && groups.stream ().anyMatch (RoleResourceGroupEnum.DEPARTMENT_ADMIN::equals);
    }

    /**
     * 移除线程变量
     */
    public static void clear () {
        LOCAL.remove ();
    }

    /**
     * 设置线程变量 set和 clear 注意要成对出现
     */
    public static void set (UserLoginAO userInfo) {
        LOCAL.set (userInfo);
    }

}
