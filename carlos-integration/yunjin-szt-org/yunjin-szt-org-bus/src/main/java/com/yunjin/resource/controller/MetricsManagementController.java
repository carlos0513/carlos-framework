package com.yunjin.resource.controller;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.yunjin.core.pagination.Paging;
import com.yunjin.core.param.ParamIdSet;
import com.yunjin.resource.convert.MetricsManagementConvert;
import com.yunjin.resource.manager.MetricsManagementManager;
import com.yunjin.resource.pojo.dto.MetricsManagementDTO;
import com.yunjin.resource.pojo.param.MetricsManagementCreateParam;
import com.yunjin.resource.pojo.param.MetricsManagementPageParam;
import com.yunjin.resource.pojo.param.MetricsManagementUpdateParam;
import com.yunjin.resource.pojo.vo.MetricsManagementVO;
import com.yunjin.resource.service.MetricsManagementService;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


/**
 * <p>
 * 系统指标管理 rest服务接口
 * </p>
 *
 * @author yunjin
 * @date 2024-8-22 10:59:20
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("org/metrics/management")
@Tag(name = "系统指标管理")
public class MetricsManagementController {

    public static final String BASE_NAME = "系统指标管理";

    private final MetricsManagementService metricsManagementService;

    private final MetricsManagementManager metricsManagementManager;


    @ApiOperationSupport(author = "yunjin")
    @PostMapping("add")
    @Operation(summary = "新增" + BASE_NAME)
    public void add(@RequestBody @Validated MetricsManagementCreateParam param) {
        MetricsManagementDTO dto = MetricsManagementConvert.INSTANCE.toDTO(param);
        metricsManagementService.addMetricsManagement(dto);
    }

    @ApiOperationSupport(author = "yunjin")
    @PostMapping("delete")
    @Operation(summary = "删除" + BASE_NAME)
    public void delete(@RequestBody ParamIdSet<String> param) {
        metricsManagementService.deleteMetricsManagement(param.getIds());
    }

    @ApiOperationSupport(author = "yunjin")
    @PostMapping("update")
    @Operation(summary = "更新" + BASE_NAME)
    public void update(@RequestBody @Validated MetricsManagementUpdateParam param) {
        MetricsManagementDTO dto = MetricsManagementConvert.INSTANCE.toDTO(param);
        metricsManagementService.updateMetricsManagement(dto);
    }

    @ApiOperationSupport(author = "yunjin")
    @GetMapping("{id}")
    @Operation(summary = BASE_NAME + "详情")
    public MetricsManagementVO detail(@PathVariable String id) {
        return MetricsManagementConvert.INSTANCE.toVO(metricsManagementManager.getDtoById(id));
    }

    @ApiOperationSupport(author = "yunjin")
    @GetMapping("page")
    @Operation(summary = BASE_NAME + "分页列表")
    public Paging<MetricsManagementVO> page(MetricsManagementPageParam param) {
        return metricsManagementManager.getPage(param);
    }
}
