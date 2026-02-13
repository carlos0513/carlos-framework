package com.carlos.boot.runner;

import com.carlos.core.spring.BootConstant;
import com.carlos.core.spring.BootUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.Ordered;

/**
 * <p>
 * 应用启动成功需要做的工作
 * </p>
 *
 * @author carlos
 * @date 2020-2-18 10:11:05
 */
@Slf4j
@RequiredArgsConstructor
public class ApplicationRunnerWorker implements ApplicationRunner, Ordered {


    @Override
    public void run(ApplicationArguments args) {
        // 打印项目信息
        printApplicationInfo();
    }

    @Override
    public int getOrder() {
        return 1;
    }


    /**
     * 打印项目信息
     *
     * @author carlos
     * @date 2020/4/9 13:46
     */
    public void printApplicationInfo() {
        // Spring Boot Admin Server地址，请先在admin模块中启动 SpringBootPlusAdminApplication
        String startSuccess = "\n" +
                "\n" +
                "    _____ __             __     _____\n" +
                "   / ___// /_____ ______/ /_   / ___/__  _______________  __________\n" +
                "   \\__ \\/ __/ __ `/ ___/ __/   \\__ \\/ / / / ___/ ___/ _ \\/ ___/ ___/\n" +
                "  _____/ / /_/ /_/ /  / /     _____/ / /_/ /__/ /__/  __(__  |__  )      \n" +
                " /____/\\__/\\__,_/_/   \\__/   /____/\\__,_/\\___/\\___/\\___/____/____/\n" +
                "";
        log.info(startSuccess);
        String profileActive = BootUtil.getProfileActive();
        if (StringUtils.isNotBlank(profileActive)) {
            log.info("Profile-Active: {}", profileActive);
        }
        String contextPath = BootUtil.getContextPath();
        if (StringUtils.isNotBlank(contextPath)) {
            log.info("Content-Path: {}", contextPath);
        }
        String adminUrl = BootUtil.getPropertyValue(BootConstant.ADMIN_CLIENT_URL);
        if (StringUtils.isNotBlank(adminUrl)) {
            log.info("Admin:   {}", adminUrl);
        }
    }

}
