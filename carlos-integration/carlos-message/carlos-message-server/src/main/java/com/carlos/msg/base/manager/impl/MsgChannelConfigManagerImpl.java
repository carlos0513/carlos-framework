package com.carlos.msg.base.manager.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.carlos.core.pagination.Paging;
import com.carlos.datasource.base.BaseServiceImpl;
import com.carlos.datasource.pagination.MybatisPage;
import com.carlos.datasource.pagination.PageInfo;
import com.carlos.msg.api.pojo.enums.ChannelType;
import com.carlos.msg.base.convert.MsgChannelConfigConvert;
import com.carlos.msg.base.manager.MsgChannelConfigManager;
import com.carlos.msg.base.mapper.MsgChannelConfigMapper;
import com.carlos.msg.base.pojo.dto.MsgChannelConfigDTO;
import com.carlos.msg.base.pojo.entity.MsgChannelConfig;
import com.carlos.msg.base.pojo.param.MsgChannelConfigPageParam;
import com.carlos.msg.base.pojo.vo.MsgChannelConfigVO;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * <p>
 * 消息渠道配置 查询封装实现类
 * </p>
 *
 * @author Carlos
 * @date 2025-3-10 10:53:03
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class MsgChannelConfigManagerImpl extends BaseServiceImpl<MsgChannelConfigMapper, MsgChannelConfig> implements MsgChannelConfigManager {

    @Override
    public boolean add(MsgChannelConfigDTO dto) {
        MsgChannelConfig entity = MsgChannelConfigConvert.INSTANCE.toDO(dto);
        boolean success = save(entity);
        if (!success) {
            log.warn("Insert 'MsgChannelConfig' data fail, entity:{}", entity);
            return false;
        }
        dto.setId(entity.getId());
        // 保存完成的后续
        if (log.isDebugEnabled()) {
            log.debug("Insert 'MsgChannelConfig' data: id:{}", entity.getId());
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
            log.warn("Remove 'MsgChannelConfig' data fail, id:{}", id);
            return false;
        }
        if (log.isDebugEnabled()) {
            log.debug("Remove 'MsgChannelConfig' data by id:{}", id);
        }
        return true;
    }

    @Override
    public boolean modify(MsgChannelConfigDTO dto) {
        MsgChannelConfig entity = MsgChannelConfigConvert.INSTANCE.toDO(dto);
        boolean success = updateById(entity);
        if (!success) {
            log.warn("Update 'MsgChannelConfig' data fail, entity:{}", entity);
            return false;
        }
        // 修改成功的后续操作
        if (log.isDebugEnabled()) {
            log.debug("Update 'MsgChannelConfig' data by id:{}", dto.getId());
        }
        return true;
    }

    @Override
    public MsgChannelConfigDTO getDtoById(Serializable id) {
        if (id == null) {
            log.warn("id is null");
            return null;
        }
        MsgChannelConfig entity = getBaseMapper().selectById(id);
        return MsgChannelConfigConvert.INSTANCE.toDTO(entity);
    }

    @Override
    public Paging<MsgChannelConfigVO> getPage(MsgChannelConfigPageParam param) {
        LambdaQueryWrapper<MsgChannelConfig> wrapper = queryWrapper();
        wrapper.select(
                MsgChannelConfig::getId,
                MsgChannelConfig::getChannelType,
                MsgChannelConfig::getChannelName,
                MsgChannelConfig::getRemark,
                MsgChannelConfig::getEnabled,
                MsgChannelConfig::getCreateBy,
                MsgChannelConfig::getCreateTime,
                MsgChannelConfig::getUpdateBy,
                MsgChannelConfig::getUpdateTime
        );
        PageInfo<MsgChannelConfig> page = page(pageInfo(param), wrapper);
        return MybatisPage.convert(page, MsgChannelConfigConvert.INSTANCE::toVO);
    }

    @Override
    public MsgChannelConfigDTO getByType(ChannelType channelType) {

        MPJLambdaWrapper<MsgChannelConfig> wrapper = new MPJLambdaWrapper<>();
        wrapper.selectAll(MsgChannelConfig.class).eq(MsgChannelConfig::getChannelType, channelType);
        return MsgChannelConfigConvert.INSTANCE.toDTO(baseMapper.selectOne(wrapper));
    }

    @Override
    public boolean updateState(Serializable id, Boolean enabled) {
        if (id == null) {
            log.warn("id is null");
            return false;
        }
        return lambdaUpdate()
                .eq(MsgChannelConfig::getId, id)
                .set(MsgChannelConfig::getEnabled, enabled)
                .update();
    }
}
