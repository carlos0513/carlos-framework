package com.carlos.audit.api.fallback;

import com.carlos.audit.api.ApiAuditLogAttachments;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;


/**
 * <p>
 * 审计日志-附件引用 api 降级
 * </p>
 *
 * @author Carlos
 * @date 2026年3月5日 下午11:36:53
 */
@Slf4j
public class ApiAuditLogAttachmentsFallbackFactory implements FallbackFactory<ApiAuditLogAttachments> {

    @Override
    public ApiAuditLogAttachments create(Throwable throwable) {
        String message = throwable.getMessage();
        log.error("审计日志-附件引用服务调用失败: message:{}", message);
        return new ApiAuditLogAttachments() {

        };
    }


    // 将此段配置放入配置类中进行Bean加载
    // @Bean
    // public ApiAuditLogAttachmentsFallbackFactory logAttachmentsFallbackFactory() {
    //     return new ApiAuditLogAttachmentsFallbackFactory();
    // }
}
