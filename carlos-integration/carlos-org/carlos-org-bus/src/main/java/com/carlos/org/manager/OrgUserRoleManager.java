package com.carlos.org.manager;

import com.carlos.core.pagination.Paging;
import com.carlos.datasource.base.BaseService;
import com.carlos.org.pojo.dto.OrgUserRoleDTO;
import com.carlos.org.pojo.entity.OrgUserRole;
import com.carlos.org.pojo.param.OrgUserRolePageParam;
import com.carlos.org.pojo.vo.OrgUserRoleVO;

import java.io.Serializable;

/**
 * <p>
 * 用户角色 查询封装接口
 * </p>
 *
 * @author Carlos
 * @date 2026年2月28日 下午1:25:36
 */
public interface OrgUserRoleManager extends BaseService<OrgUserRole> {

    /**
     * 新增用户角色
     *
     * @param dto 用户角色数据
     * @return boolean
     * @author Carlos
     * @date 2026年2月28日 下午1:25:36
     */
    boolean add(OrgUserRoleDTO dto);

    /**
     * 删除用户角色
     *
     * @param id 用户角色id
     * @return boolean
     * @author Carlos
     * @date 2026年2月28日 下午1:25:36
     */
    boolean delete(Serializable id);

    /**
     * 修改用户角色信息
     *
     * @param dto 对象信息
     * @return boolean
     * @author Carlos
     * @date 2026年2月28日 下午1:25:36
     */
    boolean modify(OrgUserRoleDTO dto);

    /**
     * 获取数据详情
     *
     * @param id 主键id
     * @return com.carlos.org.pojo.dto.OrgUserRoleDTO
     * @author Carlos
     * @date 2026年2月28日 下午1:25:36
     */
    OrgUserRoleDTO getDtoById(Serializable id);

    /**
     * 分页列表
     *
     * @param  param 分页参数
     * @author Carlos
     * @date 2026年2月28日 下午1:25:36
     */
    Paging<OrgUserRoleVO> getPage(OrgUserRolePageParam param);
}
