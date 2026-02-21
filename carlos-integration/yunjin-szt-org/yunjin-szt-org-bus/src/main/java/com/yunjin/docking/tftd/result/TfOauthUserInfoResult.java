package com.yunjin.docking.tftd.result;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * <p>
 * 翻译返回结果
 * </p>
 *
 * @author Carlos
 * @date 2022/3/8 12:18
 */
@NoArgsConstructor
@Data
@Accessors(chain = true)
public class TfOauthUserInfoResult {


    @JsonProperty("sysUser")
    private SysUser sysUser;


    @Data
    @Accessors(chain = true)
    public class SysUser {


        /**
         * 成员UserID。对应管理端的帐号
         */
        @JsonProperty("userId")
        private String userId;
        /**
         * 员名称
         */
        @JsonProperty("phone")
        private String phone;
        @JsonProperty("name")
        private String name;
        @JsonProperty("username")
        private String username;
        @JsonProperty("idCard")
        private String idCard;
        @JsonProperty("sex")
        private String sex;


    }


}
