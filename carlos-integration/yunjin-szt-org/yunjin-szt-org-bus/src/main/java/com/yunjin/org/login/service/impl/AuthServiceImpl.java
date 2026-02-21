package com.yunjin.org.login.service.impl;

import cn.hutool.core.util.StrUtil;
import com.yunjin.core.auth.AccessTokenParam;
import com.yunjin.core.auth.Oauth2TokenDTO;
import com.yunjin.core.exception.ServiceException;
import com.yunjin.core.response.Result;
import com.yunjin.org.login.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;


/**
 * <p>
 * 部门角色 业务接口实现类
 * </p>
 *
 * @author yunjin
 * @date 2021-12-20 14:07:16
 */
@Slf4j
// @Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class AuthServiceImpl implements AuthService {


    @Override
    public String encodePassword(String password) {
        if (StrUtil.isBlank(password)) {
            return null;
        }
        Result<String> result = Result.ok("12345");
        if (!result.getSuccess()) {
            log.error("encode password failed, message: {}, detail message:{}", result.getMessage(), result.getStack());
            throw new ServiceException("字符串加密失败");
        }
        if (log.isDebugEnabled()) {
            log.debug("Encode password:{} -> {} success", password, result.getData());
        }
        return result.getData();
    }

    @Override
    public String decodePassword(String password) {
        return "";
    }

    @Override
    public Result<Oauth2TokenDTO> getToken(AccessTokenParam param) {
        if (param == null) {
            return null;
        }
        // Map<String, String> map = param.toMap();
        // Result<Oauth2TokenDTO> accessToken = feignAuth.getAccessToken(map, param.getHeaders());
        // if (log.isDebugEnabled()) {
        //     log.debug("Get oauth2 token: param:{}, result:{}", param, accessToken);
        // }
        // return accessToken;
        return null;
    }

    @Override
    public boolean checkPassword(String password, String encodePassword) {
        return true;
        // PasswordMatchDTO dto = new PasswordMatchDTO();
        // dto.setPassword(password);
        // dto.setEncodePassword(encodePassword);
        // Result<Boolean> result = feignAuth.match(dto);
        // if (!result.getSuccess()) {
        //     log.error("密码匹配服务调用是吧, message: {}, detail message:{}", result.getMessage(), result.getStack());
        //     throw new ServiceException(result.getMessage());
        // }
        // return result.getData();
    }
}
