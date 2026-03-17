package com.carlos.integration.module.dingtalk.config;

import com.carlos.integration.common.exception.DockingException;
import com.carlos.integration.module.dingtalk.exception.DockingDingtalkException;
import com.carlos.redis.util.RedisUtil;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiGettokenRequest;
import com.dingtalk.api.response.OapiGettokenResponse;
import com.taobao.api.ApiException;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 钉钉AccessToken管理�?
 * </p>
 *
 * @author Carlos
 * @date 2023/4/6 10:12
 */
@Slf4j
public class DingtalkAccessTokenManager {

    private final DingtalkProperties properties;

    public final String redisKey;

    public DingtalkAccessTokenManager(DingtalkProperties properties) {
        this.properties = properties;
        redisKey = String.format(DingtalkConstant.ACCESS_TOKEN, properties.getAppkey());
    }


    public String getAccessToken() {
        // 判断缓存中是否有token
        String token = RedisUtil.getValue(redisKey);
        if (token == null) {
            // l("token已失�?);
            token = refreshAccessToken();
        }
        return token;
    }


    /**
     * 刷新AccessToken
     *
     * @author Carlos
     * @date 2023/4/7 14:53
     */
    public String refreshAccessToken() {
        OapiGettokenRequest request = new OapiGettokenRequest();
        request.setAppkey(properties.getAppkey());
        request.setAppsecret(properties.getAppsecret());
        request.setHttpMethod("GET");
        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/gettoken");

        try {
            OapiGettokenResponse response = client.execute(request);
            String errcode = response.getErrorCode();
            if (!errcode.equals("0")) {
                log.error("Dingtalk Service response error: errorCode:{}, errMsg:{}", errcode, response.getMessage());
                throw new DockingException("钉钉服务调用出错�?);
            }
            String accessToken = response.getAccessToken();
            RedisUtil.setValue(redisKey, accessToken, properties.getTokenDuration());
            log.info("Get dingtalk accessToken success: accessToken:{}", accessToken);
            return accessToken;
        } catch (ApiException e) {
            log.error("Get dingtalk access_token error, message:{}", e.getMessage(), e);
            throw new DockingDingtalkException("钉钉服务访问失败�?);

        }
    }
}
