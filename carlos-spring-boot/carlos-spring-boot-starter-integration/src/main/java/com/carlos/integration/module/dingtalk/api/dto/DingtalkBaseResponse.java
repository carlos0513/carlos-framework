package com.carlos.integration.module.dingtalk.api.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 钉钉基础响应
 * </p>
 *
 * @author Carlos
 * @since 1.0.0
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public abstract class DingtalkBaseResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 返回�?
     */
    private Integer errcode;

    /**
     * 返回信息
     */
    private String errmsg;

    /**
     * 请求ID
     */
    private String requestId;

    /**
     * 是否成功
     */
    @JsonIgnore
    public boolean isSuccess() {
        return errcode != null && errcode == 0;
    }
}

