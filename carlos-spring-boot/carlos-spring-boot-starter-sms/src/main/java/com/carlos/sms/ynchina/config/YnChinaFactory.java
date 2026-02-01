package com.carlos.sms.ynchina.config;


import com.carlos.sms.enums.SmsServiceType;
import com.carlos.sms.ynchina.core.YnChinaSmsImpl;
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
