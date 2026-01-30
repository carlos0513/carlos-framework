package com.carlos.boot.annotation;

import java.lang.annotation.*;

/**
 * <p>
 * 标识客户端api  如feign接口
 * </p>
 *
 * @author carlos
 * @date 2021/12/6 15:33
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ClientApi {

}
