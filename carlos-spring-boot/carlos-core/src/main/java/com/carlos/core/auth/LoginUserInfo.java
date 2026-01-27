package com.carlos.core.auth;


import java.io.Serializable;
import java.util.Set;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * 系统用户
 * </p>
 *
 * @author yunjin
 * @date 2021-2-20 14:54:57
 */
@Data
@Accessors(chain = true)
public class LoginUserInfo implements Serializable {

    /**
     * 用户id
     */
    private Serializable id;
    /**
     * 客户端id
     */
    private Serializable clientId;
    /**
     * 角色id
     */
    private Set<Serializable> roleIds;
    /**
     * 部门id
     */
    private Serializable departmentId;
    /**
     * 用户名
     */
    private String account;
    /**
     * 密码
     */
    private String password;

    /**
     * 是否可用
     */
    private Boolean enable = true;

}
