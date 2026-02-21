package com.yunjin.docking.yzt.result;

import lombok.Data;

/**
 * 云政通用户信息
 */
@Data
public class YztUserInfo {

    private Integer errcode;            // 返回码

    private String errmsg;              // 对返回码的文本描述内容

    private String UserId;              // 成员UserID

    private String DeviceId;            // 手机设备号

    private String usertype;            // 成员身份信息: 2-超级管理员, 4-分级管理员，5-普通成员

}
