package com.yunjin.org.api;

import com.yunjin.core.response.Result;
import com.yunjin.org.ServiceNameConstant;
import com.yunjin.org.enums.RoleResourceGroupEnum;
import com.yunjin.org.fallback.FeignResourceGroupFallbackFactory;
import com.yunjin.org.param.ApiResourceGroupParam;
import com.yunjin.org.pojo.ao.ResourceGroupAO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * 角色权限组服务
 *
 * @author shenyong
 * @e-mail sheny60@chinaunicom.cn
 * @date 2024/10/15 09:57
 **/
@FeignClient(value = ServiceNameConstant.USER, contextId = "resourceGroup", path = "/api/org/resource/group", fallbackFactory = FeignResourceGroupFallbackFactory.class)
public interface ApiResourceGroup {
    /**
     * 获取过去30天登录的用户个数
     */
    @PostMapping("getResourceGroupByRoleIds")
    Result<List<RoleResourceGroupEnum>> getResourceGroupByRoleIds(@RequestBody ApiResourceGroupParam param);

    @GetMapping("getResourceGroupByRoleId")
    Result<List<ResourceGroupAO>> getResourceGroupByRoleId(String roleId);
}
