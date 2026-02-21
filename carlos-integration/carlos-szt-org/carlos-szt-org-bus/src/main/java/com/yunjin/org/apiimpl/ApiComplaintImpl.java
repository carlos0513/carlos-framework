package com.yunjin.org.apiimpl;

import com.yunjin.core.response.Result;
import com.yunjin.org.api.ApiComplaint;
import com.yunjin.org.pojo.entity.OrgComplaintReport;
import com.yunjin.org.service.OrgComplaintReportService;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/org/complaint")
@Tag(name = "市级投诉回调", hidden = true)
@Slf4j
public class ApiComplaintImpl implements ApiComplaint {

    private final OrgComplaintReportService orgComplaintReportService;

    @Override
    @GetMapping("/processCallback")
    @Operation(summary = "市级投诉回调")
    public Result<Boolean> processCallback(String id, String content) {
        orgComplaintReportService.deal(new OrgComplaintReport().setId(id).setReply(content));
        return Result.ok(Boolean.TRUE);
    }
}
