package com.carlos.oauth.oauth2.client;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

/**
 * <p>
 * client鉴权成功
 * </p>
 *
 * @author Carlos
 * @date 2022/11/9 10:38
 */
@Slf4j
public class CustomClientAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) {
        log.info("Client authentication success ：clientId:{}", authentication.getPrincipal());

        // TODO: Carlos 2022/11/11 客户端登录成功的逻辑 将响应结果输出

    }


}
