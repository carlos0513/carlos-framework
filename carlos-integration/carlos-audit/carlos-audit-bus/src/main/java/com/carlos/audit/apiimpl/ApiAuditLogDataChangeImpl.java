package com.carlos.audit.apiimpl;


import com.carlos.audit.api.ApiAuditLogDataChange;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 审计日志-数据变更详情 api接口
 * </p>
 *
 * @author Carlos
 * @date 2026年3月5日 下午11:36:54
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("api/audit/log/data/change")
@Tag(name = "审计日志-数据变更详情Feign接口")
public class ApiAuditLogDataChangeImpl implements ApiAuditLogDataChange {


}
