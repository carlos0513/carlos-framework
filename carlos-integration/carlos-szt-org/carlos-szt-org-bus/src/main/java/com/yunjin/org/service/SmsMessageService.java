package com.yunjin.org.service;

import com.yunjin.sms.SmsUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;

@Slf4j
@Service
@RequiredArgsConstructor
public class SmsMessageService {
    public void sendSms(String phone, String templateCode, LinkedHashMap<String, String> map) {
        try {
            log.info("send sms. phone:{}, verifyType:{},map:{}", phone, templateCode,map);
            SmsUtil.sendByTemplateKey(phone, templateCode, map);
        } catch (RuntimeException e) {
            log.error("send sms failed. phone:{}, verifyType:{},map:{} error:{}", phone, templateCode, map, e.getMessage());
        }
    }
}
