package com.carlos.org.manager;

import com.carlos.core.pagination.Paging;
import com.carlos.datasource.base.BaseService;
import com.carlos.org.pojo.dto.OrgUserMenuDTO;
import com.carlos.org.pojo.entity.OrgUserMenu;
import com.carlos.org.pojo.param.OrgUserMenuPageParam;
import com.carlos.org.pojo.vo.OrgUserMenuVO;

import java.io.Serializable;
/**
 * <p>
 * 用户菜单收藏表 查询封装接口
 * </p>
 *
 * @author carlos
 * @date    2024-2-28 11:10:01
 */
public interface OrgUserMenuManager extends BaseService<OrgUserMenu>{

    /**
     * 新增用户菜单收藏表
     *
     * @param dto 用户菜单收藏表数据
     * @return boolean
     * @author carlos
     * @date    2024-2-28 11:10:01
     */
    boolean add(OrgUserMenuDTO dto);

    /**
     * 删除用户菜单收藏表
     *
     * @param id 用户菜单收藏表id
     * @return boolean
     * @author carlos
     * @date    2024-2-28 11:10:01
     */
    boolean delete(Serializable id);

    /**
     * 修改用户菜单收藏表信息
     *
     * @param dto 对象信息
     * @return boolean
     * @author carlos
     * @date    2024-2-28 11:10:01
     */
    boolean modify(OrgUserMenuDTO dto);

    /**
     * 获取数据详情
     *
     * @param id 主键id
     * @return com.carlos.pojo.dto.OrgUserMenuDTO
     * @author carlos
     * @date   2024-2-28 11:10:01
     */
    OrgUserMenuDTO getDtoById(Serializable id);

    /**
     * 分页列表
     *
     * @param  param 分页参数
     * @author carlos
     * @date   2024-2-28 11:10:01
     */
    Paging<OrgUserMenuVO> getPage(OrgUserMenuPageParam param);
}
