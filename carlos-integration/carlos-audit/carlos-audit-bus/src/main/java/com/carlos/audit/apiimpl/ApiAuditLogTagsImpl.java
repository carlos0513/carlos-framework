package com.carlos.audit.apiimpl;


import com.carlos.audit.api.ApiAuditLogTags;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 审计日志-动态标签 api接口
 * </p>
 *
 * @author Carlos
 * @date 2026年3月5日 下午11:36:54
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("api/audit/log/tags")
@Tag(name = "审计日志-动态标签Feign接口")
public class ApiAuditLogTagsImpl implements ApiAuditLogTags {


}
