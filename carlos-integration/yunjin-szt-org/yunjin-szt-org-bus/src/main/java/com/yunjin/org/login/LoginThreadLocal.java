package com.yunjin.org.login;

import cn.hutool.core.util.StrUtil;
import lombok.NoArgsConstructor;


/**
 * <p>
 *   用户登录本地线程工具
 * </p>
 *
 * @author Carlos
 * @date 2025-02-27 11:17
 */
@NoArgsConstructor
public class LoginThreadLocal {

    private static final ThreadLocal<String> TARGET_ROUTE = new ThreadLocal<>();


    public static String getRoute() {
        String route = TARGET_ROUTE.get();
        clearRoute();
        return route;
    }


    public static void clearRoute() {
        TARGET_ROUTE.remove();
    }


    public static void setRoute(String route) {
        if (StrUtil.isNotBlank(route)) {
            TARGET_ROUTE.set(route);
        }
    }
}
