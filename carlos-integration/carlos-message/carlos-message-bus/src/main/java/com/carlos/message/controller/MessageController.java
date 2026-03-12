package com.carlos.message.controller;

import com.carlos.core.pagination.Paging;
import com.carlos.core.response.Result;
import com.carlos.message.convert.MessageConvert;
import com.carlos.message.pojo.dto.MessageRecordDTO;
import com.carlos.message.pojo.param.MessageCreateParam;
import com.carlos.message.pojo.vo.MessageRecordVO;
import com.carlos.message.service.MessageQueryService;
import com.carlos.message.service.MessageSendService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

/**
 * <p>
 * 消息管理控制器
 * </p>
 *
 * @author Carlos
 * @date 2026/3/11
 */
@Slf4j
@RestController
@RequestMapping("/message")
@RequiredArgsConstructor
@Tag(name = "消息管理")
public class MessageController {

    private final MessageSendService messageSendService;
    private final MessageQueryService messageQueryService;
    private final MessageConvert messageConvert;

    @PostMapping("/send")
    @Operation(summary = "同步发送消息")
    public Result<String> send(@RequestBody @Valid MessageCreateParam param) {
        return messageSendService.send(param);
    }

    @PostMapping("/sendAsync")
    @Operation(summary = "异步发送消息")
    public Result<String> sendAsync(@RequestBody @Valid MessageCreateParam param) {
        return messageSendService.sendAsync(param);
    }

    @PostMapping("/schedule")
    @Operation(summary = "定时发送消息")
    public Result<String> schedule(
            @RequestBody @Valid MessageCreateParam param,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime scheduleTime) {
        return messageSendService.schedule(param, scheduleTime);
    }

    @PostMapping("/{messageId}/revoke")
    @Operation(summary = "撤回消息")
    public Result<Boolean> revoke(@PathVariable String messageId) {
        return messageSendService.revoke(messageId);
    }

    @GetMapping("/{messageId}")
    @Operation(summary = "查询消息详情")
    public Result<MessageRecordVO> getById(@PathVariable String messageId) {
        Result<MessageRecordDTO> result = messageQueryService.getById(messageId);
        if (!result.isSuccess()) {
            return Result.fail(result.getMessage());
        }
        return Result.ok(messageConvert.toVO(result.getData()));
    }

    @GetMapping("/page")
    @Operation(summary = "分页查询消息记录")
    public Result<Paging<MessageRecordVO>> page(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        Result<Paging<MessageRecordDTO>> result = messageQueryService.page(page, size);
        if (!result.isSuccess()) {
            return Result.fail(result.getMessage());
        }
        Paging<MessageRecordVO> voPaging = new Paging<>();
        voPaging.setRecords(messageConvert.toVOList(result.getData().getRecords()));
        voPaging.setTotal(result.getData().getTotal());
        return Result.ok(voPaging);
    }
}
