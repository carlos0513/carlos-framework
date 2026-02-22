package com.carlos.org.pojo.param;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.Set;

/**
 * 登录参数
 *
 * @author yunjin
 * @date 2019-05-15
 **/
@Data
@Schema(description = "登录参数")
public class ApiUserDeptRoleParam {

    /**
     * 部门编码
     */
    private Set<String> deptCodes;

    /**
     * 角色id
     */
    private Set<Serializable> roleIds;

    /**
     * 角色id
     */
    private Set<Serializable> roleNames;


}
