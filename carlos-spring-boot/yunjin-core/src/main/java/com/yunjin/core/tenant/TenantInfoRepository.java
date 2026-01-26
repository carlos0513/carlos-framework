package com.yunjin.core.tenant;

/**
 * <p>
 * 多租户信息仓库
 * </p>
 *
 * @author yunjin
 * @date 2022/11/6 15:57
 */
public interface TenantInfoRepository {

    /**
     * 获取租户信息
     *
     * @return com.yunjin.core.tenant.TenantInfo
     * @author yunjin
     * @date 2022/11/6 16:00
     */
    default TenantInfo getTenantInfo() {
        return null;
    }

}
