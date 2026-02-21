package com.yunjin.org.fallback;

import com.yunjin.org.api.ApiLabel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;



/**
* <p>
    * 标签 feign 降级
    * </p>
 *
 * @author  yunjin
 * @date    2024-3-22 15:36:43
 */
@Slf4j
public class FeignLabelFallbackFactory implements FallbackFactory<ApiLabel> {

    @Override
    public ApiLabel create(Throwable cause) {
        return null;
    }
}