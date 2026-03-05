package com.carlos.audit.api;

import com.carlos.audit.ServiceNameConstant;
import com.carlos.audit.api.fallback.ApiAuditLogConfigFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * <p>
 * 审计日志配置 feign 提供接口
 * </p>
 *
 * @author Carlos
 * @date 2026年3月5日 下午11:36:53
 */
@FeignClient(value = ServiceNameConstant.SERVICE_NAME, contextId = "logConfig", path = "/api/audit/log/config", fallbackFactory = ApiAuditLogConfigFallbackFactory.class)
public interface ApiAuditLogConfig {


}
