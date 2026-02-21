package com.yunjin.org.pojo.entity;


import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.yunjin.org.pojo.enums.UserGenderEnum;
import com.yunjin.org.pojo.enums.UserStateEnum;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 系统用户 数据源对象
 * </p>
 *
 * @author yunjin
 * @date 2022-11-11 18:19:17
 */
@Data
@Accessors(chain = true)
@TableName("org_user")
@JsonInclude(JsonInclude.Include.ALWAYS)
public class User extends Model<User> implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_UUID, value = "id")
    private String id;
    /**
     * 用户名
     */
    @TableField(value = "account")
    private String account;
    /**
     * 真实姓名
     */
    @TableField(value = "realname")
    private String realname;
    /**
     * 密码
     */
    @TableField(value = "pwd")
    private String pwd;
    /**
     * 盐值
     */
    @TableField(value = "salt")
    private String salt;
    /**
     * 证件号码
     */
    @TableField(value = "identify")
    private String identify;
    /**
     * 手机号码
     */
    @TableField(value = "phone")
    private String phone;
    /**
     * 行政区域编码
     */
    @TableField(value = "region_code")
    private String regionCode;
    /**
     * 详细地址
     */
    @TableField(value = "address")
    private String address;
    /**
     * 性别，0：保密, 1：男，2：女，默认0
     */
    @TableField(value = "gender")
    private UserGenderEnum gender;
    /**
     * 邮箱
     */
    @TableField(value = "email")
    private String email;
    /**
     * 头像
     */
    @TableField(value = "head")
    private String head;
    /**
     * 电子签名
     */
    @TableField(value = "signature")
    private String signature;
    /**
     * 备注
     */
    @TableField(value = "description")
    private String description;
    /**
     * 状态，0：禁用，1：启用，2：锁定
     */
    @TableField(value = "state")
    private UserStateEnum state;
    /**
     * 是否是机构管理员
     */
    @TableField(value = "is_admin")
    private Boolean isAdmin;
    /**
     * 最后登录时间
     */
    @TableField(value = "last_login")
    private LocalDateTime lastLogin;
    /**
     * 登录次数
     */
    @TableField(value = "login_count")
    private Long loginCount;
    /**
     * 版本
     */
    @Version
    @TableField(value = "version")
    private Long version;
    /**
     * 逻辑删除，0：未删除，1：已删除
     */
    @TableLogic
    @TableField(value = "is_deleted")
    private Boolean deleted;
    /**
     * 创建者
     */
    @TableField(value = "create_by", fill = FieldFill.INSERT)
    private String createBy;
    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    /**
     * 修改者
     */
    @TableField(value = "update_by", fill = FieldFill.INSERT_UPDATE)
    private String updateBy;
    /**
     * 修改时间
     */
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    /**
     * 租户id
     */
    @TableField(value = "tenant_id")
    private String tenantId;
    /**
     * 排序
     */
    @TableField(value = "sort")
    private Integer sort;

    @TableField(value = "is_push")
    private Integer isPush;
    /**
     * 角色名称
     */
    @TableField(exist = false)
    private String roleNames;
    /**
     * 密码创建时间
     */
    @TableField(value = "password_create_time", fill = FieldFill.INSERT)
    private LocalDateTime passwordCreateTime;
}
