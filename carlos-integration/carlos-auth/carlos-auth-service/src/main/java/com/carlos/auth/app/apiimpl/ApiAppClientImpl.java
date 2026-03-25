package com.carlos.auth.app.apiimpl;


import com.carlos.auth.app.api.ApiAppClient;
import com.carlos.auth.app.api.pojo.ao.AppClientAO;
import com.carlos.auth.app.convert.AppClientConvert;
import com.carlos.auth.app.pojo.dto.AppClientDTO;
import com.carlos.auth.app.service.AppClientService;
import com.carlos.core.response.Result;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;

/**
 * <p>
 * 应用信息 api接口
 * </p>
 *
 * @author Carlos
 * @date 2025-3-12 14:00:14
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("api/oauth2/app/client")
@Tag(name = "应用信息Feign接口")
public class ApiAppClientImpl implements ApiAppClient {

    private final AppClientService service;

    /**
     * 获取应用信
     *
     * @param id 应用id
     * @return com.carlos.core.response.Result<com.carlos.auth.app.api.pojo.ao.AppClientAO>
     * @author Carlos
     * @date 2025-04-15 14:58
     */
    @Override
    @GetMapping(value = "/id")
    public Result<AppClientAO> getById(@RequestParam("id") Serializable id) {
        AppClientDTO client = service.findById(id, true);
        return Result.success(AppClientConvert.INSTANCE.toAO(client));
    }

    /**
     * 获取应用信息
     *
     * @param appKey appKey
     * @return com.carlos.core.response.Result<com.carlos.auth.app.api.pojo.ao.AppClientAO>
     * @author Carlos
     * @date 2025-04-15 14:58
     */
    @Override
    @GetMapping(value = "/appKey")
    public Result<AppClientAO> getByAppKey(@RequestParam("appKey") String appKey) {
        AppClientDTO client = service.findByAppkey(appKey, true);
        return Result.success(AppClientConvert.INSTANCE.toAO(client));
    }

}
