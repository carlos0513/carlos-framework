package com.carlos.message.manager.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.carlos.core.pagination.Paging;
import com.carlos.datasource.base.BaseServiceImpl;
import com.carlos.datasource.pagination.MybatisPage;
import com.carlos.datasource.pagination.PageInfo;
import com.carlos.message.convert.MessageReceiverConvert;
import com.carlos.message.manager.MessageReceiverManager;
import com.carlos.message.mapper.MessageReceiverMapper;
import com.carlos.message.pojo.dto.MessageReceiverDTO;
import com.carlos.message.pojo.entity.MessageReceiver;
import com.carlos.message.pojo.param.MessageReceiverPageParam;
import com.carlos.message.pojo.vo.MessageReceiverVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 消息接收人表 查询封装实现类
 * </p>
 *
 * @author Carlos
 * @date 2026年3月12日 上午11:17:05
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class MessageReceiverManagerImpl extends BaseServiceImpl<MessageReceiverMapper, MessageReceiver> implements MessageReceiverManager {

    @Override
    public boolean add(MessageReceiverDTO dto) {
        MessageReceiver entity = MessageReceiverConvert.INSTANCE.toDO(dto);
        boolean success = save(entity);
        if (!success) {
            log.warn("Insert 'MessageReceiver' data fail, entity:{}", entity);
            return false;
        }
        dto.setId(entity.getId());
        // 保存完成的后续
        log.debug("Insert 'MessageReceiver' data: id:{}", entity.getId());
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
            log.warn("Remove 'MessageReceiver' data fail, id:{}", id);
            return false;
        }
        log.debug("Remove 'MessageReceiver' data by id:{}", id);
        return true;
    }

    @Override
    public boolean modify(MessageReceiverDTO dto) {
        MessageReceiver entity = MessageReceiverConvert.INSTANCE.toDO(dto);
        boolean success = updateById(entity);
        if (!success) {
            log.warn("Update 'MessageReceiver' data fail, entity:{}", entity);
            return false;
        }
        // 修改成功的后续操作
        log.debug("Update 'MessageReceiver' data by id:{}", dto.getId());
        return true;
    }

    @Override
    public MessageReceiverDTO getDtoById(Serializable id) {
        if (id == null) {
            log.warn("id is null");
            return null;
        }
        MessageReceiver entity = getBaseMapper().selectById(id);
        return MessageReceiverConvert.INSTANCE.toDTO(entity);
    }

    @Override
    public Paging<MessageReceiverVO> getPage(MessageReceiverPageParam param) {
        LambdaQueryWrapper<MessageReceiver> wrapper = queryWrapper();
        wrapper.select(

            MessageReceiver::getId,
            MessageReceiver::getMessageId,
            MessageReceiver::getChannelCode,
            MessageReceiver::getReceiverId,
            MessageReceiver::getReceiverType,
            MessageReceiver::getReceiverNumber,
            MessageReceiver::getReceiverAudience,
            MessageReceiver::getChannelMessageId,
            MessageReceiver::getStatus,
            MessageReceiver::getFailCount,
            MessageReceiver::getFailReason,
            MessageReceiver::getScheduleTime,
            MessageReceiver::getSendTime,
            MessageReceiver::getDeliverTime,
            MessageReceiver::getReadTime,
            MessageReceiver::getCallbackUrl,
            MessageReceiver::getExtras,
            MessageReceiver::getCreateBy,
            MessageReceiver::getCreateTime,
            MessageReceiver::getUpdateBy,
            MessageReceiver::getUpdateTime
        );
        PageInfo<MessageReceiver> page = page(pageInfo(param), wrapper);
        return MybatisPage.convert(page, MessageReceiverConvert.INSTANCE::toVO);
    }

    @Override
    public List<MessageReceiver> listByMessageId(String messageId) {
        LambdaQueryWrapper<MessageReceiver> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MessageReceiver::getMessageId, messageId);
        return baseMapper.selectList(wrapper);
    }

    @Override
    public List<MessageReceiver> listByReceiverAndStatus(String receiverId, Integer status) {
        LambdaQueryWrapper<MessageReceiver> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MessageReceiver::getReceiverId, receiverId)
            .eq(MessageReceiver::getStatus, status)
            .orderByDesc(MessageReceiver::getCreateTime);
        return baseMapper.selectList(wrapper);
    }

    @Override
    public boolean updateStatus(Long id, Integer status) {
        LambdaUpdateWrapper<MessageReceiver> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(MessageReceiver::getId, id)
            .set(MessageReceiver::getStatus, status)
            .set(MessageReceiver::getUpdateTime, LocalDateTime.now());
        return baseMapper.update(null, wrapper) > 0;
    }

    @Override
    public boolean incrementFailCount(Long id, String failReason) {
        LambdaUpdateWrapper<MessageReceiver> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(MessageReceiver::getId, id)
            .setSql("fail_count = fail_count + 1")
            .set(MessageReceiver::getFailReason, failReason)
            .set(MessageReceiver::getUpdateTime, LocalDateTime.now());
        return baseMapper.update(null, wrapper) > 0;
    }

    @Override
    public List<MessageReceiver> listScheduledPending(LocalDateTime before) {
        LambdaQueryWrapper<MessageReceiver> wrapper = new LambdaQueryWrapper<>();
        wrapper.isNotNull(MessageReceiver::getScheduleTime)
            .le(MessageReceiver::getScheduleTime, before)
            .eq(MessageReceiver::getStatus, 0)
            .orderByAsc(MessageReceiver::getScheduleTime);
        return baseMapper.selectList(wrapper);
    }
}
