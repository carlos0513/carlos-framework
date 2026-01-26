package com.yunjin.datacenter.provider.shuning.config;


import com.yunjin.datacenter.core.factory.AbstractProviderFactory;
import com.yunjin.datacenter.provider.shuning.core.ShuNing;
import com.yunjin.datacenter.provider.shuning.core.ShuNingDataCenter;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * <p>
 *   属宁数据平台工厂类
 * </p>
 *
 * @author Carlos
 * @date 2024-10-10 08:45
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ShuNingFactory extends AbstractProviderFactory<ShuNingDataCenter, ShuNingSupplierInfo> {

    private static final ShuNingFactory INSTANCE = new ShuNingFactory();

    /**
     * 获取建造者实例
     *
     * @return 建造者实例
     */
    public static ShuNingFactory instance() {
        return INSTANCE;
    }


    @Override
    public ShuNingDataCenter createDatacenter(ShuNingSupplierInfo supplierInfo) {
        ShuNing.init(supplierInfo);
        return new ShuNingDataCenter(supplierInfo);
    }

    @Override
    public String getSupplier() {
        return ShuNing.SUPPLIER_CODE;
    }
}
