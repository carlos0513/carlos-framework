package com.carlos.audit.api;

import com.carlos.audit.ServiceNameConstant;
import com.carlos.audit.api.fallback.ApiAuditLogMainFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * <p>
 * 审计日志宽主表（合并数据变更、技术上下文、标签、附件，保留7天热数据） feign 提供接口
 * </p>
 *
 * @author Carlos
 * @date 2026年3月6日 下午9:31:12
 */
@FeignClient(value = ServiceNameConstant.SERVICE_NAME, contextId = "logMain", path = "/api/audit/log/main", fallbackFactory = ApiAuditLogMainFallbackFactory.class)
public interface ApiAuditLogMain {


}
