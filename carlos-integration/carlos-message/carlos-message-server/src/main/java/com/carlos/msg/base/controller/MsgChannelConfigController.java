package com.carlos.msg.base.controller;

import com.carlos.core.pagination.Paging;
import com.carlos.core.param.ParamIdSet;
import com.carlos.msg.base.convert.MsgChannelConfigConvert;
import com.carlos.msg.base.manager.MsgChannelConfigManager;
import com.carlos.msg.base.pojo.dto.MsgChannelConfigDTO;
import com.carlos.msg.base.pojo.param.MsgChannelConfigCreateParam;
import com.carlos.msg.base.pojo.param.MsgChannelConfigPageParam;
import com.carlos.msg.base.pojo.param.MsgChannelConfigStateParam;
import com.carlos.msg.base.pojo.param.MsgChannelConfigUpdateParam;
import com.carlos.msg.base.pojo.vo.MsgChannelConfigVO;
import com.carlos.msg.base.service.MsgChannelConfigService;
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
 * @date 2025-3-10 10:53:03
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("channel/config")
@Tag(name = "消息渠道配置")
public class MsgChannelConfigController {

    public static final String BASE_NAME = "消息渠道配置";

    private final MsgChannelConfigService channelConfigService;

    private final MsgChannelConfigManager channelConfigManager;


    @PostMapping("add")
    @Operation(summary = "新增" + BASE_NAME)
    public void add(@RequestBody @Validated MsgChannelConfigCreateParam param) {
        MsgChannelConfigDTO dto = MsgChannelConfigConvert.INSTANCE.toDTO(param);
        channelConfigService.addMsgChannelConfig(dto);
    }


    @PostMapping("delete")
    @Operation(summary = "删除" + BASE_NAME)
    public void delete(@RequestBody ParamIdSet<Serializable> param) {
        channelConfigService.deleteMsgChannelConfig(param.getIds());
    }


    @PostMapping("state")
    @Operation(summary = "修改状态")
    public void state(@RequestBody MsgChannelConfigStateParam param) {
        channelConfigService.changeState(param.getIds(), param.getEnabled());
    }


    @PostMapping("update")
    @Operation(summary = "更新" + BASE_NAME)
    public void update(@RequestBody @Validated MsgChannelConfigUpdateParam param) {
        MsgChannelConfigDTO dto = MsgChannelConfigConvert.INSTANCE.toDTO(param);
        channelConfigService.updateMsgChannelConfig(dto);
    }


    @GetMapping("detail")
    @Operation(summary = BASE_NAME + "详情")
    public MsgChannelConfigVO detail(String id) {
        return MsgChannelConfigConvert.INSTANCE.toVO(channelConfigManager.getDtoById(id));
    }


    @GetMapping("page")
    @Operation(summary = BASE_NAME + "分页列表")
    public Paging<MsgChannelConfigVO> page(MsgChannelConfigPageParam param) {
        return channelConfigManager.getPage(param);
    }
}
