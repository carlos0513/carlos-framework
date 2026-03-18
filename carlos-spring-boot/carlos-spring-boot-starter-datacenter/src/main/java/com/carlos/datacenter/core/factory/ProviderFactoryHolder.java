package com.carlos.datacenter.core.factory;

import cn.hutool.core.collection.CollUtil;
import com.carlos.datacenter.core.instance.DatacenterInstance;
import com.carlos.datacenter.core.supplier.SupplierInfo;
import com.carlos.datacenter.exception.DatacenterInstanceException;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <p>
 *   数据平台注册工具
 * </p>
 *
 * @author Carlos
 * @date 2024-10-10 00:28
 */
public class ProviderFactoryHolder {

    /**  工厂集合 */
    private static final Map<String, BaseProviderFactory<? extends DatacenterInstance, ? extends SupplierInfo>> FACTORIES = new ConcurrentHashMap<>();


    /**
     *  注册数据平台供应商工�?
     *
     * @param factoryList  参数0
     * @author Carlos
     * @date 2024-12-11 16:51
     */
    public static void registerFactory(List<BaseProviderFactory<? extends DatacenterInstance, ? extends SupplierInfo>> factoryList) {
        if (CollUtil.isEmpty(factoryList)) {
            return;
        }
        for (BaseProviderFactory<? extends DatacenterInstance, ? extends SupplierInfo> factory : factoryList) {
            if (factory == null) {
                continue;
            }
            registerFactory(factory);
        }
    }

    public static void registerFactory(BaseProviderFactory<? extends DatacenterInstance, ? extends SupplierInfo> factory) {
        if (factory == null) {
            throw new DatacenterInstanceException("注册供应商工厂失败，工厂实例不能为空");
        }
        FACTORIES.put(factory.getSupplier(), factory);
    }

    /**
     * 获取数据平台供应商工�?
     *
     * @param supplier 供应商编�?
     * @return com.carlos.integration.datacenter.provider.factory.BaseProviderFactory<? extends com.carlos.integration.datacenter.core.instance.DatacenterInstance, ? extends com.carlos.integration.datacenter.provider.SupplierInfo>
     * @author Carlos
     * @date 2024-10-10 00:42
     */
    public static BaseProviderFactory<? extends DatacenterInstance, ? extends SupplierInfo> requireForSupplier(String supplier) {
        return FACTORIES.getOrDefault(supplier, null);
    }

}
