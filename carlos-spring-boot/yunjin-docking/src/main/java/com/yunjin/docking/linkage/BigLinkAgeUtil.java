package com.yunjin.docking.linkage;

import com.yunjin.docking.linkage.result.BigLinkAgeUserInfo;
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
public class BigLinkAgeUtil {

    private static BigLinkAgeService service;


    public BigLinkAgeUtil(BigLinkAgeService service) {
        BigLinkAgeUtil.service = service;
    }


    /**
     * 获取用户信息
     *
     * @param userId 用户id
     * @return com.yunjin.app.rzt.api.result.UserInfoResult
     * @author Carlos
     * @date 2023/4/7 14:20
     */
    public static BigLinkAgeUserInfo getUserInfo(String userId) {
        BigLinkAgeUserInfo result = service.getUserInfo(userId);
        return result;
    }


}
