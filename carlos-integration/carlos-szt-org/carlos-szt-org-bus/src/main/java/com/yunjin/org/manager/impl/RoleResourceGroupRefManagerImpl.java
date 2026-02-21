package com.yunjin.org.manager.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.yunjin.core.pagination.Paging;
import com.yunjin.datasource.base.BaseServiceImpl;
import com.yunjin.datasource.pagination.MybatisPage;
import com.yunjin.datasource.pagination.PageInfo;
import com.yunjin.org.convert.RoleResourceGroupRefConvert;
import com.yunjin.org.manager.RoleResourceGroupRefManager;
import com.yunjin.org.mapper.RoleResourceGroupRefMapper;
import com.yunjin.org.pojo.dto.RoleResourceGroupRefDTO;
import com.yunjin.org.pojo.entity.RoleResourceGroupRef;
import com.yunjin.org.pojo.param.RoleResourceGroupRefPageParam;
import com.yunjin.org.pojo.vo.RoleResourceGroupRefVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * 角色资源组关联表 查询封装实现类
 * </p>
 *
 * @author yunjin
 * @date 2024-8-22 10:59:20
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class RoleResourceGroupRefManagerImpl extends BaseServiceImpl<RoleResourceGroupRefMapper, RoleResourceGroupRef> implements RoleResourceGroupRefManager {

    @Override
    public boolean add(RoleResourceGroupRefDTO dto) {
        RoleResourceGroupRef entity = RoleResourceGroupRefConvert.INSTANCE.toDO(dto);
        boolean success = save(entity);
        if (!success) {
            log.warn("Insert 'RoleResourceGroupRef' data fail, entity:{}", entity);
            return false;
        }
        dto.setId(entity.getId());
        // 保存完成的后续
        if (log.isDebugEnabled()) {
            log.debug("Insert 'RoleResourceGroupRef' data: id:{}", entity.getId());
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
            log.warn("Remove 'RoleResourceGroupRef' data fail, id:{}", id);
            return false;
        }
        if (log.isDebugEnabled()) {
            log.debug("Remove 'RoleResourceGroupRef' data by id:{}", id);
        }
        return true;
    }

    @Override
    public boolean modify(RoleResourceGroupRefDTO dto) {
        RoleResourceGroupRef entity = RoleResourceGroupRefConvert.INSTANCE.toDO(dto);
        boolean success = updateById(entity);
        if (!success) {
            log.warn("Update 'RoleResourceGroupRef' data fail, entity:{}", entity);
            return false;
        }
        // 修改成功的后续操作
        if (log.isDebugEnabled()) {
            log.debug("Update 'RoleResourceGroupRef' data by id:{}", dto.getId());
        }
        return true;
    }

    @Override
    public RoleResourceGroupRefDTO getDtoById(Serializable id) {
        if (id == null) {
            log.warn("id is null");
            return null;
        }
        RoleResourceGroupRef entity = getBaseMapper().selectById(id);
        return RoleResourceGroupRefConvert.INSTANCE.toDTO(entity);
    }

    @Override
    public RoleResourceGroupRefDTO getByRoleId(Serializable roleId) {
        RoleResourceGroupRef ref = lambdaQuery()
                .select(
                        RoleResourceGroupRef::getId,
                        RoleResourceGroupRef::getRoleId,
                        RoleResourceGroupRef::getResourceGroupId
                ).eq(RoleResourceGroupRef::getRoleId, roleId)
                .one();

        return RoleResourceGroupRefConvert.INSTANCE.toDTO(ref);
    }

    @Override
    public Paging<RoleResourceGroupRefVO> getPage(RoleResourceGroupRefPageParam param) {
        LambdaQueryWrapper<RoleResourceGroupRef> wrapper = queryWrapper();
        wrapper.select(
                RoleResourceGroupRef::getId,
                RoleResourceGroupRef::getRoleId,
                RoleResourceGroupRef::getResourceGroupId,
                RoleResourceGroupRef::getCreateBy,
                RoleResourceGroupRef::getCreateTime,
                RoleResourceGroupRef::getUpdateBy,
                RoleResourceGroupRef::getUpdateTime
        );
        PageInfo<RoleResourceGroupRef> page = page(pageInfo(param), wrapper);
        return MybatisPage.convert(page, RoleResourceGroupRefConvert.INSTANCE::toVO);
    }

    @Override
    public boolean removeByRoleId(String roleId) {
        if (roleId == null) {
            log.warn("roleId can't be null");
            return false;
        }
        boolean success = remove(queryWrapper().eq(RoleResourceGroupRef::getRoleId, roleId));
        if (!success) {
            log.warn("Remove 'RoleResourceGroupRef' data fail, roleId:{}", roleId);
            return false;
        }
        if (log.isDebugEnabled()) {
            log.debug("Remove 'RoleResourceGroupRef' data by roleId:{}", roleId);
        }
        return true;
    }

    @Override
    public List<RoleResourceGroupRefDTO> listByRoleIds(Set<String> roleIds) {
        LambdaQueryChainWrapper<RoleResourceGroupRef> wrapper = lambdaQuery();
        List<RoleResourceGroupRef> refs = wrapper.in(CollUtil.isNotEmpty(roleIds), RoleResourceGroupRef::getRoleId, roleIds).list();
        return RoleResourceGroupRefConvert.INSTANCE.toDTO(refs);
    }
}
