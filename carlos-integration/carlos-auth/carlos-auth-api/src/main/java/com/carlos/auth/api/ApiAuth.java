package com.carlos.auth.api;

import com.carlos.auth.api.fallback.FeignAuthFallbackFactory;
import com.carlos.auth.api.pojo.PasswordMatchDTO;
import com.carlos.core.response.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
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
    value = ServiceNameConstant.SERVICE_NAME,
    contextId = "auth",
    fallbackFactory = FeignAuthFallbackFactory.class)
public interface ApiAuth {

    /**
     * 获取认证token（代理OAuth2标准端点）
     *
     * @param param 认证信息（form-urlencoded）
     * @param authorization Basic Auth 客户端认证头（可选）
     * @return 认证中心返回的原始Token Map
     * @author carlos
     * @date 2021/12/27 16:08
     */
    @PostMapping(value = "/oauth2/token", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    Map<String, Object> getAccessToken(
        @RequestBody MultiValueMap<String, String> param,
        @RequestHeader(value = "Authorization", required = false) String authorization);

    /**
     * 获取加密后的密码
     *
     * @param password 原密码
     * @return java.lang.String
     * @author carlos
     * @date 2021/12/27 16:07
     */
    @GetMapping(value = "/auth/password/encode")
    Result<String> encodePassword(@RequestParam("password") String password);

    /**
     * 检查密码是否匹配
     *
     * @param param 参数
     * @return java.lang.Boolean
     * @author carlos
     * @date 2022/4/25 15:30
     */
    @PostMapping(value = "/auth/password/match")
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
