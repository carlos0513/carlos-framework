package com.carlos.org.apiimpl;

import com.carlos.core.exception.RestException;
import com.carlos.core.response.Result;
import com.carlos.org.api.ApiLogin;
import com.carlos.org.login.convert.LoginConvert;
import com.carlos.org.login.pojo.LoginSuccessVO;
import com.carlos.org.login.pojo.param.LoginParam;
import com.carlos.org.login.service.LoginService;
import com.carlos.org.pojo.ao.LoginSuccessAO;
import com.carlos.org.pojo.param.ApiLoginParam;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/org")
@Tag(name = "系统登录Feign接口")
@Hidden
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
