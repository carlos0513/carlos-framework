package com.yunjin.org.fallback;

import com.yunjin.org.api.ApiLabelType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;


/**
* <p>
    * 标签分类 feign 降级
    * </p>
 *
 * @author  yunjin
 * @date    2024-3-22 15:07:09
 */
@Slf4j
public class FeignLabelTypeFallbackFactory implements FallbackFactory<ApiLabelType> {

    @Override
    public ApiLabelType create(Throwable cause) {
        return null;
    }
}