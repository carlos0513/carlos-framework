package com.yunjin.resource.apiimpl;


import cn.hutool.core.collection.CollUtil;
import com.yunjin.core.response.Result;
import com.yunjin.org.api.ApiResourceGroup;
import com.yunjin.org.enums.RoleResourceGroupEnum;
import com.yunjin.org.param.ApiResourceGroupParam;
import com.yunjin.org.pojo.ao.ResourceGroupAO;
import com.yunjin.resource.convert.ResourceGroupConvert;
import com.yunjin.resource.pojo.dto.ResourceGroupDTO;
import com.yunjin.resource.service.ResourceGroupService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 资源组 api接口
 * </p>
 *
 * @author yunjin
 * @date 2024-8-22 10:59:20
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("api/org/resource/group")
@Tag(name = "资源组Feign接口", hidden = true)
public class ApiResourceGroupImpl implements ApiResourceGroup {

    private final ResourceGroupService resourceGroupService;

    @PostMapping("getResourceGroupByRoleIds")
    public Result<List<RoleResourceGroupEnum>> getResourceGroupByRoleIds(@RequestBody ApiResourceGroupParam param) {
        List<ResourceGroupDTO> groups = resourceGroupService.listByRoleIds(param.getRoleIds());
        if (CollUtil.isEmpty(groups)) {
            return Result.ok(Collections.emptyList());
        }
        List<RoleResourceGroupEnum> roleResourceGroups = groups.stream().map(g -> RoleResourceGroupEnum.codeOf(g.getGroupCode()))
                .collect(Collectors.toList());
        return Result.ok(roleResourceGroups);
    }

    @Override
    public Result<List<ResourceGroupAO>> getResourceGroupByRoleId(String roleId) {
        List<ResourceGroupDTO> groups = resourceGroupService.listByRoleIds(Collections.singleton(roleId));
        if (CollUtil.isEmpty(groups)) {
            return Result.ok(Collections.emptyList());
        }
        return Result.ok(ResourceGroupConvert.INSTANCE.toAOS(groups));
    }

}
