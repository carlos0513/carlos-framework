package com.carlos.msg.base.controller;

import com.carlos.core.pagination.Paging;
import com.carlos.core.param.ParamIdSet;
import com.carlos.msg.base.convert.MsgMessageConvert;
import com.carlos.msg.base.manager.MsgMessageManager;
import com.carlos.msg.base.pojo.dto.MsgMessageDTO;
import com.carlos.msg.base.pojo.param.MsgMessageCreateParam;
import com.carlos.msg.base.pojo.param.MsgMessagePageParam;
import com.carlos.msg.base.pojo.param.MsgMessageUpdateParam;
import com.carlos.msg.base.pojo.vo.MsgMessageVO;
import com.carlos.msg.base.service.MsgMessageService;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


/**
 * <p>
 * 消息 rest服务接口
 * </p>
 *
 * @author Carlos
 * @date 2025-3-10 10:53:03
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("message")
@Api(tags = "消息")
public class MsgMessageController {

    public static final String BASE_NAME = "消息";

    private final MsgMessageService messageService;

    private final MsgMessageManager messageManager;


    @ApiOperationSupport(author = "Carlos")
    @PostMapping("add")
    @ApiOperation(value = "新增" + BASE_NAME)
    public void add(@RequestBody @Validated MsgMessageCreateParam param) {
        MsgMessageDTO dto = MsgMessageConvert.INSTANCE.toDTO(param);
        messageService.addMsgMessage(dto);
    }

    @ApiOperationSupport(author = "Carlos")
    @PostMapping("delete")
    @ApiOperation(value = "删除" + BASE_NAME)
    public void delete(@RequestBody ParamIdSet<String> param) {
        messageService.deleteMsgMessage(param.getIds());
    }

    @ApiOperationSupport(author = "Carlos")
    @PostMapping("update")
    @ApiOperation(value = "更新" + BASE_NAME)
    public void update(@RequestBody @Validated MsgMessageUpdateParam param) {
        MsgMessageDTO dto = MsgMessageConvert.INSTANCE.toDTO(param);
        messageService.updateMsgMessage(dto);
    }

    @ApiOperationSupport(author = "Carlos")
    @GetMapping("detail")
    @ApiOperation(value = BASE_NAME + "详情")
    public MsgMessageVO detail(String id) {
        return MsgMessageConvert.INSTANCE.toVO(messageManager.getDtoById(id));
    }

    @ApiOperationSupport(author = "Carlos")
    @GetMapping("page")
    @ApiOperation(value = BASE_NAME + "分页列表")
    public Paging<MsgMessageVO> page(MsgMessagePageParam param) {
        return messageManager.getPage(param);
    }
}
