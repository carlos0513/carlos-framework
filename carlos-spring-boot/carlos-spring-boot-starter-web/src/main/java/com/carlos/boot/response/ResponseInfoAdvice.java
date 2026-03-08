package com.carlos.boot.response;

import cn.hutool.json.JSONUtil;
import com.carlos.boot.annotation.ClientApi;
import com.carlos.boot.interceptors.ApplicationInterceptorProperties;
import com.carlos.boot.interceptors.GlobalInterceptorProperties;
import com.carlos.boot.request.RequestUtil;
import com.carlos.core.response.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * <p>
 * 统一响应处理 ResponseBodyAdvice接口是在Controller执行return之后， 在response返回给浏览器或者APP客户端之前，执行的对response的一些处理。可以实现对response数据的一些统一封装或者加密等操作。 对请求进行统一封装
 * </p>
 *
 * @author carlos
 * @date 2020/6/3 17:11
 */

@ControllerAdvice
@RequiredArgsConstructor
public class ResponseInfoAdvice implements ResponseBodyAdvice<Object> {

    private final ApplicationInterceptorProperties interceptorProperties;

    @Override
    public boolean supports(@Nullable final MethodParameter returnType, @Nullable final Class converterType) {
        if (returnType == null) {
            return false;
        }
        // TODO: carlos 2022/10/21 此处配置不需要进行统一响应处理的路径
        final Class<?> declaringClass = returnType.getDeclaringClass();
        // 过滤掉swagger请求
        if (declaringClass.getName().contains("springdoc")) {
            return false;
        }
        // 判断是否要执行beforeBodyWrite方法，true为执行，false不执行
        final RestController annotation = declaringClass.getAnnotation(RestController.class);
        final ClientApi apiAnnotation = declaringClass.getAnnotation(ClientApi.class);
        return (apiAnnotation == null && annotation != null);
    }

    @Override
    public Object beforeBodyWrite(Object body, @Nullable final MethodParameter returnType, @Nullable final MediaType selectedContentType,
                                  @Nullable final Class selectedConverterType, @Nullable final ServerHttpRequest request,
                                  @Nullable final ServerHttpResponse response) {
        // JakartaServletUtil.setHeader(response, HttpHeaders.X_CONTENT_TYPE_OPTIONS, "nosniff");
        if (!(body instanceof Result)) {
            if (body instanceof String) {
                body = JSONUtil.toJsonStr(Result.ok(body));
            } else {
                body = Result.ok(body);
            }
        }
        final GlobalInterceptorProperties.PrintType printType = interceptorProperties.getGlobalInterceptor().getPrintType();
        if (printType == GlobalInterceptorProperties.PrintType.RESPONSE
            || printType == GlobalInterceptorProperties.PrintType.BOTH_ORDER
            || printType == GlobalInterceptorProperties.PrintType.BOTH_TOGETHER) {
            if (printType == GlobalInterceptorProperties.PrintType.BOTH_TOGETHER) {
                RequestUtil.printRequestInfo();
            }

            // 打印响应信息
            assert body instanceof Result<?>;
            RequestUtil.printResponseInfo((Result<?>) body);
        }
        return body;
    }
}
