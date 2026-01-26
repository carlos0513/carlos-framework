package com.yunjin.boot.request;

import cn.hutool.extra.servlet.JakartaServletUtil;
import cn.hutool.http.Header;
import cn.hutool.http.useragent.UserAgent;
import cn.hutool.http.useragent.UserAgentUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>
 * 用户客户端信息工具类
 * </p>
 *
 * @author yunjin
 * @date 2020/4/10 16:15
 */
@Slf4j
public class ClientInfoUtil {

    private static final Pattern DEVICE_INFO_PATTERN = Pattern.compile(";\\s?(\\S*?\\s?\\S*?)\\s?Build/(\\S*?)[;)]");
    private static final Pattern DEVICE_INFO_PATTERN_1 = Pattern.compile(";\\s?(\\S*?\\s?\\S*?)\\s?\\)");

    /**
     * 获取请求客户端设备信息
     *
     * @author yunjin
     * @date 2020/4/24 17:49
     */
    public static ClientInfo get() {
        String userAgentString = JakartaServletUtil.getHeaderIgnoreCase(RequestUtil.getRequest(), Header.USER_AGENT.getValue());
        return get(userAgentString);
    }

    /**
     * 获取访问客户端信息
     *
     * @param userAgentString agent信息
     * @author yunjin
     * @date 2020/4/14 11:47
     */
    public static ClientInfo get(String userAgentString) {
        if (userAgentString == null) {
            log.warn("The 'User-Agent' can't be null in request header!");
            return null;
        }
        ClientInfo clientInfo = new ClientInfo();
        clientInfo.setIp(RequestUtil.getRequestInfo().getIp());
        UserAgent userAgent = UserAgentUtil.parse(userAgentString);
        // 浏览器名称
        clientInfo.setBrowserName(userAgent.getBrowser().getName());
        // 浏览器版本
        clientInfo.setBrowserVersion(userAgent.getVersion());
        // 浏览器引擎名称
        clientInfo.setEngineName(userAgent.getEngine().getName());
        // 浏览器引擎版本
        clientInfo.setEngineVersion(userAgent.getEngineVersion());
        // 用户操作系统名称
        clientInfo.setOsName(userAgent.getOs().getName());
        // 用户操作平台名称
        clientInfo.setPlatformName(userAgent.getPlatform().getName());
        if (userAgent.isMobile()) {
            // 追加移动设备名称和机型
            appendMobileInfo(clientInfo, userAgentString);
        } else {
            clientInfo.setMobileModel("");
            clientInfo.setMobileName("");
        }
        return clientInfo;
    }

    /**
     * <p>
     * 获取移动端用户设备的名称和机型
     * </p>
     *
     * @author yunjin
     * @date 2020/4/14 11:45
     */
    private static void appendMobileInfo(ClientInfo clientInfo, String userAgentString) {
        try {
            Matcher matcher = DEVICE_INFO_PATTERN.matcher(userAgentString);
            String model = null;
            String name = null;

            if (matcher.find()) {
                model = matcher.group(1);
                name = matcher.group(2);
            }

            if (model == null && name == null) {
                matcher = DEVICE_INFO_PATTERN_1.matcher(userAgentString);
                if (matcher.find()) {
                    model = matcher.group(1);
                }
            }
            clientInfo.setMobileName(name);
            clientInfo.setMobileModel(model);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
