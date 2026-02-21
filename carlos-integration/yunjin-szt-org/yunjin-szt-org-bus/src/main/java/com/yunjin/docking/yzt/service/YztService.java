package com.yunjin.docking.yzt.service;

import com.alibaba.fastjson.JSONObject;
import com.yunjin.docking.yzt.result.R;
import com.yunjin.docking.yzt.result.YztAccessToken;
import com.yunjin.docking.yzt.result.YztUserDetail;
import com.yunjin.docking.yzt.result.YztUserInfo;
import com.yunjin.docking.yzt.utils.OkHttpUtils;
import com.yunjin.redis.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * 云政通统一平台对接
 */
@Service
@Slf4j
public class YztService {

    @Value("${yunjin.docking.yzt.host:host}")
    String host;

    @Value("${yunjin.docking.yzt.corp-id:corpId}")
    String corpId;

    @Value("${yunjin.docking.yzt.token-prefix:prefix}")
    String tokenPrefix;

    @Value("${yunjin.docking.yzt.pc.secret:screst}")
    String pcSecret;

    @Value("${yunjin.docking.yzt.app.secret:asecret}")
    String appSecret;

    /**
     * 获取accessToken
     */
    public R<String> getAccessToken(String sourceType) {
        String responseBody = null;
        try {
            String url = host + "/cgi-bin/gettoken";
            Map<String, String> params = new HashMap<>(2);
            params.put("corpid", corpId);
            params.put("corpsecret", sourceType.equals("pc") ? pcSecret : appSecret);
            log.info("云政通请求url: {}", url);
            log.info("云政通请求参数: {}", params);
            responseBody = OkHttpUtils.doGet(url, params);
            log.info("云政通返回结果: {}", responseBody);
            YztAccessToken yztAccessToken = JSONObject.parseObject(responseBody, YztAccessToken.class);
            Integer errcode = yztAccessToken.getErrcode();
            if (errcode.equals(0)) {
                // 获取accessToken
                String accessToken = yztAccessToken.getAccess_token();
                // 凭证的有效时间（秒）
                Long expiresIn = yztAccessToken.getExpires_in();
                String tokenKey = String.format("%s_%s", tokenPrefix, sourceType);
                // 存到Redis缓存中并设置过期时间
                RedisUtil.setValue(tokenKey, accessToken, expiresIn);
                return R.data(accessToken);
            } else {
                String errmsg = yztAccessToken.getErrmsg();
                return R.fail(errmsg);
            }
        } catch (Exception e) {
            log.error("云政通获取accessToken失败：", e);
            return R.fail("云政通获取accessToken失败");
        }
    }

    /**
     * 根据code获取成员信息
     */
    public R<YztUserInfo> getUserInfo(String sourceType, String code) {
        String tokenKey = String.format("%s_%s", tokenPrefix, sourceType);
        // 先判断token是否失效
        if (!RedisUtil.hasKey(tokenKey)) {
            // 再获取一次token
            getAccessToken(sourceType);
        }
        String accessToken = RedisUtil.getValue(tokenKey);
        String responseBody;
        try {
            String url = host + "/cgi-bin/user/getuserinfo";
            Map<String, String> params = new HashMap<>(2);
            params.put("access_token", accessToken);
            params.put("code", code);
            log.info("云政通请求url: {}", url);
            log.info("云政通请求参数: {}", params);
            responseBody = OkHttpUtils.doGet(url, params);
            log.info("云政通返回结果: {}", responseBody);
            YztUserInfo yztUserInfo = JSONObject.parseObject(responseBody, YztUserInfo.class);
            Integer errcode = yztUserInfo.getErrcode();
            if (errcode.equals(0)) {
                return R.data(yztUserInfo);
            } else {
                String errmsg = yztUserInfo.getErrmsg();
                return R.fail(errmsg);
            }
        } catch (Exception e) {
            log.error("云政通根据code获取成员信息失败：", e);
            return R.fail("云政通根据code获取成员信息失败");
        }
    }

    /**
     * 读取成员
     */
    public R<YztUserDetail> getUserDetail(String sourceType, String userId) {
        String tokenKey = String.format("%s_%s", tokenPrefix, sourceType);
        // 先判断token是否失效
        if (!RedisUtil.hasKey(tokenKey)) {
            // 再获取一次token
            getAccessToken(sourceType);
        }
        String accessToken = RedisUtil.getValue(tokenKey);
        String responseBody;
        try {
            String url = host + "/cgi-bin/user/get";
            Map<String, String> params = new HashMap<>(2);
            params.put("access_token", accessToken);
            params.put("userid", userId);
            log.info("云政通请求url: {}", url);
            log.info("云政通请求参数: {}", params);
            responseBody = OkHttpUtils.doGet(url, params);
            log.info("云政通返回结果: {}", responseBody);
            YztUserDetail userDetail = JSONObject.parseObject(responseBody, YztUserDetail.class);
            Integer errcode = userDetail.getErrcode();
            if (errcode.equals(0)) {
                return R.data(userDetail);
            } else {
                String errmsg = userDetail.getErrmsg();
                return R.fail(errmsg);
            }
        } catch (Exception e) {
            log.error("云政通根据读取成员失败：", e);
            return R.fail("云政通读取成员失败");
        }
    }

}
