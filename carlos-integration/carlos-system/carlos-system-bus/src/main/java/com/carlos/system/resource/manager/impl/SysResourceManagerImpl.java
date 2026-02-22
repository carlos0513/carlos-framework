package com.carlos.system.resource.manager.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.carlos.core.pagination.Paging;
import com.carlos.datasource.base.BaseServiceImpl;
import com.carlos.datasource.pagination.MybatisPage;
import com.carlos.datasource.pagination.PageInfo;
import com.carlos.system.resource.convert.SysResourceConvert;
import com.carlos.system.resource.manager.SysResourceCategoryManager;
import com.carlos.system.resource.manager.SysResourceManager;
import com.carlos.system.resource.mapper.SysResourceMapper;
import com.carlos.system.resource.pojo.dto.ResourceCategoryDTO;
import com.carlos.system.resource.pojo.dto.SysResourceDTO;
import com.carlos.system.resource.pojo.dto.SysResourceTreeDTO;
import com.carlos.system.resource.pojo.entity.SysResource;
import com.carlos.system.resource.pojo.param.SysResourcePageParam;
import com.carlos.system.resource.pojo.vo.SysResourceVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * 系统资源 查询封装实现类
 * </p>
 *
 * @author yunjin
 * @date 2021-12-28 15:26:57
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class SysResourceManagerImpl extends BaseServiceImpl<SysResourceMapper, SysResource> implements SysResourceManager {

    private final SysResourceCategoryManager categoryManager;


    @Override
    public boolean add(SysResourceDTO dto) {
        SysResource entity = SysResourceConvert.INSTANCE.toDO(dto);
        boolean success = save(entity);
        if (!success) {
            log.warn("Add resource data fail, entity:{}", entity);
            return false;
        }
        if (log.isDebugEnabled()) {
            log.debug("Add resource data success, id:{}", entity.getId());
        }
        dto.setId(entity.getId());
        // 保存完成的后续
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
            log.error("Remove resource data fail, id:{}", id);
            return false;
        }
        if (log.isDebugEnabled()) {
            log.debug("Remove resource data by id:{}", id);
        }

        return true;
    }

    @Override
    public boolean modify(SysResourceDTO dto) {
        SysResource entity = SysResourceConvert.INSTANCE.toDO(dto);
        boolean success = updateById(SysResourceConvert.INSTANCE.toDO(dto));
        if (!success) {
            // 修改成功的后续操作
            log.warn("Update resource data fail,  entity:{}", entity);
            return false;
        }
        if (log.isDebugEnabled()) {
            log.debug("Update resource data success, entity:{}", entity);
        }
        return true;
    }

    @Override
    public SysResourceDTO getDtoById(String id) {
        SysResource entity = getBaseMapper().selectById(id);
        return SysResourceConvert.INSTANCE.toDTO(entity);
    }

    @Override
    public Paging<SysResourceVO> getPage(SysResourcePageParam param) {
        LambdaQueryWrapper<SysResource> wrapper = queryWrapper();
        wrapper.select(
                SysResource::getId,
                SysResource::getCategoryId,
                SysResource::getName,
                SysResource::getPath,
                SysResource::getMethod,
                SysResource::getIcon,
                SysResource::getType,
                SysResource::getState,
                SysResource::getHidden,
                SysResource::getDescription,
                SysResource::getCreateTime,
                SysResource::getUpdateTime
        );
        if (param.getCategoryId() != null) {
            wrapper.eq(SysResource::getCategoryId, param.getCategoryId());
        }
        if (param.getStart() != null) {
            wrapper.gt(SysResource::getCreateTime, param.getStart());
        }
        if (param.getEnd() != null) {
            wrapper.lt(SysResource::getCreateTime, param.getEnd());
        }
        if (StringUtils.isNotEmpty(param.getName())) {
            wrapper.like(SysResource::getName, param.getName());
        }
        PageInfo<SysResource> page = page(pageInfo(param), wrapper);
        return MybatisPage.convert(page, SysResourceConvert.INSTANCE::toVO);
    }

    @Override
    public List<SysResourceDTO> getResourceByCategoryId(String categoryId) {
        if (categoryId == null) {
            log.error("菜单ID为：[{}]", categoryId);
            return null;
        }
        List<SysResource> list = lambdaQuery().eq(SysResource::getCategoryId, categoryId).list();
        log.debug("菜单 id 为：[{}] 对应系统资源的数目为：[{}]", categoryId, list.size());
        return SysResourceConvert.INSTANCE.toDTO(list);
    }

    @Override
    public List<SysResourceTreeDTO> getResourceTree(String categoryId) {
        List<ResourceCategoryDTO> categories = categoryManager.getCategoryByParentId(categoryId, false);
        List<SysResourceTreeDTO> list = new LinkedList<>();
        for (ResourceCategoryDTO category : categories) {
            String id = category.getId();
            List<SysResourceTreeDTO> children = getResourceTree(id);
            List<SysResourceDTO> resources = getResourceByCategoryId(id);
            list.add(new SysResourceTreeDTO()
                    .setId(id)
                    .setName(category.getName())
                    .setChildren(children)
                    .setResources(resources)
            );
        }
        return list;
    }

    @Override
    public long getResourceCountByMenuId(Serializable categoryId) {
        return count(queryWrapper().eq(SysResource::getCategoryId, categoryId));
    }

    @Override
    public List<SysResourceDTO> getDtoByIds(Set<String> ids) {
        List<SysResource> resources = listByIds(ids);
        return SysResourceConvert.INSTANCE.toDTO(resources);
    }

    @Override
    public String getIdByDto(SysResourceDTO dto) {
        SysResource entity = lambdaQuery()
                .eq(SysResource::getCategoryId, dto.getCategoryId())
                .eq(SysResource::getPath, dto.getPath())
                .eq(SysResource::getMethod, dto.getMethod()).one();
        if (entity == null) {
            return null;
        }
        return entity.getId();
    }


}
