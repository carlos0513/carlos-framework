package com.yunjin.docking.jct;

import com.yunjin.docking.jct.result.LJAppAggrUser;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * <p>
 * 工具类
 * </p>
 *
 * @author Carlos
 * @date 2023/4/6 9:39
 */
@Slf4j
public class LJAppAggrUtil {

    private static LJAppAggrService service;


    public LJAppAggrUtil(LJAppAggrService service) {
        LJAppAggrUtil.service = service;
    }

    /**
     * 获取用户信息
     *
     * @param token 参数0
     * @return com.yunjin.docking.jct.result.LJAppAggrUser
     * @author Carlos
     * @date 2025-02-25 15:09
     */
    public static LJAppAggrUser getUserInfo(String token) {
        return service.getUserInfo(token);
    }


    /**
     * 获取所有用户
     *
     * @return java.util.List<com.yunjin.docking.jct.result.LJAppAggrUser>
     * @author Carlos
     * @date 2025-02-25 15:09
     */

    public static List<LJAppAggrUser> listAll() {
        return service.listAll();
    }
}
