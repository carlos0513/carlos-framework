package com.carlos.audit.apiimpl;


import com.carlos.audit.api.ApiAuditLogArchiveRecord;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
/**
 * <p>
 * 审计日志归档记录（管理冷数据归档） api接口
 * </p>
 *
 * @author Carlos
 * @date 2026年3月6日 下午9:31:12
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("api/audit/log/archive/record")
@Tag(name = "审计日志归档记录（管理冷数据归档）Feign接口")
public class ApiAuditLogArchiveRecordImpl implements ApiAuditLogArchiveRecord {


}
