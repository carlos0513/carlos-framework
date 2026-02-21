package com.yunjin.org.login;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.yunjin.core.exception.ServiceException;
import com.yunjin.docking.tftd.TfAuthUtil;
import com.yunjin.docking.tftd.result.TfOauthUserInfoResult;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 天府统一认证登录
 * </p>
 *
 * @author Carlos
 * @date 2023/10/16 0:17
 */
@Slf4j
public class TfOauthLogin implements ThridLogin<Map<String, String>> {

    @Override
    public String login(Map<String, String> param) {
        log.info("tf-oauth login param:{}", param);
        String code = param.get("code");
        if (StrUtil.isBlank(code)) {
            throw new ServiceException("code不能为空");
        }
        TfOauthUserInfoResult userinfo = null;
        try {
            userinfo = TfAuthUtil.getUserInfo(code);
        } catch (Exception e) {
            log.error("tf-oauth login failed, code:{}", code, e);
            throw new ServiceException("登录失败！");
        }

        log.info("用户信息：{}", JSONUtil.toJsonPrettyStr(userinfo));
        // 获取用户手机号
        String mobile = userinfo.getSysUser().getPhone();
        if (StrUtil.isBlank(mobile)) {
            throw new ServiceException("未获取到用户手机号");
        }
        return mobile;
    }
}
