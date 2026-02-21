package com.carlos.auth;

import com.carlos.auth.fallback.FeignAuthFallbackFactory;
import com.carlos.auth.pojo.PasswordMatchDTO;
import com.carlos.core.auth.Oauth2TokenDTO;
import com.carlos.core.response.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 统一认证中心接口调用
 *
 * @author carlos
 * @date 2021/11/11 13:51
 */
@FeignClient(
        value = ServiceNameConstant.AUTH,
        contextId = "auth",
        fallbackFactory = FeignAuthFallbackFactory.class)
public interface FeignAuth {

    /**
     * 获取认证token
     *
     * @param param 认证信息
     * @return com.carlos.core.auth.Oauth2TokenDTO
     * @author carlos
     * @date 2021/12/27 16:08
     */
    @PostMapping(value = "/oauth2/token")
    Result<Oauth2TokenDTO> getAccessToken(
            @RequestParam("param") Map<String, String> param,
            @RequestHeader(value = "headers", required = false) MultiValueMap<String, String> headers);

    /**
     * 获取加密后的密码
     *
     * @param password 原密码
     * @return java.lang.String
     * @author carlos
     * @date 2021/12/27 16:07
     */
    @GetMapping(value = "/pwd/encode")
    Result<String> encodePassword(@RequestParam("password") String password);

    /**
     * 检查密码是否匹配
     *
     * @param param 参数
     * @return java.lang.Boolean
     * @author carlos
     * @date 2022/4/25 15:30
     */
    @PostMapping(value = "pwd/match")
    Result<Boolean> match(@RequestBody PasswordMatchDTO param);

    /**
     * 检查token
     *
     * @return boolean
     * @author Carlos
     * @date 2022/11/13 17:11
     */
    @GetMapping("/api/oauth/checkToken")
    Result<String> checkToken(@RequestParam("token") String token);
}
