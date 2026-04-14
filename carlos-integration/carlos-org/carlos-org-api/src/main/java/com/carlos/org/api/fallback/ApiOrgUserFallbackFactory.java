package com.carlos.org.api.fallback;

import com.carlos.core.response.Result;
import com.carlos.org.api.ApiOrgUser;
import com.carlos.org.api.pojo.ao.OrgUserAO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;


/**
 * <p>
 * 系统用户 api 降级
 * </p>
 *
 * @author Carlos
 * @date 2026年2月28日 下午1:25:36
 */
@Slf4j
public class ApiOrgUserFallbackFactory implements FallbackFactory<ApiOrgUser> {

    @Override
    public ApiOrgUser create(Throwable throwable) {
        String message = throwable.getMessage();
        log.error("系统用户服务调用失败: message:{}", message);
        return new ApiOrgUser() {

            @Override
            public Result<OrgUserAO> getUserByIdentifier(String identifier) {
                log.error("系统用户服务调用失败: message:{}", message);
                return Result.error("系统用户服务调用失败");
            }
        };
    }


    // 将此段配置放入配置类中进行Bean加载
    // @Bean
    // public ApiOrgUserFallbackFactory userFallbackFactory() {
    //     return new ApiOrgUserFallbackFactory();
    // }
}
