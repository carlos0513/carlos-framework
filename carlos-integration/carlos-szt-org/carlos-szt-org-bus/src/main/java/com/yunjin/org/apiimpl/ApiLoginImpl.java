package com.yunjin.org.apiimpl;

import com.yunjin.core.exception.RestException;
import com.yunjin.core.response.Result;
import com.yunjin.org.api.ApiLogin;
import com.yunjin.org.login.convert.LoginConvert;
import com.yunjin.org.login.pojo.LoginSuccessVO;
import com.yunjin.org.login.pojo.param.LoginParam;
import com.yunjin.org.login.service.LoginService;
import com.yunjin.org.pojo.ao.LoginSuccessAO;
import com.yunjin.org.pojo.param.ApiLoginParam;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/org")
@Tag(name = "系统登录Feign接口", hidden = true)
@Slf4j
public class ApiLoginImpl implements ApiLogin {

    private final LoginService loginService;

    @Override
    @PostMapping("/login")
    @Operation(summary = "系统用户登录")
    public Result<LoginSuccessAO> login(@RequestBody ApiLoginParam loginParam) {
        LoginSuccessVO vo = this.loginService.login(
                new LoginParam()
                        .setAccount(loginParam.getAccount())
                        .setPassword(loginParam.getPassword())
                        .setLoginType(loginParam.getLoginType())
                        .setCode(loginParam.getCode())
                        .setVerifyToken(loginParam.getVerifyToken()));
        if (vo == null) {
            throw new RestException("登录失败!");
        }
        LoginSuccessAO ao = LoginConvert.INSTANCE.voToAO(vo);
        return Result.ok(ao);
    }
}
