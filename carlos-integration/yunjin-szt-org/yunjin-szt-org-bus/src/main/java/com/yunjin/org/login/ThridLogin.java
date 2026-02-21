package com.yunjin.org.login;

/**
 * @Description: 第三方登录
 * @Date: 2023/7/12 16:08
 */
public interface ThridLogin<T> {
    String login(T param);
}
