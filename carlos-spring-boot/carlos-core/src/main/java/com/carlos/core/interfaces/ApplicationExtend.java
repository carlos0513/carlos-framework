package com.carlos.core.interfaces;

import com.carlos.core.auth.UserContext;
import com.carlos.core.base.DepartmentInfo;
import com.carlos.core.base.Dict;
import com.carlos.core.base.RegionInfo;
import com.carlos.core.base.UserInfo;
import java.io.Serializable;

/**
 * <p>
 * 系统扩展信息
 * </p>
 *
 * @author yunjin
 * @date 2020/6/3 11:09
 */
public interface ApplicationExtend {

    /**
     * 获取请求的token信息
     *
     * @return 当前访问者的token信息
     * @author yunjin
     * @date 2020/6/3 11:15
     */
    default String getToken() {
        return null;
    }

    /**
     * 获取当前访问用户的Id
     *
     * @return 当前访问用户的id
     * @author yunjin
     * @date 2020/6/3 11:16
     */
    default Serializable getUserId() {
        return null;
    }

    /**
     * 获取当前访问用户的用户名
     *
     * @return 当前用户的用户名
     * @author yunjin
     * @date 2020/6/3 11:16
     */
    default String getUserName() {
        return null;
    }

    /**
     * 根据字典code获取字典详细信息
     *
     * @param code 字典选项code
     * @return com.carlos.core.base.DictVo
     * @author yunjin
     * @date 2021/11/25 14:10
     */
    default Dict getDictVo(String code) {
        return null;
    }

    /**
     * 根据字典id获取字典详细信息
     *
     * @param id 字典选项id
     * @return com.carlos.core.base.DictVo
     * @author yunjin
     * @date 2021/11/25 14:10
     */
    default Dict getDictById(Serializable id) {
        return null;
    }

    /**
     * 根据用户id获取用户信息
     *
     * @param userId 用户id
     * @return java.lang.String
     * @author yunjin
     * @date 2021/11/25 15:01
     */
    default UserInfo getUserById(Serializable userId) {
        return null;
    }

    /**
     * 获取用户上下文信息
     *
     * @return com.carlos.core.auth.UserContext
     * @author Carlos
     * @date 2022/11/14 19:01
     */
    default UserContext getUserContext() {
        return null;
    }


    /**
     * 根据部门id获取部门信息
     *
     * @param departmentId 部门id
     * @return java.lang.String
     * @author yunjin
     * @date 2022/12/30 11:01
     */
    default DepartmentInfo getDepartmentById(Serializable departmentId, Integer limit) {
        return null;
    }

    /**
     * 获取区域信息
     *
     * @param regionCode 区域编码
     * @param limit      上级数目
     * @return com.carlos.core.base.RegionInfo
     * @author Carlos
     * @date 2022/12/30 13:54
     */
    default RegionInfo getRegionInfo(String regionCode, Integer limit) {
        return null;
    }


    /**
     * 根据部门code获取部门信息
     *
     * @param code  部门code
     * @param limit 预览级数
     * @return com.carlos.core.base.DepartmentInfo
     * @author Carlos
     * @date 2023/8/9 16:49
     */
    default DepartmentInfo getDepartmentByCode(String code, int limit) {
        return null;
    }

    
}
