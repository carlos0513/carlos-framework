package com.carlos.org.pojo.entity;


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
 * 用户角色 数据源对象
 * </p>
 *
 * @author carlos
 * @date 2022-11-11 19:21:46
 */
@Data
@Accessors(chain = true)
@TableName("org_user_role")
@JsonInclude(JsonInclude.Include.ALWAYS)
public class UserRole extends Model<UserRole> implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID, value = "id")
    private Long id;
    /**
     * 用户id
     */
    @TableField(value = "user_id")
    private String userId;
    /**
     * 角色id
     */
    @TableField(value = "role_id")
    private String roleId;
    /**
     * 租户id
     */
    @TableField(value = "tenant_id")
    private String tenantId;

}
