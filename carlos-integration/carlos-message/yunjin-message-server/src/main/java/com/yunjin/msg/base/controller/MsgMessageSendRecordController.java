package com.carlos.msg.base.controller;

import com.carlos.core.pagination.Paging;
import com.carlos.msg.base.convert.MsgMessageSendRecordConvert;
import com.carlos.msg.base.manager.MsgMessageSendRecordManager;
import com.carlos.msg.base.pojo.param.MsgMessageSendRecordPageParam;
import com.carlos.msg.base.pojo.vo.MsgMessageSendRecordVO;
import com.carlos.msg.base.service.MsgMessageSendRecordService;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * <p>
 * 消息发送记录 rest服务接口
 * </p>
 *
 * @author Carlos
 * @date 2025-3-10 10:53:03
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("message/send/record")
@Api(tags = "消息发送记录")
public class MsgMessageSendRecordController {

    public static final String BASE_NAME = "消息发送记录";

    private final MsgMessageSendRecordService messageSendRecordService;

    private final MsgMessageSendRecordManager messageSendRecordManager;


    @ApiOperationSupport(author = "Carlos")
    @GetMapping("detail")
    @ApiOperation(value = BASE_NAME + "详情")
    public MsgMessageSendRecordVO detail(String id) {
        return MsgMessageSendRecordConvert.INSTANCE.toVO(messageSendRecordManager.getDtoById(id));
    }

    @ApiOperationSupport(author = "Carlos")
    @GetMapping("page")
    @ApiOperation(value = BASE_NAME + "分页列表")
    public Paging<MsgMessageSendRecordVO> page(MsgMessageSendRecordPageParam param) {
        return messageSendRecordManager.getPage(param);
    }
}
