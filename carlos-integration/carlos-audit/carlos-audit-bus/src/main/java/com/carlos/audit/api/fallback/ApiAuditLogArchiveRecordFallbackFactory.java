package com.carlos.audit.api.fallback;

import com.carlos.audit.api.ApiAuditLogArchiveRecord;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;


/**
 * <p>
 * 审计日志归档记录 api 降级
 * </p>
 *
 * @author Carlos
 * @date 2026年3月5日 下午11:36:53
 */
@Slf4j
public class ApiAuditLogArchiveRecordFallbackFactory implements FallbackFactory<ApiAuditLogArchiveRecord> {

    @Override
    public ApiAuditLogArchiveRecord create(Throwable throwable) {
        String message = throwable.getMessage();
        log.error("审计日志归档记录服务调用失败: message:{}", message);
        return new ApiAuditLogArchiveRecord() {

        };
    }


    // 将此段配置放入配置类中进行Bean加载
    // @Bean
    // public ApiAuditLogArchiveRecordFallbackFactory logArchiveRecordFallbackFactory() {
    //     return new ApiAuditLogArchiveRecordFallbackFactory();
    // }
}
