package com.carlos.system.menu.manager;

import com.carlos.core.pagination.Paging;
import com.carlos.datasource.base.BaseService;
import com.carlos.system.menu.pojo.dto.MenuOperateDTO;
import com.carlos.system.menu.pojo.entity.MenuOperate;
import com.carlos.system.menu.pojo.param.MenuOperatePageParam;
import com.carlos.system.menu.pojo.vo.MenuOperateVO;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * 菜单操作 查询封装接口
 * </p>
 *
 * @author yunjin
 * @date 2023-7-7 14:19:55
 */
public interface MenuOperateManager extends BaseService<MenuOperate> {

    /**
     * 新增菜单操作
     *
     * @param dto 菜单操作数据
     * @return boolean
     * @author yunjin
     * @date 2023-7-7 14:19:55
     */
    boolean add(MenuOperateDTO dto);

    /**
     * 删除菜单操作
     *
     * @param id 菜单操作id
     * @return boolean
     * @author yunjin
     * @date 2023-7-7 14:19:55
     */
    boolean delete(Serializable id);

    /**
     * 修改菜单操作信息
     *
     * @param dto 对象信息
     * @return boolean
     * @author yunjin
     * @date 2023-7-7 14:19:55
     */
    boolean modify(MenuOperateDTO dto);

    /**
     * 获取数据详情
     *
     * @param id 主键id
     * @return com.carlos.system.menu.pojo.dto.MenuOperateDTO
     * @author yunjin
     * @date 2023-7-7 14:19:55
     */
    MenuOperateDTO getDtoById(Serializable id);

    /**
     * 分页列表
     *
     * @param param 分页参数
     * @author yunjin
     * @date 2023-7-7 14:19:55
     */
    Paging<MenuOperateVO> getPage(MenuOperatePageParam param);

    /**
     * @Title: listByKeyword
     * @Description: 关键字查询
     * @Date: 2023/7/7 15:21
     * @Parameters: [keyword]
     * @Return java.util.List<com.carlos.system.menu.pojo.dto.MenuOperateDTO>
     */
    List<MenuOperateDTO> listByKeyword(String keyword);

    /**
     * @Title: listById
     * @Description: 根据ids获取列表
     * @Date: 2023/7/7 15:46
     * @Parameters: [ids]
     * @Return java.util.List<com.carlos.system.menu.pojo.dto.MenuOperateDTO>
     */
    List<MenuOperateDTO> listById(Set<String> ids);

    /**
     * @Title: listByMenuId
     * @Description: 根据menuId获取
     * @Date: 2023/7/10 10:11
     * @Parameters: [menuId]
     * @Return java.util.List<com.carlos.system.menu.pojo.dto.MenuOperateDTO>
     */
    List<MenuOperateDTO> listByMenuId(String menuId);

    /**
     * @Title: listByMenuIds
     * @Description: 根据menuIds获取
     * @Date: 2023/7/10 10:29
     * @Parameters: [menuIds]
     * @Return java.util.List<com.carlos.system.menu.pojo.dto.MenuOperateDTO>
     */
    List<MenuOperateDTO> listByMenuIds(Set<String> menuIds);
}
