package com.carlos.docking.rzt.user;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 *   用户信息
 * </p>
 *
 * @author Carlos
 * @date 2024-12-13 15:48
 */
@Data
@Accessors(chain = true)
public class SysUserInfo {
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
     * 邮箱
     */
    private String email;


}
