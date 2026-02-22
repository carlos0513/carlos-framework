package com.carlos.metric;


import com.carlos.core.response.Result;
import com.carlos.org.metric.ApiOrgMetric;
import com.carlos.org.metric.OrgMetric;
import com.carlos.org.metric.OrgMetricEnum;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * <p>
 * 服务指标接口
 * </p>
 *
 * @author Carlos
 * @date 2025-05-20 15:15
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("api/org/metric")
@Tag(name = "指标开放接口")
@Hidden
public class ApiOrgMetricImpl implements ApiOrgMetric {

    private final OrgMetricService metricService;


    @Override
    @PostMapping("/getMetric")
    public Result<OrgMetric> getMetric(@RequestBody Map<OrgMetricEnum, Object> metrics) {
        OrgMetric metric = metricService.getMetric(metrics);
        return Result.ok(metric);
    }
}
