package com.yunjin.org.pojo.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 用户部门 数据源对象
 * </p>
 *
 * @author yunjin
 * @date 2022-11-11 19:21:46
 */
@Data
@Accessors(chain = true)
@TableName("org_user_department")
@JsonInclude(JsonInclude.Include.ALWAYS)
public class UserDepartment extends Model<UserDepartment> implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_UUID, value = "id")
    private String id;
    /**
     * 用户id
     */
    @TableField(value = "user_id")
    private String userId;
    /**
     * 部门id
     */
    @TableField(value = "department_id")
    private String departmentId;
    /**
     * 是否是管理员
     */
    @TableField(value = "is_admin")
    private Boolean isAdmin;

    @TableField(value = "role_id")
    private String roleId;

    @TableField(value = "department_level_code")
    private String departmentLevelCode;

}
