package com.yunjin.org.login.service.impl;

import cn.hutool.core.util.StrUtil;
import com.yunjin.core.auth.AccessTokenParam;
import com.yunjin.core.auth.Oauth2TokenDTO;
import com.yunjin.core.response.Result;
import com.yunjin.encrypt.EncryptUtil;
import com.yunjin.org.login.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
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
@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class LocalAuthServiceImpl implements AuthService {


    @Override
    public String encodePassword(String password) {
        if (StrUtil.isBlank(password)) {
            return null;
        }
        String encrypt = EncryptUtil.encrypt(password);
        if (log.isDebugEnabled()) {
            log.debug("Encode password:{} -> {} success", password, encrypt);
        }
        return encrypt;
    }

    @Override
    public String decodePassword(String password) {
        if (StrUtil.isBlank(password)) {
            return null;
        }
        String decrypt = EncryptUtil.decrypt(password);
        if (log.isDebugEnabled()) {
            log.debug("Decode password:{} -> {} success", password, decrypt);
        }
        return decrypt;
    }

    @Override
    public Result<Oauth2TokenDTO> getToken(AccessTokenParam param) {
        if (param == null) {
            return null;
        }

        return null;
    }

    @Override
    public boolean checkPassword(String password, String encodePassword) {
        String encrypt = EncryptUtil.encrypt(password);
        return encrypt.equals(encodePassword);
    }
}
