package com.carlos.org.pojo.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 角色菜单操作表 数据源对象
 * </p>
 *
 * @author yunjin
 * @date 2023-7-7 14:19:55
 */
@Data
@Accessors(chain = true)
@TableName("org_role_operate")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RoleOperate implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_UUID, value = "id")
    private String id;
    /**
     * 角色id
     */
    @TableField(value = "role_id")
    private String roleId;
    /**
     * 菜单操作id
     */
    @TableField(value = "operate_id")
    private String operateId;

}
