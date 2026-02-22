package com.carlos.msg.sender;

import cn.hutool.core.util.StrUtil;
import com.carlos.core.exception.ServiceException;
import com.carlos.core.response.Result;
import com.carlos.msg.api.pojo.param.ApiMsgSendParam;
import com.carlos.msg.base.pojo.dto.MsgContext;
import com.carlos.msg.base.pojo.dto.MsgMessageDTO;
import com.carlos.msg.base.pojo.dto.MsgMessageReceiverDTO;
import com.carlos.msg.base.pojo.dto.MsgMessageTemplateDTO;
import com.carlos.msg.base.service.MsgMessageReceiverService;
import com.carlos.msg.base.service.MsgMessageService;
import com.carlos.msg.base.service.MsgMessageTemplateService;
import com.carlos.msg.sender.channel.Channel;
import com.carlos.msg.sender.channel.MsgChannelFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


/**
 * <p>
 * 消息发送主业务
 * </p>
 *
 * @author Carlos
 * @date 2025-3-10 10:53:04
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MsgSenderService {

    private final MsgChannelFactory channelManager;
    private final MsgMessageService messageService;
    private final MsgMessageReceiverService receiverService;
    private final MsgMessageTemplateService templateService;


    public Result<Object> msgSend(ApiMsgSendParam param) {
        String templateCode = param.getTemplateCode();
        // 检查模板
        MsgMessageTemplateDTO template = templateService.getTemplateByCode(templateCode);
        if (template == null) {
            throw new ServiceException("无效模板");
        }
        String templateContent = template.getTemplateContent();
        String msgContent = StrUtil.format(templateContent, param.getData());
        String channelConfig = template.getChannelConfig();

        MsgMessageDTO dto = new MsgMessageDTO();
        // messageService.checkMessage(dto);
        // 持久化消息数据  消息本体，接受者信息
        // 调用

        // 构建消息发送上下文  类似HttpRequest   XxxContext
        MsgContext context = buildContext();

        // 发送消息
        send(context);
        messageService.addMsgMessage(dto);
        MsgMessageReceiverDTO receiver = new MsgMessageReceiverDTO();
        // TODO 2025/3/17 构建消息接受者对象
        receiverService.addMsgMessageReceiver(receiver);
        return Result.result(true);
    }

    public MsgContext buildContext() {
        // TODO 构建消息发送上下文
        MsgContext context = new MsgContext();
        return context;
    }

    public void send(MsgContext context) {

        // 遍历渠道

        Channel channerl = channelManager.getChannel(null);
        // channerl.send(null);

        // 发送结果记录   持久化


    }


}
