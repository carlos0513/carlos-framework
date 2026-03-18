package com.carlos.sample.docking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * <p>
 * Carlos Framework 第三方平台对接示例应用启动类
 * </p>
 *
 * <p>
 * 本示例演示如何使�?carlos-spring-boot-starter-integration 模块进行第三方平台对接，包括�?
 * <ul>
 *     <li>钉钉消息推�?/li>
 *     <li>钉钉组织架构同步</li>
 *     <li>融政通消息推�?/li>
 * </ul>
 * </p>
 *
 * @author Carlos
 * @version 3.0.0
 * @since 2025-03-15
 */
@SpringBootApplication
public class DockingSampleApplication {

    public static void main(String[] args) {
        SpringApplication.run(DockingSampleApplication.class, args);
        System.out.println("========================================");
        System.out.println("  Carlos Sample Docking 启动成功!");
        System.out.println("  接口文档: http://localhost:9813/doc.html");
        System.out.println("========================================");
    }
}
