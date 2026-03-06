package com.carlos.audit.controller;

import com.carlos.audit.convert.AuditLogMainConvert;
import com.carlos.audit.manager.AuditLogMainManager;
import com.carlos.audit.pojo.dto.AuditLogMainDTO;
import com.carlos.audit.pojo.param.AuditLogMainCreateParam;
import com.carlos.audit.pojo.param.AuditLogMainPageParam;
import com.carlos.audit.pojo.param.AuditLogMainUpdateParam;
import com.carlos.audit.pojo.vo.AuditLogMainVO;
import com.carlos.audit.service.AuditLogMainService;
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
 * 审计日志宽主表（合并数据变更、技术上下文、标签、附件，保留7天热数据） rest服务接口
 * </p>
 *
 * @author Carlos
 * @date 2026年3月6日 下午9:31:12
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("audit/log/main")
@Tag(name = "审计日志")
public class AuditLogMainController {

    public static final String BASE_NAME = "审计日志";

    private final AuditLogMainService logMainService;

    private final AuditLogMainManager logMainManager;


    @PostMapping("add")
    @Operation(summary = "新增" + BASE_NAME)
    public void add(@RequestBody @Validated AuditLogMainCreateParam param) {
        AuditLogMainDTO dto = AuditLogMainConvert.INSTANCE.toDTO(param);
        logMainService.addAuditLogMain(dto);
    }


    @PostMapping("delete")
    @Operation(summary = "删除" + BASE_NAME)
    public void delete(@RequestBody ParamIdSet<Serializable> param) {
        logMainService.deleteAuditLogMain(param.getIds());
    }


    @PostMapping("update")
    @Operation(summary = "更新" + BASE_NAME)
    public void update(@RequestBody @Validated AuditLogMainUpdateParam param) {
        AuditLogMainDTO dto = AuditLogMainConvert.INSTANCE.toDTO(param);
        logMainService.updateAuditLogMain(dto);
    }


    @GetMapping("detail")
    @Operation(summary = BASE_NAME + "详情")
    public AuditLogMainVO detail(String id) {
        return AuditLogMainConvert.INSTANCE.toVO(logMainManager.getDtoById(id));
    }


    @GetMapping("page")
    @Operation(summary = BASE_NAME + "分页列表")
    public Paging<AuditLogMainVO> page(AuditLogMainPageParam param) {
        return logMainManager.getPage(param);
    }
}
