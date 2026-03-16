package com.carlos.sample.json;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * <p>
 * JSON 序列化示例应用启动类
 * </p>
 *
 * <p>
 * 本示例演示 carlos-spring-boot-starter-json 的使用，包括：
 * - Jackson JSON 序列化/反序列化
 * - Long 类型精度保持（自动转为 String）
 * - 日期时间格式化
 * - 自定义注解序列化
 * </p>
 *
 * @author carlos
 * @date 2026/3/15
 */
@Slf4j
@SpringBootApplication
public class JsonSampleApplication {

    public static void main(String[] args) {
        SpringApplication.run(JsonSampleApplication.class, args);
        log.info("=== JSON 示例应用启动成功 ===");
        log.info("访问地址: http://localhost:9813");
    }
}
