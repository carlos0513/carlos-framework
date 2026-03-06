package com.carlos.audit.api;

import com.carlos.audit.ServiceNameConstant;
import com.carlos.audit.api.fallback.ApiAuditLogArchiveRecordFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * <p>
 * 审计日志归档记录（管理冷数据归档） feign 提供接口
 * </p>
 *
 * @author Carlos
 * @date 2026年3月6日 下午9:31:12
 */
@FeignClient(value = ServiceNameConstant.SERVICE_NAME, contextId = "logArchiveRecord", path = "/api/audit/log/archive/record", fallbackFactory = ApiAuditLogArchiveRecordFallbackFactory.class)
public interface ApiAuditLogArchiveRecord {


}
