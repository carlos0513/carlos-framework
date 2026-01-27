package com.carlos.docking.rzt.config;

import cn.hutool.extra.spring.SpringUtil;
import com.carlos.docking.rzt.FeignRzt;
import com.carlos.docking.rzt.RztService;
import com.carlos.docking.rzt.exception.DockingRztException;
import com.carlos.docking.rzt.result.AccessTokenResult;
import com.carlos.redis.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 蓉政通AccessToken管理器
 * </p>
 *
 * @author Carlos
 * @date 2023/4/6 10:12
 */
@Slf4j
public class RztAccessTokenManager {

    private final RztProperties properties;

    private final FeignRzt feignRzt;

    public final String redisKey;

    public RztAccessTokenManager(RztProperties properties, FeignRzt feignRzt) {
        this.properties = properties;
        this.feignRzt = feignRzt;
        redisKey = String.format(RztConstant.ACCESS_TOKEN, properties.getCorpid());
    }


    public String getAccessToken() {
        // 判断缓存中是否有token
        String token = RedisUtil.getValue(redisKey);
        if (token == null) {
            // l("token已失效");
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
        try {
            RztService rztService = SpringUtil.getBean(RztService.class);
            AccessTokenResult accessTokenResult = feignRzt.getAccessToken(rztService.getHeaders(), properties.getCorpid(), properties.getSecret());
            String accessToken = accessTokenResult.getAccessToken();
            RedisUtil.setValue(redisKey, accessToken, properties.getTokenDuration());
            log.info("Get accessToken success: accessToken:{}", accessToken);
            return accessToken;
        } catch (Exception e) {
            log.error("Send getAccessToken request failed: message:{}", e.getMessage(), e);
            throw new DockingRztException("accesstoken 获取失败");
        }
    }
}
