package com.carlos.org.fallback;

import com.carlos.core.response.Result;
import com.carlos.org.api.ApiUserRole;
import com.carlos.org.pojo.ao.UserRoleAO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;

import java.util.List;


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


            @Override
            public Result<List<UserRoleAO>> allUserRole() {
                return Result.fail("用户角色信息获取失败", message);
            }

            @Override
            public Result<List<UserRoleAO>> getUserRole(List<String> userIds) {
                return Result.fail("根据用户id获取用户角色信息失败", message);
            }

            @Override
            public Result<List<String>> getUserRoleNameByUserId(String userId) {
                return Result.fail("根据用户id获取用户角色信息失败", message);
            }
        };
    }
}