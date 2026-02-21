package com.yunjin.org.fallback;

import com.yunjin.core.response.Result;
import com.yunjin.org.api.ApiLogin;
import com.yunjin.org.pojo.ao.LoginSuccessAO;
import com.yunjin.org.pojo.param.ApiLoginParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;


/**
 * <p>
 * 登录降级服务
 * </p>
 *
 * @author Carlos
 * @date 2022/11/16 10:57
 */

@Slf4j
public class FeignLoginFallbackFactory implements FallbackFactory<ApiLogin> {

    @Override
    public ApiLogin create(Throwable throwable) {
        String message = throwable.getMessage();
        log.error("移动端登录服务调用失败: message:{}", message);
        return new ApiLogin() {

            @Override
            public Result<LoginSuccessAO> login(ApiLoginParam loginParam) {
                return Result.fail("移动端登录失败", message);
            }
        };
    }
}