package com.carlos.integration.module.dingtalk.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * <p>
 * 钉钉 Code 换取用户信息请求
 * </p>
 *
 * @author Carlos
 * @since 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class DingtalkCodeRequest extends DingtalkBaseRequest {

    private static final long serialVersionUID = 1L;

    /**
     * 临时授权�?
     */
    private String code;
}

