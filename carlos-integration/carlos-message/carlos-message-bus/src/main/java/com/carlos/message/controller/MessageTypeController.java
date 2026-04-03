package com.carlos.message.controller;

import cn.hutool.core.util.StrUtil;
import com.carlos.core.exception.BusinessException;
import com.carlos.core.pagination.Paging;
import com.carlos.core.param.ParamIdSet;
import com.carlos.message.convert.MessageTypeConvert;
import com.carlos.message.manager.MessageTypeManager;
import com.carlos.message.pojo.dto.MessageTypeDTO;
import com.carlos.message.pojo.param.MessageTypeCreateParam;
import com.carlos.message.pojo.param.MessageTypePageParam;
import com.carlos.message.pojo.param.MessageTypeUpdateParam;
import com.carlos.message.pojo.vo.MessageTypeVO;
import com.carlos.message.service.MessageTypeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;


/**
 * <p>
 * 消息类型 rest服务接口
 * </p>
 *
 * @author Carlos
 * @date 2026年3月12日 上午11:17:05
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("message/type")
@Tag(name = "消息类型")
public class MessageTypeController {

    public static final String BASE_NAME = "消息类型";

    private final MessageTypeService typeService;

    private final MessageTypeManager typeManager;


    @PostMapping("add")
    @Operation(summary = "新增" + BASE_NAME)
    public void add(@RequestBody @Validated MessageTypeCreateParam param) {
        MessageTypeDTO dto = MessageTypeConvert.INSTANCE.toDTO(param);
        typeService.addMessageType(dto);
    }


    @PostMapping("delete")
    @Operation(summary = "删除" + BASE_NAME)
    public void delete(@RequestBody ParamIdSet<Serializable> param) {
        typeService.deleteMessageType(param.getIds());
    }


    @PostMapping("update")
    @Operation(summary = "更新" + BASE_NAME)
    public void update(@RequestBody @Validated MessageTypeUpdateParam param) {
        MessageTypeDTO dto = MessageTypeConvert.INSTANCE.toDTO(param);
        typeService.updateMessageType(dto);
    }


    @GetMapping("detail")
    @Operation(summary = BASE_NAME + "详情")
    public MessageTypeVO detail(String id) {
        return MessageTypeConvert.INSTANCE.toVO(typeManager.getDtoById(id));
    }


    @GetMapping("page")
    @Operation(summary = BASE_NAME + "分页列表")
    public Paging<MessageTypeVO> page(MessageTypePageParam param) {
        return typeManager.getPage(param);
    }

    @GetMapping("getByCode")
    @Operation(summary = "根据编码查询" + BASE_NAME)
    @Parameter(name = "typeCode", description = "类型编码", required = true)
    public MessageTypeVO getByCode(@RequestParam String typeCode) {
        if (StrUtil.isBlank(typeCode)) {
            throw new BusinessException("类型编码不能为空");
        }
        MessageTypeDTO dto = typeService.getByTypeCode(typeCode);
        return MessageTypeConvert.INSTANCE.toVO(dto);
    }

    @PostMapping("{id}/enable")
    @Operation(summary = "启用" + BASE_NAME)
    public void enable(@PathVariable Serializable id) {
        typeService.enableType(id);
    }

    @PostMapping("{id}/disable")
    @Operation(summary = "禁用" + BASE_NAME)
    public void disable(@PathVariable Serializable id) {
        typeService.disableType(id);
    }
}
