package com.carlos.org;

import com.alibaba.ttl.TransmittableThreadLocal;
import com.carlos.core.exception.ServiceException;
import com.carlos.core.response.Result;
import com.carlos.org.api.ApiDepartment;
import com.carlos.org.api.ApiUser;
import com.carlos.org.pojo.ao.UserLoginAO;
import com.carlos.org.pojo.ao.UserLoginAO.Department;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.io.Serializable;
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


    public UserUtil(@Nullable ApiUser apiUser, @Nullable ApiDepartment apiDepartment) {
        UserUtil.apiUser = apiUser;

    }


    /**
     * 获取用户id
     *
     * @return java.lang.String
     * @author Carlos
     * @date 2023/7/16 23:42
     */
    public static Serializable getId() {
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
     * @return com.carlos.org.pojo.ao.UserAO
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
