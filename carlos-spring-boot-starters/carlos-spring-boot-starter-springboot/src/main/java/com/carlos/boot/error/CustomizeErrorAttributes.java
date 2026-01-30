package com.carlos.boot.error;

import com.carlos.core.exception.GlobalException;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.WebRequest;

import java.util.Map;

/**
 * <p>
 * 自定义error返回的信息
 * </p>
 *
 * @author carlos
 * @date 2021/3/3 23:56
 */
@Component
public class CustomizeErrorAttributes extends DefaultErrorAttributes {

    @Override
    public Map<String, Object> getErrorAttributes(final WebRequest webRequest, final ErrorAttributeOptions options) {
        final Map<String, Object> errorAttributes = super.getErrorAttributes(webRequest, options);
        final Throwable error = this.getError(webRequest);
        if (error instanceof GlobalException) {
            errorAttributes.put("code", ((GlobalException) error).getErrorCode());
        }
        return errorAttributes;
    }
}
