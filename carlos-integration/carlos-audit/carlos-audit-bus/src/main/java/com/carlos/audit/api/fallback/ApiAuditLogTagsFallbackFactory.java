package com.carlos.audit.api.fallback;

import com.carlos.audit.api.ApiAuditLogTags;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;


/**
 * <p>
 * 审计日志-动态标签 api 降级
 * </p>
 *
 * @author Carlos
 * @date 2026年3月5日 下午11:36:53
 */
@Slf4j
public class ApiAuditLogTagsFallbackFactory implements FallbackFactory<ApiAuditLogTags> {

    @Override
    public ApiAuditLogTags create(Throwable throwable) {
        String message = throwable.getMessage();
        log.error("审计日志-动态标签服务调用失败: message:{}", message);
        return new ApiAuditLogTags() {

        };
    }


    // 将此段配置放入配置类中进行Bean加载
    // @Bean
    // public ApiAuditLogTagsFallbackFactory logTagsFallbackFactory() {
    //     return new ApiAuditLogTagsFallbackFactory();
    // }
}
