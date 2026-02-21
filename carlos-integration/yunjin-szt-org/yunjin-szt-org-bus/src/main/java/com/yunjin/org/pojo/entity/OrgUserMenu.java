package com.yunjin.org.pojo.entity;


import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.yunjin.org.pojo.emuns.UserMenuCollectionTypeEnum;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.lang.Boolean;
import java.lang.String;

/**
 * <p>
 * 用户菜单收藏表 数据源对象
 * </p>
 *
 * @author yunjin
 * @date 2024-2-28 11:10:01
 */
@Data
@Accessors(chain = true)
@TableName("org_user_menu")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrgUserMenu implements Serializable {
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
     * 菜单id
     */
    @TableField(value = "menu_id")
    private String menuId;
    @TableField(value = "status")
    /** 收藏状态*/
    private UserMenuCollectionTypeEnum status;

}
