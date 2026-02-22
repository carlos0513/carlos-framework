package com.carlos.system.menu.pojo.dto;


import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * <p>
 * 菜单操作 数据传输对象，service和manager向外传输对象
 * </p>
 *
 * @author carlos
 * @date 2023-7-7 14:19:55
 */
@Data
@Accessors(chain = true)
public class MenuOperateDTO {
    /**
     * 主键
     */
    private String id;
    /**
     * 资源名称
     */
    private String operateName;
    /**
     * 资源编码
     */
    private String operateCode;
    /**
     * 接口路径
     */
    private String path;
    /**
     * 菜单id
     */
    private String menuId;
    /**
     * 请求方式
     */
    private String operateMethod;
    /**
     * 图标
     */
    private String icon;
    /**
     * 资源类型，按钮
     */
    private String operateType;
    /**
     * 状态，0：禁用，1：启用
     */
    private String state;
    /**
     * 显示和隐藏，0：显示，1：隐藏
     */
    private Boolean hidden;
    /**
     * 资源描述
     */
    private String description;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    /**
     * 修改时间
     */
    private LocalDateTime updateTime;
}
