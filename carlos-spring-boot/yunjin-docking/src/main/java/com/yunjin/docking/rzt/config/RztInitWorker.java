package com.yunjin.docking.rzt.config;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.cron.CronUtil;
import cn.hutool.cron.task.Task;
import com.yunjin.docking.rzt.organization.RztOrganizationManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;

import java.util.Date;

/**
 * <p>
 * 蓉政通接口初始化
 * </p>
 *
 * @author Carlos
 * @date 2023/4/6 10:18
 */
@Slf4j
@RequiredArgsConstructor
public class RztInitWorker implements ApplicationRunner {

    private final RztAccessTokenManager tokenManager;
    private final RztOrganizationManager organizationManager;


    private final String corn;
    private final String userCacheCorn;


    @Override
    public void run(ApplicationArguments args) {
        // 初始化accesstoken, 并触发定时器刷新
        // 判断是否已经初始化任务
        String jobId = CronUtil.schedule("RZT-ACCESSTOKEN-TASK", corn, new Task() {
            @Override
            public void execute() {
                try {
                    log.info("rzt refresh accessToken, date:{}", DateUtil.format(new Date(), DatePattern.NORM_DATETIME_MS_PATTERN));
                    tokenManager.refreshAccessToken();
                } catch (Exception e) {
                    log.error("rzt refresh accessToken fail, date:{}", DateUtil.format(new Date(), DatePattern.NORM_DATETIME_MS_PATTERN), e);
                }
            }
        });

        log.info("Job '{}' has been registered", jobId);

        // 判断是否已经初始化任务
        String jobId2 = CronUtil.schedule("RZT-USER-TASK", userCacheCorn, new Task() {
            @Override
            public void execute() {
                try {
                    log.info("rzt refresh user cache, date:{}", DateUtil.format(new Date(), DatePattern.NORM_DATETIME_MS_PATTERN));
                    organizationManager.cacheUsers(1, 200, null, null);
                } catch (Exception e) {
                    log.error("rzt refresh user cache fail, date:{}", DateUtil.format(new Date(), DatePattern.NORM_DATETIME_MS_PATTERN), e);
                }
            }
        });

        log.info("Job2 '{}' has been registered", jobId2);
    }


}
