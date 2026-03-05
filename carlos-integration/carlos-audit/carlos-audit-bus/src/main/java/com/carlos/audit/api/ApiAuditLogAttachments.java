package com.carlos.audit.api;

import com.carlos.audit.ServiceNameConstant;
import com.carlos.audit.api.fallback.ApiAuditLogAttachmentsFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * <p>
 * 审计日志-附件引用 feign 提供接口
 * </p>
 *
 * @author Carlos
 * @date 2026年3月5日 下午11:36:53
 */
@FeignClient(value = ServiceNameConstant.SERVICE_NAME, contextId = "logAttachments", path = "/api/audit/log/attachments", fallbackFactory = ApiAuditLogAttachmentsFallbackFactory.class)
public interface ApiAuditLogAttachments {


}
