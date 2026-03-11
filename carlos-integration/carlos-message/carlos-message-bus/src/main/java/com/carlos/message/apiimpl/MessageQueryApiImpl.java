package com.carlos.message.apiimpl;

import com.carlos.core.pojo.response.Paging;
import com.carlos.core.response.Result;
import com.carlos.message.api.MessageQueryApi;
import com.carlos.message.pojo.ao.MessageRecordAO;
import com.carlos.message.pojo.ao.MessageReceiverAO;
import com.carlos.message.pojo.param.MessagePageParam;
import com.carlos.message.service.MessageQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 消息查询服务 API 实现
 * </p>
 *
 * @author Carlos
 * @date 2026/3/11
 */
@Slf4j
@RestController
@RequestMapping("/api/message")
@RequiredArgsConstructor
@Tag(name = "消息查询服务")
public class MessageQueryApiImpl implements MessageQueryApi {

    private final MessageQueryService messageQueryService;

    @Override
    @GetMapping("/{messageId}")
    @Operation(summary = "查询消息记录详情")
    public Result<MessageRecordAO> getById(@PathVariable String messageId) {
        log.info("查询消息详情, messageId: {}", messageId);
        // TODO: 实现转换
        return Result.fail("功能待实现");
    }

    @Override
    @PostMapping("/page")
    @Operation(summary = "分页查询消息记录")
    public Result<Paging<MessageRecordAO>> page(@RequestBody MessagePageParam param) {
        log.info("分页查询消息记录, param: {}", param);
        // TODO: 实现转换
        return Result.fail("功能待实现");
    }

    @Override
    @GetMapping("/{messageId}/receivers")
    @Operation(summary = "查询消息接收人列表")
    public Result<List<MessageReceiverAO>> getReceivers(@PathVariable String messageId) {
        log.info("查询消息接收人列表, messageId: {}", messageId);
        // TODO: 实现转换
        return Result.fail("功能待实现");
    }

    @Override
    @GetMapping("/unread/{userId}")
    @Operation(summary = "查询用户的未读消息")
    public Result<List<MessageRecordAO>> getUnread(@PathVariable String userId) {
        log.info("查询用户未读消息, userId: {}", userId);
        // TODO: 实现转换
        return Result.fail("功能待实现");
    }
}
