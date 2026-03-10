package com.carlos.msg.base.controller;

import com.carlos.core.pagination.Paging;
import com.carlos.core.param.ParamIdSet;
import com.carlos.msg.base.convert.MsgMessageReceiverConvert;
import com.carlos.msg.base.manager.MsgMessageReceiverManager;
import com.carlos.msg.base.pojo.dto.MsgMessageReceiverDTO;
import com.carlos.msg.base.pojo.param.MsgMessageReceiverCreateParam;
import com.carlos.msg.base.pojo.param.MsgMessageReceiverPageParam;
import com.carlos.msg.base.pojo.param.MsgMessageReceiverUpdateParam;
import com.carlos.msg.base.pojo.vo.MsgMessageReceiverVO;
import com.carlos.msg.base.service.MsgMessageReceiverService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


/**
 * <p>
 * 消息接受者 rest服务接口
 * </p>
 *
 * @author Carlos
 * @date 2025-3-10 10:53:03
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("message/receiver")
@Tag(name = "消息接受者")
public class MsgMessageReceiverController {

    public static final String BASE_NAME = "消息接受者";

    private final MsgMessageReceiverService messageReceiverService;

    private final MsgMessageReceiverManager messageReceiverManager;


    @PostMapping("add")
    @Operation(summary = "新增" + BASE_NAME)
    public void add(@RequestBody @Validated MsgMessageReceiverCreateParam param) {
        MsgMessageReceiverDTO dto = MsgMessageReceiverConvert.INSTANCE.toDTO(param);
        messageReceiverService.addMsgMessageReceiver(dto);
    }


    @PostMapping("delete")
    @Operation(summary = "删除" + BASE_NAME)
    public void delete(@RequestBody ParamIdSet<String> param) {
        messageReceiverService.deleteMsgMessageReceiver(param.getIds());
    }


    @PostMapping("update")
    @Operation(summary = "更新" + BASE_NAME)
    public void update(@RequestBody @Validated MsgMessageReceiverUpdateParam param) {
        MsgMessageReceiverDTO dto = MsgMessageReceiverConvert.INSTANCE.toDTO(param);
        messageReceiverService.updateMsgMessageReceiver(dto);
    }


    @GetMapping("detail")
    @Operation(summary = BASE_NAME + "详情")
    public MsgMessageReceiverVO detail(String id) {
        return MsgMessageReceiverConvert.INSTANCE.toVO(messageReceiverManager.getDtoById(id));
    }


    @GetMapping("page")
    @Operation(summary = BASE_NAME + "分页列表")
    public Paging<MsgMessageReceiverVO> page(MsgMessageReceiverPageParam param) {
        return messageReceiverManager.getPage(param);
    }
}
