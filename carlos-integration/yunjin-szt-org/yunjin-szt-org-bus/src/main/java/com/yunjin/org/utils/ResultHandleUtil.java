package com.yunjin.org.utils;

import com.yunjin.core.exception.ServiceException;
import com.yunjin.core.response.Result;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 远端调用Result处理工具类
 * </p>
 *
 * @author Mutsuki
 * @date 2023/7/27 17:02
 */
@Slf4j
public class ResultHandleUtil {

    public static <T> T handleResult(Result<T> result) {
        if (result == null || !result.getSuccess()) {
            log.error("远程服务调用失败！异常信息：" + result);
            throw new ServiceException("远程服务调用失败！异常信息：" + result.getMessage());
        }
        return result.getData();

    }
}
