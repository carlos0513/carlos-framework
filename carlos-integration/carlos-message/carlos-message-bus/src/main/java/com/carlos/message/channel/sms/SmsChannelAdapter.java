package com.carlos.message.channel.sms;

import com.carlos.message.channel.AbstractChannelAdapter;
import com.carlos.message.core.channel.ChannelType;
import com.carlos.message.core.protocol.MessageContext;
import com.carlos.message.core.protocol.SendResult;
import com.carlos.message.manager.MessageChannelManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * <p>
 * 短信渠道适配器
 * </p>
 *
 * @author Carlos
 * @date 2026/3/12
 */
@Slf4j
@Component
public class SmsChannelAdapter extends AbstractChannelAdapter {

    public SmsChannelAdapter(MessageChannelManager messageChannelManager) {
        super(messageChannelManager);
    }

    @Override
    public String getChannelCode() {
        return "ALIYUN_SMS";
    }

    @Override
    public ChannelType getChannelType() {
        return ChannelType.SMS;
    }

    @Override
    public boolean supports(String messageType) {
        // 短信渠道支持验证码、通知类消息
        return "VERIFY_CODE".equals(messageType) ||
            "SYSTEM_NOTIFY".equals(messageType) ||
            "TASK_NOTIFY".equals(messageType) ||
            "APPROVAL_REMIND".equals(messageType);
    }

    @Override
    protected SendResult doSend(MessageContext context) {
        try {
            // 获取接收号码
            String phoneNumber = context.getReceiverNumber();
            if (phoneNumber == null || phoneNumber.isEmpty()) {
                return SendResult.failed("接收号码不能为空");
            }

            // 获取短信内容
            String content = context.getMessageContent();
            if (content == null || content.isEmpty()) {
                return SendResult.failed("短信内容不能为空");
            }

            // TODO: 集成实际的短信服务商 SDK（阿里云、腾讯云等）
            // 这里模拟发送成功
            log.info("模拟发送短信: phone={}, content={}", phoneNumber, content);

            // 模拟调用短信服务商 API
            // 实际代码应该是：
            // SmsClient client = createSmsClient();
            // SendSmsResponse response = client.sendSms(phoneNumber, content);
            // if (response.isSuccess()) {
            //     return SendResult.success(response.getMessageId());
            // } else {
            //     return SendResult.failed(response.getErrorMessage());
            // }

            // 模拟成功，生成渠道消息ID
            String channelMessageId = "SMS" + System.currentTimeMillis();

            return SendResult.builder()
                .success(true)
                .channelMessageId(channelMessageId)
                .build();

        } catch (Exception e) {
            log.error("短信发送失败", e);
            return SendResult.failed("短信发送异常: " + e.getMessage());
        }
    }
}
