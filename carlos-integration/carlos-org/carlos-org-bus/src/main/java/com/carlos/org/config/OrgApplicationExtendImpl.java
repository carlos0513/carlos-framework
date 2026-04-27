package com.carlos.org.config;

import com.carlos.boot.request.RequestUtil;
import com.carlos.core.auth.UserContext;
import com.carlos.core.interfaces.ApplicationExtend;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;

/**
 * <p>
 * 自定义系统扩展实现
 * </p>
 *
 * @author Carlos
 * @date 2023-7-18 23:37:15
 */
@Slf4j
@AllArgsConstructor

public class OrgApplicationExtendImpl implements ApplicationExtend {

    @Override
    public Serializable getUserId() {
        UserContext userContext = RequestUtil.getRequestInfo().getUserContext();
        return userContext.getUserId();
    }
}
