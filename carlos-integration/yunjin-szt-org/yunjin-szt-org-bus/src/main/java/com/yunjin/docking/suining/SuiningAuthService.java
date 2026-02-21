package com.yunjin.docking.suining;


import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.scca.api.DefaultSccaClient;
import com.scca.api.SccaClient;
import com.scca.api.internal.util.SccaHashMap;
import com.scca.api.request.common.RestRequestSsoV1;
import com.scca.api.response.common.RestResponseSsoV1;
import com.tencent.kona.crypto.CryptoUtils;
import com.tencent.kona.crypto.KonaCryptoProvider;
import com.yunjin.docking.suining.config.SuiningAuthProperties;
import com.yunjin.docking.suining.exception.DockingSuiningAuthException;
import com.yunjin.docking.suining.result.AccessTokenResult;
import com.yunjin.docking.suining.result.SuiningManagerUser;
import com.yunjin.docking.suining.result.SuiningNormalUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.PostConstruct;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.util.Optional;

/**
 * <p>
 * 引擎相关服务
 * </p>
 *
 * @author Carlos
 * @date 2022/2/23 18:05
 */
@Slf4j
@RequiredArgsConstructor
public class SuiningAuthService {

    private final SuiningAuthProperties properties;

    static {
        //这里在类加载的时候执行(只执行一次)，否则会报Provider故障
        if (java.security.Security.getProvider("KonaCrypto") == null) {
            java.security.Security.addProvider(new KonaCryptoProvider());

        } else {
            // 如果这里没有先remove再addprovide的话，系统可能报错，
            // 错误解释在 http://blogs.sun.com/jluehe/entry/how_a_jruby_based_web
            java.security.Security.removeProvider("KonaCrypto");
            java.security.Security.addProvider(new KonaCryptoProvider());
        }
    }


    @PostConstruct
    public void init() {

    }


    // sm4的ecb解密
    public String decrypt(String encDataBase64) throws Exception {
        if (encDataBase64 == null) {
            return "";
        }
        String plianData = "";
        String sm4key = properties.getSm4().getKey();
        byte[] sm4keyBytes = CryptoUtils.toBytes(sm4key);

        SecretKey secretKey = new SecretKeySpec(sm4keyBytes, "SM4");
        //腾讯  工具解密方法

        Cipher cipher = Cipher.getInstance("SM4/ECB/PKCS7Padding");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] ciphertext = cipher.doFinal(decodeBase64(encDataBase64));
        plianData = new String(ciphertext, "utf-8");//这里是解密后的json字符串
        return plianData;
    }

    // sm4的ecb加密，返回数据将加密的byte进行base64编码
    public String encrypt(String data) throws Exception {
        if (data == null) {
            return "";
        }
        String sm4key = properties.getSm4().getKey();
        byte[] sm4keyBytes = CryptoUtils.toBytes(sm4key);

        SecretKey secretKey = new SecretKeySpec(sm4keyBytes, "SM4");
        //腾讯  工具解密方法

        Cipher cipher = Cipher.getInstance("SM4/ECB/PKCS7Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] ciphertext = cipher.doFinal(data.getBytes("utf-8"));
        return base64(ciphertext);
    }


    /**
     * 将base64编码的字符串还原为byte数组
     *
     * @param content 字符串
     * @return 原byte数组
     */
    public byte[] decodeBase64(String content) {
        return Base64.getDecoder().decode(content);
    }

    /**
     * 将byte数组使用base64编码
     *
     * @param data byte数组
     * @return base64字符串
     */
    public String base64(byte[] data) {
        return Base64.getEncoder().encodeToString(data);
    }



    /**
     * 获取token
     *
     * @param code 前端获取的code
     * @return com.yunjin.docking.Suiningtd.result.AccessTokenResult
     * @author Carlos
     * @date 2023/7/10 13:34
     */
    public String getAccessToken(String code) {
        SccaClient sccaClient = new DefaultSccaClient(properties.getBaseUrl(), properties.getAppId(), properties.getPrivateKey(),
                "", "json", properties.getConnectTimeout(), properties.getReadTimeout(), "rsa-ssov2", properties.getPublicKey());
        //实例化具体API对应的request类,加入当前调用接口名称：
        RestRequestSsoV1 oauth2TokenRequest = new RestRequestSsoV1("/services/rest/token/oauth2Token");
        //调用时传入的参数
        SccaHashMap paramMap = oauth2TokenRequest.getRequsetParamMap();
        paramMap.put("code", code);
        paramMap.put("grant_type", "authorization_code");
        AccessTokenResult result;
        try {
            // 调用SDK,连接服务器
            RestResponseSsoV1<AccessTokenResult> response = sccaClient.execute(oauth2TokenRequest);
            log.info("request response:{}", JSONUtil.toJsonStr(response));
            if (!response.isSuccess()) {
                log.warn("登录失败：{}", response.getMessage());
                throw new DockingSuiningAuthException("认证失败！");
            }
            String responseString = response.getResponseString();
            result = JSONUtil.toBean(responseString, AccessTokenResult.class);
        } catch (Exception e) {
            log.error("请求错误：{}", e.getMessage(), e);
            throw new DockingSuiningAuthException("认证失败！");
        }
        String userType = result.getUserType();
        if (StrUtil.isBlank(userType)) {
            throw new DockingSuiningAuthException("用户类型为空！");
        }
        String userinfo = result.getUserinfo();
        if (StrUtil.isBlank(userinfo)) {
            throw new DockingSuiningAuthException("用户信息为空！");
        }
        try {
            userinfo = decrypt(userinfo);
        } catch (Exception e) {
            log.error("用户信息解密失败：{}", e.getMessage());
            throw new DockingSuiningAuthException(e);
        }

        switch (userType) {
            // 自然人
            case "user":
                SuiningNormalUser user = JSONUtil.toBean(userinfo, SuiningNormalUser.class);
                return Optional.ofNullable(user.getBase()).map(SuiningNormalUser.Base::getUsername).orElse("");
            // 法人
            case "manager":
                SuiningManagerUser manager = JSONUtil.toBean(userinfo, SuiningManagerUser.class);
                return Optional.ofNullable(manager.getBase()).map(SuiningManagerUser.Base::getUsername).orElse("");
            default:
                throw new DockingSuiningAuthException("认证失败！");
        }

    }
}
