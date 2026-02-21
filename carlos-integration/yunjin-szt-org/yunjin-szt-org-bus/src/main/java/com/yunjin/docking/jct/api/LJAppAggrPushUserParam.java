package com.yunjin.docking.jct.api;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 *   后续userName，nickName，phoneNumber会用rsa加密，解密秘钥由黄建华（产互）线下提供
 * </p>
 *
 * @author Carlos
 * @date 2025-02-27 08:52
 */
@NoArgsConstructor
@Data
@Accessors(chain = true)
public class LJAppAggrPushUserParam implements Serializable {
    /** 用户登录账号 */
    @JsonProperty("userName")
    private String userName;
    /** 用户名 */
    @JsonProperty("nickName")
    private String nickName;
    /** 11位手机号 */
    @JsonProperty("phoneNumber")
    private String phoneNumber;
    /** 用户标识 */
    @JsonProperty("openId")
    private String openId;
    /** 1:新增2:信息更新3:暂停4:暂停状态恢复5:注销 */
    @JsonProperty("status")
    private Integer status;
    /** 省份名称 */
    @JsonProperty("provinceName")
    private String provinceName;
    /** 地市名称 */
    @JsonProperty("cityName")
    private String cityName;
    /** MD5加密数据完整性校验 */
    @JsonProperty("sign")
    private String sign;
}
