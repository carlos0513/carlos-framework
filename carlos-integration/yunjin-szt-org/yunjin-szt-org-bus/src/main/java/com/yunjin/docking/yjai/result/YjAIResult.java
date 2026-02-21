package com.yunjin.docking.yjai.result;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 元景大模型统一返回结果
 * </p>
 *
 * @author Carlos
 * @date 2022/3/8 12:18
 */
@Data
@Accessors(chain = true)
public class YjAIResult<T> implements Serializable {

    /**
     * 错误码
     */
    @JsonProperty("code")
    private String code;
    /**
     * 错误信息
     */
    @JsonProperty("msg")
    private String msg;
    /**
     * 版本信息
     */
    @JsonProperty("version")
    private String version;
    /**
     * 错误信息
     */
    @JsonProperty("response")
    private T response;
}
