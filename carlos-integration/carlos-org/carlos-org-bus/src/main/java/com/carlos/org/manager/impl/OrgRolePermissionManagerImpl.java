package com.carlos.org.manager.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.carlos.core.pagination.Paging;
import com.carlos.datasource.base.BaseServiceImpl;
import com.carlos.datasource.pagination.MybatisPage;
import com.carlos.datasource.pagination.PageInfo;
import com.carlos.org.convert.OrgRolePermissionConvert;
import com.carlos.org.manager.OrgRolePermissionManager;
import com.carlos.org.mapper.OrgRolePermissionMapper;
import com.carlos.org.pojo.dto.OrgRolePermissionDTO;
import com.carlos.org.pojo.entity.OrgRolePermission;
import com.carlos.org.pojo.param.OrgRolePermissionPageParam;
import com.carlos.org.pojo.vo.OrgRolePermissionVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * <p>
 * 角色权限 查询封装实现类
 * </p>
 *
 * @author Carlos
 * @date 2026年2月28日 下午1:25:36
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class OrgRolePermissionManagerImpl extends BaseServiceImpl<OrgRolePermissionMapper, OrgRolePermission> implements OrgRolePermissionManager {

    @Override
    public boolean add(OrgRolePermissionDTO dto) {
        OrgRolePermission entity = OrgRolePermissionConvert.INSTANCE.toDO(dto);
        boolean success = save(entity);
        if (!success) {
            log.warn("Insert 'OrgRolePermission' data fail, entity:{}", entity);
            return false;
        }
        dto.setId(entity.getId());
        // 保存完成的后续
        if (log.isDebugEnabled()) {
            log.debug("Insert 'OrgRolePermission' data: id:{}", entity.getId());
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
            log.warn("Remove 'OrgRolePermission' data fail, id:{}", id);
            return false;
        }
        if (log.isDebugEnabled()) {
            log.debug("Remove 'OrgRolePermission' data by id:{}", id);
        }
        return true;
    }

    @Override
    public boolean modify(OrgRolePermissionDTO dto) {
        OrgRolePermission entity = OrgRolePermissionConvert.INSTANCE.toDO(dto);
        boolean success = updateById(entity);
        if (!success) {
            log.warn("Update 'OrgRolePermission' data fail, entity:{}", entity);
            return false;
        }
        // 修改成功的后续操作
        if (log.isDebugEnabled()) {
            log.debug("Update 'OrgRolePermission' data by id:{}", dto.getId());
        }
        return true;
    }

    @Override
    public OrgRolePermissionDTO getDtoById(Serializable id) {
        if (id == null) {
            log.warn("id is null");
            return null;
        }
        OrgRolePermission entity = getBaseMapper().selectById(id);
        return OrgRolePermissionConvert.INSTANCE.toDTO(entity);
    }

    @Override
    public Paging<OrgRolePermissionVO> getPage(OrgRolePermissionPageParam param) {
        LambdaQueryWrapper<OrgRolePermission> wrapper = queryWrapper();
        wrapper.select(

                OrgRolePermission::getId,
                OrgRolePermission::getRoleId,
                OrgRolePermission::getPermissionId,
                OrgRolePermission::getCreateBy,
                OrgRolePermission::getCreateTime,
                OrgRolePermission::getTenantId
        );
        PageInfo<OrgRolePermission> page = page(pageInfo(param), wrapper);
        return MybatisPage.convert(page, OrgRolePermissionConvert.INSTANCE::toVO);
    }

}
