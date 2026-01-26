package com.yunjin.docking.rzt.result;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
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
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class AccessTokenResult extends RztResult {


    /**
     * accesstoken
     */
    @JsonProperty("access_token")
    private String accessToken;
    /**
     * 过期时间（秒）
     */
    @JsonProperty("expires_in")
    private Integer expiresIn;
}
