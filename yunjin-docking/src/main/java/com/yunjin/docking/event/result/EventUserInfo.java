package com.yunjin.docking.event.result;


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
public class EventUserInfo {

    /**
     * 主键
     */
    private String id;
    /**
     * 用户名
     */
    private String account;
    /**
     * 真实姓名
     */
    private String realname;
    /**
     * 证件号码
     */
    private String identify;
    /**
     * 手机号码
     */
    private String phone;
    /**
     * 详细地址
     */
    private String address;
    //@Schema(description = "钉钉")
    // private String dingding;
    //@Schema(description = "政治面貌")
    // private String politicalOutlook;
    //@Schema(description = "学历")
    // private String educationBackground;
    //@Schema(description = "最后登录时间")
    // private LocalDateTime lastLogin;
    //@Schema(description = "登录次数")
    // private Integer loginCount;
    //@Schema(description = "创建者")
    // private String createBy;
    //@Schema(description = "创建时间")
    // private LocalDateTime createTime;
    //@Schema(description = "修改者")
    // private String updateBy;
    //@Schema(description = "修改时间")
    // private LocalDateTime updateTime;
}
