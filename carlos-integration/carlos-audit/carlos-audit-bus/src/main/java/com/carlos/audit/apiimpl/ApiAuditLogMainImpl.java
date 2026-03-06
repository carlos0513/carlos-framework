package com.carlos.audit.apiimpl;


import com.carlos.audit.api.ApiAuditLogMain;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
/**
 * <p>
 * 审计日志宽主表（合并数据变更、技术上下文、标签、附件，保留7天热数据） api接口
 * </p>
 *
 * @author Carlos
 * @date 2026年3月6日 下午9:31:12
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("api/audit/log/main")
@Tag(name = "审计日志宽主表（合并数据变更、技术上下文、标签、附件，保留7天热数据）Feign接口")
public class ApiAuditLogMainImpl implements ApiAuditLogMain {


}
