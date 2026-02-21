package com.yunjin.resource.service;

import cn.hutool.core.collection.CollUtil;
import com.yunjin.org.manager.RoleResourceGroupRefManager;
import com.yunjin.org.pojo.dto.RoleResourceGroupRefDTO;
import com.yunjin.resource.manager.ResourceGroupManager;
import com.yunjin.resource.pojo.dto.ResourceGroupDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * <p>
 * 资源组 业务接口实现类
 * </p>
 *
 * @author yunjin
 * @date 2024-8-22 10:59:20
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ResourceGroupService {

    private final ResourceGroupManager resourceGroupManager;
    private final RoleResourceGroupRefManager roleResourceGroupRefManager;

    public void addResourceGroup(ResourceGroupDTO dto) {
        boolean success = resourceGroupManager.add(dto);
        if (!success) {
            // 保存失败的应对措施
            return;
        }
        Serializable id = dto.getId();
        // 保存完成的后续业务
    }

    public void deleteResourceGroup(Set<String> ids) {
        for (Serializable id : ids) {
            boolean success = resourceGroupManager.delete(id);
            if (!success) {
                // 删除失败的措施
                continue;
            }
            // 删除成功的后续业务
        }
    }

    public void updateResourceGroup(ResourceGroupDTO dto) {
        boolean success = resourceGroupManager.modify(dto);
        if (!success) {
            // 修改失败操作
            return;
        }
        // 修改成功的后续操作
    }

    /**
     * @Title: listAll
     * @Description: 获取全量列表
     * @Date: 2024/8/22 14:32
     * @Parameters: []
     * @Return com.yunjin.system.pojo.dto.ResourceGroupDTO
     */
    public List<ResourceGroupDTO> listAll() {
        return resourceGroupManager.listAll();

    }

    public ResourceGroupDTO getById(String resourceGroupId) {
        return resourceGroupManager.getDtoById(resourceGroupId);
    }

    /**
     * 根据角色ID获取资源组列表
     *
     * @param roleIds 角色ID集合
     * @return List<ResourceGroupDTO>
     */
    public List<ResourceGroupDTO> listByRoleIds(Set<String> roleIds) {
        if (CollUtil.isEmpty(roleIds)) {
            return Collections.emptyList();
        }
        List<RoleResourceGroupRefDTO> refs = roleResourceGroupRefManager.listByRoleIds(roleIds);
        if (CollUtil.isEmpty(refs)) {
            return Collections.emptyList();
        }
        Set<String> groupIds = refs.stream().map(RoleResourceGroupRefDTO::getResourceGroupId).collect(Collectors.toSet());
        return resourceGroupManager.getByGroupIds(groupIds);
    }
}
