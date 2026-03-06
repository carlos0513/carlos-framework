package com.carlos.audit.api;

import com.carlos.audit.ServiceNameConstant;
import com.carlos.audit.api.fallback.ApiAuditLogConfigFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * <p>
 * 审计日志配置（动态TTL与采样策略） feign 提供接口
 * </p>
 *
 * @author Carlos
 * @date 2026年3月6日 下午9:31:12
 */
@FeignClient(value = ServiceNameConstant.SERVICE_NAME, contextId = "logConfig", path = "/api/audit/log/config", fallbackFactory = ApiAuditLogConfigFallbackFactory.class)
public interface ApiAuditLogConfig {


}
