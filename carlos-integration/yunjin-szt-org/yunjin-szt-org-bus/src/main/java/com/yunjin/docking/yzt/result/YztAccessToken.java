package com.yunjin.docking.yzt.result;

import lombok.Data;

/**
 * 云政通accesssToken
 */
@Data
public class YztAccessToken {

    private Integer errcode;            // 返回码

    private String errmsg;              // 对返回码的文本描述内容

    private String access_token;        // 获取到的凭证,最长为512字节

    private Long expires_in;            // 凭证的有效时间（秒）

}
