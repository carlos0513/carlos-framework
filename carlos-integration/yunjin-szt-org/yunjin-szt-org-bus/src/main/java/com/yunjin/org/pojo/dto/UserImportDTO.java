package com.yunjin.org.pojo.dto;


import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 用户导入 数据传输对象，service和manager向外传输对象
 * </p>
 *
 * @author Carlos
 * @date 2023-5-27 12:52:09
 */
@Data
@Accessors(chain = true)
public class UserImportDTO {

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
     * 角色名称
     */
    private String role;
    /**
     * 部门完整信息，以”-“分割部门级别
     */
    private String department;
    /**
     * 行政区域编码
     */
    private String regionCode;
    /**
     * 性别，0：保密, 1：男，2：女，默认0
     */
    private String gender;
    /**
     * 邮箱
     */
    private String email;
}
