package com.yunjin.sms.ynchina.config;


import com.yunjin.sms.enums.SmsServiceType;
import com.yunjin.sms.ynchina.core.YnChinaSmsImpl;
import org.dromara.sms4j.provider.factory.BaseProviderFactory;

public class YnChinaFactory implements BaseProviderFactory<YnChinaSmsImpl, YnChinaConfig> {

    private static final YnChinaFactory INSTANCE = new YnChinaFactory();

    public static YnChinaFactory instance() {
        return INSTANCE;
    }

    @Override
    public YnChinaSmsImpl createSms(YnChinaConfig config) {
        return new YnChinaSmsImpl(config);
    }

    @Override
    public Class<YnChinaConfig> getConfigClass() {
        return YnChinaConfig.class;
    }

    @Override
    public String getSupplier() {
        return SmsServiceType.YNCHINA.getCode();
    }
}
