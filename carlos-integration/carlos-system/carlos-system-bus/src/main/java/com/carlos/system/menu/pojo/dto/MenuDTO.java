package com.carlos.system.menu.pojo.dto;

import com.carlos.system.enums.MenuType;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * 系统菜单 数据传输对象，service和manager向外传输对象
 *
 * @author carlos
 * @date 2021-12-28 15:26:57
 */
@Data
@Accessors(chain = true)
public class MenuDTO {

    /**
     * 主键
     */
    private Long id;
    /**
     * 父级ID
     */
    private Long parentId;
    /**
     * controller名称
     */
    private String title;
    /**
     * 前端路由
     */
    private String path;
    /**
     * 前端名称
     */
    private String name;
    /**
     * 前端图标
     */
    private String icon;
    /**
     * 菜单配置
     */
    private String meta;
    /**
     * 目标组件
     */
    private String component;
    /**
     * 状态
     */
    private Boolean state;
    /**
     * 请求路径
     */
    private String url;
    /**
     * 菜单级数
     */
    private Integer level;
    /**
     * 菜单排序
     */
    private Integer sort;
    /**
     * 显示和隐藏，0：显示，1：隐藏
     */
    private Boolean hidden;
    /**
     * 备注
     */
    private String remark;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    /**
     * 修改时间
     */
    private LocalDateTime updateTime;
    /**
     * 子菜单
     */
    List<MenuDTO> children;
    /**
     * 菜单类型，PC：pc端菜单，MOBILE：移动端菜单
     */
    private MenuType menuType;

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final MenuDTO menuDTO = (MenuDTO) o;
        return this.id.equals(menuDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }
}
