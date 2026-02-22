package com.carlos.org.pojo.ao;

import com.carlos.org.pojo.enums.UserGenderEnum;
import com.carlos.org.pojo.enums.UserStateEnum;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Set;

/**
 * 系统用户 数据传输对象，service和manager向外传输对象
 *
 * @author carlos
 * @date 2022-11-11 18:19:17
 */
@Data
@Accessors(chain = true)
public class UserDetailAO {

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
     * 行政区域编码
     */
    private String regionCode;
    /**
     * 详细地址
     */
    private String address;
    /**
     * 性别，0：保密, 1：男，2：女，默认0
     */
    private UserGenderEnum gender;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 头像
     */
    private String head;
    /**
     * 电子签名
     */
    private String signature;
    /**
     * 备注
     */
    private String description;
    /**
     * 状态，0：禁用，1：启用，2：锁定
     */
    private UserStateEnum state;

    /**
     * 所属部门信息
     */
    private Set<String> departmentIds;


}
