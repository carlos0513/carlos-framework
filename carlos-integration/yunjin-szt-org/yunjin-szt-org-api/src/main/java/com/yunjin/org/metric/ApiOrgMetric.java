package com.yunjin.org.metric;


import com.yunjin.core.response.Result;
import com.yunjin.org.ServiceNameConstant;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

/**
 * <p>
 * 表单指标管理
 * </p>
 *
 * @author Carlos
 * @date 2025-05-19 23:02
 */
@FeignClient(value = ServiceNameConstant.USER, path = "/api/org/metric", contextId = "orgMetric", fallbackFactory = ApiOrgMetricFallbackFactory.class)
public interface ApiOrgMetric {

    /**
     * 获取指标
     *
     * @param metrics 指标枚举
     * @return 指标
     */
    @PostMapping("/getMetric")
    Result<OrgMetric> getMetric(@RequestBody Map<OrgMetricEnum, Object> metrics);
}
