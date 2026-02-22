package com.carlos.org.fallback;

import com.carlos.core.response.Result;
import com.carlos.org.api.ApiUserScope;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;

import java.io.Serializable;
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
public class FeignUserScopeFallbackFactory implements FallbackFactory<ApiUserScope> {

    @Override
    public ApiUserScope create(Throwable throwable) {
        String message = throwable.getMessage();
        log.error("用户服务调用失败: message:{}", message);
        return new ApiUserScope() {

            @Override
            public Result<Set<Serializable>> getCurrentRoleUserId() {
                return Result.fail("当前用户角色信息获取失败", message);
            }

            @Override
            public Result<Set<Serializable>> getCurrentDeptUserId() {
                return Result.fail("当前用户部门信息获取失败", message);
            }

            @Override
            public Result<Set<Serializable>> getCurrentDeptAllUserId(Serializable departmentId) {
                return Result.fail("当前用户部门信息获取失败", message);
            }

            @Override
            public Result<Set<Serializable>> getCurrentDeptAllId(Serializable departmentId) {
                return Result.fail("当前用户部门信息获取失败", message);
            }

            @Override
            public Result<Set<String>> getCurrentRegionAllCode() {
                return Result.fail("当前用户区域信息获取失败", message);
            }
        };
    }
}