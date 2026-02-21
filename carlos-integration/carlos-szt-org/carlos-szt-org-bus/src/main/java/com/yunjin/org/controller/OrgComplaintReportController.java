package com.yunjin.org.controller;

import com.yunjin.core.pagination.Paging;
import com.yunjin.org.convert.OrgComplaintReportConvert;
import com.yunjin.org.pojo.dto.OrgComplaintReportDTO;
import com.yunjin.org.pojo.entity.OrgComplaintReport;
import com.yunjin.org.pojo.param.OrgComplaintReportCreateParam;
import com.yunjin.org.pojo.param.OrgComplaintReportPageParam;
import com.yunjin.org.pojo.vo.OrgComplaintReportDetailVO;
import com.yunjin.org.pojo.vo.OrgComplaintReportVO;
import com.yunjin.org.service.OrgComplaintReportService;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;


/**
 * <p>
 * 投诉反馈 rest服务接口
 * </p>
 *
 * @author yunjin
 * @date 2022-11-11 18:19:17
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/org/complaint")
@Tag(name = "投诉反馈")
public class OrgComplaintReportController {

    private final OrgComplaintReportService complaintReportService;

    @PostMapping
    @Operation(summary = "新增投诉反馈")
    public void addComplaintReport(@RequestBody @Validated final OrgComplaintReportCreateParam param) {
        OrgComplaintReportDTO dto = OrgComplaintReportConvert.INSTANCE.toDTO(param);
        this.complaintReportService.addComplaintReport(dto);
    }

    @GetMapping("page")
    @Operation(summary = "投诉反馈分页列表")
    public Paging<OrgComplaintReportVO> queryComplaintReport(OrgComplaintReportPageParam param) {
        return complaintReportService.queryComplaintReport(param);
    }

    @GetMapping("detail")
    @Operation(summary = "投诉反馈详情")
    public OrgComplaintReportDetailVO queryComplaintDetail(@RequestParam String id) {
        return complaintReportService.queryComplaintDetail(id);
    }

    @GetMapping("sign")
    @Operation(summary = "签收")
    public void signFor(@RequestParam String id) {
        this.complaintReportService.signFor(id);
    }

    @GetMapping("deny")
    @Operation(summary = "不受理")
    public void deny(@RequestParam String id, @RequestParam String content) {
        this.complaintReportService.deny(id, content);
    }

    @GetMapping("urge")
    @Operation(summary = "催办")
    public void urge(@RequestParam String id, @RequestParam String content) {
        this.complaintReportService.urge(id, content);
    }

    @PostMapping("deal")
    @Operation(summary = "处理")
    public void deal(@RequestBody final OrgComplaintReport dealInfo) {
        this.complaintReportService.deal(dealInfo);
    }

    @GetMapping("upgrade")
    @Operation(summary = "上报市级")
    public void upgrade(@RequestParam String id) {
        complaintReportService.upgrade(id);
    }

    @GetMapping("withdraw")
    @Operation(summary = "从市级撤回")
    public void withdraw(@RequestParam String id) {
        complaintReportService.withdraw(id);
    }


    @PostMapping("export")
    @Operation(summary = "导出")
    public void export(HttpServletResponse response, @RequestBody final OrgComplaintReportPageParam param) {
        this.complaintReportService.export(response, param);
    }

    @PostMapping("whiteListAddComplaintReport")
    @Operation(summary = "白名单新增投诉反馈")
    public void whiteListAddComplaintReport(@RequestBody @Validated OrgComplaintReportCreateParam param) {
        OrgComplaintReportDTO dto = OrgComplaintReportConvert.INSTANCE.toDTO(param);
        this.complaintReportService.whiteListAddComplaintReport(dto);
    }

}
