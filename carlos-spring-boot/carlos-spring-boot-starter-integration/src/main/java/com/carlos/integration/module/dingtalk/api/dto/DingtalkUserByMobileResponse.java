package com.carlos.integration.module.dingtalk.api.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 根据手机号查询用户响�?
 * </p>
 *
 * @author Carlos
 * @since 1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class DingtalkUserByMobileResponse extends DingtalkBaseResponse {

    private static final long serialVersionUID = 1L;

    /**
     * 用户信息
     */
    private UserGetByMobileResponse result;

    /**
     * <p>
     * 手机号查询用户结�?
     * </p>
     */
    @Data
    public static class UserGetByMobileResponse {

        /**
         * 用户ID
         */
        private String userid;

        /**
         * 用户unionId
         */
        private String unionid;
    }
}

