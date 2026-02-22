package com.carlos.org.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 菜单树形数据
 *
 * @author carlos
 * @date 2021-12-28 15:26:57
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class UserMenuDTO {
    private String id;

    private String path;

    private String name;

    private String component;

    private Boolean hidden;

    private Boolean state;

    private String icon;

    private String url;

    private Integer sort;

    private Map<String, Object> meta;

    List<UserMenuDTO> children;

    List<SysApiDTO> apis;

    /**
     * 该字段为前端需要
     */
    private Set<String> roles;
}
