package com.carlos.oauth.web;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.KeyPair;
import java.security.interfaces.RSAPublicKey;
import java.util.Map;

/**
 * <p>
 * 获取RSA公钥接口
 * </p>
 *
 * @author carlos
 * @date 2021/11/4 12:48
 */
@AllArgsConstructor
@RestController
@ConditionalOnBean(KeyPair.class)
public class KeyPairController {

    private final KeyPair keyPair;

    /**
     * 由于我们的网关服务需要RSA的公钥来验证签名是否合法，所以认证服务需要有个接口把公钥暴露出来
     *
     * @return java.util.Map<java.lang.String, java.lang.Object>
     * @author carlos
     * @date 2021/12/6 10:38
     */
    @GetMapping("/rsa/public")
    public Map<String, Object> getKey() {
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAKey key = new RSAKey.Builder(publicKey).build();
        return new JWKSet(key).toJSONObject();
    }

}
