package com.yunjin.org.fallback;

import com.yunjin.core.response.Result;
import com.yunjin.org.api.ApiResourceGroup;
import com.yunjin.org.enums.RoleResourceGroupEnum;
import com.yunjin.org.param.ApiResourceGroupParam;
import com.yunjin.org.pojo.ao.ResourceGroupAO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;

import java.util.List;

/**
 * 角色权限组降级服务
 *
 * @author shenyong
 * @e-mail sheny60@chinaunicom.cn
 * @date 2024/10/15 09:59
 **/
@Slf4j
public class FeignResourceGroupFallbackFactory implements FallbackFactory<ApiResourceGroup> {
    @Override
    public ApiResourceGroup create(Throwable cause) {
        String message = cause.getMessage();
        log.error("用户服务调用失败: message:{}", message);
        return new ApiResourceGroup() {
            @Override
            public Result<List<RoleResourceGroupEnum>> getResourceGroupByRoleIds(ApiResourceGroupParam param) {
                return Result.fail("用户角色权限组查询失败", message);
            }

            @Override
            public Result<List<ResourceGroupAO>> getResourceGroupByRoleId(String roleId) {
                return Result.fail("用户角色权限组查询失败", message);
            }
        };
    }
}
