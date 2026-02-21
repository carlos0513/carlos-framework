package com.yunjin.docking.yzt.config;

import com.yunjin.docking.yzt.YztAuthUtil;
import com.yunjin.docking.yzt.service.YztService;
import com.yunjin.org.service.OrgDockingMappingService;
import com.yunjin.org.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class YztAuthConfig {

    private final YztService yztService;

    private final OrgDockingMappingService orgDockingMappingService;

    private final UserService userService;

    @Bean
    public YztAuthUtil yztAuthUtil() {
        return new YztAuthUtil(yztService, orgDockingMappingService, userService);
    }

}
