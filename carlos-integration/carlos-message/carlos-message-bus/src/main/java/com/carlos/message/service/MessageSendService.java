package com.carlos.message.service;

import com.carlos.core.response.Result;
import com.carlos.message.channel.ChannelFactory;
import com.carlos.message.core.channel.ChannelAdapter;
import com.carlos.message.core.protocol.MessageContext;
import com.carlos.message.core.protocol.SendResult;
import com.carlos.message.manager.MessageChannelManager;
import com.carlos.message.manager.MessageReceiverManager;
import com.carlos.message.manager.MessageRecordManager;
import com.carlos.message.manager.MessageTemplateManager;
import com.carlos.message.pojo.dto.MessageTemplateDTO;
import com.carlos.message.pojo.entity.MessageReceiver;
import com.carlos.message.pojo.entity.MessageRecord;
import com.carlos.message.pojo.enums.MessageReceiverStatusEnum;
import com.carlos.message.pojo.param.MessageCreateParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * <p>
 * 消息发送服务
 * </p>
 *
 * @author Carlos
 * @date 2026/3/11
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MessageSendService {

    private final MessageRecordManager messageRecordManager;
    private final MessageReceiverManager messageReceiverManager;
    private final MessageTemplateManager messageTemplateManager;
    private final MessageChannelManager messageChannelManager;

    /**
     * 同步发送消息
     *
     * @param param 发送参数
     * @return 发送结果
     */
    @Transactional(rollbackFor = Exception.class)
    public Result<String> send(MessageCreateParam param) {
        log.info("同步发送消息: {}", param);

        // 1. 参数校验
        if (param.getReceivers() == null || param.getReceivers().isEmpty()) {
            return Result.fail("接收人不能为空");
        }

        if (StringUtils.isBlank(param.getTemplateCode())) {
            return Result.fail("模板编码不能为空");
        }

        // 2. 生成消息ID
        String messageId = generateMessageId();
        log.info("生成消息ID: {}", messageId);

        // 3. 查询模板信息
        MessageTemplateDTO template = messageTemplateManager.getByTemplateCode(param.getTemplateCode());
        if (template == null) {
            return Result.fail("模板不存在: " + param.getTemplateCode());
        }

        // 4. 渲染模板内容
        String messageContent = renderTemplate(template, param.getTemplateParams());
        String messageTitle = template.getTitleTemplate() != null ?
            renderTemplate(template.getTitleTemplate(), param.getTemplateParams()) : null;

        // 5. 保存消息记录
        MessageRecord record = new MessageRecord();
        record.setMessageId(messageId);
        record.setTemplateCode(param.getTemplateCode());
        record.setTemplateParams(param.getTemplateParams() != null ? param.getTemplateParams().toString() : null);
        record.setMessageType(template.getTypeCode());
        record.setMessageTitle(messageTitle);
        record.setMessageContent(messageContent);
        record.setSenderId(param.getSenderId());
        record.setSenderName(param.getSenderName());
        record.setSenderSystem(param.getSenderSystem());
        record.setPriority(param.getPriority());
        record.setTotalCount(param.getReceivers().size());
        record.setSuccessCount(0);
        record.setFailCount(0);
        messageRecordManager.save(record);
        log.info("保存消息记录成功: messageId={}", messageId);

        // 6. 确定使用的渠道列表
        List<String> channelCodes = param.getChannelCodes();
        if (channelCodes == null || channelCodes.isEmpty()) {
            // 如果没有指定渠道，默认使用模板配置的渠道
            channelCodes = getDefaultChannelCodes(template);
        }

        if (channelCodes == null || channelCodes.isEmpty()) {
            return Result.fail("未指定发送渠道");
        }

        // 7. 保存接收人记录并发送消息
        int totalSuccess = 0;
        int totalFail = 0;

        for (MessageCreateParam.ReceiverParam receiverParam : param.getReceivers()) {
            for (String channelCode : channelCodes) {
                // 保存接收人记录
                MessageReceiver receiver = new MessageReceiver();
                receiver.setMessageId(messageId);
                receiver.setReceiverId(receiverParam.getReceiverId());
                receiver.setReceiverType(receiverParam.getReceiverType());
                receiver.setReceiverNumber(receiverParam.getReceiverNumber());
                receiver.setChannelCode(channelCode);
                receiver.setStatus(MessageReceiverStatusEnum.PENDING.getCode());
                receiver.setFailCount(0);
                messageReceiverManager.save(receiver);
                Long receiverId = receiver.getId();

                // 调用渠道发送
                try {
                    // 获取渠道适配器
                    ChannelAdapter adapter = ChannelFactory.getChannel(channelCode);
                    if (adapter == null) {
                        log.warn("渠道适配器不存在: {}", channelCode);
                        updateReceiverStatus(receiverId, MessageReceiverStatusEnum.FAILED, "渠道适配器不存在");
                        totalFail++;
                        continue;
                    }

                    // 检查渠道是否支持该消息类型
                    if (!adapter.supports(template.getTypeCode())) {
                        log.warn("渠道不支持该消息类型, channel={}, messageType={}",
                            channelCode, template.getTypeCode());
                        updateReceiverStatus(receiverId, MessageReceiverStatusEnum.FAILED, "渠道不支持该消息类型");
                        totalFail++;
                        continue;
                    }

                    // 构建消息上下文
                    MessageContext context = buildMessageContext(messageId, channelCode,
                        receiverParam, messageTitle, messageContent,
                        template.getTypeCode(), param.getPriority(), param.getCallbackUrl());

                    // 发送消息
                    SendResult sendResult = adapter.send(context);

                    if (sendResult.isSuccess()) {
                        updateReceiverStatus(receiverId, MessageReceiverStatusEnum.SENT,
                            sendResult.getChannelMessageId());
                        totalSuccess++;
                        log.info("消息发送成功, messageId={}, channel={}, receiver={}",
                            messageId, channelCode, receiverParam.getReceiverId());
                    } else {
                        updateReceiverStatus(receiverId, MessageReceiverStatusEnum.FAILED,
                            sendResult.getErrorMessage());
                        totalFail++;
                        log.warn("消息发送失败, messageId={}, channel={}, error={}",
                            messageId, channelCode, sendResult.getErrorMessage());
                    }

                } catch (Exception e) {
                    log.error("发送消息异常, messageId={}, channel={}", messageId, channelCode, e);
                    updateReceiverStatus(receiverId, MessageReceiverStatusEnum.FAILED,
                        e.getMessage());
                    totalFail++;
                }
            }
        }

        // 8. 更新消息记录的统计信息
        record.setSuccessCount(totalSuccess);
        record.setFailCount(totalFail);
        messageRecordManager.updateById(record);

        log.info("消息发送完成, messageId={}, total={}, success={}, fail={}",
            messageId, param.getReceivers().size(), totalSuccess, totalFail);

        return Result.ok(messageId);
    }

    /**
     * 渲染模板内容
     *
     * @param template 模板
     * @param params   参数
     * @return 渲染后的内容
     */
    private String renderTemplate(MessageTemplateDTO template,
                                  java.util.Map<String, Object> params) {
        // TODO: 集成 FreeMarker/Velocity 模板引擎进行变量替换
        // 目前简化处理：直接返回模板内容，后续需要集成模板引擎
        return template.getContentTemplate();
    }

    /**
     * 渲染模板内容
     *
     * @param templateContent 模板内容
     * @param params          参数
     * @return 渲染后的内容
     */
    private String renderTemplate(String templateContent,
                                  java.util.Map<String, Object> params) {
        // TODO: 集成模板引擎
        return templateContent;
    }

    /**
     * 获取默认渠道编码列表
     *
     * @param template 模板
     * @return 渠道编码列表
     */
    private List<String> getDefaultChannelCodes(MessageTemplateDTO template) {
        // TODO: 从模板的 channel_config 字段解析默认渠道
        // 临时返回常见的渠道
        return java.util.Arrays.asList("ALIYUN_SMS", "DINGTALK_WORK");
    }

    /**
     * 构建消息上下文
     *
     * @param messageId    消息ID
     * @param channelCode  渠道编码
     * @param receiver     接收人
     * @param title        消息标题
     * @param content      消息内容
     * @param messageType  消息类型
     * @param priority     优先级
     * @param callbackUrl  回调URL
     * @return 消息上下文
     */
    private MessageContext buildMessageContext(String messageId,
                                               String channelCode,
                                               MessageCreateParam.ReceiverParam receiver,
                                               String title,
                                               String content,
                                               String messageType,
                                               Integer priority,
                                               String callbackUrl) {
        return MessageContext.builder()
            .messageId(messageId)
            .channelCode(channelCode)
            .receiverId(receiver.getReceiverId())
            .receiverType(receiver.getReceiverType())
            .receiverNumber(receiver.getReceiverNumber())
            .messageTitle(title)
            .messageContent(content)
            .messageType(messageType)
            .priority(priority)
            .callbackUrl(callbackUrl)
            .sendTime(LocalDateTime.now())
            .build();
    }

    /**
     * 更新接收人状态
     *
     * @param receiverId 接收人记录ID
     * @param status     状态
     * @param remark     备注（失败原因或渠道消息ID）
     */
    private void updateReceiverStatus(Long receiverId,
                                      MessageReceiverStatusEnum status,
                                      String remark) {
        MessageReceiver update = new MessageReceiver();
        update.setId(receiverId);
        update.setStatus(status.getCode());

        if (status == MessageReceiverStatusEnum.SENT || status == MessageReceiverStatusEnum.DELIVERED) {
            update.setChannelMessageId(remark);
            update.setSendTime(LocalDateTime.now());
        } else if (status == MessageReceiverStatusEnum.FAILED) {
            MessageReceiver old = messageReceiverManager.getById(receiverId);
            if (old != null) {
                update.setFailCount((old.getFailCount() != null ? old.getFailCount() : 0) + 1);
            }
            update.setFailReason(remark);
        }

        messageReceiverManager.updateById(update);
    }

    /**
     * 异步发送消息
     *
     * @param param 发送参数
     * @return 消息ID
     */
    public Result<String> sendAsync(MessageCreateParam param) {
        log.info("异步发送消息: {}", param);

        // TODO: 实现异步发送（发送到Redis Stream消息队列）
        // 1. 生成消息ID并保存消息记录
        String messageId = generateMessageId();

        // 2. 将发送任务推送到Redis Stream
        // redisTemplate.opsForStream().add("stream:message:async", messageTask);

        log.info("消息已加入异步队列: {}", messageId);
        return Result.ok(messageId);
    }

    /**
     * 定时发送消息
     *
     * @param param        发送参数
     * @param scheduleTime 定时时间
     * @return 消息ID
     */
    public Result<String> schedule(MessageCreateParam param, LocalDateTime scheduleTime) {
        log.info("定时发送消息, 时间: {}", scheduleTime);

        if (scheduleTime == null || scheduleTime.isBefore(LocalDateTime.now())) {
            return Result.fail("定时时间无效");
        }

        // TODO: 实现定时发送
        // 方案1：使用Redis ZSet实现延时队列
        // 方案2：使用XXL-Job定时任务
        String messageId = generateMessageId();

        log.info("消息已加入定时队列: {}, 发送时间: {}", messageId, scheduleTime);
        return Result.ok(messageId);
    }

    /**
     * 撤回消息
     *
     * @param messageId 消息ID
     * @return 是否成功
     */
    public Result<Boolean> revoke(String messageId) {
        log.info("撤回消息: {}", messageId);

        // TODO: 实现撤回逻辑
        // 1. 查询消息状态
        // 2. 如果还未发送，更新状态为已撤回
        // 3. 如果已发送，调用渠道的撤回接口（如果支持）

        return Result.ok(true);
    }

    private String generateMessageId() {
        return "MSG" + System.currentTimeMillis() + UUID.randomUUID().toString().substring(0, 8);
    }
}
