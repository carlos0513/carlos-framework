package com.carlos.audit.controller;

import com.carlos.audit.convert.AuditLogFieldChangeConvert;
import com.carlos.audit.manager.AuditLogFieldChangeManager;
import com.carlos.audit.pojo.dto.AuditLogFieldChangeDTO;
import com.carlos.audit.pojo.param.AuditLogFieldChangeCreateParam;
import com.carlos.audit.pojo.param.AuditLogFieldChangePageParam;
import com.carlos.audit.pojo.param.AuditLogFieldChangeUpdateParam;
import com.carlos.audit.pojo.vo.AuditLogFieldChangeVO;
import com.carlos.audit.service.AuditLogFieldChangeService;
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
 * 审计日志-字段级变更明细 rest服务接口
 * </p>
 *
 * @author Carlos
 * @date 2026年3月5日 下午11:36:54
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("audit/log/field/change")
@Tag(name = "审计日志-字段级变更明细")
public class AuditLogFieldChangeController {

    public static final String BASE_NAME = "审计日志-字段级变更明细";

    private final AuditLogFieldChangeService logFieldChangeService;

    private final AuditLogFieldChangeManager logFieldChangeManager;


    @PostMapping("add")
    @Operation(summary = "新增" + BASE_NAME)
    public void add(@RequestBody @Validated AuditLogFieldChangeCreateParam param) {
        AuditLogFieldChangeDTO dto = AuditLogFieldChangeConvert.INSTANCE.toDTO(param);
        logFieldChangeService.addAuditLogFieldChange(dto);
    }


    @PostMapping("delete")
    @Operation(summary = "删除" + BASE_NAME)
    public void delete(@RequestBody ParamIdSet<Serializable> param) {
        logFieldChangeService.deleteAuditLogFieldChange(param.getIds());
    }


    @PostMapping("update")
    @Operation(summary = "更新" + BASE_NAME)
    public void update(@RequestBody @Validated AuditLogFieldChangeUpdateParam param) {
        AuditLogFieldChangeDTO dto = AuditLogFieldChangeConvert.INSTANCE.toDTO(param);
        logFieldChangeService.updateAuditLogFieldChange(dto);
    }


    @GetMapping("detail")
    @Operation(summary = BASE_NAME + "详情")
    public AuditLogFieldChangeVO detail(String id) {
        return AuditLogFieldChangeConvert.INSTANCE.toVO(logFieldChangeManager.getDtoById(id));
    }


    @GetMapping("page")
    @Operation(summary = BASE_NAME + "分页列表")
    public Paging<AuditLogFieldChangeVO> page(AuditLogFieldChangePageParam param) {
        return logFieldChangeManager.getPage(param);
    }
}
