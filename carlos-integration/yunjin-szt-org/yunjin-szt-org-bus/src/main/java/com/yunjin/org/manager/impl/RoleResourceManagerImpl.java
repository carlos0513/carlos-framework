package com.yunjin.org.manager.impl;

import com.yunjin.datasource.base.BaseServiceImpl;
import com.yunjin.org.convert.RoleResourceConvert;
import com.yunjin.org.manager.RoleResourceManager;
import com.yunjin.org.mapper.RoleResourceMapper;
import com.yunjin.org.pojo.dto.RoleResourceDTO;
import com.yunjin.org.pojo.entity.RoleResource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * <p>
 * 角色资源 查询封装实现类
 * </p>
 *
 * @author yunjin
 * @date 2022-11-11 19:21:46
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class RoleResourceManagerImpl extends BaseServiceImpl<RoleResourceMapper, RoleResource> implements RoleResourceManager {


    @Override
    public boolean deleteByRoleId(Serializable roleId) {
        if (roleId == null) {
            log.error("role id can't be null！");
            return false;
        }
        boolean success = this.remove(queryWrapper().eq(RoleResource::getRoleId, roleId));
        if (!success) {
            log.warn("Remove 'RoleResource' data fail, roleId:{}", roleId);
            return false;
        }
        if (log.isDebugEnabled()) {
            log.debug("Remove 'RoleResource' data by roleId:{}", roleId);
        }
        return true;
    }

    @Override
    public boolean add(RoleResourceDTO dto) {
        RoleResource entity = RoleResourceConvert.INSTANCE.toDO(dto);
        boolean success = save(entity);
        if (!success) {
            log.warn("Insert 'RoleResource' data fail, entity:{}", entity);
            return false;
        }
        dto.setId(entity.getId());
        // 保存完成的后续
        if (log.isDebugEnabled()) {
            log.debug("Insert 'RoleResource' data: id:{}", entity.getId());
        }
        return true;
    }


    @Override
    public RoleResourceDTO getDtoById(String id) {
        if (id == null) {
            log.warn("id is null");
            return null;
        }
        RoleResource entity = getBaseMapper().selectById(id);
        return RoleResourceConvert.INSTANCE.toDTO(entity);
    }


}
