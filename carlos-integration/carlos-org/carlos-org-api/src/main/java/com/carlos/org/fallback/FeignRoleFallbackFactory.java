package com.carlos.org.fallback;

import com.carlos.core.response.Result;
import com.carlos.org.api.ApiRole;
import com.carlos.org.pojo.ao.RoleAO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;

import java.io.Serializable;
import java.util.List;
import java.util.Set;


/**
 * <p>
 * 用户降级服务
 * </p>
 *
 * @author Carlos
 * @date 2022/11/16 10:57
 */

@Slf4j
public class FeignRoleFallbackFactory implements FallbackFactory<ApiRole> {

    @Override
    public ApiRole create(Throwable throwable) {
        String message = throwable.getMessage();
        log.error("用户服务调用失败: message:{}", message);
        return new ApiRole() {

            @Override
            public Result<RoleAO> getById(String id) {
                return Result.fail("用户角色查询失败", message);
            }

            @Override
            public Result<List<RoleAO>> getRoleList() {
                return Result.fail("用户角色列表查询失败", message);
            }

            @Override
            public Result<Set<Serializable>> getUserIdByRoleId(Set<Serializable> roleIds) {
                return Result.fail("角色查询用户Id失败", message);
            }

            @Override
            public Result<RoleAO> getByName(String roleName) {
                return Result.fail("用户角色查询失败", message);
            }
        };
    }
}