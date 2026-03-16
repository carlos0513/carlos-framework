package com.carlos.gateway.auth;

import lombok.Data;

import java.io.Serializable;
import java.util.List;


/**
 * <p>
 *   菜单和接口的映射关系
 * </p>
 *
 * @author Carlos
 * @date 2025-01-13 10:34
 */
@Data
public class MenuApiMapping {

    /** 菜单id */
    private Serializable menuId;
    /** 菜单路径 */
    private String menuPath;
    /** 接口路径 */
    private List<com.carlos.gateway.auth.ApiInfo> apis;
}
