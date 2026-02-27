package com.carlos.org.fallback;

import com.carlos.org.api.ApiUserRole;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;


/**
 * <p>
 * 用户降级服务
 * </p>
 *
 * @author Carlos
 * @date 2022/11/16 10:57
 */

@Slf4j
public class FeignUserRoleFallbackFactory implements FallbackFactory<ApiUserRole> {

    @Override
    public ApiUserRole create(Throwable throwable) {
        String message = throwable.getMessage();
        log.error("用户角色服务调用失败: message:{}", message);
        return new ApiUserRole() {


        };
    }
}