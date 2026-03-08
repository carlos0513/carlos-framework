package com.carlos.system.resource.manager.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.carlos.core.pagination.Paging;
import com.carlos.datasource.base.BaseServiceImpl;
import com.carlos.datasource.pagination.MybatisPage;
import com.carlos.datasource.pagination.PageInfo;
import com.carlos.system.resource.convert.SysResourceCategoryConvert;
import com.carlos.system.resource.manager.SysResourceCategoryManager;
import com.carlos.system.resource.mapper.SysResourceCategoryMapper;
import com.carlos.system.resource.pojo.dto.ResourceCategoryDTO;
import com.carlos.system.resource.pojo.entity.SysResourceCategory;
import com.carlos.system.resource.pojo.param.SysResourceCategoryPageParam;
import com.carlos.system.resource.pojo.vo.SysResourceCategoryVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 资源分类 查询封装实现类
 * </p>
 *
 * @author carlos
 * @date 2022-1-5 17:23:27
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class SysResourceCategoryManagerImpl extends BaseServiceImpl<SysResourceCategoryMapper, SysResourceCategory> implements SysResourceCategoryManager {

    @Override
    public boolean add(ResourceCategoryDTO dto) {
        SysResourceCategory entity = SysResourceCategoryConvert.INSTANCE.toDO(dto);
        boolean success = save(entity);
        if (!success) {
            log.warn("Insert 'ResourceCategory' data fail, entity:{}", entity);
            return false;
        }
        dto.setId(entity.getId());
        // 保存完成的后续
        if (log.isDebugEnabled()) {
            log.debug("Insert 'ResourceCategory' data: id:{}", entity.getId());
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
            log.warn("Remove 'ResourceCategory' data fail, id:{}", id);
            return false;
        }
        if (log.isDebugEnabled()) {
            log.debug("Remove 'ResourceCategory' data by id:{}", id);
        }
        return true;
    }

    @Override
    public boolean modify(ResourceCategoryDTO dto) {
        SysResourceCategory entity = SysResourceCategoryConvert.INSTANCE.toDO(dto);
        boolean success = updateById(entity);
        if (!success) {
            log.warn("Update 'ResourceCategory' data fail, entity:{}", entity);
            return false;
        }
        // 修改成功的后续操作
        if (log.isDebugEnabled()) {
            log.debug("Update 'ResourceCategory' data by id:{}", dto.getId());
        }
        return true;
    }

    @Override
    public ResourceCategoryDTO getDtoById(Serializable id) {
        if (id == null) {
            log.warn("id is null");
            return null;
        }
        SysResourceCategory entity = getBaseMapper().selectById(id);
        return SysResourceCategoryConvert.INSTANCE.toDTO(entity);
    }

    @Override
    public Paging<SysResourceCategoryVO> getPage(SysResourceCategoryPageParam param) {
        LambdaQueryWrapper<SysResourceCategory> wrapper = queryWrapper();
        wrapper.select(
            SysResourceCategory::getId,
            SysResourceCategory::getParentId,
            SysResourceCategory::getName,
            SysResourceCategory::getCreateTime,
            SysResourceCategory::getUpdateTime
        );
        if (StringUtils.isNotBlank(param.getName())) {
            wrapper.like(SysResourceCategory::getName, param.getName());
        }
        if (param.getStart() != null) {
            wrapper.gt(SysResourceCategory::getCreateTime, param.getStart());
        }
        if (param.getEnd() != null) {
            wrapper.lt(SysResourceCategory::getCreateTime, param.getEnd());
        }
        PageInfo<SysResourceCategory> page = page(pageInfo(param), wrapper);
        return MybatisPage.convert(page, SysResourceCategoryConvert.INSTANCE::toVO);
    }

    @Override
    public List<ResourceCategoryDTO> getCategoryTree(Serializable categoryId, boolean detail) {
        List<ResourceCategoryDTO> categories = getCategoryByParentId(categoryId, detail);
        for (ResourceCategoryDTO category : categories) {
            Serializable id = category.getId();
            List<ResourceCategoryDTO> children = getCategoryTree(id, detail);
            category.setChildren(children);
        }
        return categories;
    }

    @Override
    public List<ResourceCategoryDTO> getCategoryByParentId(Serializable parentId, boolean detail) {
        LambdaQueryWrapper<SysResourceCategory> wrapper = queryWrapper()
            .eq(SysResourceCategory::getParentId, parentId == null ? 0 : parentId);
        if (detail) {
            wrapper.select(
                SysResourceCategory::getId,
                SysResourceCategory::getName,
                SysResourceCategory::getParentId,
                SysResourceCategory::getCreateTime,
                SysResourceCategory::getUpdateTime
            );
        } else {
            wrapper.select(
                SysResourceCategory::getId,
                SysResourceCategory::getName,
                SysResourceCategory::getParentId
            );
        }
        List<SysResourceCategory> menus = list(wrapper);
        return SysResourceCategoryConvert.INSTANCE.toDTO(menus);
    }

    @Override
    public String getNameById(Serializable categoryId) {
        SysResourceCategory category = getById(categoryId);
        if (category == null) {
            return null;
        }
        return category.getName();
    }

    @Override
    public boolean existChildren(Serializable categoryId) {
        return lambdaQuery().eq(SysResourceCategory::getParentId, categoryId).count() > 0;
    }

    @Override
    public Serializable getIdByName(Serializable parentId, String name) {
        SysResourceCategory entity = lambdaQuery().eq(SysResourceCategory::getParentId, parentId).eq(SysResourceCategory::getName, name).one();
        if (entity == null) {
            return null;
        }
        return entity.getId();
    }

}
