package com.carlos.sms.ynchina.config;


import com.carlos.sms.enums.SmsServiceType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.sms4j.provider.config.BaseConfig;

/**
 * <p>
 *   云南
 * </p>
 *
 * @author Carlos
 * @date 2025-03-26 10:57 
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class YnChinaConfig extends BaseConfig {

    private String url = "http://59.216.220.11:18086/sms/inner/sendMultipart";
    private String serverPublicKey;
    private String appPrivateKey;
    private String appId;


    @Override
    public String getSupplier() {
        return SmsServiceType.YNCHINA.getCode();
    }

}
