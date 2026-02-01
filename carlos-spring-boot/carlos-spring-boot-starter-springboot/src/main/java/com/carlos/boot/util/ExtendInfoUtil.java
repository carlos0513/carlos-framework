package com.carlos.boot.util;

import com.carlos.core.auth.UserContext;
import com.carlos.core.exception.ComponentException;
import com.carlos.core.exception.ServiceException;
import com.carlos.core.interfaces.ApplicationExtend;
import com.carlos.core.response.StatusCode;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * <p>
 * 应用扩展信息工具类
 * </p>
 *
 * @author carlos
 * @date 2021/12/28 9:50
 */
@Component
public final class ExtendInfoUtil {

    private static ApplicationExtend extend;

    public ExtendInfoUtil(@Nullable final ApplicationExtend applicationExtend) {
        ExtendInfoUtil.extend = applicationExtend;
    }


    /**
     * 获取当前请求用户id
     *
     * @return java.io.Serializable
     * @author carlos
     * @date 2021/12/28 10:18
     */
    public static Serializable getUserId() {
        if (extend == null) {
            throw new ComponentException("Can't find Bean of 'ApplicationExtend'");
        }
        return extend.getUserId();
    }

    /**
     * 获取角色id
     *
     * @return java.io.Serializable
     * @author Carlos
     * @date 2022/11/14 18:58
     */
    public static Serializable getRoleId() {
        if (extend == null) {
            throw new ComponentException("Can't find Bean of 'ApplicationExtend'");
        }
        final UserContext context = extend.getUserContext();
        final Serializable roleId = context.getRoleId();
        if (roleId == null) {
            throw new ServiceException(StatusCode.NOT_PERMISSION);
        }
        return roleId;
    }

    /**
     * 获取部门id
     *
     * @return java.io.Serializable
     * @author Carlos
     * @date 2022/11/16 18:23
     */
    public static Serializable getDepartmentId() {
        if (extend == null) {
            throw new ComponentException("Can't find Bean of 'ApplicationExtend'");
        }
        final UserContext context = extend.getUserContext();
        final Serializable departmentId = context.getDepartmentId();
        if (departmentId == null) {
            throw new ServiceException(StatusCode.NOT_PERMISSION);
        }
        return departmentId;
    }

    /**
     * 获取用户登陆信息
     *
     * @return com.carlos.core.auth.UserContext
     * @author Carlos
     * @date 2022/12/1 12:54
     */
    public static UserContext getUserContext() {
        if (extend == null) {
            throw new ComponentException("Can't find Bean of 'ApplicationExtend'");
        }
        final UserContext context = extend.getUserContext();
        if (extend == null) {
            throw new ServiceException("用户登陆信息为空");
        }
        return context;
    }
}
