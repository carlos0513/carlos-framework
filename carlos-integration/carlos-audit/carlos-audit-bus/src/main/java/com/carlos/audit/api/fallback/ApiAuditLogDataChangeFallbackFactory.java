package com.carlos.audit.api.fallback;

import com.carlos.audit.api.ApiAuditLogDataChange;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;


/**
 * <p>
 * 审计日志-数据变更详情 api 降级
 * </p>
 *
 * @author Carlos
 * @date 2026年3月5日 下午11:36:53
 */
@Slf4j
public class ApiAuditLogDataChangeFallbackFactory implements FallbackFactory<ApiAuditLogDataChange> {

    @Override
    public ApiAuditLogDataChange create(Throwable throwable) {
        String message = throwable.getMessage();
        log.error("审计日志-数据变更详情服务调用失败: message:{}", message);
        return new ApiAuditLogDataChange() {

        };
    }


    // 将此段配置放入配置类中进行Bean加载
    // @Bean
    // public ApiAuditLogDataChangeFallbackFactory logDataChangeFallbackFactory() {
    //     return new ApiAuditLogDataChangeFallbackFactory();
    // }
}
