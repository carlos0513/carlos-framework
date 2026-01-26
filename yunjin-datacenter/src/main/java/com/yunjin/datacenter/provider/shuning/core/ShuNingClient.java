package com.yunjin.datacenter.provider.shuning.core;

import cn.hutool.crypto.digest.DigestUtil;
import cn.hutool.json.JSONObject;
import com.yunjin.datacenter.provider.shuning.config.ShuNingSupplierInfo;
import com.yunjin.datacenter.utils.DatacenterHttpUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 *
 * </p>
 *
 * @author Carlos
 * @date 2024-12-13 11:09
 */
@Slf4j
public class ShuNingClient {

    private ShuNingSupplierInfo supplierInfo;

    private final DatacenterHttpUtils http = DatacenterHttpUtils.instance();

    protected ShuNingClient(ShuNingSupplierInfo supplierInfo) {
        this.supplierInfo = supplierInfo;
    }

    private Map<String, String> getHeaders() {
        Map<String, String> headers = new HashMap<>();
        String timestamp = String.valueOf(System.currentTimeMillis());
        String token = supplierInfo.getAppKey() + supplierInfo.getAppSecret() + timestamp;
        headers.put(ShuNing.HEADER_SIGNATURE, DigestUtil.sha256Hex(token));
        headers.put(ShuNing.HEADER_TIMESTAMP, timestamp);
        return headers;
    }


    public ShuNingResponse get(String uri, Map<String, Object> param) {
        String url = this.supplierInfo.getBaseUrl() + uri;
        if (log.isDebugEnabled()) {
            log.debug("【ShuNing】request info, url:{}, param:{}", url, param);
        }
        try {
            JSONObject response = http.get(url, param, null, getHeaders());
            ShuNingResponse smsResponse = response.toBean(ShuNingResponse.class);
            if (ShuNingErrorCode.FAILED.getCode().equals(smsResponse.getCode())) {
                return smsResponse;
            }

        } catch (Throwable e) {
            log.error("【ShuNing】request failed, url:{} param:{}", url, param, e);
        }
        return null;
    }
}
