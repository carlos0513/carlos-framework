package com.carlos.sms.ynchina.core;

import cn.hutool.core.codec.Base64;
import cn.hutool.crypto.SmUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.SM2;
import cn.hutool.crypto.symmetric.SM4;
import cn.hutool.json.JSONUtil;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zhouyh
 * @version 1.0.0
 * @date 2025/07/01 16:11
 */
@Slf4j
public final class YuChinaGatewaySDK {

    private static final String HEADER_ACCESS_INFO = "Access-Info";
    private static final String HEADER_DATA_SIGN = "Data-Sign";
    private static final String HEADER_CLIENT_VERSION = "X-Client-Version";

    private final SM2 appPrivateKey;
    private final SM2 serverPublicKey;
    private final SM2 accessInfoPublicKey;
    private final String appId;
    @Setter
    private RestTemplate restTemplate;

    private static final String GATEWAY_CLIENT_VERSION = "1.0.0";

    private static final String ACCESS_INFO_PUBLIC_KEY = "MFkwEwYHKoZIzj0CAQYIKoEcz1UBgi0DQgAEioJ10yCaTsWnCz3Pw3Iqz1axWYGLUG8gyURDqt5MJIM8ksEHTxy6mO5OZF5k0itkO4mbe3Ga2jhhol0fqwii3Q==";

    public YuChinaGatewaySDK(@NonNull String appId, @NonNull String appPrivateKey, @NonNull String serverPublicKey) {
        this.appId = appId;
        if (!StringUtils.hasText(appPrivateKey) || !StringUtils.hasText(serverPublicKey)) {
            throw new IllegalArgumentException("appPrivateKey and serverPublicKey cannot be null or empty");
        }
        try {
            this.appPrivateKey = SmUtil.sm2(Base64.decode(appPrivateKey), null);
            this.serverPublicKey = SmUtil.sm2(null, Base64.decode(serverPublicKey));
            this.accessInfoPublicKey = SmUtil.sm2(null, Base64.decode(ACCESS_INFO_PUBLIC_KEY));
        } catch (Exception e) {
            log.error("初始化SM2密钥失败", e);
            throw new IllegalArgumentException("Invalid appPrivateKey or serverPublicKey", e);
        }
    }

    private <T extends BaseRequest<R>, R extends BaseResponse> R doPost(@NonNull String url, @NonNull T requestBody, @NonNull HttpHeaders requestHeaders) throws GatewayException, RestClientException {
        if (!StringUtils.hasText(url)) {
            throw new IllegalArgumentException("URL cannot be null or empty");
        }
        String requestBodyJsonStr = JSONUtil.toJsonStr(requestBody);
        String accessInfo = encryptAccessInfo();
        String sign = signRequestBody(requestBodyJsonStr);
        String requestBodyEncrypt = encryptRequestBody(requestBodyJsonStr, sign);
        requestHeaders.setContentType(MediaType.APPLICATION_JSON);
        requestHeaders.set(HEADER_CLIENT_VERSION, GATEWAY_CLIENT_VERSION);
        requestHeaders.set(HEADER_ACCESS_INFO, accessInfo);
        requestHeaders.set(HEADER_DATA_SIGN, sign);
        log.info("==================网关请求================");
        log.info("请求头");
        for (Map.Entry<String, List<String>> entry : requestHeaders.entrySet()) {
            log.info("    {}:{}", entry.getKey(), entry.getValue());
        }
        log.info("请求体");
        log.info("明文请求体：{}", requestBodyJsonStr);
        log.info("密文请求体：{}", requestBodyEncrypt);
        log.info("========================================");
        HttpEntity<String> requestEntity = new HttpEntity<>(requestBodyEncrypt, requestHeaders);
        ResponseEntity<String> gatewayResponse = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
        return handleResponse(gatewayResponse, requestBody.getResponseType());
    }

    private <T extends BaseResponse> T handleResponse(@NonNull ResponseEntity<String> response, @NonNull Class<T> responseType) throws GatewayException {
        HttpStatusCode statusCode = response.getStatusCode();
        String responseBodyStr = response.getBody();
        log.info("Response Code: {}", statusCode);
        log.info("Response: {}", responseBodyStr);
        if (statusCode.is2xxSuccessful()) {
            GatewayResponse gatewayResponse = JSONUtil.toBean(responseBodyStr, GatewayResponse.class);
            Integer code = gatewayResponse.getCode();
            // 适配兼容原始协议不符合网关标准接口响应 add by zhouyh @ 2025-07-10
            if (code == null) {
                return JSONUtil.toBean(responseBodyStr, responseType);
            } else if (code == 200 || code == 2000) {
                String responseBodyDataStr = gatewayResponse.getData();
                String responseBodyKeyStr = gatewayResponse.getRandomSecretKey();
                String sign = gatewayResponse.getSign();
                if (!StringUtils.hasText(sign)) {
                    sign = response.getHeaders().getFirst(HEADER_DATA_SIGN);
                }
                String decryptedData;
                boolean verify = true;
                try {
                    decryptedData = decryptByAppPrivateKey(responseBodyDataStr, responseBodyKeyStr);
                    if (StringUtils.hasText(sign)) {
                        verify = verifySign(sign, decryptedData);
                        log.info("Verify result: {}", verify);
                    } else {
                        log.warn("响应中未包含签名");
                    }
                } catch (Exception e) {
                    log.error("解密或签名验证失败", e);
                    throw new GatewayException("解密或签名验证失败: " + e.getMessage(), e);
                }
                log.info("Decrypted data: {}", decryptedData);
                if (verify) {
                    if (PlainTextResponse.class.isAssignableFrom(responseType)) {
                        T plainTextResponse;
                        try {
                            plainTextResponse = responseType.getDeclaredConstructor().newInstance();
                        } catch (Exception e) {
                            throw new GatewayException("无法创建响应对象实例: " + e.getMessage(), e);
                        }
                        ((PlainTextResponse) plainTextResponse).setData(decryptedData);
                        return plainTextResponse;
                    }
                    return JSONUtil.toBean(decryptedData, responseType);
                } else {
                    throw new GatewayException(-1, "签名验证失败");
                }
            } else {
                throw new GatewayException(code, "服务调用异常: " + gatewayResponse.getMsg());
            }
        } else if (statusCode.is4xxClientError()) {
            throw new GatewayException(response.getStatusCodeValue(), "客户端请求异常");
        } else {
            throw new GatewayException(response.getStatusCodeValue(), "服务调用异常");
        }
    }

    public <T extends BaseRequest<R>, R extends BaseResponse> R post(@NonNull String url, @NonNull T requestBody) throws GatewayException, RestClientException {
        HttpHeaders requestHeaders = new HttpHeaders();
        return doPost(url, requestBody, requestHeaders);
    }

    public <T extends BaseRequest<R>, R extends BaseResponse> R post(@NonNull String url, @NonNull T requestBody, @Nullable HttpHeaders requestHeaders) throws GatewayException, RestClientException {
        if (requestHeaders == null) {
            requestHeaders = new HttpHeaders();
        }
        return doPost(url, requestBody, requestHeaders);
    }

    public String getVersion() {
        return GATEWAY_CLIENT_VERSION;
    }

    private String encryptAccessInfo() {
        HashMap<String, Object> accessInfo = new HashMap<String, Object>() {{
            put("appId", appId);
            put("timestamp", System.currentTimeMillis());
        }};
        String text = JSONUtil.toJsonStr(accessInfo);
        return accessInfoPublicKey.encryptBase64(text, KeyType.PublicKey);
    }

    private String encryptRequestBody(@NonNull String requestBodyJsonStr, @NonNull String sign) {
        SM4 sm4 = SmUtil.sm4();
        byte[] keyBytes = sm4.getSecretKey().getEncoded();
        String ketStr = Base64.encode(keyBytes);
        String randomKey = serverPublicKey.encryptBase64(ketStr, KeyType.PublicKey);
        String encryptHex = sm4.encryptBase64(requestBodyJsonStr);
        HashMap<String, String> map = new HashMap<>();
        map.put("sign", sign);
        map.put("dataEncrypt", encryptHex);
        map.put("randomSecretKey", randomKey);
        return JSONUtil.toJsonStr(map);
    }

    private String signRequestBody(@NonNull String requestBodyJsonStr) {
        String digestHex = SmUtil.sm3(requestBodyJsonStr);
        log.info("requestBody digestHex: {}", digestHex);
        return appPrivateKey.signHex(digestHex);
    }

    private String decryptByAppPrivateKey(@NonNull String responseBodyDataStr, @NonNull String responseBodyKeyStr) {
        String sm4key = appPrivateKey.decryptStr(responseBodyKeyStr, KeyType.PrivateKey);
        SM4 sm4 = SmUtil.sm4(Base64.decode(sm4key));
        return sm4.decryptStr(responseBodyDataStr);
    }

    private boolean verifySign(@NonNull String responseSign, @NonNull String responseBody) {
        String digestHex = SmUtil.sm3(responseBody);
        return serverPublicKey.verifyHex(digestHex, responseSign);
    }

    public static abstract class BaseRequest<T extends BaseResponse> {
        public abstract Class<T> getResponseType();
    }

    public static abstract class BaseResponse {

    }

    /**
     * 原文响应类型（应用于部分接口返回的非JSON格式数据）
     */
    @EqualsAndHashCode(callSuper = true)
    @Data
    public static abstract class PlainTextResponse extends BaseResponse {
        private String data;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GatewayResponse {
        private Integer code;
        private String msg;
        private Boolean success;
        private String source;
        private String requestId;
        private String data;
        private String randomSecretKey;
        private String sign;
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    public static class GatewayException extends RuntimeException {
        private Integer code;
        private String message;

        public GatewayException(String message, Throwable cause) {
            this(-1, message, cause);
        }

        public GatewayException(Integer code, String message, Throwable cause) {
            super(message, cause);
            this.code = code;
        }
    }
}
