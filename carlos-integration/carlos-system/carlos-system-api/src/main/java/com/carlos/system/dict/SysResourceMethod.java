package com.carlos.system.dict;

import com.carlos.core.dict.Dict;
import com.carlos.core.dict.DictEnum;
import com.carlos.core.exception.ServiceException;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 资源请i去方式
 *
 * @author carlos
 * @date 2020/4/11 23:26
 */
@Getter
@AllArgsConstructor
@Dict(code = "sys_resource_method", name = "系统资源请求方式")
public enum SysResourceMethod implements DictEnum {

    /**
     * 资源请求方式
     */
    GET("resource_method_get", "GET"),
    POST("resource_method_post", "POST"),
    PUT("resource_method_put", "PUT"),
    TRACE("resource_method_trace", "TRACE"),
    DELETE("resource_method_delete", "DELETE"),
    ;

    private final String code;

    private final String name;

    public static DictEnum nameOf(final String name) {
        final SysResourceMethod[] values = SysResourceMethod.values();
        for (final SysResourceMethod value : values) {
            if (value.name().equals(name)) {
                return value;
            }
        }
        return null;
    }

    public static DictEnum ofCode(final String code) {
        final SysResourceMethod[] values = SysResourceMethod.values();
        for (final SysResourceMethod value : values) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        throw new ServiceException("Can't find enum of code " + code);
    }
}
