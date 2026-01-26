package com.yunjin.sms.ynchina.core;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class YnChinaSendMultipartRequest extends YuChinaGatewaySDK.BaseRequest<YnChinaSendMultipartResponse> {
    private String templateId;
    private String signId;
    private List<String> templateParams;
    private List<String> phoneNumbers;

    @Override
    public Class<YnChinaSendMultipartResponse> getResponseType() {
        return YnChinaSendMultipartResponse.class;
    }
}