package com.carlos.datacenter.provider.shuning.core;


import com.carlos.datacenter.provider.shuning.config.ShuNingSupplierInfo;

/**
 * <p>
 *
 * </p>
 *
 * @author Carlos
 * @date 2024-10-10 16:26
 */
public class ShuNing {

    /** 厂商标识 */
    public static final String SUPPLIER_CODE = "ShuNing";


    public static final String HEADER_SIGNATURE = "signature";
    public static final String HEADER_TIMESTAMP = "timestamp";

    /**
     * 资源分类目录(全量)
     */
    public static final String SOURCE_DIR = "/bbt/dataMart/dirCategory";

    /**
     * 获取元数据列�?
     */
    public static final String METADATA_LIST = "/bbt/dataMart/getAllApiAssets";

    /**
     * 获取元数据结构详�?
     */
    public static final String METADATA_STRUCTURE = "/bbt/dataMart/getAssetsFieldsInfo";

    /**
     * 分页数据获取
     */
    public static final String PAGE_DATA = "/bbt/dataMart/getAssetsData";

    /** 配置信息 */
    private static ShuNingSupplierInfo supplierInfo;


    private static volatile ShuNingClient client;

    private ShuNing() {
    }


    public static void init(ShuNingSupplierInfo supplierInfo) {
        ShuNing.supplierInfo = supplierInfo;
    }


    /**
     * 获取统一客户�?
     *
     * @return com.carlos.integration.datacenter.provider.shuning.core.ShuNingClient
     * @throws
     * @author Carlos
     * @date 2024-10-10 17:16
     */
    public static ShuNingClient getClient() {
        if (ShuNing.client == null) {
            synchronized (ShuNing.class) {
                if (ShuNing.client == null) {
                    ShuNing.client = new ShuNingClient(supplierInfo);
                }
            }
        }
        return ShuNing.client;
    }


}
