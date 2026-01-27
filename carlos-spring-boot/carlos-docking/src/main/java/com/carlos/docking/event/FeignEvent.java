package com.carlos.docking.event;

import com.carlos.core.response.Result;
import com.carlos.docking.event.result.EventUserInfo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

/**
 * <p>
 * 郫都区大联动接口
 * </p>
 *
 * @author Carlos
 * @date 2023/5/6 11:26
 */
public interface FeignEvent {

    /**
     * @Title: getUserInfo
     * @Description: 获取登录用户信息
     * @Date: 2023/6/29 15:51
     * @Parameters: [appcode, userid]
     * @Return java.lang.String
     */
    @GetMapping("getLoginUserInfo")
    Result<EventUserInfo> getLoginUserInfo(@RequestHeader("Authorization") String token);
}
