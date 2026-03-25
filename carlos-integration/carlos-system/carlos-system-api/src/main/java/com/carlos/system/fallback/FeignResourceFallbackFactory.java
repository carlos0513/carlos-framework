package com.carlos.system.fallback;

import com.carlos.core.response.Result;
import com.carlos.system.api.ApiResource;
import com.carlos.system.pojo.ao.SysResourceAO;
import com.carlos.system.pojo.param.ApiResourceCategoryAddParam;
import com.carlos.system.pojo.param.ApiSysResourceAddParam;
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
public class FeignResourceFallbackFactory implements FallbackFactory<ApiResource> {

    @Override
    public ApiResource create(Throwable throwable) {
        String message = throwable.getMessage();
        log.error("接口资源服务调用失败: message:{}", message);
        return new ApiResource() {

            @Override
            public Result<SysResourceAO> getResourceById(String id) {
                return Result.error();
            }

            @Override
            public Result<Boolean> addResource(ApiSysResourceAddParam param) {
                return Result.error();
            }

            @Override
            public Result<Boolean> addResourceCategory(ApiResourceCategoryAddParam param) {
                return Result.error();
            }
        };
    }
}
