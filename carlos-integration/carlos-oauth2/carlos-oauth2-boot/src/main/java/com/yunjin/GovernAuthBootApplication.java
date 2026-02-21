package com.carlos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * 启动程序
 *
 * @author yunjin
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class GovernAuthBootApplication {

    public static void main(final String[] args) {
        SpringApplication.run(GovernAuthBootApplication.class, args);
    }
}
