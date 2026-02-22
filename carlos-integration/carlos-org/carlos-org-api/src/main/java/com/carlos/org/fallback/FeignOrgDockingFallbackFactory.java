package com.carlos.org.fallback;

import com.carlos.core.response.Result;
import com.carlos.org.api.ApiOrgDocking;
import com.carlos.org.pojo.ao.OrgDockingMappingAO;
import com.carlos.org.pojo.enums.OrgDockingTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;


/**
 * <p>
 * 用户降级服务
 * </p>
 *
 * @author Carlos
 * @date 2022/11/16 10:57
 */

@Slf4j
public class FeignOrgDockingFallbackFactory implements FallbackFactory<ApiOrgDocking> {

    @Override
    public ApiOrgDocking create(Throwable throwable) {
        String message = throwable.getMessage();
        log.error("组织信息对接服务调用失败: message:{}", message);
        return new ApiOrgDocking() {
            @Override
            public Result<OrgDockingMappingAO> getDocking(String systemId, String targetCode, OrgDockingTypeEnum type) {
                return Result.fail("对接配置获取失败！");
            }
        };
    }
}