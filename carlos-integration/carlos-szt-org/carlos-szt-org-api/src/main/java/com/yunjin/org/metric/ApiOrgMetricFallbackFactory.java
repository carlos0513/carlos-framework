package com.yunjin.org.metric;

import com.yunjin.core.response.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;

import java.util.Map;


/**
 * <p>
 * 表单字段降级服务
 * </p>
 *
 * @author Carlos
 * @date 2022/11/16 10:57
 */

@Slf4j
public class ApiOrgMetricFallbackFactory implements FallbackFactory<ApiOrgMetric> {

    @Override
    public ApiOrgMetric create(Throwable throwable) {
        String message = throwable.getMessage();
        log.error("表单指标服务调用失败: message:{}", message);
        return new ApiOrgMetric() {

            @Override
            public Result<OrgMetric> getMetric(Map<OrgMetricEnum, Object> metrics) {
                return Result.fail("指标数据获取失败");
            }
        };
    }
}