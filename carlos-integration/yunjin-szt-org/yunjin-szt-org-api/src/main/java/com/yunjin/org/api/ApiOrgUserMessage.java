package com.yunjin.org.api;

import com.yunjin.core.param.ParamIdSet;
import com.yunjin.core.response.Result;
import com.yunjin.org.ServiceNameConstant;
import com.yunjin.org.fallback.FeignOrgUserMessageFallbackFactory;
import com.yunjin.org.pojo.ao.OrgUserMessageDetailAO;
import com.yunjin.org.pojo.param.ApiOrgUserMessageCreateParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * <p>
 * 用户消息表 feign 提供接口
 * </p>
 *
 * @author yunjin
 * @date 2024-2-28 17:39:16
 */
@FeignClient(value = ServiceNameConstant.USER, path = "/api/org/user/message", contextId = "message", fallbackFactory = FeignOrgUserMessageFallbackFactory.class)
public interface ApiOrgUserMessage {

    @GetMapping("getMessageOne")
    Result<OrgUserMessageDetailAO> getMessageOne(@RequestParam("id") String id);

    @PostMapping("messagesRead")
    void messagesRead(@RequestBody ParamIdSet<String> ids);

    @PostMapping("addMessage")
    void addMessage(@RequestBody ApiOrgUserMessageCreateParam param);

    @GetMapping("smsMessage")
    void smsMessage(@RequestParam("id") String id);
}
