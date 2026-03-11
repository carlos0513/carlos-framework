package com.carlos.message.apiimpl;

import com.carlos.core.response.Result;
import com.carlos.message.api.MessageSendApi;
import com.carlos.message.pojo.ao.MessageStatusAO;
import com.carlos.message.pojo.ao.SendResultAO;
import com.carlos.message.pojo.param.MessageCreateParam;
import com.carlos.message.pojo.param.MessageSendParam;
import com.carlos.message.service.MessageQueryService;
import com.carlos.message.service.MessageSendService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 消息发送服务 API 实现
 * </p>
 *
 * @author Carlos
 * @date 2026/3/11
 */
@Slf4j
@RestController
@RequestMapping("/api/message")
@RequiredArgsConstructor
@Tag(name = "消息发送服务")
public class MessageSendApiImpl implements MessageSendApi {

    private final MessageSendService messageSendService;
    private final MessageQueryService messageQueryService;

    @Override
    @PostMapping("/send")
    @Operation(summary = "同步发送消息")
    public Result<SendResultAO> send(@RequestBody MessageSendParam param) {
        log.info("发送消息请求: {}", param);
        MessageCreateParam createParam = new MessageCreateParam();
        BeanUtils.copyProperties(param, createParam);
        
        Result<String> result = messageSendService.send(createParam);
        if (!result.isSuccess()) {
            return Result.fail(result.getMessage());
        }
        SendResultAO ao = new SendResultAO();
        ao.setMessageId(result.getData());
        ao.setSuccess(true);
        return Result.ok(ao);
    }

    @Override
    @PostMapping("/sendAsync")
    @Operation(summary = "异步发送消息")
    public Result<String> sendAsync(@RequestBody MessageSendParam param) {
        log.info("异步发送消息请求: {}", param);
        MessageCreateParam createParam = new MessageCreateParam();
        BeanUtils.copyProperties(param, createParam);
        return messageSendService.sendAsync(createParam);
    }

    @Override
    @PostMapping("/sendBatch")
    @Operation(summary = "批量发送消息")
    public Result<SendResultAO> sendBatch(@RequestBody List<MessageSendParam> params) {
        log.info("批量发送消息请求, 数量: {}", params.size());
        return Result.fail("批量发送功能待实现");
    }

    @Override
    @PostMapping("/schedule")
    @Operation(summary = "定时发送消息")
    public Result<String> schedule(
            @RequestBody MessageSendParam param,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime scheduleTime) {
        log.info("定时发送消息请求, 时间: {}", scheduleTime);
        MessageCreateParam createParam = new MessageCreateParam();
        BeanUtils.copyProperties(param, createParam);
        return messageSendService.schedule(createParam, scheduleTime);
    }

    @Override
    @PostMapping("/{messageId}/revoke")
    @Operation(summary = "撤回消息")
    public Result<Boolean> revoke(@PathVariable String messageId) {
        log.info("撤回消息请求, messageId: {}", messageId);
        return messageSendService.revoke(messageId);
    }

    @Override
    @GetMapping("/{messageId}/status")
    @Operation(summary = "查询消息状态")
    public Result<MessageStatusAO> queryStatus(@PathVariable String messageId) {
        log.info("查询消息状态请求, messageId: {}", messageId);
        Result<Integer> statusResult = messageQueryService.queryStatus(messageId);
        if (!statusResult.isSuccess()) {
            return Result.fail(statusResult.getMessage());
        }
        MessageStatusAO ao = new MessageStatusAO();
        ao.setMessageId(messageId);
        ao.setStatus(statusResult.getData());
        return Result.ok(ao);
    }
}
