package com.carlos.gateway.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 *   用户菜单信息
 * </p>
 *
 * @author Carlos
 * @date 2025-01-13 11:52
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class UserMenu {
    private String id;

    private String path;

    private String name;

    private String component;

    private Boolean hidden;

    private Boolean state;

    private String icon;

    private Integer sort;

    List<Api> apis;


    @Data
    @Accessors(chain = true)
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Api {
        private Serializable id;

        private String url;

        private String name;
    }
}
