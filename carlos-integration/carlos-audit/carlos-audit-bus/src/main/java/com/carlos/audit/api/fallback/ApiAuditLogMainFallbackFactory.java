package com.carlos.audit.api.fallback;

import com.carlos.audit.api.ApiAuditLogMain;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;


/**
 * <p>
 * 审计日志宽主表（合并数据变更、技术上下文、标签、附件，保留7天热数据） api 降级
 * </p>
 *
 * @author Carlos
 * @date 2026年3月6日 下午9:31:12
 */
@Slf4j
public class ApiAuditLogMainFallbackFactory implements FallbackFactory<ApiAuditLogMain> {

    @Override
    public ApiAuditLogMain create(Throwable throwable) {
        String message = throwable.getMessage();
        log.error("审计日志宽主表（合并数据变更、技术上下文、标签、附件，保留7天热数据）服务调用失败: message:{}", message);
        return new ApiAuditLogMain() {

        };
    }


    // 将此段配置放入配置类中进行Bean加载
    // @Bean
    // public ApiAuditLogMainFallbackFactory logMainFallbackFactory() {
    //     return new ApiAuditLogMainFallbackFactory();
    // }
}
