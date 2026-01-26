package com.yunjin.boot.error;

import com.yunjin.core.exception.GlobalException;
import java.util.Map;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.WebRequest;

/**
 * <p>
 * 自定义error返回的信息
 * </p>
 *
 * @author yunjin
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
