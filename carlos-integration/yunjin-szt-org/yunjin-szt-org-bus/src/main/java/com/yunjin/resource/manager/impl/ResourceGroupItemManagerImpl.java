package com.yunjin.resource.manager.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yunjin.core.pagination.Paging;
import com.yunjin.datasource.base.BaseServiceImpl;
import com.yunjin.datasource.pagination.MybatisPage;
import com.yunjin.datasource.pagination.PageInfo;
import com.yunjin.resource.convert.ResourceGroupItemConvert;
import com.yunjin.resource.manager.ResourceGroupItemManager;
import com.yunjin.resource.mapper.ResourceGroupItemMapper;
import com.yunjin.resource.pojo.dto.ResourceGroupItemDTO;
import com.yunjin.resource.pojo.entity.ResourceGroupItem;
import com.yunjin.resource.pojo.param.ResourceGroupItemPageParam;
import com.yunjin.resource.pojo.vo.ResourceGroupItemVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * 资源组详情项 查询封装实现类
 * </p>
 *
 * @author yunjin
 * @date 2024-8-22 10:59:20
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class ResourceGroupItemManagerImpl extends BaseServiceImpl<ResourceGroupItemMapper, ResourceGroupItem> implements ResourceGroupItemManager {

    @Override
    public boolean add(ResourceGroupItemDTO dto) {
        ResourceGroupItem entity = ResourceGroupItemConvert.INSTANCE.toDO(dto);
        boolean success = save(entity);
        if (!success) {
            log.warn("Insert 'ResourceGroupItem' data fail, entity:{}", entity);
            return false;
        }
        dto.setId(entity.getId());
        // 保存完成的后续
        if (log.isDebugEnabled()) {
            log.debug("Insert 'ResourceGroupItem' data: id:{}", entity.getId());
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
            log.warn("Remove 'ResourceGroupItem' data fail, id:{}", id);
            return false;
        }
        if (log.isDebugEnabled()) {
            log.debug("Remove 'ResourceGroupItem' data by id:{}", id);
        }
        return true;
    }

    @Override
    public boolean modify(ResourceGroupItemDTO dto) {
        ResourceGroupItem entity = ResourceGroupItemConvert.INSTANCE.toDO(dto);
        boolean success = updateById(entity);
        if (!success) {
            log.warn("Update 'ResourceGroupItem' data fail, entity:{}", entity);
            return false;
        }
        // 修改成功的后续操作
        if (log.isDebugEnabled()) {
            log.debug("Update 'ResourceGroupItem' data by id:{}", dto.getId());
        }
        return true;
    }

    @Override
    public ResourceGroupItemDTO getDtoById(Serializable id) {
        if (id == null) {
            log.warn("id is null");
            return null;
        }
        ResourceGroupItem entity = getBaseMapper().selectById(id);
        return ResourceGroupItemConvert.INSTANCE.toDTO(entity);
    }

    @Override
    public Paging<ResourceGroupItemVO> getPage(ResourceGroupItemPageParam param) {
        LambdaQueryWrapper<ResourceGroupItem> wrapper = queryWrapper();
        wrapper.select(
                ResourceGroupItem::getId,
                ResourceGroupItem::getGroupId,
                ResourceGroupItem::getResourceType,
                ResourceGroupItem::getResourceKey,
                ResourceGroupItem::getCreateBy,
                ResourceGroupItem::getCreateTime,
                ResourceGroupItem::getUpdateBy,
                ResourceGroupItem::getUpdateTime
        );
        PageInfo<ResourceGroupItem> page = page(pageInfo(param), wrapper);
        return MybatisPage.convert(page, ResourceGroupItemConvert.INSTANCE::toVO);
    }

    @Override
    public List<ResourceGroupItemDTO> listByGroupIds(Set<String> resourceGroupIds) {
        List<ResourceGroupItem> list = lambdaQuery().in(CollUtil.isNotEmpty(resourceGroupIds), ResourceGroupItem::getGroupId, resourceGroupIds).list();
        return ResourceGroupItemConvert.INSTANCE.toDTO(list);
    }
}
