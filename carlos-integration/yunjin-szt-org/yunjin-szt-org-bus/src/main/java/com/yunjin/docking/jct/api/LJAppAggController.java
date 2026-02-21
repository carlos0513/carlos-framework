package com.yunjin.docking.jct.api;

import com.yunjin.boot.annotation.ClientApi;
import com.yunjin.docking.jct.LJAppAggrService;
import com.yunjin.docking.jct.config.LJAppAggrConfig;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 市级平台调用区级平台接口
 * </p>
 *
 * @author Carlos
 * @date 2023/10/9 13:48
 */
@Slf4j
@ClientApi
@ConditionalOnBean(LJAppAggrConfig.class)
@RestController
@RequiredArgsConstructor
@RequestMapping("org/jct")
@Tag(name = "黑龙江一体化平台对接")
public class LJAppAggController {

    private final LJAppAggrService service;


    @PostMapping("receiveUser")
    @Operation(summary = "接收用户信息")
    public LJAppAggrApiResult<Void> receiveDispatchTask(@RequestBody LJAppAggrPushUserParam param) {
        try {
            service.receiveUser(param);
        } catch (Exception e) {
            return LJAppAggrApiResult.fail(e.getMessage());
        }
        return LJAppAggrApiResult.ok();
    }

}
