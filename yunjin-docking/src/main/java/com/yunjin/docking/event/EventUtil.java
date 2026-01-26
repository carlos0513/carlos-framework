package com.yunjin.docking.event;

import com.yunjin.docking.event.result.EventUserInfo;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 蓉政通工具类
 * </p>
 *
 * @author Carlos
 * @date 2023/4/6 9:39
 */
@Slf4j
public class EventUtil {

    private static EventService service;


    public EventUtil(EventService service) {
        EventUtil.service = service;
    }


    /**
     * 获取用户信息
     *
     * @param userId 用户id
     * @return com.yunjin.app.rzt.api.result.UserInfoResult
     * @author Carlos
     * @date 2023/4/7 14:20
     */
    public static EventUserInfo getUserInfo(String userId) {
        EventUserInfo result = service.getUserInfo(userId);
        return result;
    }


}
