package com.carlos.audit.api.fallback;

import com.carlos.audit.api.ApiAuditLogFieldChange;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;


/**
 * <p>
 * 审计日志-字段级变更明细 api 降级
 * </p>
 *
 * @author Carlos
 * @date 2026年3月5日 下午11:36:53
 */
@Slf4j
public class ApiAuditLogFieldChangeFallbackFactory implements FallbackFactory<ApiAuditLogFieldChange> {

    @Override
    public ApiAuditLogFieldChange create(Throwable throwable) {
        String message = throwable.getMessage();
        log.error("审计日志-字段级变更明细服务调用失败: message:{}", message);
        return new ApiAuditLogFieldChange() {

        };
    }


    // 将此段配置放入配置类中进行Bean加载
    // @Bean
    // public ApiAuditLogFieldChangeFallbackFactory logFieldChangeFallbackFactory() {
    //     return new ApiAuditLogFieldChangeFallbackFactory();
    // }
}
