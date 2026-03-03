package com.carlos.org.manager.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.carlos.datasource.base.BaseServiceImpl;
import com.carlos.org.convert.OrgPermissionConvert;
import com.carlos.org.manager.OrgPermissionManager;
import com.carlos.org.mapper.OrgPermissionMapper;
import com.carlos.org.pojo.dto.OrgPermissionDTO;
import com.carlos.org.pojo.entity.OrgPermission;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 权限 查询封装实现类
 * </p>
 *
 * @author Carlos
 * @date 2026年2月28日 下午1:25:36
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class OrgPermissionManagerImpl extends BaseServiceImpl<OrgPermissionMapper, OrgPermission> implements OrgPermissionManager {

    @Override
    public boolean add(OrgPermissionDTO dto) {
        OrgPermission entity = OrgPermissionConvert.INSTANCE.toDO(dto);
        boolean success = save(entity);
        if (!success) {
            log.warn("Insert 'OrgPermission' data fail, entity:{}", entity);
            return false;
        }
        dto.setId(entity.getId());
        if (log.isDebugEnabled()) {
            log.debug("Insert 'OrgPermission' data: id:{}", entity.getId());
        }
        return true;
    }

    @Override
    public boolean delete(Serializable id) {
        if (id == null) {
            log.warn("id can't be null");
            return false;
        }
        boolean success = removeById(id);
        if (!success) {
            log.warn("Remove 'OrgPermission' data fail, id:{}", id);
            return false;
        }
        if (log.isDebugEnabled()) {
            log.debug("Remove 'OrgPermission' data by id:{}", id);
        }
        return true;
    }

    @Override
    public boolean modify(OrgPermissionDTO dto) {
        OrgPermission entity = OrgPermissionConvert.INSTANCE.toDO(dto);
        boolean success = updateById(entity);
        if (!success) {
            log.warn("Update 'OrgPermission' data fail, entity:{}", entity);
            return false;
        }
        if (log.isDebugEnabled()) {
            log.debug("Update 'OrgPermission' data by id:{}", dto.getId());
        }
        return true;
    }

    @Override
    public OrgPermissionDTO getDtoById(Serializable id) {
        if (id == null) {
            log.warn("id is null");
            return null;
        }
        OrgPermission entity = getBaseMapper().selectById(id);
        return OrgPermissionConvert.INSTANCE.toDTO(entity);
    }

    @Override
    public List<OrgPermissionDTO> listAll() {
        LambdaQueryWrapper<OrgPermission> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByAsc(OrgPermission::getSort);
        List<OrgPermission> list = list(wrapper);
        return list.stream()
                .map(OrgPermissionConvert.INSTANCE::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<OrgPermissionDTO> getChildrenByParentId(Serializable parentId) {
        LambdaQueryWrapper<OrgPermission> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OrgPermission::getParentId, parentId);
        List<OrgPermission> list = list(wrapper);
        return list.stream()
                .map(OrgPermissionConvert.INSTANCE::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public OrgPermissionDTO getByPermCode(String permCode) {
        LambdaQueryWrapper<OrgPermission> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OrgPermission::getPermCode, permCode);
        wrapper.last("LIMIT 1");
        OrgPermission entity = getBaseMapper().selectOne(wrapper);
        return OrgPermissionConvert.INSTANCE.toDTO(entity);
    }

    @Override
    public int getRoleUseCount(Serializable permId) {
        return getBaseMapper().getRoleUseCount(permId);
    }

}
