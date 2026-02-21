package com.yunjin.org.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.text.CharSequenceUtil;
import com.yunjin.core.exception.ServiceException;
import com.yunjin.org.convert.RoleResourceGroupRefConvert;
import com.yunjin.org.manager.RoleResourceGroupRefManager;
import com.yunjin.org.pojo.dto.RoleResourceGroupDTO;
import com.yunjin.org.pojo.dto.RoleResourceGroupRefDTO;
import com.yunjin.resource.pojo.dto.ResourceGroupDTO;
import com.yunjin.resource.pojo.dto.ResourceGroupItemDTO;
import com.yunjin.resource.service.ResourceGroupItemService;
import com.yunjin.resource.service.ResourceGroupService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * <p>
 * 角色资源组关联表 业务接口实现类
 * </p>
 *
 * @author yunjin
 * @date 2024-8-22 10:59:20
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RoleResourceGroupRefService {

    private final RoleResourceGroupRefManager roleResourceGroupRefManager;
    private final ResourceGroupService resourceGroupService;
    private final ResourceGroupItemService resourceGroupItemService;

    @Transactional(rollbackFor = Exception.class)
    public void addRoleResourceGroupRef(RoleResourceGroupRefDTO dto) {
        String resourceGroupId = dto.getResourceGroupId();
        if (CharSequenceUtil.isBlank(resourceGroupId)) {
            log.error("resourceGroup id null");
        }
        ResourceGroupDTO resourceGroup = resourceGroupService.getById(resourceGroupId);
        if (resourceGroup == null) {
            log.error("resourceGroup is null, id:{}", resourceGroupId);
            throw new ServiceException("资源不存在，保存角色资源关联失败!");
        }
        // 删除原有关联
        roleResourceGroupRefManager.removeByRoleId(dto.getRoleId());
        boolean success = roleResourceGroupRefManager.add(dto);
        if (!success) {
            // 保存失败的应对措施
            throw new ServiceException("保存角色资源关联失败!");
        }
        Serializable id = dto.getId();
        // 保存完成的后续业务
    }

    public void removeByRoleId(String roleId) {
        roleResourceGroupRefManager.removeByRoleId(roleId);
    }

    public void deleteRoleResourceGroupRef(Set<String> ids) {
        for (Serializable id : ids) {
            boolean success = roleResourceGroupRefManager.delete(id);
            if (!success) {
                // 删除失败的措施
                continue;
            }
            // 删除成功的后续业务
        }
    }

    public void updateRoleResourceGroupRef(RoleResourceGroupRefDTO dto) {
        boolean success = roleResourceGroupRefManager.modify(dto);
        if (!success) {
            // 修改失败操作
            return;
        }
        // 修改成功的后续操作
    }

    public List<RoleResourceGroupDTO> getByRoleIds(Set<String> roleIds) {
        List<RoleResourceGroupRefDTO> refs = roleResourceGroupRefManager.listByRoleIds(roleIds);
        if (CollUtil.isEmpty(refs)) {
            return Collections.emptyList();
        }
        Set<String> resourceGroupIds = refs.stream().map(RoleResourceGroupRefDTO::getResourceGroupId).collect(Collectors.toSet());
        // 根据资源组id获取
        List<ResourceGroupItemDTO> itemList = resourceGroupItemService.listByGroupIds(resourceGroupIds);
        return RoleResourceGroupRefConvert.INSTANCE.toGroupDTOS(itemList);
    }

    /**
     * 获取角色权限组信息
     *
     * @param roleId 参数0
     * @return com.yunjin.org.pojo.dto.RoleResourceGroupRefDTO
     * @throws
     * @author Carlos
     * @date 2025-05-13 13:55
     */
    public RoleResourceGroupRefDTO getByRoleId(Serializable roleId) {
        if (roleId == null) {
            return null;
        }
        RoleResourceGroupRefDTO ref = roleResourceGroupRefManager.getByRoleId(roleId);
        return ref;
    }
}
