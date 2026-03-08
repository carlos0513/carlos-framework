package com.carlos.auth.fallback;

import com.carlos.auth.FeignAuth;
import com.carlos.auth.pojo.PasswordMatchDTO;
import com.carlos.core.auth.Oauth2TokenDTO;
import com.carlos.core.response.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.util.MultiValueMap;

import java.util.Map;

/**
 * 认证服务降级处理
 *
 * @author Carlos
 * @date 2022/11/16 11:13
 */
@Slf4j
public class FeignAuthFallbackFactory implements FallbackFactory<FeignAuth> {

    @Override
    public FeignAuth create(final Throwable throwable) {
        final String message = throwable.getMessage();
        log.error("认证服务调用失败: message:{}", message);
        return new FeignAuth() {

            @Override
            public Result<Oauth2TokenDTO> getAccessToken(
                final Map<String, String> param, final MultiValueMap<String, String> headers) {
                return Result.fail("token获取失败", message);
            }

            @Override
            public Result<String> encodePassword(final String password) {
                return Result.fail("加密失败");
            }

            @Override
            public Result<Boolean> match(final PasswordMatchDTO param) {
                return Result.fail();
            }

            @Override
            public Result<String> checkToken(String token) {
                return Result.fail();
            }
        };
    }
}
