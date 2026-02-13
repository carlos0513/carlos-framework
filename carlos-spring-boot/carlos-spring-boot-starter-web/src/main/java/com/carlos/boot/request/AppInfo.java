package com.carlos.boot.request;

import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 应用信息
 * </p>
 *
 * @author Carlos
 * @date 2025-04-15 14:09
 */
@Data
public class AppInfo implements Serializable {

    /**
     * 应用id
     */
    private String appId;

    /**
     * appKey
     */
    private String appKey;

    /**
     * app名称
     */
    private String appName;

}
