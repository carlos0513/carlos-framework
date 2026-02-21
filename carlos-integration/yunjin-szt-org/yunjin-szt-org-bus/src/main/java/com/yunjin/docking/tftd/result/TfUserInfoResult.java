package com.yunjin.docking.tftd.result;


import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * <p>
 * 用户列表
 * </p>
 *
 * @author Carlos
 * @date 2022/3/8 12:18
 */
@NoArgsConstructor
@Data
@Accessors(chain = true)
public class TfUserInfoResult {

    /**
     * avatar
     */
    @JsonProperty("avatar")
    private String avatar;
    /**
     * createTime
     */
    @JsonProperty("createTime")
    private String createTime;
    /**
     * depts
     */
    @JsonProperty("depts")
    private List<TfDeptInfoResult> depts;
    /**
     * email
     */
    @JsonProperty("email")
    private String email;
    /**
     * idCard
     */
    @JsonProperty("idCard")
    private String idCard;
    /**
     * isEffective
     */
    @JsonProperty("isEffective")
    private String isEffective;
    /**
     * lockFlag
     */
    @JsonProperty("lockFlag")
    private String lockFlag;
    /**
     * name
     */
    @JsonProperty("name")
    private String name;
    /**
     * phone
     */
    @JsonProperty("phone")
    private String phone;
    /**
     * sex
     */
    @JsonProperty("sex")
    private String sex;
    /**
     * tenantId
     */
    @JsonProperty("tenantId")
    private String tenantId;
    /**
     * updateTime
     */
    @JsonProperty("updateTime")
    private String updateTime;
    /**
     * userId
     */
    @JsonProperty("userId")
    private String userId;
    /**
     * username
     */
    @JsonProperty("username")
    private String username;

}
