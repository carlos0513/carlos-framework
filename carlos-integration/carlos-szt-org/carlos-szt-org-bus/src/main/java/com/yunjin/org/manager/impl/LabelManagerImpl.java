package com.yunjin.org.manager.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yunjin.core.pagination.Paging;
import com.yunjin.datasource.base.BaseServiceImpl;
import com.yunjin.datasource.pagination.MybatisPage;
import com.yunjin.datasource.pagination.PageInfo;
import com.yunjin.org.convert.LabelConvert;
import com.yunjin.org.enums.LabelTypeEnum;
import com.yunjin.org.manager.LabelManager;
import com.yunjin.org.manager.LabelTypeManager;
import com.yunjin.org.mapper.LabelMapper;
import com.yunjin.org.pojo.dto.LabelDTO;
import com.yunjin.org.pojo.entity.Label;
import com.yunjin.org.pojo.param.LabelPageParam;
import com.yunjin.org.pojo.vo.LabelVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 标签 查询封装实现类
 * </p>
 *
 * @author  yunjin
 * @date    2024-3-23 12:31:52
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class LabelManagerImpl  extends BaseServiceImpl<LabelMapper, Label> implements LabelManager {

    private final LabelTypeManager labelTypeManager;
    private final LabelMapper labelMapper;

    @Override
    public boolean add(LabelDTO dto) {
        Label entity = LabelConvert.INSTANCE.toDO(dto);
        boolean success = save(entity);
        if (!success) {
            log.warn("Insert 'Label' data fail, entity:{}", entity);
            return false;
        }
        dto.setId(entity.getId());
        // 保存完成的后续
        if (log.isDebugEnabled()) {
            log.debug("Insert 'Label' data: id:{}", entity.getId());
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
            log.warn("Remove 'Label' data fail, id:{}", id);
            return false;
        }
        if (log.isDebugEnabled()) {
            log.debug("Remove 'Label' data by id:{}", id);
        }
        return true;
    }

    @Override
    public boolean modify(LabelDTO dto) {
        Label entity = LabelConvert.INSTANCE.toDO(dto);
        boolean success = updateById(entity);
        if (!success) {
            log.warn("Update 'Label' data fail, entity:{}", entity);
            return false;
        }
        // 修改成功的后续操作
        if (log.isDebugEnabled()) {
            log.debug("Update 'Label' data by id:{}", dto.getId());
        }
        return true;
    }

    @Override
    public LabelDTO getDtoById(Serializable id) {
        if (id == null) {
            log.warn("id is null");
            return null;
        }
        Label entity = getBaseMapper().selectById(id);
        LabelDTO dto = LabelConvert.INSTANCE.toDTO(entity);
        if(null!=dto){
            dto.setTypeName(labelTypeManager.getDtoById(dto.getTypeId()).getName());
        }
        return dto;
    }

    @Override
    public Paging<LabelVO> getPage(LabelPageParam param){
        LambdaQueryWrapper<Label> wrapper = queryWrapper();
        wrapper.select(
                Label::getId,
                Label::getName,
                Label::getCode,
                Label::getTypeId,
                Label::getSort,
                Label::getHidden,
                Label::getCreateBy,
                Label::getCreateTime,
                Label::getUpdateBy,
                Label::getUpdateTime
                );
        PageInfo<Label> page = page(pageInfo(param), wrapper);
        return MybatisPage.convert(page, LabelConvert.INSTANCE::toVO);
    }

    @Override
    public List<LabelDTO> getByName(String name, Boolean isHidden, LabelTypeEnum labelType) {
        LambdaQueryWrapper<Label> wrapper = queryWrapper();
        wrapper.eq(Label::getLabelType, labelType);
        if (isHidden != Boolean.TRUE) {
            wrapper.eq(Label::getHidden, Boolean.FALSE);
        }

        if (StrUtil.isNotBlank(name)) {
            wrapper.like(Label::getName, name);
        }
        List<Label> list = getBaseMapper().selectList(wrapper);
        return LabelConvert.INSTANCE.toDTO(list);
    }

    @Override
    public List<LabelDTO> listByIdsIncludeDeleted(List<String> ids) {
        List<Label> label = labelMapper.listByIdsIncludeDeleted(ids);
        return LabelConvert.INSTANCE.toDTO(label);
    }

    @Override
    public LabelDTO getByIdIncludeDeleted(String id) {
        Label label = labelMapper.getByIdIncludeDeleted(id);
        return LabelConvert.INSTANCE.toDTO(label);
    }

    @Override
    public List<LabelDTO> listByType(String typeId, Boolean isHidden) {
        LambdaQueryWrapper<Label> wrapper = queryWrapper();
        if (isHidden != Boolean.TRUE) {
            wrapper.eq(Label::getHidden, Boolean.FALSE);
        }
        wrapper.eq(Label::getTypeId, typeId).orderByAsc(Label::getSort);
        return LabelConvert.INSTANCE.toDTO(getBaseMapper().selectList(wrapper));
    }

    @Override
    public List<LabelDTO> listByTypeIds(List<String> typeIds, Boolean isHidden) {
        if (typeIds == null || typeIds.isEmpty()) {
            return new ArrayList<>();
        }
        LambdaQueryWrapper<Label> wrapper = queryWrapper();
        wrapper.in(Label::getTypeId, typeIds);
        if (isHidden != Boolean.TRUE) {
            wrapper.eq(Label::getHidden, Boolean.FALSE);
        }
        wrapper.orderByAsc(Label::getTypeId, Label::getSort);
        return LabelConvert.INSTANCE.toDTO(getBaseMapper().selectList(wrapper));
    }

}
