package com.carlos.audit.config;

import com.carlos.boot.request.RequestUtil;
import com.carlos.core.auth.UserContext;
import com.carlos.core.interfaces.ApplicationExtend;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * <p>
 * 自定义系统扩展实现
 * </p>
 *
 * @author Carlos
 * @date 2026年3月6日 下午9:31:12
 */
@Slf4j
@Component
@AllArgsConstructor
@ConditionalOnMissingBean(ApplicationExtend.class)
public class ApplicationExtendImpl implements ApplicationExtend {


    @Override
    public Serializable getUserId() {
        UserContext userContext = RequestUtil.getRequestInfo().getUserContext();
        return userContext.getUserId();
    }

}
