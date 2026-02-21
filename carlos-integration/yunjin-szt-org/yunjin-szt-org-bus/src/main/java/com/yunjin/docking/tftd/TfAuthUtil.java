package com.yunjin.docking.tftd;

import com.yunjin.docking.tftd.result.AccessTokenResult;
import com.yunjin.docking.tftd.result.TfOauthUserInfoResult;
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
public class TfAuthUtil {

    private static TfAuthService tfAuthService;


    public TfAuthUtil(TfAuthService tfAuthService) {
        TfAuthUtil.tfAuthService = tfAuthService;
    }


    /**
     * 获取用户信息
     *
     * @param code 用户id
     * @return com.yunjin.app.tfAuth.api.result.UserInfoResult
     * @author Carlos
     * @date 2023/4/7 14:20
     */
    public static TfOauthUserInfoResult getUserInfo(String code) {
        AccessTokenResult accessToken = tfAuthService.getAccessToken(code);
        String token = "Bearer " + accessToken.getAccessToken();
        return tfAuthService.getUserInfo(token);
    }


    /**
     * 获取用户信息
     *
     * @param token 用户id
     * @return com.yunjin.app.tfAuth.api.result.UserInfoResult
     * @author Carlos
     * @date 2023/4/7 14:20
     */
    public static TfOauthUserInfoResult getUserInfoByToken(String token) {
        token = "Bearer " + token;
        return tfAuthService.getUserInfo(token);
    }


}
