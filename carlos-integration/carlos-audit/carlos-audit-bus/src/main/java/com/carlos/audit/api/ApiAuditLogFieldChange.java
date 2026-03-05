package com.carlos.audit.api;

import com.carlos.audit.ServiceNameConstant;
import com.carlos.audit.api.fallback.ApiAuditLogFieldChangeFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * <p>
 * 审计日志-字段级变更明细 feign 提供接口
 * </p>
 *
 * @author Carlos
 * @date 2026年3月5日 下午11:36:53
 */
@FeignClient(value = ServiceNameConstant.SERVICE_NAME, contextId = "logFieldChange", path = "/api/audit/log/field/change", fallbackFactory = ApiAuditLogFieldChangeFallbackFactory.class)
public interface ApiAuditLogFieldChange {


}
