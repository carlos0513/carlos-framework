package com.carlos.docking.rzt.user;

import java.util.List;

public interface RztUserManager {


    /**
     * 加载系统全部用户
     *
     * @return java.util.List<com.carlos.docking.rzt.user.UserInfo>
     * @author Carlos
     * @date 2024-12-13 15:49
     */
    List<SysUserInfo> load();

}
