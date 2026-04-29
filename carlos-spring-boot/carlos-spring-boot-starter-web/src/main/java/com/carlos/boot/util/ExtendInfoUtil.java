package com.carlos.boot.util;

import com.carlos.core.auth.UserContext;
import com.carlos.core.exception.BusinessException;
import com.carlos.core.exception.ComponentException;
import com.carlos.core.interfaces.ApplicationExtend;
import com.carlos.core.response.CommonErrorCode;
import org.springframework.lang.Nullable;

import java.io.Serializable;

/**
 * <p>
 * 应用扩展信息工具类
 * </p>
 *
 * @author carlos
 * @date 2021/12/28 9:50
 */
public final class ExtendInfoUtil {

    private static ApplicationExtend extend;

    /**
     * 初始化静态字段
     *
     * @param applicationExtend 应用扩展接口
     */
    public static void init(@Nullable final ApplicationExtend applicationExtend) {
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
            throw new BusinessException(CommonErrorCode.FORBIDDEN);
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
            throw new BusinessException(CommonErrorCode.FORBIDDEN);
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
            throw CommonErrorCode.UNAUTHORIZED.exception("用户登录信息为空");
        }
        return context;
    }
}
