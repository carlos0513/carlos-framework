package com.carlos.org.login.service;

import com.carlos.org.login.pojo.LoginCodeUserVO;
import com.carlos.org.login.pojo.LoginSuccessVO;
import com.carlos.org.login.pojo.LoginVerifyVO;
import com.carlos.org.login.pojo.enums.SmsCodeTypeEnum;
import com.carlos.org.login.pojo.param.LoginParam;
import com.carlos.org.login.pojo.param.ThirdLoginParam;
import com.carlos.org.pojo.dto.UserDTO;

/**
 * <p>
 * 登录服务接口
 * </p>
 *
 * @author yunjin
 * @date 2019-05-23
 **/
public interface LoginService {


    /**
     * 登录服务
     *
     * @param loginParam 登录参数
     * @return 返回token和用户的登录信息
     * @author yunjin
     * @date 2020/4/15 14:49
     */
    LoginSuccessVO login(LoginParam loginParam);



    /**
     * 检查登录信息
     *
     * @param loginParam 登陆信息
     * @author Carlos
     * @date 2022/11/11 13:29
     */
    UserDTO checkLoginInfo(LoginParam loginParam);

    /**
     * 退出登录
     *
     * @author yunjin
     * @date 2021/2/20 12:05
     */
    void logout();

    /**
     * 获取用户初始化信息(用户基本信息，菜单信息，权限等)
     *
     * @return com.carlos.user.pojo.dto.UserDTO
     * @author Carlos
     * @date 2021/12/28 10:22
     */
    UserDTO getLoginUserInfo(String account, String roleId);

    /**
     * 获取验证码信息
     *
     * @param width     参数0
     * @param height    参数1
     * @param codeCount 参数2
     * @return com.carlos.org.login.pojo.LoginVerifyVO
     * @author Carlos
     * @date 2023/4/4 10:26
     */
    LoginVerifyVO getVerify(Integer width, Integer height, Integer codeCount);

    /**
     * @Title: thirdLogin
     * @Description: 第三方登录统一接口
     * @Date: 2023/7/12 15:31
     * @Parameters: [loginParam]
     * @Return com.carlos.org.login.pojo.LoginSuccessVO
     */
    LoginSuccessVO thirdLogin(ThirdLoginParam loginParam);

    /**
     * 切换当前登录用户部门
     *
     * @param departmentId
     * @return
     */
    boolean choiceUserDepartment(String departmentId);

    /**
     * 获取短信验证码
     *
     * @param phone       手机号
     * @param verifyToken 图形验证码token
     * @param code        图形验证码code
     * @param verifyType  短信验证码类型
     * @return
     */
    void getSmsCode(String phone, String verifyToken, String code, SmsCodeTypeEnum verifyType);

    /**
     * 获取签名信息
     *
     * @return java.lang.String
     * @author Carlos
     * @date 2024/4/19 15:46
     */
    String sign();

    Boolean loginCityFlag();

    /**
     * 根据code获取用户信息
     *
     * @param code 参数0
     * @return com.carlos.org.login.pojo.LoginCodeUserVO
     * @author Carlos
     * @date 2025-05-09 13:40
     */
    LoginCodeUserVO getUserByCode(String code);
}
