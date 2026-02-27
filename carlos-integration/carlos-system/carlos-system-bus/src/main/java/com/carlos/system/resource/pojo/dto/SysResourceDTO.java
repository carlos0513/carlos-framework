package com.carlos.system.resource.pojo.dto;


import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * <p>
 * 系统资源 数据传输对象，service和manager向外传输对象
 * </p>
 *
 * @author carlos
 * @date 2021-12-28 15:26:57
 */
@Data
@Accessors(chain = true)
public class SysResourceDTO {

    /**
     * 主键
     */
    private Long id;
    /**
     * 分类ID
     */
    private Long categoryId;
    /**
     * 资源名称
     */
    private String name;
    /**
     * 接口路径
     */
    private String path;
    /**
     * 接口路径前缀
     */
    private String pathPrefix;
    /**
     * 请求方式
     */
    private String method;
    /**
     * 图标
     */
    private String icon;
    /**
     * 资源类型，按钮
     */
    private String type;
    /**
     * 状态，0：禁用，1：启用
     */
    private String state;
    /**
     * 显示和隐藏，0：显示，1：隐藏
     */
    private Boolean hidden;
    /**
     * 是否是白名单地址
     */
    private Boolean whitelist;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SysResourceDTO that = (SysResourceDTO) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
