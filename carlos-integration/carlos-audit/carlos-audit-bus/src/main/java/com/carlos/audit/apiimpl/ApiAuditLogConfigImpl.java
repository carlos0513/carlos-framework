package com.carlos.audit.apiimpl;


import com.carlos.audit.api.ApiAuditLogConfig;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 审计日志配置 api接口
 * </p>
 *
 * @author Carlos
 * @date 2026年3月5日 下午11:36:54
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("api/audit/log/config")
@Tag(name = "审计日志配置Feign接口")
public class ApiAuditLogConfigImpl implements ApiAuditLogConfig {


}
