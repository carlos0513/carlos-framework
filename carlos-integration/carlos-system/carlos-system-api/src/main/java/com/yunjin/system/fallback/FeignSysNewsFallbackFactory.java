package com.carlos.system.fallback;

import com.carlos.core.response.Result;
import com.carlos.system.api.ApiSysNews;
import com.carlos.system.pojo.ao.SysNewsAO;
import com.carlos.system.pojo.ao.SysNewsDetailAO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;

import java.util.List;

@Slf4j
public class FeignSysNewsFallbackFactory implements FallbackFactory<ApiSysNews> {

    @Override
    public ApiSysNews create(final Throwable throwable) {
        final String message = throwable.getMessage();
        log.error("用户服务调用失败: message:{}", message);
        return new ApiSysNews() {
            @Override
            public Result<List<SysNewsAO>> list() {
                return Result.fail("获取系统通知列表调用失败");
            }

            @Override
            public Result<SysNewsDetailAO> getById(String id) {
                return Result.fail("获取系统通知单例详情调用失败");
            }

            @Override
            public boolean addSysNews(SysNewsAO dto) {
                return false;
            }
        };
    }
}
