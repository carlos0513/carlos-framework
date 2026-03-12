package com.carlos.message.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * <p>
 * 消息中心服务端启动类
 * </p>
 *
 * @author Carlos
 * @date 2026/3/11
 */
@EnableFeignClients(basePackages = "com.carlos.message.api")
@SpringBootApplication(scanBasePackages = "com.carlos.message")
public class MessageServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(MessageServerApplication.class, args);
    }
}
