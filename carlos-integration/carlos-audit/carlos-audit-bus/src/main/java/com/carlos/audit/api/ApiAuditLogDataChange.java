package com.carlos.audit.api;

import com.carlos.audit.ServiceNameConstant;
import com.carlos.audit.api.fallback.ApiAuditLogDataChangeFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * <p>
 * 审计日志-数据变更详情 feign 提供接口
 * </p>
 *
 * @author Carlos
 * @date 2026年3月5日 下午11:36:53
 */
@FeignClient(value = ServiceNameConstant.SERVICE_NAME, contextId = "logDataChange", path = "/api/audit/log/data/change", fallbackFactory = ApiAuditLogDataChangeFallbackFactory.class)
public interface ApiAuditLogDataChange {


}
