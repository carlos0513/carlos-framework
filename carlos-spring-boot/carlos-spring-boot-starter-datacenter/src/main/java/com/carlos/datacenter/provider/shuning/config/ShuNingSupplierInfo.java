package com.carlos.datacenter.provider.shuning.config;

import com.carlos.datacenter.core.supplier.BaseSupplierInfo;
import com.carlos.datacenter.provider.shuning.core.ShuNing;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 属宁数据中台配置项
 * </p>
 *
 * @author Carlos
 * @date 2024-10-10 08:26
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ShuNingSupplierInfo extends BaseSupplierInfo {

    /**
     * 地址前缀
     */
    private String baseUrl;
    /**
     * appkey
     */
    private String appKey;
    /**
     * ip
     */
    private String appSecret;

    /** 加密配置 */
    private EncryptInfo encrypt = new EncryptInfo();

    /**
     * 前置库属性配置
     */
    @Data
    public static class EncryptInfo {

        /**
         * 秘钥
         */
        private String key;

        /**
         * iv向量 CBC模式需要
         */
        private String iv;

        /**
         * 密文类型
         */
        private String encryptTextType;
    }


    @Override
    public String getSupplier() {
        return ShuNing.SUPPLIER_CODE;
    }

}
