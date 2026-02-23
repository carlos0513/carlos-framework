package com.carlos;

import com.carlos.cloud.SpringCloudApplication;
import org.springframework.boot.SpringApplication;

/**
 * 启动程序
 *
 * @author carlos
 */
@SpringCloudApplication
public class MessageCloudApplication {

    public static void main(final String[] args) {
        SpringApplication.run(MessageCloudApplication.class, args);
    }
}
