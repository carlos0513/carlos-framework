package com.yunjin.org.manager.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.yunjin.core.pagination.Paging;
import com.yunjin.datasource.base.BaseServiceImpl;
import com.yunjin.datasource.pagination.MybatisPage;
import com.yunjin.datasource.pagination.PageInfo;
import com.yunjin.org.UserUtil;
import com.yunjin.org.convert.LabelTypeConvert;
import com.yunjin.org.enums.LabelTypeEnum;
import com.yunjin.org.manager.LabelTypeManager;
import com.yunjin.org.mapper.LabelTypeMapper;
import com.yunjin.org.pojo.dto.LabelTypeDTO;
import com.yunjin.org.pojo.entity.LabelType;
import com.yunjin.org.pojo.param.LabelTypePageParam;
import com.yunjin.org.pojo.vo.LabelTypeVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * 标签分类 查询封装实现类
 * </p>
 *
 * @author  yunjin
 * @date    2024-3-22 15:07:09
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class LabelTypeManagerImpl extends BaseServiceImpl<LabelTypeMapper, LabelType> implements LabelTypeManager {

    @Override
    public boolean add(LabelTypeDTO dto) {
        LabelType entity = LabelTypeConvert.INSTANCE.toDO(dto);
        boolean success = save(entity);
        if (!success) {
            log.warn("Insert 'BbtLabelType' data fail, entity:{}", entity);
            return false;
        }
        dto.setId(entity.getId());
        // 保存完成的后续
        if (log.isDebugEnabled()) {
            log.debug("Insert 'BbtLabelType' data: id:{}", entity.getId());
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
            log.warn("Remove 'BbtLabelType' data fail, id:{}", id);
            return false;
        }
        if (log.isDebugEnabled()) {
            log.debug("Remove 'BbtLabelType' data by id:{}", id);
        }
        return true;
    }

    @Override
    public boolean modify(LabelTypeDTO dto) {
        LabelType entity = LabelTypeConvert.INSTANCE.toDO(dto);
        boolean success = updateById(entity);
        if (!success) {
            log.warn("Update 'BbtLabelType' data fail, entity:{}", entity);
            return false;
        }
        // 修改成功的后续操作
        if (log.isDebugEnabled()) {
            log.debug("Update 'BbtLabelType' data by id:{}", dto.getId());
        }
        return true;
    }

    @Override
    public LabelTypeDTO getDtoById(Serializable id) {
        if (id == null) {
            log.warn("id is null");
            return null;
        }
        LabelType entity = getBaseMapper().selectById(id);
        return LabelTypeConvert.INSTANCE.toDTO(entity);
    }

    @Override
    public Paging<LabelTypeVO> getPage(LabelTypePageParam param){
        LambdaQueryWrapper<LabelType> wrapper = queryWrapper();
        wrapper.select(
                LabelType::getId,
                LabelType::getName,
                LabelType::getParentId,
                LabelType::getSort,
                LabelType::getHidden,
                LabelType::getCreateBy,
                LabelType::getCreateTime,
                LabelType::getUpdateBy,
                LabelType::getUpdateTime,
                LabelType::getLabelType
        ).eq(LabelType::getLabelType, param.getLabelTypeEnum());
        if (LabelTypeEnum.CUSTOM == param.getLabelTypeEnum()) {
            wrapper.eq(LabelType::getCreateBy, UserUtil.getId());
        }
        PageInfo<LabelType> page = page(pageInfo(param), wrapper);
        return MybatisPage.convert(page, LabelTypeConvert.INSTANCE::toVO);
    }

    @Override
    public Boolean isExist(LabelTypeDTO dto) {
        LambdaQueryWrapper<LabelType> wrapper = queryWrapper();
        wrapper.eq(LabelType::getName, dto.getName());
        wrapper.eq(LabelType::getLabelType, dto.getLabelType().getCode());
        return getBaseMapper().exists(wrapper);
    }
    @Override
    public List<LabelTypeDTO> listByParentId(String parentId,Boolean isHidden) {
        LambdaQueryWrapper<LabelType> wrapper = queryWrapper();
        wrapper.eq(LabelType::getParentId, parentId);
        wrapper.eq(isHidden==Boolean.FALSE,LabelType::getHidden,isHidden);
        wrapper.orderByAsc(LabelType::getSort);
        return LabelTypeConvert.INSTANCE.toDTO(getBaseMapper().selectList(wrapper));
    }

    @Override
    public void modifySortForDel(String parentId,Integer sortDel) {
        LambdaQueryWrapper<LabelType> wrapper = queryWrapper();
        wrapper.gt(LabelType::getSort, sortDel);
        wrapper.eq(LabelType::getParentId, parentId);
        List<LabelType> labelTypes = getBaseMapper().selectList(wrapper);
        if(CollUtil.isNotEmpty(labelTypes)){
            labelTypes.forEach(labelType -> {
                labelType.setSort(labelType.getSort() - 1);
                getBaseMapper().updateById(labelType);
            });
        }
    }

    @Override
    public Set<String> listParentIds(Set<String> ids) {
        Set<String> parentIds = CollUtil.newHashSet();
        if(CollUtil.isNotEmpty(ids)){
            LambdaQueryWrapper<LabelType> wrapper = queryWrapper();
            wrapper.in(LabelType::getId, ids);
            List<LabelType> labelTypes = getBaseMapper().selectList(wrapper);
            if(CollUtil.isNotEmpty(labelTypes)){
                labelTypes.forEach(labelType -> {
                    if(labelType.getParentId() != null){
                        parentIds.add(labelType.getParentId());
                    }
                });
            }
        }
        return parentIds;
    }

    @Override
    public List<LabelTypeDTO> listByName(String name) {
        LambdaQueryWrapper<LabelType> wrapper = queryWrapper();
        wrapper.like(StrUtil.isNotBlank(name), LabelType::getName, name);
        wrapper.orderByAsc(LabelType::getSort);
        return LabelTypeConvert.INSTANCE.toDTO(getBaseMapper().selectList(wrapper));
    }

    public List<LabelType> listByNameAndType(String name, LabelTypeEnum labelType) {
        LambdaQueryWrapper<LabelType> wrapper = queryWrapper();
        wrapper.like(StrUtil.isNotBlank(name), LabelType::getName, name);
        wrapper.eq(ObjectUtils.isNotNull(labelType), LabelType::getLabelType, labelType);
        if (LabelTypeEnum.CUSTOM == labelType) {
            wrapper.eq(LabelType::getCreateBy, UserUtil.getId());
        }
        wrapper.orderByAsc(LabelType::getSort);
        return getBaseMapper().selectList(wrapper);
    }
}
