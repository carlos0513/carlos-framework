package com.yunjin.org.service.impl;

import com.yunjin.org.manager.RoleResourceManager;
import com.yunjin.org.pojo.dto.RoleResourceDTO;
import com.yunjin.org.service.RoleResourceService;
import com.yunjin.system.pojo.ao.SysResourceAO;
import java.io.Serializable;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


/**
 * <p>
 * 角色资源 业务接口实现类
 * </p>
 *
 * @author yunjin
 * @date 2022-11-11 19:21:46
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RoleResourceServiceImpl implements RoleResourceService {

    private final RoleResourceManager roleResourceManager;

    @Override
    public void addRoleResource(Serializable roleId, Set<String> resourceIds) {
        // 删除关联表数据
        removeByRoleId((Serializable) roleId);
        for (Serializable resourceId : resourceIds) {
            roleResourceManager.add(new RoleResourceDTO().setRoleId(String.valueOf(roleId)).setResourceId(String.valueOf(resourceId)));
        }
    }


    @Override
    public List<SysResourceAO> getByRoleId(Serializable roleId) {
        // return roleResourceManager.getByRoleId(roleId);
        return null;
    }

    @Override
    public Set<String> getResourceIdByRoleId(Serializable roleId) {
        if (roleId == null) {
            return null;
        }
        // List<SysResourceAO> resources = roleResourceManager.getByRoleId(roleId);
        // return resources.stream().map(SysResource::getId).collect(Collectors.toSet());
        return null;
    }


    @Override
    public void removeByRoleId(Serializable roleId) {
        roleResourceManager.deleteByRoleId(roleId);
    }


}
