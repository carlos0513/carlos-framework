package com.carlos.org.login;


import com.carlos.core.auth.UserContext;
import com.carlos.core.exception.RestException;
import com.carlos.log.annotation.Log;
import com.carlos.log.enums.BusinessType;
import com.carlos.org.config.OrgConstant;
import com.carlos.org.login.pojo.LoginCodeUserVO;
import com.carlos.org.login.pojo.LoginSuccessVO;
import com.carlos.org.login.pojo.LoginVerifyVO;
import com.carlos.org.login.pojo.enums.SmsCodeTypeEnum;
import com.carlos.org.login.pojo.param.ChoiceDepartmentParam;
import com.carlos.org.login.pojo.param.LoginParam;
import com.carlos.org.login.pojo.param.ThirdLoginParam;
import com.carlos.org.login.service.LoginService;
import com.carlos.redis.util.RedisUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


/**
 * <p>
 * 登录相关接口
 * </p>
 *
 * @author carlos
 * @date 2021/12/28 9:38
 */
@RestController
@Tag(name = "Login", description = "Login")
@RequestMapping("/org")
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;

    @PostMapping("/login")
    @Operation(summary = "登录", description = "系统用户登录")
    @Log(title = "用户登录", businessType = BusinessType.LOGIN)
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
    @Operation(summary = "第三方登录统一接口")
    public LoginSuccessVO thirdLogin(@Validated @RequestBody ThirdLoginParam loginParam) {
        LoginSuccessVO vo = this.loginService.thirdLogin(loginParam);
        if (vo == null) {
            throw new RestException("登录失败!");
        }
        return vo;
    }

    @PostMapping("/choiceDept")
    @Operation(summary = "选择部门")
    @Log(title = "用户选择部门", businessType = BusinessType.SELECT_ROLE)
    public boolean choiceUserDepartment(@RequestBody ChoiceDepartmentParam param) {
        return this.loginService.choiceUserDepartment(param.getDepartmentId().toString());
    }

}
