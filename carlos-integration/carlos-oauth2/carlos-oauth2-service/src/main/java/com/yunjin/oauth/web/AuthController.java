package com.carlos.oauth.web;

import cn.hutool.core.util.StrUtil;
import com.carlos.boot.request.RequestInfo;
import com.carlos.boot.request.RequestUtil;
import com.carlos.core.response.Result;
import com.carlos.oauth.security.service.LoginService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;


/**
 * <p>
 *
 * </p>
 *
 * @author Carlos
 * @date 2022/11/13 21:14
 */
@AllArgsConstructor
@RestController
@RequestMapping("api/oauth")
public class AuthController {

    private final OAuth2AuthorizationService authorizationService;

    private final LoginService loginService;
    private final RegisteredClientRepository clientService;

    @ApiOperation("Oauth2获取token")
    @Parameters({
            @Parameter(name = "grant_type", value = "授权模式", required = true),
            @Parameter(name = "client_id", value = "Oauth2客户端ID", required = true),
            @Parameter(name = "client_secret", value = "Oauth2客户端秘钥", required = true),
            @Parameter(name = "refresh_token", value = "刷新token"),
            @Parameter(name = "username", value = "登录用户名"),
            @Parameter(name = "password", value = "登录密码")
    })


    /**
     * 退出并删除token
     *
     * @param authHeader Authorization
     */
    @PostMapping("/logout")
    public Result<?> logout(@RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authHeader) {
        RequestInfo requestInfo = RequestUtil.getRequestInfo();
        String token = requestInfo.getToken();
        if (StrUtil.isBlank(token)) {
            return Result.ok();
        }
        return this.removeToken(token);
    }

    @GetMapping("/checkToken")
    public Result<String> checkToken(@RequestParam("token") String token) {
        // RequestInfo requestInfo = RequestUtil.getRequestInfo();
        // String token = requestInfo.getToken();
        OAuth2Authorization authorization = this.authorizationService.findByToken(token, OAuth2TokenType.ACCESS_TOKEN);
        String registeredClientId = authorization.getRegisteredClientId();
        RegisteredClient client = clientService.findById(registeredClientId);
        // 如果令牌不存在 返回401
        return Result.ok(client.getClientId());
    }


    /**
     * 删除token
     *
     * @return com.carlos.common.core.response.Result<?>
     * @author Carlos
     * @date 2022/11/13 21:05
     */
    @PostMapping("delete")
    public Result<?> removeToken(String token) {
        OAuth2Authorization authorization = this.authorizationService.findByToken(token, OAuth2TokenType.ACCESS_TOKEN);
        OAuth2Authorization.Token<OAuth2AccessToken> accessToken = authorization.getAccessToken();
        if (accessToken == null || StrUtil.isBlank(accessToken.getToken().getTokenValue())) {
            return Result.ok();
        }
        // 清空access token
        this.authorizationService.remove(authorization);
        return Result.ok();
    }
}
