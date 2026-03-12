package com.carlos.message.manager.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.carlos.core.pagination.Paging;
import com.carlos.datasource.base.BaseServiceImpl;
import com.carlos.datasource.pagination.MybatisPage;
import com.carlos.datasource.pagination.PageInfo;
import com.carlos.message.convert.MessageChannelConvert;
import com.carlos.message.manager.MessageChannelManager;
import com.carlos.message.mapper.MessageChannelMapper;
import com.carlos.message.pojo.dto.MessageChannelDTO;
import com.carlos.message.pojo.entity.MessageChannel;
import com.carlos.message.pojo.param.MessageChannelPageParam;
import com.carlos.message.pojo.vo.MessageChannelVO;
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
 * @date 2026年3月12日 上午11:17:05
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class MessageChannelManagerImpl extends BaseServiceImpl<MessageChannelMapper, MessageChannel> implements MessageChannelManager {

    @Override
    public boolean add(MessageChannelDTO dto) {
        MessageChannel entity = MessageChannelConvert.INSTANCE.toDO(dto);
        boolean success = save(entity);
        if (!success) {
            log.warn("Insert 'MessageChannel' data fail, entity:{}", entity);
            return false;
        }
        dto.setId(entity.getId());
        // 保存完成的后续
        if (log.isDebugEnabled()) {
            log.debug("Insert 'MessageChannel' data: id:{}", entity.getId());
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
            log.warn("Remove 'MessageChannel' data fail, id:{}", id);
            return false;
        }
        if (log.isDebugEnabled()) {
            log.debug("Remove 'MessageChannel' data by id:{}", id);
        }
        return true;
    }

    @Override
    public boolean modify(MessageChannelDTO dto) {
        MessageChannel entity = MessageChannelConvert.INSTANCE.toDO(dto);
        boolean success = updateById(entity);
        if (!success) {
            log.warn("Update 'MessageChannel' data fail, entity:{}", entity);
            return false;
        }
        // 修改成功的后续操作
        if (log.isDebugEnabled()) {
            log.debug("Update 'MessageChannel' data by id:{}", dto.getId());
        }
        return true;
    }

    @Override
    public MessageChannelDTO getDtoById(Serializable id) {
        if (id == null) {
            log.warn("id is null");
            return null;
        }
        MessageChannel entity = getBaseMapper().selectById(id);
        return MessageChannelConvert.INSTANCE.toDTO(entity);
    }

    @Override
    public Paging<MessageChannelVO> getPage(MessageChannelPageParam param) {
        LambdaQueryWrapper<MessageChannel> wrapper = queryWrapper();
        wrapper.select(

            MessageChannel::getId,
            MessageChannel::getChannelCode,
            MessageChannel::getChannelName,
            MessageChannel::getChannelType,
            MessageChannel::getProvider,
            MessageChannel::getChannelConfig,
            MessageChannel::getRateLimitQps,
            MessageChannel::getRateLimitBurst,
            MessageChannel::getRetryTimes,
            MessageChannel::getRetryInterval,
            MessageChannel::getWeight,
            MessageChannel::getEnabled,
            MessageChannel::getCreateBy,
            MessageChannel::getCreateTime,
            MessageChannel::getUpdateBy,
            MessageChannel::getUpdateTime
        );
        PageInfo<MessageChannel> page = page(pageInfo(param), wrapper);
        return MybatisPage.convert(page, MessageChannelConvert.INSTANCE::toVO);
    }

}
