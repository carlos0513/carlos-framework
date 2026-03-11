package com.carlos.message.manager.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.carlos.message.manager.MessageReceiverManager;
import com.carlos.message.mapper.MessageReceiverMapper;
import com.carlos.message.pojo.entity.MessageReceiver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>
 * 消息接收人 Manager 实现
 * </p>
 *
 * @author Carlos
 * @date 2026/3/11
 */
@Slf4j
@Component
public class MessageReceiverManagerImpl extends ServiceImpl<MessageReceiverMapper, MessageReceiver> implements MessageReceiverManager {

    @Override
    public List<MessageReceiver> listByMessageId(String messageId) {
        return baseMapper.selectByMessageId(messageId);
    }

    @Override
    public List<MessageReceiver> listByReceiverAndStatus(String receiverId, Integer status) {
        return baseMapper.selectByReceiverAndStatus(receiverId, status);
    }

    @Override
    public boolean updateStatus(Long id, Integer status) {
        return baseMapper.updateStatus(id, status) > 0;
    }

    @Override
    public boolean incrementFailCount(Long id, String failReason) {
        return baseMapper.incrementFailCount(id, failReason) > 0;
    }
}
