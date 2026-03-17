package com.carlos.integration.core.client;

import com.carlos.integration.core.support.DockingRestClientException;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * HTTP 请求构建�?
 * 提供流式 API 构建请求
 * </p>
 *
 * @author Carlos
 * @since 1.0.0
 */
public class RequestBuilder {

    private final RestClient restClient;
    private final String path;

    private HttpMethod method = HttpMethod.GET;
    private final Map<String, Object> queryParams = new HashMap<>();
    private Object body;
    private final Map<String, String> headers = new HashMap<>();
    private MediaType contentType = MediaType.APPLICATION_JSON;

    public RequestBuilder(RestClient restClient, String path) {
        this.restClient = restClient;
        this.path = path;
    }

    /**
     * 设置请求方法
     */
    public RequestBuilder method(HttpMethod method) {
        this.method = method;
        return this;
    }

    public RequestBuilder get() {
        this.method = HttpMethod.GET;
        return this;
    }

    public RequestBuilder post() {
        this.method = HttpMethod.POST;
        return this;
    }

    public RequestBuilder put() {
        this.method = HttpMethod.PUT;
        return this;
    }

    public RequestBuilder delete() {
        this.method = HttpMethod.DELETE;
        return this;
    }

    public RequestBuilder patch() {
        this.method = HttpMethod.PATCH;
        return this;
    }

    /**
     * 添加查询参数
     */
    public RequestBuilder queryParam(String name, Object value) {
        this.queryParams.put(name, value);
        return this;
    }

    /**
     * 添加多个查询参数
     */
    public RequestBuilder queryParams(Map<String, Object> params) {
        this.queryParams.putAll(params);
        return this;
    }

    /**
     * 设置请求�?
     */
    public RequestBuilder body(Object body) {
        this.body = body;
        return this;
    }

    /**
     * 添加请求�?
     */
    public RequestBuilder header(String name, String value) {
        this.headers.put(name, value);
        return this;
    }

    /**
     * 添加多个请求�?
     */
    public RequestBuilder headers(Map<String, String> headers) {
        this.headers.putAll(headers);
        return this;
    }

    /**
     * 设置 Content-Type
     */
    public RequestBuilder contentType(MediaType contentType) {
        this.contentType = contentType;
        return this;
    }

    /**
     * 执行请求（返�?Map�?
     */
    public Map<String, Object> execute() {
        return execute(new ParameterizedTypeReference<>() {
        });
    }

    /**
     * 执行请求（指定返回类型）
     */
    public <T> T execute(Class<T> returnType) {
        RestClient.RequestBodySpec spec = buildRequest();

        if (body != null) {
            spec.body(body);
        }

        return spec.retrieve()
            .onStatus(
                status -> status.isError(),
                (request, response) -> {
                    throw new DockingRestClientException(
                        "Request failed: " + response.getStatusText());
                }
            )
            .body(returnType);
    }

    /**
     * 执行请求（指定泛型返回类型）
     */
    public <T> T execute(ParameterizedTypeReference<T> returnType) {
        RestClient.RequestBodySpec spec = buildRequest();

        if (body != null) {
            spec.body(body);
        }

        return spec.retrieve()
            .onStatus(
                status -> status.isError(),
                (request, response) -> {
                    throw new DockingRestClientException(
                        "Request failed: " + response.getStatusText());
                }
            )
            .body(returnType);
    }

    /**
     * 执行请求（无返回值）
     */
    public void executeVoid() {
        RestClient.RequestBodySpec spec = buildRequest();

        if (body != null) {
            spec.body(body);
        }

        spec.retrieve()
            .onStatus(
                status -> status.isError(),
                (request, response) -> {
                    throw new DockingRestClientException(
                        "Request failed: " + response.getStatusText());
                }
            )
            .toBodilessEntity();
    }

    /**
     * 构建请求
     */
    private RestClient.RequestBodySpec buildRequest() {
        RestClient.UriSpec<RestClient.RequestBodySpec> uriSpec = switch (method) {
            case GET -> restClient.get();
            case POST -> restClient.post();
            case PUT -> restClient.put();
            case DELETE -> restClient.delete();
            case PATCH -> restClient.patch();
            case HEAD -> restClient.head();
            case OPTIONS -> restClient.options();
            default -> throw new IllegalArgumentException("Unsupported method: " + method);
        };

        // 构建 URI
        RestClient.RequestBodySpec spec;
        if (queryParams.isEmpty()) {
            spec = uriSpec.uri(path);
        } else {
            spec = uriSpec.uri(uriBuilder -> {
                uriBuilder.path(path);
                queryParams.forEach((k, v) -> uriBuilder.queryParam(k, v));
                return uriBuilder.build();
            });
        }

        // 添加请求�?
        headers.forEach(spec::header);
        spec.header("Content-Type", contentType.toString());

        return spec;
    }
}
