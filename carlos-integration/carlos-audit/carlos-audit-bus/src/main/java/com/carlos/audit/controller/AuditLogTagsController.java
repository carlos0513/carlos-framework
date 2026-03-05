package com.carlos.audit.controller;

import com.carlos.audit.convert.AuditLogTagsConvert;
import com.carlos.audit.manager.AuditLogTagsManager;
import com.carlos.audit.pojo.dto.AuditLogTagsDTO;
import com.carlos.audit.pojo.param.AuditLogTagsCreateParam;
import com.carlos.audit.pojo.param.AuditLogTagsPageParam;
import com.carlos.audit.pojo.param.AuditLogTagsUpdateParam;
import com.carlos.audit.pojo.vo.AuditLogTagsVO;
import com.carlos.audit.service.AuditLogTagsService;
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
 * 审计日志-动态标签 rest服务接口
 * </p>
 *
 * @author Carlos
 * @date 2026年3月5日 下午11:36:54
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("audit/log/tags")
@Tag(name = "审计日志-动态标签")
public class AuditLogTagsController {

    public static final String BASE_NAME = "审计日志-动态标签";

    private final AuditLogTagsService logTagsService;

    private final AuditLogTagsManager logTagsManager;


    @PostMapping("add")
    @Operation(summary = "新增" + BASE_NAME)
    public void add(@RequestBody @Validated AuditLogTagsCreateParam param) {
        AuditLogTagsDTO dto = AuditLogTagsConvert.INSTANCE.toDTO(param);
        logTagsService.addAuditLogTags(dto);
    }


    @PostMapping("delete")
    @Operation(summary = "删除" + BASE_NAME)
    public void delete(@RequestBody ParamIdSet<Serializable> param) {
        logTagsService.deleteAuditLogTags(param.getIds());
    }


    @PostMapping("update")
    @Operation(summary = "更新" + BASE_NAME)
    public void update(@RequestBody @Validated AuditLogTagsUpdateParam param) {
        AuditLogTagsDTO dto = AuditLogTagsConvert.INSTANCE.toDTO(param);
        logTagsService.updateAuditLogTags(dto);
    }


    @GetMapping("detail")
    @Operation(summary = BASE_NAME + "详情")
    public AuditLogTagsVO detail(String id) {
        return AuditLogTagsConvert.INSTANCE.toVO(logTagsManager.getDtoById(id));
    }


    @GetMapping("page")
    @Operation(summary = BASE_NAME + "分页列表")
    public Paging<AuditLogTagsVO> page(AuditLogTagsPageParam param) {
        return logTagsManager.getPage(param);
    }
}
