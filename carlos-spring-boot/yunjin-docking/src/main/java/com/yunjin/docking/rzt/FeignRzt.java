package com.yunjin.docking.rzt;

import com.yunjin.docking.rzt.config.RztFeignConfig;
import com.yunjin.docking.rzt.param.RztRevokeMessageParam;
import com.yunjin.docking.rzt.result.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 数据接入记录 feign 提供接口
 * </p>
 *
 * @author Carlos
 * @date 2022-1-17 13:46:52
 */
@FeignClient(value = "rzt-bbt", path = "${yunjin.docking.rzt.api.path}", url = "${yunjin.docking.rzt.api.host}", contextId = "rzt", configuration = RztFeignConfig.class)
public interface FeignRzt {

    /**
     * 获取accessToken
     *
     * @param corpid 参数0
     * @param secret 参数1
     * @return com.yunjin.app.rzt.api.result.AccessTokenResult
     * @author Carlos
     * @date 2023/4/7 14:44
     */
    @GetMapping("gettoken")
    AccessTokenResult getAccessToken(@RequestHeader MultiValueMap<String, String> headers, @RequestParam("corpid") String corpid, @RequestParam("corpsecret") String secret);

    /**
     * 获取用户id
     *
     * @param token token
     * @param code  code
     * @return com.yunjin.app.rzt.api.result.UserIdResult
     * @author Carlos
     * @date 2023/4/7 16:08
     * @deprecated 使用getLoginInfo代替
     */
    @Deprecated
    @GetMapping("/user/getuserinfo")
    UserIdResult getUserId(@RequestHeader MultiValueMap<String, String> headers, @RequestParam("access_token") String token, @RequestParam("code") String code);

    /**
     * 获取用户id
     *
     * @param token token
     * @param code  code
     * @return com.yunjin.app.rzt.api.result.UserIdResult
     * @author Carlos
     * @date 2023/4/7 16:08
     */
    @GetMapping("/login/info")
    UserInfoResult getLoginInfo(@RequestHeader MultiValueMap<String, String> headers, @RequestParam("token") String token, @RequestParam("code") String code);

    /**
     * 获取用户详情信息
     *
     * @param token  token
     * @param userid 用户id
     * @return com.yunjin.app.rzt.api.result.UserInfoResult
     * @author Carlos
     * @date 2023/4/7 16:44
     */
    @GetMapping("/user/get")
    UserInfoResult getUserInfo(@RequestHeader MultiValueMap<String, String> headers, @RequestParam("access_token") String token, @RequestParam("userid") String userid);

    /**
     * 消息发送
     *
     * @param param 请求参数
     * @return com.yunjin.docking.rzt.result.MessageSendResult
     * @author Carlos
     * @date 2024-10-28 16:03
     */
    @PostMapping("/message/send/virtual_id")
    MessageSendResult sendMessage(@RequestHeader MultiValueMap<String, String> headers, @RequestBody String param);

    /**
     * 撤回消息
     *
     * @param token access_token
     * @param param 请求参数
     * @return com.yunjin.docking.rzt.result.RztResult
     * @author Carlos
     * @date 2024-11-18 22:27
     */
    @PostMapping("/message/revoke")
    RztResult revokeMessage(@RequestHeader MultiValueMap<String, String> headers, @RequestParam("access_token") String token, @RequestBody RztRevokeMessageParam param);


}
