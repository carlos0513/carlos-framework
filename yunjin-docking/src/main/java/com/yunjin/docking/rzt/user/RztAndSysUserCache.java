package com.yunjin.docking.rzt.user;

import com.yunjin.docking.rzt.organization.result.RztUserInfoResult;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * <p>
 *   用户信息
 * </p>
 *
 * @author Carlos
 * @date 2024-12-13 15:48
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class RztAndSysUserCache {
    /**
     * 蓉政通用户信息
     */
    private RztUserInfoResult rztUser;
    /**
     * 系统用户信息
     */
    private SysUserInfo sysUserInfo;
}
