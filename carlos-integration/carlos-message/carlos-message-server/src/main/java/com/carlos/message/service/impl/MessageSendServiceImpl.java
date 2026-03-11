package com.carlos.message.service.impl;

import com.carlos.core.response.Result;
import com.carlos.message.manager.MessageRecordManager;
import com.carlos.message.manager.MessageReceiverManager;
import com.carlos.message.pojo.entity.MessageRecord;
import com.carlos.message.pojo.entity.MessageReceiver;
import com.carlos.message.pojo.enums.MessageReceiverStatusEnum;
import com.carlos.message.pojo.param.MessageCreateParam;
import com.carlos.message.service.MessageSendService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * <p>
 * 消息发送服务实现
 * </p>
 *
 * @author Carlos
 * @date 2026/3/11
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MessageSendServiceImpl implements MessageSendService {

    private final MessageRecordManager messageRecordManager;
    private final MessageReceiverManager messageReceiverManager;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<String> send(MessageCreateParam param) {
        log.info("同步发送消息: {}", param);
        
        // 生成消息ID
        String messageId = generateMessageId();
        
        // 保存消息记录
        MessageRecord record = new MessageRecord();
        record.setMessageId(messageId);
        record.setTemplateCode(param.getTemplateCode());
        record.setSenderId(param.getSenderId());
        record.setSenderName(param.getSenderName());
        record.setSenderSystem(param.getSenderSystem());
        record.setPriority(param.getPriority());
        record.setTotalCount(param.getReceivers().size());
        record.setSuccessCount(0);
        record.setFailCount(0);
        messageRecordManager.save(record);
        
        // 保存接收人记录
        List<MessageReceiver> receivers = new ArrayList<>();
        for (MessageCreateParam.ReceiverParam receiverParam : param.getReceivers()) {
            MessageReceiver receiver = new MessageReceiver();
            receiver.setMessageId(messageId);
            receiver.setReceiverId(receiverParam.getReceiverId());
            receiver.setReceiverType(receiverParam.getReceiverType());
            receiver.setReceiverNumber(receiverParam.getReceiverNumber());
            receiver.setStatus(MessageReceiverStatusEnum.PENDING.getCode());
            receiver.setFailCount(0);
            receivers.add(receiver);
        }
        messageReceiverManager.saveBatch(receivers);
        
        // TODO: 调用渠道发送
        
        return Result.ok(messageId);
    }

    @Override
    public Result<String> sendAsync(MessageCreateParam param) {
        log.info("异步发送消息: {}", param);
        // TODO: 实现异步发送（发送到消息队列）
        String messageId = generateMessageId();
        return Result.ok(messageId);
    }

    @Override
    public Result<String> schedule(MessageCreateParam param, LocalDateTime scheduleTime) {
        log.info("定时发送消息, 时间: {}", scheduleTime);
        // TODO: 实现定时发送
        String messageId = generateMessageId();
        return Result.ok(messageId);
    }

    @Override
    public Result<Boolean> revoke(String messageId) {
        log.info("撤回消息: {}", messageId);
        // TODO: 实现撤回逻辑
        return Result.ok(true);
    }

    private String generateMessageId() {
        return "MSG" + System.currentTimeMillis() + UUID.randomUUID().toString().substring(0, 8);
    }
}
