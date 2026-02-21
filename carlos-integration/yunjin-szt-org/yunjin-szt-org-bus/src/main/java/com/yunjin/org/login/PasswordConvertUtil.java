package com.yunjin.org.login;


import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.StrUtil;
import com.yunjin.core.exception.ComponentException;
import com.yunjin.encrypt.EncryptUtil;
import lombok.extern.slf4j.Slf4j;


/**
 * <p>
 *    密码工具
 * </p>
 *
 * @author Carlos
 * @date 2025-09-08 22:11
 */
@Slf4j
public class PasswordConvertUtil {


    /**
     * sm2转base64
     *
     * @param sm2 参数0
     * @return java.lang.String
     * @throws
     * @author Carlos
     * @date 2025-09-08 22:11
     */
    public static String sm2ToBase64(String sm2) {
        if (StrUtil.isBlank(sm2)) {
            return StrUtil.EMPTY;
        }
        String str = null;
        try {
            str = EncryptUtil.sm2Decrypt(sm2);
        } catch (Exception e) {
            log.error("sm2密码解密失败！sm2:{}", sm2, e);
            throw new ComponentException("无效密码！");
        }
        return Base64.encode(str);
    }

}
