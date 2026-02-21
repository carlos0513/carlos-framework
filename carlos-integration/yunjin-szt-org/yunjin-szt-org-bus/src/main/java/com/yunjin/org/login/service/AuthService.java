package com.yunjin.org.login.service;

import com.yunjin.core.auth.AccessTokenParam;
import com.yunjin.core.auth.Oauth2TokenDTO;
import com.yunjin.core.response.Result;

/**
 * <p>
 * 认证服务功能
 * </p>
 *
 * @author yunjin
 * @date 2021-12-20 14:07:16
 */

public interface AuthService {

    /**
     * 密码编码处理
     *
     * @param password 原密码
     * @return java.lang.String
     * @author yunjin
     * @date 2021/12/27 16:30
     */
    String encodePassword(String password);

    /**
     * 密码解码处理
     *
     * @param password 加密密码
     * @return java.lang.String
     * @author yunjin
     * @date 2021/12/27 16:30
     */
    String decodePassword(String password);

    /**
     * 获取token
     *
     * @param param token获取参数
     * @return com.yunjin.core.auth.Oauth2TokenDTO
     * @author yunjin
     * @date 2021/12/27 16:34
     */
    Result<Oauth2TokenDTO> getToken(AccessTokenParam param);

    /**
     * 检查密码是否正确
     *
     * @param password       需要检查的密码
     * @param encodePassword 加密密码
     * @return boolean
     * @author yunjin
     * @date 2022/4/25 18:37
     */
    boolean checkPassword(String password, String encodePassword);
}
