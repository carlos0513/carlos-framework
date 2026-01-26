package com.yunjin.core.base;


import com.yunjin.core.auth.LoginUserInfo;

/**
 * <p>
 * 登录用户来源
 * </p>
 *
 * @author yunjin
 * @date 2021/11/5 10:45
 */
public interface LoginUserSource {


    /**
     * 根据用户名获取登录用户信息
     *
     * @param username 登录用户名
     * @return com.yunjin.core.auth.LoginUserInfo
     * @author yunjin
     * @date 2021/11/5 10:47
     */
    LoginUserInfo getLoginUserInfoByName(String username);

}
