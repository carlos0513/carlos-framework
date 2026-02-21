package com.yunjin;

import cn.hutool.cron.CronUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 启动程序
 *
 * @author yunjin
 */
@SpringBootApplication
public class YunjinSztOrgCloudApplication {

    public static void main(final String[] args) {
        SpringApplication.run(YunjinSztOrgCloudApplication.class, args);
        // 支持秒级别定时任务
        CronUtil.setMatchSecond(true);
        // 启动定时任务
        CronUtil.start(true);
    }
}
