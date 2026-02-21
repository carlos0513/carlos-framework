package com.yunjin.org.login;


import com.yunjin.core.auth.UserContext;
import com.yunjin.core.exception.RestException;
import com.yunjin.docking.rzt.RztUtil;
import com.yunjin.log.annotation.Log;
import com.yunjin.log.commonlog.pojo.common.LogConstant;
import com.yunjin.log.core.aop.aspect.LogCollector;
import com.yunjin.log.enums.BusinessType;
import com.yunjin.org.config.OrgConstant;
import com.yunjin.org.login.pojo.LoginCodeUserVO;
import com.yunjin.org.login.pojo.LoginSuccessVO;
import com.yunjin.org.login.pojo.LoginVerifyVO;
import com.yunjin.org.login.pojo.enums.SmsCodeTypeEnum;
import com.yunjin.org.login.pojo.param.*;
import com.yunjin.org.login.service.LoginService;
import com.yunjin.redis.util.RedisUtil;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * 登录相关接口
 * </p>
 *
 * @author yunjin
 * @date 2021/12/28 9:38
 */
@RestController
@Api(value = "Login", tags = {"用户-登录"})
@RequestMapping("/org")
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;

    @PostMapping("/login")
    @Operation(summary = "登录", notes = "系统用户登录")
    @Log(title = "用户登录", businessType = BusinessType.LOGIN)
    @LogCollector(logType = LogConstant.LOGIN_LOG, businessType = "用户登录")
    public LoginSuccessVO login(@Validated @RequestBody LoginParam loginParam, HttpServletResponse response) {
        LoginSuccessVO vo = this.loginService.login(loginParam);
        if (vo == null) {
            throw new RestException("登录失败!");
        }
        // 设置token响应头
        response.setHeader(vo.getName(), vo.getToken());
        return vo;
    }

    @PostMapping("/logout")
    @Operation(summary = "登出")
    @Log(title = "用户退出登录", businessType = BusinessType.LOGOUT)
    public void logout() {
        this.loginService.logout();
    }

    @GetMapping("verify")
    @Operation(summary = "获取验证码")
    public LoginVerifyVO getVerify(Integer width, Integer height, Integer codeCount) {
        return loginService.getVerify(width, height, codeCount);
    }

    @GetMapping("sign")
    @Operation(summary = "获取签名")
    public String sign() {
        return loginService.sign();
    }

    @GetMapping("user/getByCode")
    @Operation(summary = "根据code获取用户信息")
    public LoginCodeUserVO getUserByCode(String code) {
        return loginService.getUserByCode(code);
    }

    @GetMapping("getSmsCode")
    @Operation(summary = "获取短信验证码")
    @Parameters({
            @Parameter(name = "phone", description = "手机号"),
            @Parameter(name = "verifyToken", description = "图形验证码Token"),
            @Parameter(name = "verifyType", description = "验证码类型"),
            @Parameter(name = "code", description = "图形验证码code")
    })
    public void getSmsCode(@RequestParam(value = "phone", required = true) String phone,
                           @RequestParam(value = "verifyToken", required = true) String verifyToken,
                           @RequestParam(value = "code", required = true) String code,
                           @RequestParam(value = "verifyType", required = true) SmsCodeTypeEnum verifyType) {
        loginService.getSmsCode(phone, verifyToken, code, verifyType);
    }

    @GetMapping("checkToken")
    @Operation(summary = "检查验证码")
    public String checkToken(String token) {
        UserContext context = RedisUtil.getValue(String.format(OrgConstant.LOGIN_USER_CONTEXT_CACHE, token), UserContext.class);
        if (context == null) {
            return null;
        } else {
            return (String) context.getUserId();
        }
    }

    @PostMapping("/third/login")
    @Operation(summary = "第三方登录统一接口", notes = "第三方登录统一接口")
    public LoginSuccessVO thirdLogin(@Validated @RequestBody ThirdLoginParam loginParam) {
        LoginSuccessVO vo = this.loginService.thirdLogin(loginParam);
        if (vo == null) {
            throw new RestException("登录失败!");
        }
        return vo;
    }

    @PostMapping("/linkage/login")
    @Operation(summary = "大联动登录", notes = "大联动用户登录")
    public LoginSuccessVO bigLinkageLogin(@Validated @RequestBody BigLinkAgeLoginParam loginParam) {
        LoginSuccessVO vo = this.loginService.bigLinkAgeLogin(loginParam);
        if (vo == null) {
            throw new RestException("登录失败!");
        }
        return vo;
    }

    @PostMapping("/event/login")
    @Operation(summary = "事件中枢登录", notes = "事件中枢登录")
    public LoginSuccessVO eventLogin(@Validated @RequestBody EventLoginParam loginParam) {
        LoginSuccessVO vo = this.loginService.eventLogin(loginParam);
        if (vo == null) {
            throw new RestException("登录失败!");
        }
        return vo;
    }

    @PostMapping("/dingtalk/login")
    @Operation(summary = "钉钉登录", notes = "钉钉登录")
    public LoginSuccessVO dingtalkLogin(@Validated @RequestBody DingtalkLoginParam loginParam) {
        LoginSuccessVO vo = this.loginService.dingtalkLogin(loginParam);
        if (vo == null) {
            throw new RestException("登录失败!");
        }
        return vo;
    }

    @PostMapping("/tfoauth/login")
    @Operation(summary = "天府Oauth登录", notes = "天府Oauth登录")
    public LoginSuccessVO tfOauthLogin(@Validated @RequestBody TfLoginLoginParam loginParam) {
        LoginSuccessVO vo = this.loginService.tfLoginLogin(loginParam);
        if (vo == null) {
            throw new RestException("登录失败!");
        }
        return vo;
    }

    @PostMapping("/choiceDept")
    @Operation(summary = "选择部门", notes = "选择部门")
    @Log(title = "用户选择部门", businessType = BusinessType.SELECT_ROLE)
    public boolean choiceUserDepartment(@RequestBody ChoiceDepartmentParam param) {
        return this.loginService.choiceUserDepartment(param.getDepartmentId().toString());
    }

    @PostMapping("/cacheRztUser")
    @Operation(summary = "缓存蓉政通用户", notes = "缓存蓉政通用户")
    @Log(title = "蓉政通用户缓存", businessType = BusinessType.SELECT_ROLE)
    public void cacheRztUser() {
        RztUtil.cacheUser();
    }

    @PostMapping("/updateCacheRztUser")
    @Operation(summary = "更新蓉政通用户缓存", notes = "缓存蓉政通用户")
    @Log(title = "缓存蓉政通用户", businessType = BusinessType.SELECT_ROLE)
    public void updateCacheRztUser(String filter, String searchType) {
        RztUtil.updateRztUsers(filter, searchType);
    }

    @GetMapping("/loginCityFlag")
    @Operation(summary = "可登录市级标识", notes = "可登录市级标识")
    @Log(title = "可登录市级标识", businessType = BusinessType.SELECT_ROLE)
    public Boolean loginCityFlag() {
        return this.loginService.loginCityFlag();
    }
}
