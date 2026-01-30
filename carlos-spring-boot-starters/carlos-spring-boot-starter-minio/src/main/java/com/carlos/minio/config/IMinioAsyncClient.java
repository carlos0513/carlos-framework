package com.carlos.minio.config;

import com.google.common.collect.Multimap;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MinioAsyncClient;
import io.minio.Signer;
import io.minio.credentials.Credentials;
import io.minio.errors.InsufficientDataException;
import io.minio.errors.InternalException;
import io.minio.errors.XmlParserException;
import io.minio.http.Method;
import okhttp3.HttpUrl;
import okhttp3.Request;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.ExecutionException;

import static io.minio.http.HttpUtils.EMPTY_BODY;

public class IMinioAsyncClient extends MinioAsyncClient {

    private String publicBaseUrl;

    public void setPublicBaseUrl(String publicBaseUrl) {
        this.publicBaseUrl = publicBaseUrl;
    }

    protected IMinioAsyncClient(MinioAsyncClient client) {
        super(client);
    }

    /**
     * 获取公网临时访问地址
     */
    public String getPresignedObjectPublicUrl(GetPresignedObjectUrlArgs args)
            throws InsufficientDataException, IOException, NoSuchAlgorithmException, InvalidKeyException, XmlParserException, InternalException, ExecutionException, InterruptedException {
        return getPresignedObjectUrl(args, publicBaseUrl);
    }

    /**
     * 获取临时访问地址，可以指定baseurl
     */
    public String getPresignedObjectUrl(GetPresignedObjectUrlArgs args, String endpoint)
            throws InsufficientDataException, InternalException,
                   InvalidKeyException, IOException, NoSuchAlgorithmException,
                   XmlParserException, ExecutionException, InterruptedException {

        checkArgs(args);

        byte[] body = (args.method() == Method.PUT || args.method() == Method.POST) ? EMPTY_BODY : null;

        Multimap<String, String> queryParams = newMultimap(args.extraQueryParams());
        if (args.versionId() != null) {
            queryParams.put("versionId", args.versionId());
        }

        String region = getRegionAsync(args.bucket(), args.region()).get();
        if (provider == null) {
            HttpUrl url = buildUrl(args.method(), args.bucket(), args.object(), region, queryParams);
            return url.toString();
        }

        Credentials creds = provider.fetch();
        if (creds.sessionToken() != null) {
            queryParams.put("X-Amz-Security-Token", creds.sessionToken());
        }
        HttpUrl url = buildUrl(args.method(), args.bucket(), args.object(), region, queryParams);
        // 这部分修改访问地址，修改方式就是直接将baseurl替换成自定义的地址
        if (endpoint != null) {
            url = url.newBuilder(url.toString().replace(baseUrl.toString(), endpoint)).build();
        }
        Request request =
                createRequest(
                        url,
                        args.method(),
                        args.extraHeaders() == null ? null : httpHeaders(args.extraHeaders()),
                        body,
                        0,
                        creds);
        url = Signer.presignV4(request, region, creds.accessKey(), creds.secretKey(), args.expiry());
        return url.toString();
    }


}
