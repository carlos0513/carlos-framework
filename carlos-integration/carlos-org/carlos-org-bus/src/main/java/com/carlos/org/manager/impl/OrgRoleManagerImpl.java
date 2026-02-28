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
import com.carlos.org.pojo.entity.OrgRole;
import com.carlos.org.pojo.param.OrgRolePageParam;
import com.carlos.org.pojo.vo.OrgRoleVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * <p>
 * 系统角色 查询封装实现类
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
        // 保存完成的后续
        if (log.isDebugEnabled()) {
            log.debug("Insert 'OrgRole' data: id:{}", entity.getId());
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
            log.warn("Remove 'OrgRole' data fail, id:{}", id);
            return false;
        }
        if (log.isDebugEnabled()) {
            log.debug("Remove 'OrgRole' data by id:{}", id);
        }
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
        // 修改成功的后续操作
        if (log.isDebugEnabled()) {
            log.debug("Update 'OrgRole' data by id:{}", dto.getId());
        }
        return true;
    }

    @Override
    public OrgRoleDTO getDtoById(Serializable id) {
        if (id == null) {
            log.warn("id is null");
            return null;
        }
        OrgRole entity = getBaseMapper().selectById(id);
        return OrgRoleConvert.INSTANCE.toDTO(entity);
    }

    @Override
    public Paging<OrgRoleVO> getPage(OrgRolePageParam param) {
        LambdaQueryWrapper<OrgRole> wrapper = queryWrapper();
        wrapper.select(

                OrgRole::getId,
                OrgRole::getRoleName,
                OrgRole::getRoleCode,
                OrgRole::getRoleType,
                OrgRole::getDataScope,
                OrgRole::getState,
                OrgRole::getDescription,
                OrgRole::getCreateBy,
                OrgRole::getCreateTime,
                OrgRole::getUpdateBy,
                OrgRole::getUpdateTime,
                OrgRole::getTenantId
        );
        PageInfo<OrgRole> page = page(pageInfo(param), wrapper);
        return MybatisPage.convert(page, OrgRoleConvert.INSTANCE::toVO);
    }

}
