package com.yunjin.org.api;

import com.yunjin.org.ServiceNameConstant;
import com.yunjin.org.fallback.FeignLabelTypeFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * <p>
 * 标签分类 feign 提供接口
 * </p>
 *
 * @author  yunjin
 * @date    2024-3-22 15:07:09
 */
@FeignClient(value = ServiceNameConstant.USER, path = "/api/org/label/type", contextId = "labelType", fallbackFactory = FeignLabelTypeFallbackFactory.class)
public interface ApiLabelType {


}
