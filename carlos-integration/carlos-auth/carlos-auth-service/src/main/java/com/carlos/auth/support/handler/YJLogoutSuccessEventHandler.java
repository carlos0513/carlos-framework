package com.carlos.auth.support.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.LogoutSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Component;

/**
 * @author carlos
 * @WebSite www.carlos.cn
 * @Description: 处理退出登录成功事件
 */
@Component
public class YJLogoutSuccessEventHandler implements ApplicationListener<LogoutSuccessEvent> {

    private static final Logger log = LoggerFactory.getLogger(YJLogoutSuccessEventHandler.class);

    @Override
    public void onApplicationEvent(LogoutSuccessEvent event) {
        Authentication authentication = (Authentication) event.getSource();
        if (authentication instanceof PreAuthenticatedAuthenticationToken) {
            handle(authentication);
        }
    }

    /**
     * 处理退出成功方法
     * <p>
     * 获取到登录的authentication 对象
     *
     * @param authentication 登录对象
     */
    public void handle(Authentication authentication) {
        log.info("用户：{} 退出成功", authentication.getPrincipal());
//		String username = authentication.getName();
//		SysLoginInfo sysLoginInfo = new SysLoginInfo();
//		SysLogVO sysLog = SysLogUtils.getSysLog();
//		sysLoginInfo.setStatus(SecurityConstants.LOGOUT_SUCCESS);
//		sysLoginInfo.setUserName(username);
//		sysLoginInfo.setMsg("退出成功");
//		sysLoginInfo.setIpaddr(IpUtils.getIpAddr());
//		// 发送异步日志事件
//		sysLoginInfo.setCreateTime(DateUtils.getNowDate());
//		sysLoginInfo.setCreateBy(username);
//		sysLoginInfo.setUpdateBy(username);
//		SpringContextHolder.publishEvent(new SysLoginLogEvent(sysLoginInfo));
    }

}
