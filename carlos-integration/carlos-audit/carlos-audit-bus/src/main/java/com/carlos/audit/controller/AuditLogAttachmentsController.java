package com.carlos.audit.controller;

import com.carlos.audit.convert.AuditLogAttachmentsConvert;
import com.carlos.audit.manager.AuditLogAttachmentsManager;
import com.carlos.audit.pojo.dto.AuditLogAttachmentsDTO;
import com.carlos.audit.pojo.param.AuditLogAttachmentsCreateParam;
import com.carlos.audit.pojo.param.AuditLogAttachmentsPageParam;
import com.carlos.audit.pojo.param.AuditLogAttachmentsUpdateParam;
import com.carlos.audit.pojo.vo.AuditLogAttachmentsVO;
import com.carlos.audit.service.AuditLogAttachmentsService;
import com.carlos.core.pagination.Paging;
import com.carlos.core.param.ParamIdSet;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;


/**
 * <p>
 * 审计日志-附件引用 rest服务接口
 * </p>
 *
 * @author Carlos
 * @date 2026年3月5日 下午11:36:54
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("audit/log/attachments")
@Tag(name = "审计日志-附件引用")
public class AuditLogAttachmentsController {

    public static final String BASE_NAME = "审计日志-附件引用";

    private final AuditLogAttachmentsService logAttachmentsService;

    private final AuditLogAttachmentsManager logAttachmentsManager;


    @PostMapping("add")
    @Operation(summary = "新增" + BASE_NAME)
    public void add(@RequestBody @Validated AuditLogAttachmentsCreateParam param) {
        AuditLogAttachmentsDTO dto = AuditLogAttachmentsConvert.INSTANCE.toDTO(param);
        logAttachmentsService.addAuditLogAttachments(dto);
    }


    @PostMapping("delete")
    @Operation(summary = "删除" + BASE_NAME)
    public void delete(@RequestBody ParamIdSet<Serializable> param) {
        logAttachmentsService.deleteAuditLogAttachments(param.getIds());
    }


    @PostMapping("update")
    @Operation(summary = "更新" + BASE_NAME)
    public void update(@RequestBody @Validated AuditLogAttachmentsUpdateParam param) {
        AuditLogAttachmentsDTO dto = AuditLogAttachmentsConvert.INSTANCE.toDTO(param);
        logAttachmentsService.updateAuditLogAttachments(dto);
    }


    @GetMapping("detail")
    @Operation(summary = BASE_NAME + "详情")
    public AuditLogAttachmentsVO detail(String id) {
        return AuditLogAttachmentsConvert.INSTANCE.toVO(logAttachmentsManager.getDtoById(id));
    }


    @GetMapping("page")
    @Operation(summary = BASE_NAME + "分页列表")
    public Paging<AuditLogAttachmentsVO> page(AuditLogAttachmentsPageParam param) {
        return logAttachmentsManager.getPage(param);
    }
}
