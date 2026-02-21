package com.yunjin.docking.yzt.utils;

import lombok.extern.slf4j.Slf4j;
import okhttp3.*;

import javax.net.ssl.*;
import java.io.IOException;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;


@Slf4j
public class OkHttpUtils {

    private static final MediaType MEDIA_TYPE = MediaType.parse("application/json; charset=utf-8");

    private static int connTimeOut = 10;
    private static int readTimeOut = 10;
    private static int writeTimeOut = 10;
    private static int okHttpTimeOut = 10;
    public static OkHttpClient client = null;

    static {
        try {
            // 创建不验证证书链的信任管理器
            final TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                        }

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                        }

                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new java.security.cert.X509Certificate[]{};
                        }
                    }
            };
            if (trustAllCerts.length != 1 || !(trustAllCerts[0] instanceof X509TrustManager)) {
                throw new IllegalStateException("Unexpected default trust managers:" + Arrays.toString(trustAllCerts));
            }
            X509TrustManager x509TrustManager = (X509TrustManager) trustAllCerts[0];

            // 安装全信任信任管理器
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            // 使用我们完全信任的管理器创建 ssl 套接字工厂
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            OkHttpClient.Builder builder = new OkHttpClient.Builder()
                    .connectTimeout(60, TimeUnit.SECONDS)
                    .readTimeout(60, TimeUnit.SECONDS)
                    .writeTimeout(120, TimeUnit.SECONDS);
            builder.sslSocketFactory(sslSocketFactory, x509TrustManager);
            builder.hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });
            client = builder.build();
        } catch (Exception e) {
            log.error("创建OkHttpClient不进行SSL（证书）验证失败：{}", e.getMessage());
            throw new RuntimeException(e);
        }

//        client = new OkHttpClient.Builder()
//                .connectTimeout(connTimeOut, TimeUnit.SECONDS)
//                .readTimeout(readTimeOut, TimeUnit.SECONDS)
//                .writeTimeout(writeTimeOut, TimeUnit.SECONDS)
//                .retryOnConnectionFailure(true)
//                .build();
    }

    public OkHttpUtils() {
    }

    public static String doGet(Integer okHttpTimeOut, Request request) throws Exception {
        // 1 创建okhttp客户端对象
        OkHttpClient client = new OkHttpClient().newBuilder()
                .connectTimeout(okHttpTimeOut, TimeUnit.SECONDS)
                .readTimeout(okHttpTimeOut, TimeUnit.SECONDS)
                .build();
        // 2 request 默认是get请求
        Response response = client.newCall(request).execute();
        // 4 判断是否请求成功
        if (response.isSuccessful()) {
            // 得到响应体中的身体,将其转成  string
            String res = response.body().string();
            log.info("Response Parameter：{}", res);
            return res;
        } else {
            throw new Exception("获取失败");
        }
    }

    public static String doPost(String url) throws Exception {
        log.info("---->Request URL：{}", url);
        Request request = new Request.Builder()
                .url(url)
                .build();
        // 1 创建okhttp客户端对象
        OkHttpClient client = new OkHttpClient().newBuilder()
                .connectTimeout(okHttpTimeOut, TimeUnit.SECONDS)
                .readTimeout(okHttpTimeOut, TimeUnit.SECONDS)
                .build();
        // 2 request 默认是get请求
        Response response = client.newCall(request).execute();
        // 4 判断是否请求成功
        if (response.isSuccessful()) {
            // 得到响应体中的身体,将其转成  string
            String res = response.body().string();
            log.info("Response Parameter：{}", res);
            return res;
        } else {
            throw new Exception("获取失败");
        }
    }

    public static String doPost(String url, String requestJson) throws Exception {
        log.info("---->Request URL：{}, Request Parameter：{}", url, requestJson);
        RequestBody requestBody = RequestBody.create(MEDIA_TYPE, requestJson);
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        // 1 创建okhttp客户端对象
        OkHttpClient client = new OkHttpClient().newBuilder()
                .connectTimeout(okHttpTimeOut, TimeUnit.SECONDS)
                .readTimeout(okHttpTimeOut, TimeUnit.SECONDS)
                .build();
        // 2 request 默认是get请求
        Response response = client.newCall(request).execute();
        // 4 判断是否请求成功
        if (response.isSuccessful()) {
            // 得到响应体中的身体,将其转成  string
            String res = response.body().string();
            log.info("Response Parameter：{}", res);
            return res;
        } else {
            throw new Exception("获取失败");
        }
    }

    public static String doPost(String url, String requestJson, String mediaType) throws Exception {
        log.info("---->Request URL：{}, Request Parameter：{}", url, requestJson);
        RequestBody requestBody = RequestBody.create(MediaType.parse(mediaType), requestJson);
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        // 1 创建okhttp客户端对象
        OkHttpClient client = new OkHttpClient().newBuilder()
                .connectTimeout(okHttpTimeOut, TimeUnit.SECONDS)
                .readTimeout(okHttpTimeOut, TimeUnit.SECONDS)
                .build();
        // 2 request 默认是get请求
        Response response = client.newCall(request).execute();
        // 4 判断是否请求成功
        if (response.isSuccessful()) {
            // 得到响应体中的身体,将其转成  string
            String res = response.body().string();
            log.info("Response Parameter：{}", res);
            return res;
        } else {
            throw new Exception("获取失败");
        }
    }

    public static String doPost(String url, String requestJson, Map<String, String> headers) throws Exception {
        log.info("---->Request URL：{}, Request Parameter：{}", url, requestJson);
        RequestBody requestBody = RequestBody.create(MEDIA_TYPE, requestJson);
        Request.Builder requestBuilder = new Request.Builder();
        if (headers != null && headers.size() > 0) {
            Iterator iterator = headers.keySet().iterator();
            while (iterator.hasNext()) {
                String key = (String) iterator.next();
                requestBuilder.addHeader(key, (String) headers.get(key));
            }
        }
        Request request = requestBuilder
                .url(url)
                .post(requestBody)
                .build();
        // 1 创建okhttp客户端对象
        OkHttpClient client = new OkHttpClient().newBuilder()
                .connectTimeout(okHttpTimeOut, TimeUnit.SECONDS)
                .readTimeout(okHttpTimeOut, TimeUnit.SECONDS)
                .build();
        // 2 request 默认是get请求
        Response response = client.newCall(request).execute();
        // 4 判断是否请求成功
        if (response.isSuccessful()) {
            // 得到响应体中的身体,将其转成  string
            String res = response.body().string();
            log.info("Response Parameter：{}", res);
            return res;
        } else {
            throw new Exception("获取失败");
        }
    }

    public static String doGet(String host, String path, Map<String, String> headers, Map<String, String> querys) throws IOException {
        StringBuffer url = new StringBuffer(host + (path == null ? "" : path));
        if (querys != null) {
            url.append("?");
            Iterator iterator = querys.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, String> e = (Map.Entry) iterator.next();
                url.append((String) e.getKey()).append("=").append((String) e.getValue() + "&");
            }
            url = new StringBuffer(url.substring(0, url.length() - 1));
        }
        Request.Builder requestBuilder = new Request.Builder();
        if (headers != null && headers.size() > 0) {
            Iterator iterator = headers.keySet().iterator();
            while (iterator.hasNext()) {
                String key = (String) iterator.next();
                requestBuilder.addHeader(key, (String) headers.get(key));
            }
        }
        Request request = (requestBuilder).url(url.toString()).build();
        Response response = client.newCall(request).execute();
        String responseStr = response.body() == null ? "" : response.body().string();
        return responseStr;
    }

    public static String doGet(String reqUrl, Map<String, String> querys) throws IOException {
        StringBuffer url = new StringBuffer(reqUrl);
        if (querys != null) {
            url.append("?");
            Iterator iterator = querys.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, String> e = (Map.Entry) iterator.next();
                url.append((String) e.getKey()).append("=").append((String) e.getValue() + "&");
            }
            url = new StringBuffer(url.substring(0, url.length() - 1));
        }
        Request.Builder requestBuilder = new Request.Builder();
        Request request = (requestBuilder).url(url.toString()).build();
        Response response = client.newCall(request).execute();
        String responseStr = response.body() == null ? "" : response.body().string();
        return responseStr;
    }

    public static String doPost(String url, Map<String, String> headers, Map<String, String> querys) throws IOException {
        log.info("---->Request URL：{}, Header: {}, Request Parameter：{}", url, headers, querys);
        FormBody.Builder formbody = new FormBody.Builder();
        if (null != querys) {
            Iterator iterator = querys.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, String> elem = (Map.Entry) iterator.next();
                formbody.add((String) elem.getKey(), (String) elem.getValue());
            }
        }

        RequestBody body = formbody.build();
        Request.Builder requestBuilder = (new Request.Builder()).url(url);
        if (headers != null && headers.size() > 0) {
            Iterator iteratorHeader = headers.keySet().iterator();
            while (iteratorHeader.hasNext()) {
                String key = (String) iteratorHeader.next();
                requestBuilder.addHeader(key, (String) headers.get(key));
            }
        }

        Request requet = requestBuilder.post(body).build();
        Response response = client.newCall(requet).execute();
        String responseStr = response.body() == null ? "" : response.body().string();
        return responseStr;
    }

    public static String doPost(String url, Map<String, String> headers, String sendMessage) throws IOException {
        log.info("---->Request URL：{}, Header: {}, Request Parameter：{}", url, headers, sendMessage);
        RequestBody body = FormBody.create(MediaType.parse("application/json"), sendMessage);
        Request.Builder requestBuilder = (new Request.Builder()).url(url);
        if (headers != null && headers.size() > 0) {
            Iterator iteratorHeader = headers.keySet().iterator();
            while (iteratorHeader.hasNext()) {
                String key = (String) iteratorHeader.next();
                requestBuilder.addHeader(key, (String) headers.get(key));
            }
        }

        Request requet = requestBuilder.post(body).build();
        Response response = client.newCall(requet).execute();
        String responseStr = response.body() == null ? "" : response.body().string();
        return responseStr;
    }

    public static String doPut(String host, String path, Map<String, String> headers, Map<String, String> querys) throws IOException {
        FormBody.Builder builder = new FormBody.Builder();
        Iterator iterator = querys.entrySet().iterator();

        while (iterator.hasNext()) {
            Map.Entry<String, String> elem = (Map.Entry) iterator.next();
            builder.add((String) elem.getKey(), (String) elem.getValue());
        }

        RequestBody body = builder.build();
        Request.Builder requestBuilder = (new Request.Builder()).url(host + path);
        if (headers != null && headers.size() > 0) {
            Iterator iteratorHeader = headers.keySet().iterator();
            while (iteratorHeader.hasNext()) {
                String key = (String) iteratorHeader.next();
                requestBuilder.addHeader(key, (String) headers.get(key));
            }
        }

        Request requet = requestBuilder.put(body).build();
        Response response = client.newCall(requet).execute();
        String responseStr = response.body() == null ? "" : response.body().string();
        return responseStr;
    }

    public static SSLSocketFactory getSSLSocketFactory() {
        try {
            SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, getTrustManager(), new SecureRandom());
            return sslContext.getSocketFactory();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static TrustManager[] getTrustManager() {
        return new TrustManager[]{
                new X509TrustManager() {
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
                }
        };
    }

    public static X509TrustManager getX509TrustManager() {
        X509TrustManager trustManager = null;
        try {
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init((KeyStore) null);
            TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
            if (trustManagers.length != 1 || !(trustManagers[0] instanceof X509TrustManager)) {
                throw new IllegalStateException("Unexpected default trust managers:" + Arrays.toString(trustManagers));
            }
            trustManager = (X509TrustManager) trustManagers[0];
        } catch (Exception e) {
            e.printStackTrace();
        }
        return trustManager;
    }

    public static String sendPostWithHttps(String url, String requestJson) throws Exception {
        log.info("---->Request URL：{}, Request Parameter：{}", url, requestJson);
        RequestBody requestBody = RequestBody.create(MEDIA_TYPE, requestJson);
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        OkHttpClient client = new OkHttpClient.Builder()
                .followRedirects(true)
                .connectTimeout(15, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
                .readTimeout(15, TimeUnit.SECONDS)
                .retryOnConnectionFailure(false)
                .sslSocketFactory(getSSLSocketFactory(), getX509TrustManager())
                .hostnameVerifier(((s, sslSession) -> true))
                .build();
        Response response = client.newCall(request).execute();
        // 4 判断是否请求成功
        if (response.isSuccessful()) {
            // 得到响应体中的身体,将其转成  string
            String res = response.body().string();
            log.info("Response Parameter：{}", res);
            return res;
        } else {
            throw new Exception("获取失败");
        }
    }

}
