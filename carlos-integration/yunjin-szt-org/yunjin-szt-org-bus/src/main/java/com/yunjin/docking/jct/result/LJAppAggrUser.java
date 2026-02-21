package com.yunjin.docking.jct.result;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * <p>
 *   法人信息
 * </p>
 *
 * @author Carlos
 * @date 2025-01-15 08:58
 */
@NoArgsConstructor
@Data
@Accessors(chain = true)
public class LJAppAggrUser {


    @JsonProperty("userName")
    private String userName;
    @JsonProperty("nickName")
    private String nickName;
    @JsonProperty("phoneNumber")
    private String phoneNumber;
    @JsonProperty("openId")
    private String openId;
    @JsonProperty("deptId")
    private Integer deptId;
    @JsonProperty("zoneId")
    private Integer zoneId;
}
