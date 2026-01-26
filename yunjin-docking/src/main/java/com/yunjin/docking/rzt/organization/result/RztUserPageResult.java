package com.yunjin.docking.rzt.organization.result;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * <p>
 * 翻译返回结果
 * </p>
 *
 * @author Carlos
 * @date 2022/3/8 12:18
 */
@Data
@Accessors(chain = true)
public class RztUserPageResult<T> extends RztPageResult<T> {

    /**
     * 请求唯一标识，用于定位异常信息
     */
    @JsonProperty("regId")
    private String regId;
    /**
     * 请求的状态
     */
    @JsonProperty("httpStatus")
    private String httpStatus;
    /**
     * 错误字段
     */
    @JsonProperty("fieldErrors")
    private List<Object> fieldErrors;
    /**
     * 返回信息
     */
    @JsonProperty("message")
    private String message;
    /**
     * 错误提示
     */
    @JsonProperty("fillParameter")
    private List<Object> fillParameter;
}
