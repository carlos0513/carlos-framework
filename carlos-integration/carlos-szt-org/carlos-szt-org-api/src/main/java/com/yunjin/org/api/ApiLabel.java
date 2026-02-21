package com.yunjin.org.api;

import com.yunjin.core.response.Result;
import com.yunjin.org.ServiceNameConstant;
import com.yunjin.org.fallback.FeignLabelFallbackFactory;
import com.yunjin.org.pojo.ao.LabelAO;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * <p>
 * 标签 feign 提供接口
 * </p>
 *
 * @author  yunjin
 * @date    2024-3-22 15:36:43
 */
// @FeignClient("com.yunjin.org")
@FeignClient(value = ServiceNameConstant.USER, path = "/api/org/label", contextId = "label", fallbackFactory = FeignLabelFallbackFactory.class)
public interface ApiLabel {

    public static final String BASE_NAME = "标签";

    @GetMapping("/id/{id}")
    @Operation(summary = BASE_NAME + "详情")
    Result<LabelAO> getById(@PathVariable(value = "id") String id);

    @GetMapping("/ids/{ids}")
    Result<List<LabelAO> > getByIds(@PathVariable(value = "ids") String ids);


}
