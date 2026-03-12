package com.carlos.message.controller;

import com.carlos.core.pagination.Paging;
import com.carlos.core.param.ParamIdSet;
import com.carlos.message.convert.MessageSendLogConvert;
import com.carlos.message.manager.MessageSendLogManager;
import com.carlos.message.pojo.dto.MessageSendLogDTO;
import com.carlos.message.pojo.param.MessageSendLogCreateParam;
import com.carlos.message.pojo.param.MessageSendLogPageParam;
import com.carlos.message.pojo.param.MessageSendLogUpdateParam;
import com.carlos.message.pojo.vo.MessageSendLogVO;
import com.carlos.message.service.MessageSendLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;


/**
 * <p>
 * 消息发送日志 rest服务接口
 * </p>
 *
 * @author Carlos
 * @date 2026年3月12日 上午11:17:05
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("message/send/log")
@Tag(name = "消息发送日志")
public class MessageSendLogController {

    public static final String BASE_NAME = "消息发送日志";

    private final MessageSendLogService sendLogService;

    private final MessageSendLogManager sendLogManager;


    @PostMapping("add")
    @Operation(summary = "新增" + BASE_NAME)
    public void add(@RequestBody @Validated MessageSendLogCreateParam param) {
        MessageSendLogDTO dto = MessageSendLogConvert.INSTANCE.toDTO(param);
        sendLogService.addMessageSendLog(dto);
    }


    @PostMapping("delete")
    @Operation(summary = "删除" + BASE_NAME)
    public void delete(@RequestBody ParamIdSet<Serializable> param) {
        sendLogService.deleteMessageSendLog(param.getIds());
    }


    @PostMapping("update")
    @Operation(summary = "更新" + BASE_NAME)
    public void update(@RequestBody @Validated MessageSendLogUpdateParam param) {
        MessageSendLogDTO dto = MessageSendLogConvert.INSTANCE.toDTO(param);
        sendLogService.updateMessageSendLog(dto);
    }


    @GetMapping("detail")
    @Operation(summary = BASE_NAME + "详情")
    public MessageSendLogVO detail(String id) {
        return MessageSendLogConvert.INSTANCE.toVO(sendLogManager.getDtoById(id));
    }


    @GetMapping("page")
    @Operation(summary = BASE_NAME + "分页列表")
    public Paging<MessageSendLogVO> page(MessageSendLogPageParam param) {
        return sendLogManager.getPage(param);
    }
}
