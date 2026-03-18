package com.carlos.test.pojo.entity;


import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 系统用户 数据源对�?
 * </p>
 *
 * @author Carlos
 * @date 2023-8-12 11:16:18
 */
@Data
@Accessors(chain = true)
@TableName("org_user")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrgUser implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID, value = "id")
    private Long id;
    /**
     * 用户�?
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
     * 详细地址
     */
    @TableField(value = "address")
    private String address;
    /**
     * 性别�?：保�? 1：男�?：女，默�?
     */
    @TableField(value = "gender")
    private Integer gender;
    /**
     * 头像文件的id
     */
    @TableField(value = "head")
    private String head;
    /**
     * 排序
     */
    @TableField(value = "sort")
    private Integer sort;
    /**
     * 备注
     */
    @TableField(value = "description")
    private String description;
    /**
     * 钉钉
     */
    @TableField(value = "dingding")
    private String dingding;
    /**
     * 政治面貌
     */
    @TableField(value = "political_outlook")
    private String politicalOutlook;
    /**
     * 学历
     */
    @TableField(value = "education_background")
    private String educationBackground;
    /**
     * 最后登录时�?
     */
    @TableField(value = "last_login")
    private LocalDateTime lastLogin;
    /**
     * 登录次数
     */
    @TableField(value = "login_count")
    private Integer loginCount;
    /**
     * 逻辑删除�?：未删除�?：已删除
     */
    @TableLogic
    @TableField(value = "is_deleted")
    private Boolean deleted;
    /**
     * 创建�?
     */
    @TableField(value = "create_by", fill = FieldFill.INSERT)
    private Long createBy;
    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    /**
     * 修改�?
     */
    @TableField(value = "update_by", fill = FieldFill.INSERT_UPDATE)
    private Long updateBy;
    /**
     * 修改时间
     */
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

}
