package com.carlos.message.channel.dingtalk;

import com.carlos.message.channel.AbstractChannelAdapter;
import com.carlos.message.core.channel.ChannelType;
import com.carlos.message.core.protocol.MessageContext;
import com.carlos.message.core.protocol.SendResult;
import com.carlos.message.manager.MessageChannelManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * <p>
 * 钉钉渠道适配器
 * </p>
 *
 * @author Carlos
 * @date 2026/3/12
 */
@Slf4j
@Component
public class DingtalkChannelAdapter extends AbstractChannelAdapter {

    public DingtalkChannelAdapter(MessageChannelManager messageChannelManager) {
        super(messageChannelManager);
    }

    @Override
    public String getChannelCode() {
        return "DINGTALK_WORK";
    }

    @Override
    public ChannelType getChannelType() {
        return ChannelType.DINGTALK;
    }

    @Override
    public boolean supports(String messageType) {
        // 钉钉渠道支持所有类型的消息
        return true;
    }

    @Override
    protected SendResult doSend(MessageContext context) {
        try {
            // 获取接收者ID（钉钉用户ID）
            String userId = context.getReceiverId();
            if (userId == null || userId.isEmpty()) {
                return SendResult.fail("接收者ID不能为空");
            }

            // 获取消息标题和内容
            String title = context.getMessageTitle();
            String content = context.getMessageContent();

            if (content == null || content.isEmpty()) {
                return SendResult.fail("消息内容不能为空");
            }

            // TODO: 集成钉钉 SDK
            // 实际代码应该是：
            // DingtalkClient client = createDingtalkClient();
            // OapiMessageCorpconversationAsyncsendV2Request request = new OapiMessageCorpconversationAsyncsendV2Request();
            // request.setUserId(userId);
            // request.setMsg(content);
            // OapiMessageCorpconversationAsyncsendV2Response response = client.send(request);

            // 模拟发送成功
            log.info("模拟发送钉钉工作通知: userId={}, title={}, content={}",
                userId, title, content);

            String channelMessageId = "DT" + System.currentTimeMillis();

            return SendResult.builder()
                .success(true)
                .channelMessageId(channelMessageId)
                // .messageId(context.getMessageId())
                .build();

        } catch (Exception e) {
            log.error("钉钉消息发送失败", e);
            return SendResult.fail("钉钉消息发送异常: " + e.getMessage());
        }
    }
}
