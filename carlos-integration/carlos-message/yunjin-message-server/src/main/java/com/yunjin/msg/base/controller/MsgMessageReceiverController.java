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
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
@Api(tags = "消息接受者")
public class MsgMessageReceiverController {

    public static final String BASE_NAME = "消息接受者";

    private final MsgMessageReceiverService messageReceiverService;

    private final MsgMessageReceiverManager messageReceiverManager;


    @ApiOperationSupport(author = "Carlos")
    @PostMapping("add")
    @ApiOperation(value = "新增" + BASE_NAME)
    public void add(@RequestBody @Validated MsgMessageReceiverCreateParam param) {
        MsgMessageReceiverDTO dto = MsgMessageReceiverConvert.INSTANCE.toDTO(param);
        messageReceiverService.addMsgMessageReceiver(dto);
    }

    @ApiOperationSupport(author = "Carlos")
    @PostMapping("delete")
    @ApiOperation(value = "删除" + BASE_NAME)
    public void delete(@RequestBody ParamIdSet<String> param) {
        messageReceiverService.deleteMsgMessageReceiver(param.getIds());
    }

    @ApiOperationSupport(author = "Carlos")
    @PostMapping("update")
    @ApiOperation(value = "更新" + BASE_NAME)
    public void update(@RequestBody @Validated MsgMessageReceiverUpdateParam param) {
        MsgMessageReceiverDTO dto = MsgMessageReceiverConvert.INSTANCE.toDTO(param);
        messageReceiverService.updateMsgMessageReceiver(dto);
    }

    @ApiOperationSupport(author = "Carlos")
    @GetMapping("detail")
    @ApiOperation(value = BASE_NAME + "详情")
    public MsgMessageReceiverVO detail(String id) {
        return MsgMessageReceiverConvert.INSTANCE.toVO(messageReceiverManager.getDtoById(id));
    }

    @ApiOperationSupport(author = "Carlos")
    @GetMapping("page")
    @ApiOperation(value = BASE_NAME + "分页列表")
    public Paging<MsgMessageReceiverVO> page(MsgMessageReceiverPageParam param) {
        return messageReceiverManager.getPage(param);
    }
}
