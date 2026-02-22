package com.carlos.msg.base.apiimpl;


import com.carlos.core.response.Result;
import com.carlos.msg.api.ApiMsgMessage;
import com.carlos.msg.api.pojo.ao.MsgSendResponseAO;
import com.carlos.msg.api.pojo.param.ApiMsgSendParam;
import com.carlos.msg.sender.MsgSenderService;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 消息 api接口
 * </p>
 *
 * @author Carlos
 * @date 2025-3-10 10:53:03
 */
@Hidden
@RestController
@RequiredArgsConstructor
@RequestMapping("api/message")
@Tag(name = "消息Feign接口")
public class ApiMsgMessageImpl implements ApiMsgMessage {

    private final MsgSenderService senderService;


    @PostMapping("send")
    public Result<MsgSendResponseAO> msgSend(ApiMsgSendParam param) {
        senderService.msgSend(param);
        return null;
    }


}
