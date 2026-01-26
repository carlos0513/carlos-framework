package com.yunjin.docking.linkage;


import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import cn.hutool.json.JSONUtil;
import com.yunjin.docking.exception.DockingException;
import com.yunjin.docking.linkage.config.BigLinkAgeProperties;
import com.yunjin.docking.linkage.result.BigLinkAgeResult;
import com.yunjin.docking.linkage.result.BigLinkAgeUserInfo;
import com.yunjin.docking.linkage.result.UserInfoResult;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

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
public class BigLinkAgeService {

    private final FeignBigLinkAge feignBigLinkAge;

    private final BigLinkAgeProperties properties;

    private RSA rsa;

    @PostConstruct
    public void init() {
        String privateKey = properties.getPrivateKey();
        rsa = new RSA(privateKey, "");
    }

    /**
     * 获取用户信息
     *
     * @param userId 用户id
     * @return com.yunjin.app.rzt.api.result.UserInfoResult
     * @author Carlos
     * @date 2023/4/7 16:40
     */
    public BigLinkAgeUserInfo getUserInfo(String userId) {
        if (StrUtil.isBlank(userId)) {
            throw new DockingException("userid can't be null");
        }
        String appCode = properties.getAppCode();
        String result;
        try {
            result = feignBigLinkAge.getUserInfo(appCode, userId);
            log.info("BigLinkAge feign get user info result:{}", result);
        } catch (Exception e) {
            throw new DockingException("用户信息获取失败", e);
        }
        UserInfoResult userInfoResult = JSONUtil.toBean(result, UserInfoResult.class);
        checkResult(userInfoResult);
        String results = userInfoResult.getResults();
        // 对内容进行解密

        String decodeResult = decodeResult(results);
        BigLinkAgeUserInfo userInfo = null;
        try {
            userInfo = JSONUtil.toBean(decodeResult, BigLinkAgeUserInfo.class);
        } catch (Exception e) {
            throw new DockingException("用户信息反序列化失败", e);
        }
        return userInfo;
    }


    /**
     * 对内容进行解密
     *
     * @param results 参数0
     * @return java.lang.String
     * @author Carlos
     * @date 2023/5/6 14:14
     */
    public String decodeResult(String results) {
        try {
            String s = rsa.decryptStr(results, KeyType.PrivateKey);
            log.info("BigLinkAge result decode success: {}", s);
            return s;
        } catch (Exception e) {
            throw new DockingException("内容解密失败！", e);
        }
    }

    private void checkResult(BigLinkAgeResult result) {
        String errcode = result.getResultCode();
        if (StrUtil.isNotBlank(errcode)) {
            String errmsg = result.getResultMsg();
            ErrorCodeEnum code = ErrorCodeEnum.ofCode(errcode);
            if (code != null) {
                if (code != ErrorCodeEnum.A_003) {
                    log.error("BigLinkAge api result error: message:{}", code);
                    throw new DockingException(code.getName());
                }
            } else {
                log.error("BigLinkAge Service response error: errorCode:{}, errMsg:{}", errcode, errmsg);
                throw new DockingException(errmsg);
            }
        }
    }

}
