package com.carlos.msg.base.controller;

import cn.hutool.core.util.StrUtil;
import cn.idev.excel.FastExcel;
import com.carlos.core.exception.ServiceException;
import com.carlos.core.pagination.Paging;
import com.carlos.core.param.ParamIdSet;
import com.carlos.core.response.Result;
import com.carlos.msg.base.convert.MsgMessageTemplateConvert;
import com.carlos.msg.base.listener.MsgMessageTemplateExcelListener;
import com.carlos.msg.base.manager.MsgMessageTemplateManager;
import com.carlos.msg.base.pojo.dto.MsgMessageTemplateDTO;
import com.carlos.msg.base.pojo.excel.MsgMessageTemplateExcel;
import com.carlos.msg.base.pojo.param.MsgMessageTemplateCreateParam;
import com.carlos.msg.base.pojo.param.MsgMessageTemplatePageParam;
import com.carlos.msg.base.pojo.param.MsgMessageTemplateUpdateParam;
import com.carlos.msg.base.pojo.vo.MsgMessageTemplatePageVO;
import com.carlos.msg.base.pojo.vo.MsgMessageTemplateVO;
import com.carlos.msg.base.service.MsgMessageTemplateService;
import com.carlos.util.easyexcel.ExcelUtil;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


/**
 * <p>
 * 消息模板 rest服务接口
 * </p>
 *
 * @author Carlos
 * @date 2025-3-10 10:53:03
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("message/template")
@Tag(name = "消息模板")
public class MsgMessageTemplateController {

    public static final String BASE_NAME = "消息模板";

    private final MsgMessageTemplateService messageTemplateService;

    private final MsgMessageTemplateManager messageTemplateManager;


    @ApiOperationSupport(author = "Carlos")
    @PostMapping("add")
    @Operation(summary = "新增" + BASE_NAME)
    public void add(@RequestBody @Validated MsgMessageTemplateCreateParam param) {
        MsgMessageTemplateDTO dto = MsgMessageTemplateConvert.INSTANCE.toDTO(param);
        messageTemplateService.addMsgMessageTemplate(dto);
    }

    @ApiOperationSupport(author = "Carlos")
    @PostMapping("delete")
    @Operation(summary = "删除" + BASE_NAME)
    public void delete(@RequestBody ParamIdSet<String> param) {
        messageTemplateService.deleteMsgMessageTemplate(param.getIds());
    }

    @ApiOperationSupport(author = "Carlos")
    @PostMapping("update")
    @Operation(summary = "更新" + BASE_NAME)
    public void update(@RequestBody @Validated MsgMessageTemplateUpdateParam param) {
        MsgMessageTemplateDTO dto = MsgMessageTemplateConvert.INSTANCE.toDTO(param);
        messageTemplateService.updateMsgMessageTemplate(dto);
    }

    @ApiOperationSupport(author = "Carlos")
    @GetMapping("detail")
    @Operation(summary = BASE_NAME + "详情")
    public MsgMessageTemplateVO detail(String id) {
        return MsgMessageTemplateConvert.INSTANCE.toVO(messageTemplateManager.getDtoById(id));
    }

    @ApiOperationSupport(author = "Carlos")
    @GetMapping("page")
    @Operation(summary = BASE_NAME + "分页列表")
    public Paging<MsgMessageTemplatePageVO> page(MsgMessageTemplatePageParam param) {
        return messageTemplateManager.getPage(param);
    }

    @PostMapping("import")
    @Operation(summary = "导入消息模板")
    public Result<?> importData(@RequestPart final MultipartFile file) {
        final MsgMessageTemplateExcelListener listener = new MsgMessageTemplateExcelListener(messageTemplateService);
        try {
            final String filename = file.getOriginalFilename();
            if (StrUtil.isBlank(filename)) {
                throw new ServiceException("文件名不能为空");
            }
            ExcelUtil.checkExcel(filename);
            FastExcel.read(file.getInputStream(), MsgMessageTemplateExcel.class, listener).sheet().doRead();
        } catch (final IOException e) {
            return Result.fail("消息模板导入失败！");
        }
        return Result.ok("消息模板导入成功！");
    }

    @GetMapping("export")
    @Operation(summary = "导出消息模板")
    public void exportMsgTemplate(final HttpServletResponse response) {
        messageTemplateService.exportMsgTemplate(response);
    }

    @GetMapping("example/export")
    @Operation(summary = "导出样例模板")
    public void exportTemplate(final HttpServletResponse response) {
        messageTemplateService.exportTemplate(response);
    }
}
