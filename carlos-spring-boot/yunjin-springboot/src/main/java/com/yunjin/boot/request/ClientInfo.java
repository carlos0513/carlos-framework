package com.yunjin.boot.request;

import java.io.Serializable;
import lombok.Data;

/**
 * <p>
 * 用户客户端信息对象
 * </p>
 *
 * @author yunjin
 * @date 2020/4/14 11:44
 */
@Data
public class ClientInfo implements Serializable {

    /**
     * ip
     */
    private String ip;

    /**
     * 浏览器名称
     */
    private String browserName;

    /**
     * 浏览器版本
     */
    private String browserVersion;

    /**
     * 浏览器引擎名称
     */
    private String engineName;

    /**
     * 浏览器引擎版本
     */
    private String engineVersion;

    /**
     * 系统名称
     */
    private String osName;

    /**
     * 平台名称
     */
    private String platformName;

    /**
     * 移动端设备名称
     */
    private String mobileName;

    /**
     * 移动端设备型号
     */
    private String mobileModel;

}
