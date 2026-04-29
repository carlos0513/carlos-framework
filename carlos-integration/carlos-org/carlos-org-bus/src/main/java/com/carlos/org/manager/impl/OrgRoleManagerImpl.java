package com.carlos.org.manager.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.carlos.core.pagination.Paging;
import com.carlos.datasource.base.BaseServiceImpl;
import com.carlos.datasource.pagination.MybatisPage;
import com.carlos.datasource.pagination.PageInfo;
import com.carlos.org.convert.OrgRoleConvert;
import com.carlos.org.manager.OrgRoleManager;
import com.carlos.org.mapper.OrgRoleMapper;
import com.carlos.org.pojo.dto.OrgRoleDTO;
import com.carlos.org.pojo.dto.OrgRoleUserDTO;
import com.carlos.org.pojo.entity.OrgRole;
import com.carlos.org.pojo.param.OrgRolePageParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 角色 查询封装实现类
 * </p>
 *
 * @author Carlos
 * @date 2026年2月28日 下午1:25:36
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class OrgRoleManagerImpl extends BaseServiceImpl<OrgRoleMapper, OrgRole> implements OrgRoleManager {

    @Override
    public boolean add(OrgRoleDTO dto) {
        OrgRole entity = OrgRoleConvert.INSTANCE.toDO(dto);
        boolean success = save(entity);
        if (!success) {
            log.warn("Insert 'OrgRole' data fail, entity:{}", entity);
            return false;
        }
        dto.setId(entity.getId());
        log.debug("Insert 'OrgRole' data: id:{}", entity.getId());
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
            log.warn("Remove 'OrgRole' data fail, id:{}", id);
            return false;
        }
        log.debug("Remove 'OrgRole' data by id:{}", id);
        return true;
    }

    @Override
    public boolean modify(OrgRoleDTO dto) {
        OrgRole entity = OrgRoleConvert.INSTANCE.toDO(dto);
        boolean success = updateById(entity);
        if (!success) {
            log.warn("Update 'OrgRole' data fail, entity:{}", entity);
            return false;
        }
        log.debug("Update 'OrgRole' data by id:{}", dto.getId());
        return true;
    }

    @Override
    public OrgRoleDTO getDtoById(Serializable id) {
        if (id == null) {
            log.warn("id is null");
            return null;
        }
        OrgRole entity = getBaseMapper().selectById(id);
        OrgRoleDTO dto = OrgRoleConvert.INSTANCE.toDTO(entity);
        if (dto != null) {
            // 查询用户数量
            dto.setUserCount(getUserCountByRoleId(id));
            // 查询权限ID列表
            dto.setPermissionIds(getPermissionIdsByRoleId(id));
        }
        return dto;
    }

    @Override
    public Paging<OrgRoleDTO> getPage(OrgRolePageParam param) {
        LambdaQueryWrapper<OrgRole> wrapper = new LambdaQueryWrapper<>();
        if (param.getRoleName() != null && !param.getRoleName().isEmpty()) {
            wrapper.like(OrgRole::getRoleName, param.getRoleName());
        }
        if (param.getRoleCode() != null && !param.getRoleCode().isEmpty()) {
            wrapper.like(OrgRole::getRoleCode, param.getRoleCode());
        }
        if (param.getRoleType() != null) {
            wrapper.eq(OrgRole::getRoleType, param.getRoleType());
        }
        if (param.getState() != null) {
            wrapper.eq(OrgRole::getState, param.getState());
        }
        wrapper.orderByDesc(OrgRole::getCreateTime);
        PageInfo<OrgRole> page = page(pageInfo(param), wrapper);
        return MybatisPage.convert(page, entities -> entities.stream()
            .map(OrgRoleConvert.INSTANCE::toDTO)
            .collect(Collectors.toList()));
    }

    @Override
    public OrgRoleDTO getByRoleCode(String roleCode) {
        LambdaQueryWrapper<OrgRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OrgRole::getRoleCode, roleCode);
        wrapper.last("LIMIT 1");
        OrgRole entity = getBaseMapper().selectOne(wrapper);
        return OrgRoleConvert.INSTANCE.toDTO(entity);
    }

    @Override
    public List<Long> getPermissionIdsByRoleId(Serializable roleId) {
        return getBaseMapper().getPermissionIdsByRoleId(roleId);
    }

    @Override
    public int getUserCountByRoleId(Serializable roleId) {
        return getBaseMapper().getUserCountByRoleId(roleId);
    }

    @Override
    public boolean assignPermissions(Serializable roleId, List<Serializable> permissionIds) {
        // 先删除原有权限
        getBaseMapper().deleteRolePermissions(roleId);
        // 插入新权限
        if (permissionIds != null && !permissionIds.isEmpty()) {
            getBaseMapper().insertRolePermissions(roleId, permissionIds);
        }
        return true;
    }

    @Override
    public Paging<OrgRoleUserDTO> getRoleUsers(Serializable roleId, OrgRolePageParam param) {
        // 先获取所有用户，然后手动分页
        List<OrgRoleUserDTO> allUsers = getBaseMapper().getRoleUsers(roleId);

        int total = allUsers.size();
        int pages = (int) Math.ceil((double) total / param.getSize());
        int fromIndex = (param.getCurrent() - 1) * param.getSize();
        int toIndex = Math.min(fromIndex + param.getSize(), total);

        List<OrgRoleUserDTO> records = fromIndex < total
            ? allUsers.subList(fromIndex, toIndex)
            : Collections.emptyList();

        Paging<OrgRoleUserDTO> paging = new Paging<>();
        paging.setCurrent(param.getCurrent());
        paging.setSize(param.getSize());
        paging.setTotal(total);
        paging.setPages(pages);
        paging.setRecords(records);
        return paging;
    }

}
