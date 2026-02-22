package com.carlos.msg.base.manager.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.carlos.core.exception.ServiceException;
import com.carlos.core.pagination.Paging;
import com.carlos.datasource.base.BaseServiceImpl;
import com.carlos.datasource.pagination.MybatisPage;
import com.carlos.datasource.pagination.PageInfo;
import com.carlos.msg.base.convert.MsgMessageTypeConvert;
import com.carlos.msg.base.manager.MsgMessageTypeManager;
import com.carlos.msg.base.mapper.MsgMessageTypeMapper;
import com.carlos.msg.base.pojo.dto.MsgMessageTypeDTO;
import com.carlos.msg.base.pojo.entity.MsgMessageType;
import com.carlos.msg.base.pojo.param.MsgMessageTypePageParam;
import com.carlos.msg.base.pojo.vo.MsgMessageTypeBaseVO;
import com.carlos.msg.base.pojo.vo.MsgMessageTypeVO;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 消息类型 查询封装实现类
 * </p>
 *
 * @author Carlos
 * @date 2025-3-10 10:53:03
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class MsgMessageTypeManagerImpl extends BaseServiceImpl<MsgMessageTypeMapper, MsgMessageType> implements MsgMessageTypeManager {

    @Override
    public boolean add(MsgMessageTypeDTO dto) {
        MsgMessageType entity = MsgMessageTypeConvert.INSTANCE.toDO(dto);
        boolean success = save(entity);
        if (!success) {
            log.warn("Insert 'MsgMessageType' data fail, entity:{}", entity);
            return false;
        }
        dto.setId(entity.getId());
        // 保存完成的后续
        if (log.isDebugEnabled()) {
            log.debug("Insert 'MsgMessageType' data: id:{}", entity.getId());
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
            log.warn("Remove 'MsgMessageType' data fail, id:{}", id);
            return false;
        }
        if (log.isDebugEnabled()) {
            log.debug("Remove 'MsgMessageType' data by id:{}", id);
        }
        return true;
    }

    @Override
    public boolean modify(MsgMessageTypeDTO dto) {
        MsgMessageType entity = MsgMessageTypeConvert.INSTANCE.toDO(dto);
        boolean success = updateById(entity);
        if (!success) {
            log.warn("Update 'MsgMessageType' data fail, entity:{}", entity);
            return false;
        }
        // 修改成功的后续操作
        if (log.isDebugEnabled()) {
            log.debug("Update 'MsgMessageType' data by id:{}", dto.getId());
        }
        return true;
    }

    @Override
    public MsgMessageTypeDTO getDtoById(Serializable id) {
        if (id == null) {
            log.warn("id is null");
            return null;
        }
        MsgMessageType entity = getBaseMapper().selectById(id);
        return MsgMessageTypeConvert.INSTANCE.toDTO(entity);
    }

    @Override
    public Paging<MsgMessageTypeVO> getPage(MsgMessageTypePageParam param) {
        LambdaQueryWrapper<MsgMessageType> wrapper = queryWrapper();
        wrapper.select(
                MsgMessageType::getId,
                MsgMessageType::getTypeCode,
                MsgMessageType::getTypeName,
                MsgMessageType::getEnabled,
                MsgMessageType::getCreateBy,
                MsgMessageType::getCreateTime,
                MsgMessageType::getUpdateBy,
                MsgMessageType::getUpdateTime
        );
        PageInfo<MsgMessageType> page = page(pageInfo(param), wrapper);
        return MybatisPage.convert(page, MsgMessageTypeConvert.INSTANCE::toVO);
    }

    @Override
    public MsgMessageTypeDTO getByName(String typeName) {
        if (StrUtil.isBlank(typeName)) {
            throw new ServiceException("类型名称不能为空！");
        }
        MPJLambdaWrapper<MsgMessageType> wrapper = new MPJLambdaWrapper<>();
        wrapper.selectAll(MsgMessageType.class).eq(MsgMessageType::getTypeName, typeName);
        return MsgMessageTypeConvert.INSTANCE.toDTO(baseMapper.selectOne(wrapper));
    }

    @Override
    public MsgMessageTypeDTO getByCode(String typeCode) {
        if (StrUtil.isBlank(typeCode)) {
            throw new ServiceException("类型编码不能为空！");
        }
        MPJLambdaWrapper<MsgMessageType> wrapper = new MPJLambdaWrapper<>();
        wrapper.selectAll(MsgMessageType.class).eq(MsgMessageType::getTypeCode, typeCode);
        return MsgMessageTypeConvert.INSTANCE.toDTO(baseMapper.selectOne(wrapper));
    }

    @Override
    public List<MsgMessageTypeBaseVO> getEnabledPage(String keyword) {
        MPJLambdaWrapper<MsgMessageType> wrapper = new MPJLambdaWrapper<>();
        wrapper.selectAll(MsgMessageType.class).eq(MsgMessageType::getEnabled, 1)
                .and(e -> {
                    e.like(StrUtil.isNotBlank(keyword), MsgMessageType::getTypeName, keyword)
                            .or()
                            .like(StrUtil.isNotBlank(keyword), MsgMessageType::getTypeCode, keyword);
                });
        return MsgMessageTypeConvert.INSTANCE.toBaseVO(baseMapper.selectList(wrapper));
    }

}
