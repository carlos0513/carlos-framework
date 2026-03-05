package com.carlos.audit.api;

import com.carlos.audit.ServiceNameConstant;
import com.carlos.audit.api.fallback.ApiAuditLogTechnicalFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * <p>
 * 审计日志-技术上下文 feign 提供接口
 * </p>
 *
 * @author Carlos
 * @date 2026年3月5日 下午11:36:53
 */
@FeignClient(value = ServiceNameConstant.SERVICE_NAME, contextId = "logTechnical", path = "/api/audit/log/technical", fallbackFactory = ApiAuditLogTechnicalFallbackFactory.class)
public interface ApiAuditLogTechnical {


}
