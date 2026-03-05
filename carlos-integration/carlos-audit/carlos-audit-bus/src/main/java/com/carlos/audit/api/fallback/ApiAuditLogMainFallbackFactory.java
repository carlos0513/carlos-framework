package com.carlos.audit.api.fallback;

import com.carlos.audit.api.ApiAuditLogMain;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;


/**
 * <p>
 * 审计日志主表 api 降级
 * </p>
 *
 * @author Carlos
 * @date 2026年3月5日 下午11:36:53
 */
@Slf4j
public class ApiAuditLogMainFallbackFactory implements FallbackFactory<ApiAuditLogMain> {

    @Override
    public ApiAuditLogMain create(Throwable throwable) {
        String message = throwable.getMessage();
        log.error("审计日志主表服务调用失败: message:{}", message);
        return new ApiAuditLogMain() {

        };
    }


    // 将此段配置放入配置类中进行Bean加载
    // @Bean
    // public ApiAuditLogMainFallbackFactory logMainFallbackFactory() {
    //     return new ApiAuditLogMainFallbackFactory();
    // }
}
