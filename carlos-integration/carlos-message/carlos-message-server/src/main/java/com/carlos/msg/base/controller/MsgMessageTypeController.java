package com.carlos.msg.base.controller;

import com.carlos.core.pagination.Paging;
import com.carlos.core.param.ParamIdSet;
import com.carlos.msg.base.convert.MsgMessageTypeConvert;
import com.carlos.msg.base.manager.MsgMessageTypeManager;
import com.carlos.msg.base.pojo.dto.MsgMessageTypeDTO;
import com.carlos.msg.base.pojo.param.MsgMessageTypeCreateParam;
import com.carlos.msg.base.pojo.param.MsgMessageTypePageParam;
import com.carlos.msg.base.pojo.param.MsgMessageTypeUpdateParam;
import com.carlos.msg.base.pojo.param.MsgMessageTypeUpdateStateParam;
import com.carlos.msg.base.pojo.vo.MsgMessageTypeBaseVO;
import com.carlos.msg.base.pojo.vo.MsgMessageTypeVO;
import com.carlos.msg.base.service.MsgMessageTypeService;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * <p>
 * 消息类型 rest服务接口
 * </p>
 *
 * @author Carlos
 * @date 2025-3-10 10:53:03
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("message/type")
@Tag(name = "消息类型")
public class MsgMessageTypeController {

    public static final String BASE_NAME = "消息类型";

    private final MsgMessageTypeService messageTypeService;

    private final MsgMessageTypeManager messageTypeManager;


    @ApiOperationSupport(author = "Carlos")
    @PostMapping("add")
    @Operation(summary = "新增" + BASE_NAME)
    public void add(@RequestBody @Validated MsgMessageTypeCreateParam param) {
        MsgMessageTypeDTO dto = MsgMessageTypeConvert.INSTANCE.toDTO(param);
        messageTypeService.addMsgMessageType(dto);
    }

    @ApiOperationSupport(author = "Carlos")
    @PostMapping("delete")
    @Operation(summary = "删除" + BASE_NAME)
    public void delete(@RequestBody ParamIdSet<String> param) {
        messageTypeService.deleteMsgMessageType(param.getIds());
    }

    @ApiOperationSupport(author = "Carlos")
    @PostMapping("update")
    @Operation(summary = "更新" + BASE_NAME)
    public void update(@RequestBody @Validated MsgMessageTypeUpdateParam param) {
        MsgMessageTypeDTO dto = MsgMessageTypeConvert.INSTANCE.toDTO(param);
        messageTypeService.updateMsgMessageType(dto);
    }

    @ApiOperationSupport(author = "Carlos")
    @PostMapping("update/state")
    @Operation(summary = "更新" + BASE_NAME)
    public void update(@RequestBody @Validated MsgMessageTypeUpdateStateParam param) {
        MsgMessageTypeDTO dto = MsgMessageTypeConvert.INSTANCE.toDTO(param);
        messageTypeService.updateMsgMessageType(dto);
    }

    @ApiOperationSupport(author = "Carlos")
    @GetMapping("detail")
    @Operation(summary = BASE_NAME + "详情")
    public MsgMessageTypeVO detail(String id) {
        return MsgMessageTypeConvert.INSTANCE.toVO(messageTypeManager.getDtoById(id));
    }

    @ApiOperationSupport(author = "Carlos")
    @GetMapping("list")
    @Operation(summary = "获取可用消息类型列表")
    public List<MsgMessageTypeBaseVO> list(String keyword) {
        return messageTypeManager.getEnabledPage(keyword);
    }

    @ApiOperationSupport(author = "Carlos")
    @GetMapping("page")
    @Operation(summary = BASE_NAME + "分页列表")
    public Paging<MsgMessageTypeVO> page(MsgMessageTypePageParam param) {
        return messageTypeManager.getPage(param);
    }
}
