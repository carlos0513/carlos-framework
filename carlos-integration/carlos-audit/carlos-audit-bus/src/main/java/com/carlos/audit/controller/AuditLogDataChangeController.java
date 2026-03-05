package com.carlos.audit.controller;

import com.carlos.audit.convert.AuditLogDataChangeConvert;
import com.carlos.audit.manager.AuditLogDataChangeManager;
import com.carlos.audit.pojo.dto.AuditLogDataChangeDTO;
import com.carlos.audit.pojo.param.AuditLogDataChangeCreateParam;
import com.carlos.audit.pojo.param.AuditLogDataChangePageParam;
import com.carlos.audit.pojo.param.AuditLogDataChangeUpdateParam;
import com.carlos.audit.pojo.vo.AuditLogDataChangeVO;
import com.carlos.audit.service.AuditLogDataChangeService;
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
 * 审计日志-数据变更详情 rest服务接口
 * </p>
 *
 * @author Carlos
 * @date 2026年3月5日 下午11:36:54
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("audit/log/data/change")
@Tag(name = "审计日志-数据变更详情")
public class AuditLogDataChangeController {

    public static final String BASE_NAME = "审计日志-数据变更详情";

    private final AuditLogDataChangeService logDataChangeService;

    private final AuditLogDataChangeManager logDataChangeManager;


    @PostMapping("add")
    @Operation(summary = "新增" + BASE_NAME)
    public void add(@RequestBody @Validated AuditLogDataChangeCreateParam param) {
        AuditLogDataChangeDTO dto = AuditLogDataChangeConvert.INSTANCE.toDTO(param);
        logDataChangeService.addAuditLogDataChange(dto);
    }


    @PostMapping("delete")
    @Operation(summary = "删除" + BASE_NAME)
    public void delete(@RequestBody ParamIdSet<Serializable> param) {
        logDataChangeService.deleteAuditLogDataChange(param.getIds());
    }


    @PostMapping("update")
    @Operation(summary = "更新" + BASE_NAME)
    public void update(@RequestBody @Validated AuditLogDataChangeUpdateParam param) {
        AuditLogDataChangeDTO dto = AuditLogDataChangeConvert.INSTANCE.toDTO(param);
        logDataChangeService.updateAuditLogDataChange(dto);
    }


    @GetMapping("detail")
    @Operation(summary = BASE_NAME + "详情")
    public AuditLogDataChangeVO detail(String id) {
        return AuditLogDataChangeConvert.INSTANCE.toVO(logDataChangeManager.getDtoById(id));
    }


    @GetMapping("page")
    @Operation(summary = BASE_NAME + "分页列表")
    public Paging<AuditLogDataChangeVO> page(AuditLogDataChangePageParam param) {
        return logDataChangeManager.getPage(param);
    }
}
