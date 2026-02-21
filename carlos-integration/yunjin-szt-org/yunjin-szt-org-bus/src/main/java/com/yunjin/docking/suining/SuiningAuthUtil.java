package com.yunjin.docking.suining;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 遂宁登录工具类
 * </p>
 *
 * @author Carlos
 * @date 2023/4/6 9:39
 */
@Slf4j
public class SuiningAuthUtil {

    private static SuiningAuthService service;


    public SuiningAuthUtil(SuiningAuthService service) {
        SuiningAuthUtil.service = service;
    }


    public static String getUserPhone(String code) {
        String phone = service.getAccessToken(code);
        return phone;
    }
}
