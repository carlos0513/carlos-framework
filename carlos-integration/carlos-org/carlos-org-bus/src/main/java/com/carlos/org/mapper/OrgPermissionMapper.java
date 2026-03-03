package com.carlos.org.mapper;

import com.carlos.org.pojo.entity.OrgPermission;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.io.Serializable;


/**
 * <p>
 * 权限 查询接口
 * </p>
 *
 * @author Carlos
 * @date 2026年2月28日 下午1:25:36
 */
@Mapper
public interface OrgPermissionMapper extends MPJBaseMapper<OrgPermission> {

    /**
     * 获取权限被角色使用的数量
     *
     * @param permId 权限ID
     * @return int
     */
    int getRoleUseCount(@Param("permId") Serializable permId);

}
