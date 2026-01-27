package com.carlos.docking.event;


import cn.hutool.core.util.StrUtil;
import com.carlos.core.response.Result;
import com.carlos.core.response.StatusCode;
import com.carlos.docking.event.config.EventProperties;
import com.carlos.docking.event.result.EventUserInfo;
import com.carlos.docking.exception.DockingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 引擎相关服务
 * </p>
 *
 * @author Carlos
 * @date 2022/2/23 18:05
 */
@Slf4j
@RequiredArgsConstructor
public class EventService {

    private final FeignEvent feignEvent;

    private final EventProperties properties;


    /**
     * 获取用户信息
     *
     * @param token token 信息
     * @return com.carlos.app.rzt.api.result.UserInfoResult
     * @author Carlos
     * @date 2023/4/7 16:40
     */
    public EventUserInfo getUserInfo(String token) {
        if (StrUtil.isBlank(token)) {
            throw new DockingException("token can't be null");
        }
        Result<EventUserInfo> result;
        try {
            result = feignEvent.getLoginUserInfo(token);
            log.info("event feign get user info result:{}", result);
        } catch (Exception e) {
            throw new DockingException("用户信息获取失败", e);
        }
        checkResult(result);
        EventUserInfo userInfo = result.getData();
        return userInfo;
    }


    private void checkResult(Result result) {
        Integer code = result.getCode();
        if (code != null) {
            String errmsg = result.getMessage();
            StatusCode apiCode = StatusCode.getApiCode(code);
            if (apiCode != null) {
                if (StatusCode.SUCCESS != apiCode) {
                    log.error("event api result error: message:{}", apiCode);
                    throw new DockingException(apiCode.getMessage());
                }
            } else {
                log.error("event Service response error: errorCode:{}, errMsg:{}", code, errmsg);
                throw new DockingException(errmsg);
            }
        }
    }

}
