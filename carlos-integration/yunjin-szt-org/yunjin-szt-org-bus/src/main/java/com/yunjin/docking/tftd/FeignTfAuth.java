package com.yunjin.docking.tftd;

import com.yunjin.docking.tftd.param.DeptListParam;
import com.yunjin.docking.tftd.param.UserListParam;
import com.yunjin.docking.tftd.result.AccessTokenResult;
import com.yunjin.docking.tftd.result.TfAuthResult;
import com.yunjin.docking.tftd.result.TfDeptInfoResult;
import com.yunjin.docking.tftd.result.TfOauthUserInfoResult;
import com.yunjin.docking.tftd.result.TfUserInfoResult;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * <p>
 * 数据接入记录 feign 提供接口
 * </p>
 *
 * @author Carlos
 * @date 2022-1-17 13:46:52
 */
public interface FeignTfAuth {

    /**
     * 获取accessToken
     *
     * @param corpid 参数0
     * @param secret 参数1
     * @return com.yunjin.app.tfAuth.api.result.AccessTokenResult
     * @author Carlos
     * @date 2023/4/7 14:44
     */
    @GetMapping("/auth/oauth/authorize")
    AccessTokenResult getCode(@RequestParam("corpid") String corpid, @RequestParam("corpsecret") String secret);

    /**
     * getAccessToken
     *
     * @param grantType   参数0
     * @param scope       参数1
     * @param code        参数2
     * @param redirectUri 参数3
     * @param token       参数4
     * @param tenantId    参数5
     * @return com.yunjin.docking.tftd.result.AccessTokenResult
     * @author Carlos
     * @date 2023/7/12 19:22
     */
    @PostMapping("auth/oauth/token")
    AccessTokenResult oauthToken(@RequestParam("grant_type") String grantType,
                                 @RequestParam("scope") String scope,
                                 @RequestParam("code") String code,
                                 @RequestParam("redirect_uri") String redirectUri,
                                 @RequestHeader("Authorization") String token,
                                 @RequestHeader("TENANT-ID") String tenantId
    );


    /**
     * 获取当前登录用户信息
     *
     * @param token    参数0
     * @param tenantId 参数1
     * @return com.yunjin.docking.tftd.result.TfAuthResult<com.yunjin.docking.tftd.result.UserInfoResult>
     * @author Carlos
     * @date 2023/7/10 11:23
     */
    @GetMapping("/admin/user/info")
    TfAuthResult<TfOauthUserInfoResult> getUserInfo(@RequestHeader("Authorization") String token,
                                                    @RequestHeader("TENANT-ID") String tenantId);


    @PostMapping("/admin/v2/user/list")
    TfAuthResult<List<TfUserInfoResult>> userList(@RequestHeader("Authorization") String token,
                                                  @RequestHeader("TENANT-ID") String tenantId, @RequestBody UserListParam userListParam);

    @PostMapping("/admin/v2/dept/list")
    TfAuthResult<List<TfDeptInfoResult>> deptList(@RequestHeader("Authorization") String token,
                                                  @RequestHeader("TENANT-ID") String tenantId, @RequestBody DeptListParam deptListParam);


}
