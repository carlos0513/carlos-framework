package com.carlos.system.config;

import com.carlos.boot.request.RequestUtil;
import com.carlos.core.auth.UserContext;
import com.carlos.core.interfaces.ApplicationExtend;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;


/**
 * <p>
 * 自定义系统扩展实现
 * </p>
 *
 * @author carlos
 * @date 2022-11-8 19:30:24
 */
@Slf4j
@AllArgsConstructor
public class SystemApplicationExtendImpl implements ApplicationExtend {

    @Override
    public UserContext getUserContext() {
        return RequestUtil.getRequestInfo().getUserContext();
    }
}
