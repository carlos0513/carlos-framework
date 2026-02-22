package com.carlos.system.resource.pojo.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * <p>
 * 菜单树形数据
 * </p>
 *
 * @author carlos
 * @date 2021-12-28 15:26:57
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class SysResourceTreeDTO {

    /**
     * 主键
     */
    private String id;
    /**
     * 前端名称
     */
    private String name;
    /**
     * 接口路径
     */
    private String path;

    /**
     * 菜单资源
     */
    List<SysResourceDTO> resources;

    /**
     * 子菜单
     */
    List<SysResourceTreeDTO> children;


}
