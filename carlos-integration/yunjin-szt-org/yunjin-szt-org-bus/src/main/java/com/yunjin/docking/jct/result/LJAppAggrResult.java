package com.yunjin.docking.jct.result;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 统一返回结果
 * </p>
 *
 * @author Carlos
 * @date 2022/3/8 12:18
 */
@Data
@Accessors(chain = true)
public class LJAppAggrResult<T> implements Serializable {

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
     * 错误信息
     */
    @JsonProperty("data")
    private T data;
}
