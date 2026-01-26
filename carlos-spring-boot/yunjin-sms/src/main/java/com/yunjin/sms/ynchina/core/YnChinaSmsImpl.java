package com.yunjin.sms.ynchina.core;

import com.google.common.collect.Lists;
import com.yunjin.sms.SmsUtil;
import com.yunjin.sms.enums.SmsServiceType;
import com.yunjin.sms.template.SmsTemplate;
import com.yunjin.sms.ynchina.config.YnChinaConfig;
import lombok.extern.slf4j.Slf4j;
import org.dromara.sms4j.api.SmsBlend;
import org.dromara.sms4j.api.entity.SmsResponse;
import org.dromara.sms4j.provider.service.AbstractSmsBlend;
import org.springframework.web.client.RestTemplate;

import java.util.LinkedHashMap;
import java.util.List;

@Slf4j
public class YnChinaSmsImpl extends AbstractSmsBlend<YnChinaConfig> implements SmsBlend {

    private final YuChinaGatewaySDK sdkService;


    public YnChinaSmsImpl(YnChinaConfig config) {
        super(config);
        sdkService = new YuChinaGatewaySDK(config.getAppId(), config.getAppPrivateKey(), config.getServerPublicKey());
        sdkService.setRestTemplate(new RestTemplate());
    }

    @Override
    public String getSupplier() {
        return SmsServiceType.YNCHINA.getCode();
    }

    @Override
    public SmsResponse sendMessage(String phone, String message) {
        // 直接调用群发方法
        return massTexting(Lists.newArrayList(phone), message);
    }

    @Override
    public SmsResponse sendMessage(String phone, LinkedHashMap<String, String> messages) {
        return massTexting(Lists.newArrayList(phone), getConfig().getTemplateId(), messages);
    }

    @Override
    public SmsResponse sendMessage(String phone, String templateId, LinkedHashMap<String, String> messages) {
        return massTexting(Lists.newArrayList(phone), templateId, messages);
    }

    @Override
    public SmsResponse massTexting(List<String> phones, String message) {
        return massTexting(phones, getConfig().getTemplateId(), new LinkedHashMap<>());
    }

    @Override
    public SmsResponse massTexting(List<String> phones, String templateId, LinkedHashMap<String, String> messages) {
        SmsTemplate template = SmsUtil.getCurrentTemplate();
        YnChinaConfig config = this.getConfig();
        String url = config.getUrl();
        YnChinaSendMultipartRequest request = new YnChinaSendMultipartRequest();
        request.setTemplateId(template.getTemplateId());
        request.setSignId("");
        request.setTemplateParams(Lists.newArrayList(messages.values()));
        request.setPhoneNumbers(phones);
        SmsResponse smsResponse = new SmsResponse();
        smsResponse.setSuccess(true);
        smsResponse.setConfigId(getConfigId());
        try {
            YnChinaSendMultipartResponse response = sdkService.post(url, request);
            smsResponse.setData(response);
        } catch (Throwable e) {
            log.warn("短信发送失败：{}", e.getMessage(), e);
            smsResponse.setData(e.getMessage());
            smsResponse.setSuccess(false);
        }
        return smsResponse;
    }

}