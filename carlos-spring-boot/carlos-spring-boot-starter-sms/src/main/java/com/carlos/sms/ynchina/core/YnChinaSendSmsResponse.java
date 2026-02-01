package com.carlos.sms.ynchina.core;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class YnChinaSendSmsResponse extends YuChinaGatewaySDK.BaseResponse {
    private Integer status;
    private String msgGroupId;
    private String serialId;
}