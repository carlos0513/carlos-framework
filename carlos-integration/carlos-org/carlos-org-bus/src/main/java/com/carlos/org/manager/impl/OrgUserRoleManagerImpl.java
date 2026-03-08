package com.carlos.org.manager.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.carlos.core.pagination.Paging;
import com.carlos.datasource.base.BaseServiceImpl;
import com.carlos.datasource.pagination.MybatisPage;
import com.carlos.datasource.pagination.PageInfo;
import com.carlos.org.convert.OrgUserRoleConvert;
import com.carlos.org.manager.OrgUserRoleManager;
import com.carlos.org.mapper.OrgUserRoleMapper;
import com.carlos.org.pojo.dto.OrgUserRoleDTO;
import com.carlos.org.pojo.entity.OrgUserRole;
import com.carlos.org.pojo.param.OrgUserRolePageParam;
import com.carlos.org.pojo.vo.OrgUserRoleVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * <p>
 * 用户角色 查询封装实现类
 * </p>
 *
 * @author Carlos
 * @date 2026年2月28日 下午1:25:36
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class OrgUserRoleManagerImpl extends BaseServiceImpl<OrgUserRoleMapper, OrgUserRole> implements OrgUserRoleManager {

    @Override
    public boolean add(OrgUserRoleDTO dto) {
        OrgUserRole entity = OrgUserRoleConvert.INSTANCE.toDO(dto);
        boolean success = save(entity);
        if (!success) {
            log.warn("Insert 'OrgUserRole' data fail, entity:{}", entity);
            return false;
        }
        dto.setId(entity.getId());
        // 保存完成的后续
        if (log.isDebugEnabled()) {
            log.debug("Insert 'OrgUserRole' data: id:{}", entity.getId());
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
            log.warn("Remove 'OrgUserRole' data fail, id:{}", id);
            return false;
        }
        if (log.isDebugEnabled()) {
            log.debug("Remove 'OrgUserRole' data by id:{}", id);
        }
        return true;
    }

    @Override
    public boolean modify(OrgUserRoleDTO dto) {
        OrgUserRole entity = OrgUserRoleConvert.INSTANCE.toDO(dto);
        boolean success = updateById(entity);
        if (!success) {
            log.warn("Update 'OrgUserRole' data fail, entity:{}", entity);
            return false;
        }
        // 修改成功的后续操作
        if (log.isDebugEnabled()) {
            log.debug("Update 'OrgUserRole' data by id:{}", dto.getId());
        }
        return true;
    }

    @Override
    public OrgUserRoleDTO getDtoById(Serializable id) {
        if (id == null) {
            log.warn("id is null");
            return null;
        }
        OrgUserRole entity = getBaseMapper().selectById(id);
        return OrgUserRoleConvert.INSTANCE.toDTO(entity);
    }

    @Override
    public Paging<OrgUserRoleVO> getPage(OrgUserRolePageParam param) {
        LambdaQueryWrapper<OrgUserRole> wrapper = queryWrapper();
        wrapper.select(

            OrgUserRole::getId,
            OrgUserRole::getUserId,
            OrgUserRole::getRoleId,
            OrgUserRole::getDeptId,
            OrgUserRole::getExpireTime,
            OrgUserRole::getCreateBy,
            OrgUserRole::getCreateTime,
            OrgUserRole::getTenantId
        );
        PageInfo<OrgUserRole> page = page(pageInfo(param), wrapper);
        return MybatisPage.convert(page, OrgUserRoleConvert.INSTANCE::toVO);
    }

}
