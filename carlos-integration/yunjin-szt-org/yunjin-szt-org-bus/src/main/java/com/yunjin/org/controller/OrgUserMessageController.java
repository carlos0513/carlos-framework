package com.yunjin.org.controller;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.yunjin.core.pagination.Paging;
import com.yunjin.core.param.ParamIdSet;
import com.yunjin.org.config.AuthorConstant;
import com.yunjin.org.convert.OrgUserMessageConvert;
import com.yunjin.org.manager.OrgUserMessageManager;
import com.yunjin.org.pojo.dto.OrgUserMessageDTO;
import com.yunjin.org.pojo.param.OrgUserMessageCreateParam;
import com.yunjin.org.pojo.param.OrgUserMessagePageParam;
import com.yunjin.org.pojo.param.OrgUserMessageUpdateParam;
import com.yunjin.org.pojo.vo.OrgUserMessageVO;
import com.yunjin.org.service.OrgUserMessageService;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


/**
 * <p>
 * 用户消息表 rest服务接口
 * </p>
 *
 * @author yunjin
 * @date 2024-2-28 17:39:16
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("org/user/message")
@Tag(name = "用户消息表")
public class OrgUserMessageController {

    public static final String BASE_NAME = "用户消息表";

    private final OrgUserMessageService userMessageService;

    private final OrgUserMessageManager userMessageManager;


    @ApiOperationSupport(author = AuthorConstant.DEFAULT)
    @PostMapping("add")
    @Operation(summary = "新增" + BASE_NAME)
    public void add(@RequestBody @Validated OrgUserMessageCreateParam param) {
        OrgUserMessageDTO dto = OrgUserMessageConvert.INSTANCE.toDTO(param);
        userMessageService.addOrgUserMessage(dto);
    }

    @ApiOperationSupport(author = AuthorConstant.DEFAULT)
    @PostMapping("delete")
    @Operation(summary = "删除" + BASE_NAME)
    public void delete(@RequestBody ParamIdSet<String> param) {
        userMessageService.deleteOrgUserMessage(param.getIds());
    }

    @ApiOperationSupport(author = AuthorConstant.DEFAULT)
    @PostMapping("update")
    @Operation(summary = "更新" + BASE_NAME)
    public void update(@RequestBody @Validated OrgUserMessageUpdateParam param) {
        OrgUserMessageDTO dto = OrgUserMessageConvert.INSTANCE.toDTO(param);
        userMessageService.updateOrgUserMessage(dto);
    }

    @ApiOperationSupport(author = AuthorConstant.DEFAULT)
    @GetMapping("{id}")
    @Operation(summary = BASE_NAME + "详情")
    public OrgUserMessageVO detail(@PathVariable String id) {
        return OrgUserMessageConvert.INSTANCE.toVO(userMessageService.getMessageById(id));
    }

    @PostMapping("read")
    public void messagesRead(@RequestBody ParamIdSet<String> ids) {
        userMessageService.messagesRead(ids);
    }

    @PostMapping("page")
    @Operation(summary = "获取消息公告列表", notes = "获取消息公告列表")
    public Paging<OrgUserMessageVO> getMessageList(@RequestBody @Validated OrgUserMessagePageParam param) {
        // 只有系统公告需要更新
        // userMessageService.updateMessages();
        Paging<OrgUserMessageVO> page = userMessageManager.getPage(param);
        return page;
    }
}
