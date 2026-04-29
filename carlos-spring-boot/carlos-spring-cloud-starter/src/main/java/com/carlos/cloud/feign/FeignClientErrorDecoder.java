package com.carlos.cloud.feign;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.carlos.core.response.CommonErrorCode;
import feign.FeignException;
import feign.Response;
import feign.RetryableException;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

/**
 * <p>
 * openfeign 客户端调用自定义错误解码器
 * </p>
 *
 * @author carlos
 * @date 2022/1/17 10:30
 */
@Slf4j
public class FeignClientErrorDecoder extends ErrorDecoder.Default {

    @Override
    public Exception decode(final String methodKey, final Response response) {
        final Exception exception = super.decode(methodKey, response);
        // 如果是请求重试的异常，返回继续重试
        if (exception instanceof RetryableException) {
            return exception;
        }

        // 如果是FeignException，则对其进行处理，并抛出BusinessException
        if (!(exception instanceof FeignException)) {
            return exception;
        }
        final String s;
        try {
            final ByteBuffer responseBody = ((FeignException) exception).responseBody().get();
            s = StandardCharsets.UTF_8.newDecoder().decode(responseBody.asReadOnlyBuffer()).toString();
            // s = Util.toString(response.body().asReader());
        } catch (final IOException e) {
            log.error("异常内容读取失败:{}", e.getMessage(), e);
            return new OpenFeignException();
        }
        // 将异常信息，转换为ExceptionInfo对象
        final ExceptionInfo exceptionInfo;
        try {
            exceptionInfo = JSONUtil.toBean(s, ExceptionInfo.class);
            log.debug("Get exception:{}", exceptionInfo);
        } catch (final Exception e) {
            log.error("Can't convert  string [{}] to 'ExceptionInfo'", StrUtil.subWithLength(s, 0, 100));
            return exception;
        }
        return Optional.ofNullable(exceptionInfo.getException()).map(name -> {
            try {
                final Class<?> clazz = Class.forName(exceptionInfo.getException());
                return (Exception) clazz.getDeclaredConstructor(String.class).newInstance(exceptionInfo.getMessage());
            } catch (final Exception e) {
                return new OpenFeignException();
            }
        }).orElseGet(() -> {
            // 如果excepiton中code不为空，则使用该code，否则使用默认的错误code
            final String code = Optional.ofNullable(exceptionInfo.getCode()).orElse(CommonErrorCode.INTERNAL_ERROR.getCode());
            // 如果excepiton中message不为空，则使用该message，否则使用默认的错误message
            final String message = Optional.ofNullable(exceptionInfo.getMessage()).orElse("openfeign调用异常");
            return new OpenFeignException(code, message);
        });
    }


}
