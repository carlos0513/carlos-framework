package com.carlos.message.controller;

import com.carlos.core.pagination.Paging;
import com.carlos.core.param.ParamIdSet;
import com.carlos.message.convert.MessageTemplateConvert;
import com.carlos.message.manager.MessageTemplateManager;
import com.carlos.message.pojo.dto.MessageTemplateDTO;
import com.carlos.message.pojo.param.MessageTemplateCreateParam;
import com.carlos.message.pojo.param.MessageTemplatePageParam;
import com.carlos.message.pojo.param.MessageTemplateUpdateParam;
import com.carlos.message.pojo.vo.MessageTemplateVO;
import com.carlos.message.service.MessageTemplateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;


/**
 * <p>
 * 消息模板 rest服务接口
 * </p>
 *
 * @author Carlos
 * @date 2026年3月12日 上午11:17:05
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("message/template")
@Tag(name = "消息模板")
public class MessageTemplateController {

    public static final String BASE_NAME = "消息模板";

    private final MessageTemplateService templateService;

    private final MessageTemplateManager templateManager;


    @PostMapping("add")
    @Operation(summary = "新增" + BASE_NAME)
    public void add(@RequestBody @Validated MessageTemplateCreateParam param) {
        MessageTemplateDTO dto = MessageTemplateConvert.INSTANCE.toDTO(param);
        templateService.addMessageTemplate(dto);
    }


    @PostMapping("delete")
    @Operation(summary = "删除" + BASE_NAME)
    public void delete(@RequestBody ParamIdSet<Serializable> param) {
        templateService.deleteMessageTemplate(param.getIds());
    }


    @PostMapping("update")
    @Operation(summary = "更新" + BASE_NAME)
    public void update(@RequestBody @Validated MessageTemplateUpdateParam param) {
        MessageTemplateDTO dto = MessageTemplateConvert.INSTANCE.toDTO(param);
        templateService.updateMessageTemplate(dto);
    }


    @GetMapping("detail")
    @Operation(summary = BASE_NAME + "详情")
    public MessageTemplateVO detail(String id) {
        return MessageTemplateConvert.INSTANCE.toVO(templateManager.getDtoById(id));
    }


    @GetMapping("page")
    @Operation(summary = BASE_NAME + "分页列表")
    public Paging<MessageTemplateVO> page(MessageTemplatePageParam param) {
        return templateManager.getPage(param);
    }
}
