package com.carlos.system.api;

import com.carlos.core.response.Result;
import com.carlos.system.ServiceNameConstant;
import com.carlos.system.fallback.FeignSysNewsFallbackFactory;
import com.carlos.system.pojo.ao.SysNewsAO;
import com.carlos.system.pojo.ao.SysNewsDetailAO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * <p>
 * 系统-日志表 feign 提供接口
 * </p>
 *
 * @author yunjin
 * @date 2022-11-15 15:52:46
 */
@FeignClient(value = ServiceNameConstant.SYSTEM, contextId = "news", path = "/api/sys/news", fallbackFactory = FeignSysNewsFallbackFactory.class)
public interface ApiSysNews {

    @PostMapping("list")
    Result<List<SysNewsAO>> list();

    @GetMapping("/{id}")
    Result<SysNewsDetailAO> getById(@PathVariable String id);

    @PostMapping
    boolean addSysNews(@RequestBody SysNewsAO ao);
}
