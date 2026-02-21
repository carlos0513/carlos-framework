package com.yunjin.org.fallback;

import com.yunjin.core.response.Result;
import com.yunjin.org.api.ApiComplaint;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.web.bind.annotation.RequestParam;


/**
 * <p>
 * 登录降级服务
 * </p>
 *
 * @author Carlos
 * @date 2022/11/16 10:57
 */

@Slf4j
public class FeignComplaintFallbackFactory implements FallbackFactory<ApiComplaint> {

    @Override
    public ApiComplaint create(Throwable throwable) {
        String message = throwable.getMessage();
        log.error("移动端登录服务调用失败: message:{}", message);
        return new ApiComplaint() {

            @Override
            public Result<Boolean> processCallback(@RequestParam String id, @RequestParam String content) {
                return Result.fail("市级投诉处理回调失败", message);
            }
        };
    }
}