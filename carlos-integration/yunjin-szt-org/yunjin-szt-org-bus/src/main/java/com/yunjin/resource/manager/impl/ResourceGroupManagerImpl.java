package com.yunjin.resource.manager.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yunjin.core.pagination.Paging;
import com.yunjin.datasource.base.BaseServiceImpl;
import com.yunjin.datasource.pagination.MybatisPage;
import com.yunjin.datasource.pagination.PageInfo;
import com.yunjin.resource.convert.ResourceGroupConvert;
import com.yunjin.resource.manager.ResourceGroupManager;
import com.yunjin.resource.mapper.ResourceGroupMapper;
import com.yunjin.resource.pojo.dto.ResourceGroupDTO;
import com.yunjin.resource.pojo.entity.ResourceGroup;
import com.yunjin.resource.pojo.param.ResourceGroupPageParam;
import com.yunjin.resource.pojo.vo.ResourceGroupVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * 资源组 查询封装实现类
 * </p>
 *
 * @author yunjin
 * @date 2024-8-22 10:59:20
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class ResourceGroupManagerImpl extends BaseServiceImpl<ResourceGroupMapper, ResourceGroup> implements ResourceGroupManager {

    @Override
    public boolean add(ResourceGroupDTO dto) {
        ResourceGroup entity = ResourceGroupConvert.INSTANCE.toDO(dto);
        boolean success = save(entity);
        if (!success) {
            log.warn("Insert 'ResourceGroup' data fail, entity:{}", entity);
            return false;
        }
        dto.setId(entity.getId());
        // 保存完成的后续
        if (log.isDebugEnabled()) {
            log.debug("Insert 'ResourceGroup' data: id:{}", entity.getId());
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
            log.warn("Remove 'ResourceGroup' data fail, id:{}", id);
            return false;
        }
        if (log.isDebugEnabled()) {
            log.debug("Remove 'ResourceGroup' data by id:{}", id);
        }
        return true;
    }

    @Override
    public boolean modify(ResourceGroupDTO dto) {
        ResourceGroup entity = ResourceGroupConvert.INSTANCE.toDO(dto);
        boolean success = updateById(entity);
        if (!success) {
            log.warn("Update 'ResourceGroup' data fail, entity:{}", entity);
            return false;
        }
        // 修改成功的后续操作
        if (log.isDebugEnabled()) {
            log.debug("Update 'ResourceGroup' data by id:{}", dto.getId());
        }
        return true;
    }

    @Override
    public ResourceGroupDTO getDtoById(Serializable id) {
        if (id == null) {
            log.warn("id is null");
            return null;
        }
        ResourceGroup entity = getBaseMapper().selectById(id);
        return ResourceGroupConvert.INSTANCE.toDTO(entity);
    }

    @Override
    public Paging<ResourceGroupVO> getPage(ResourceGroupPageParam param) {
        LambdaQueryWrapper<ResourceGroup> wrapper = queryWrapper();
        wrapper.select(
                ResourceGroup::getId,
                ResourceGroup::getGroupCode,
                ResourceGroup::getGroupName,
                ResourceGroup::getDescription,
                ResourceGroup::getState,
                ResourceGroup::getCreateTime,
                ResourceGroup::getUpdateTime
        );
        PageInfo<ResourceGroup> page = page(pageInfo(param), wrapper);
        return MybatisPage.convert(page, ResourceGroupConvert.INSTANCE::toVO);
    }

    @Override
    public List<ResourceGroupDTO> listAll() {
        return ResourceGroupConvert.INSTANCE.toDTO(list());
    }

    @Override
    public List<ResourceGroupDTO> getByGroupIds(Set<String> groupIds) {
        List<ResourceGroup> resourceGroups = getBaseMapper().selectBatchIds(groupIds);
        return ResourceGroupConvert.INSTANCE.toDTO(resourceGroups);
    }
}
