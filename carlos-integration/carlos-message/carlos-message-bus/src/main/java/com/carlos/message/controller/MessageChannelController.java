package com.carlos.message.controller;

import com.carlos.core.pagination.Paging;
import com.carlos.core.param.ParamIdSet;
import com.carlos.message.convert.MessageChannelConvert;
import com.carlos.message.manager.MessageChannelManager;
import com.carlos.message.pojo.dto.MessageChannelDTO;
import com.carlos.message.pojo.param.MessageChannelCreateParam;
import com.carlos.message.pojo.param.MessageChannelPageParam;
import com.carlos.message.pojo.param.MessageChannelUpdateParam;
import com.carlos.message.pojo.vo.MessageChannelVO;
import com.carlos.message.service.MessageChannelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;


/**
 * <p>
 * 消息渠道配置 rest服务接口
 * </p>
 *
 * @author Carlos
 * @date 2026年3月12日 上午11:17:05
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("message/channel")
@Tag(name = "消息渠道配置")
public class MessageChannelController {

    public static final String BASE_NAME = "消息渠道配置";

    private final MessageChannelService channelService;

    private final MessageChannelManager channelManager;


    @PostMapping("add")
    @Operation(summary = "新增" + BASE_NAME)
    public void add(@RequestBody @Validated MessageChannelCreateParam param) {
        MessageChannelDTO dto = MessageChannelConvert.INSTANCE.toDTO(param);
        channelService.addMessageChannel(dto);
    }


    @PostMapping("delete")
    @Operation(summary = "删除" + BASE_NAME)
    public void delete(@RequestBody ParamIdSet<Serializable> param) {
        channelService.deleteMessageChannel(param.getIds());
    }


    @PostMapping("update")
    @Operation(summary = "更新" + BASE_NAME)
    public void update(@RequestBody @Validated MessageChannelUpdateParam param) {
        MessageChannelDTO dto = MessageChannelConvert.INSTANCE.toDTO(param);
        channelService.updateMessageChannel(dto);
    }


    @GetMapping("detail")
    @Operation(summary = BASE_NAME + "详情")
    public MessageChannelVO detail(String id) {
        return MessageChannelConvert.INSTANCE.toVO(channelManager.getDtoById(id));
    }


    @GetMapping("page")
    @Operation(summary = BASE_NAME + "分页列表")
    public Paging<MessageChannelVO> page(MessageChannelPageParam param) {
        return channelManager.getPage(param);
    }

    @PostMapping("{id}/enable")
    @Operation(summary = "启用" + BASE_NAME)
    public void enable(@PathVariable Serializable id) {
        channelService.enableChannel(id);
    }

    @PostMapping("{id}/disable")
    @Operation(summary = "禁用" + BASE_NAME)
    public void disable(@PathVariable Serializable id) {
        channelService.disableChannel(id);
    }
}
