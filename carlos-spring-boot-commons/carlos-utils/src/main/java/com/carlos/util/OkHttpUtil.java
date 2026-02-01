package com.carlos.util;


import javax.net.ssl.*;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

/**
 * <p>
 * okHttpUtil
 * </p>
 *
 * @author Carlos
 * @date 2024/9/23 22:46
 */
public class OkHttpUtil {

    /**
     * X509TrustManager instance which ignored SSL certification
     */
    public static final X509TrustManager IGNORE_SSL_TRUST_MANAGER_X509 = new X509TrustManager() {
        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) {
        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) {
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[]{};
        }
    };

    /**
     * Get initialized SSLContext instance which ignored SSL certification
     *
     * @return
     * @throws NoSuchAlgorithmException
     * @throws KeyManagementException
     */
    public static SSLContext getIgnoreInitedSslContext() throws NoSuchAlgorithmException, KeyManagementException {
        SSLContext sslContext = SSLContext.getInstance("SSL");
        sslContext.init(null, new TrustManager[]{IGNORE_SSL_TRUST_MANAGER_X509}, new SecureRandom());
        return sslContext;
    }

    /**
     * Get HostnameVerifier which ignored SSL certification
     *
     * @return
     */
    public static HostnameVerifier getIgnoreSslHostnameVerifier() {
        return new HostnameVerifier() {
            @Override
            public boolean verify(String arg0, SSLSession arg1) {
                return true;
            }
        };
    }

    /**
     * 跳过ssl检查
     *
     * @param client okhttpClient
     * @return okhttp3.OkHttpClient
     * @author Carlos
     * @date 2024/9/23 22:55
     */
    public static okhttp3.OkHttpClient disableCertCheck(okhttp3.OkHttpClient client)
            throws KeyManagementException, NoSuchAlgorithmException {
        SSLContext sslContext = getIgnoreInitedSslContext();
        SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
        return client
                .newBuilder()
                .sslSocketFactory(sslSocketFactory, IGNORE_SSL_TRUST_MANAGER_X509)
                .hostnameVerifier(getIgnoreSslHostnameVerifier())
                .build();
    }
}
