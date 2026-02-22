package com.carlos.gateway.config;

import com.carlos.core.exception.GlobalException;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes;
import org.springframework.web.reactive.function.server.ServerRequest;

import java.util.Map;

/**
 * <p>
 * 自定义error返回的信息
 * </p>
 *
 * @author carlos
 * @date 2021/3/3 23:56
 */

public class GatewayErrorAttributes extends DefaultErrorAttributes {

    @Override
    public Map<String, Object> getErrorAttributes(ServerRequest request, ErrorAttributeOptions options) {
        Map<String, Object> errorAttributes = super.getErrorAttributes(request, options);
        Throwable error = this.getError(request);
        if (error instanceof GlobalException) {
            errorAttributes.put("code", ((GlobalException) error).getErrorCode());
        }
        return errorAttributes;
    }
}
