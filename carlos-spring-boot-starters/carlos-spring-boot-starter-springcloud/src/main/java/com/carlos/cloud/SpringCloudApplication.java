package com.carlos.cloud;


import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

import java.lang.annotation.*;

/**
 * <p>
 * springcloud注解
 * </p>
 *
 * @author carlos
 * @date 2022/1/17 21:49
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@EnableDiscoveryClient
@EnableFeignClients("com.carlos")
@SpringBootApplication
public @interface SpringCloudApplication {

}
