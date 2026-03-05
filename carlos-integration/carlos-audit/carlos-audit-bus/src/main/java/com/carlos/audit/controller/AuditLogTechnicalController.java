package com.carlos.audit.controller;

import com.carlos.audit.convert.AuditLogTechnicalConvert;
import com.carlos.audit.manager.AuditLogTechnicalManager;
import com.carlos.audit.pojo.dto.AuditLogTechnicalDTO;
import com.carlos.audit.pojo.param.AuditLogTechnicalCreateParam;
import com.carlos.audit.pojo.param.AuditLogTechnicalPageParam;
import com.carlos.audit.pojo.param.AuditLogTechnicalUpdateParam;
import com.carlos.audit.pojo.vo.AuditLogTechnicalVO;
import com.carlos.audit.service.AuditLogTechnicalService;
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
 * 审计日志-技术上下文 rest服务接口
 * </p>
 *
 * @author Carlos
 * @date 2026年3月5日 下午11:36:54
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("audit/log/technical")
@Tag(name = "审计日志-技术上下文")
public class AuditLogTechnicalController {

    public static final String BASE_NAME = "审计日志-技术上下文";

    private final AuditLogTechnicalService logTechnicalService;

    private final AuditLogTechnicalManager logTechnicalManager;


    @PostMapping("add")
    @Operation(summary = "新增" + BASE_NAME)
    public void add(@RequestBody @Validated AuditLogTechnicalCreateParam param) {
        AuditLogTechnicalDTO dto = AuditLogTechnicalConvert.INSTANCE.toDTO(param);
        logTechnicalService.addAuditLogTechnical(dto);
    }


    @PostMapping("delete")
    @Operation(summary = "删除" + BASE_NAME)
    public void delete(@RequestBody ParamIdSet<Serializable> param) {
        logTechnicalService.deleteAuditLogTechnical(param.getIds());
    }


    @PostMapping("update")
    @Operation(summary = "更新" + BASE_NAME)
    public void update(@RequestBody @Validated AuditLogTechnicalUpdateParam param) {
        AuditLogTechnicalDTO dto = AuditLogTechnicalConvert.INSTANCE.toDTO(param);
        logTechnicalService.updateAuditLogTechnical(dto);
    }


    @GetMapping("detail")
    @Operation(summary = BASE_NAME + "详情")
    public AuditLogTechnicalVO detail(String id) {
        return AuditLogTechnicalConvert.INSTANCE.toVO(logTechnicalManager.getDtoById(id));
    }


    @GetMapping("page")
    @Operation(summary = BASE_NAME + "分页列表")
    public Paging<AuditLogTechnicalVO> page(AuditLogTechnicalPageParam param) {
        return logTechnicalManager.getPage(param);
    }
}
