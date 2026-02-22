package com.carlos.org.manager;

import com.carlos.core.pagination.Paging;
import com.carlos.datasource.base.BaseService;
import com.carlos.org.pojo.dto.RoleOperateDTO;
import com.carlos.org.pojo.entity.RoleOperate;
import com.carlos.org.pojo.param.RoleOperatePageParam;
import com.carlos.org.pojo.vo.RoleOperateVO;

import java.io.Serializable;

/**
 * <p>
 * 角色菜单操作表 查询封装接口
 * </p>
 *
 * @author yunjin
 * @date 2023-7-7 14:19:55
 */
public interface RoleOperateManager extends BaseService<RoleOperate> {

    /**
     * 新增角色菜单操作表
     *
     * @param dto 角色菜单操作表数据
     * @return boolean
     * @author yunjin
     * @date 2023-7-7 14:19:55
     */
    boolean add(RoleOperateDTO dto);

    /**
     * 删除角色菜单操作表
     *
     * @param id 角色菜单操作表id
     * @return boolean
     * @author yunjin
     * @date 2023-7-7 14:19:55
     */
    boolean delete(Serializable id);

    /**
     * 修改角色菜单操作表信息
     *
     * @param dto 对象信息
     * @return boolean
     * @author yunjin
     * @date 2023-7-7 14:19:55
     */
    boolean modify(RoleOperateDTO dto);

    /**
     * 获取数据详情
     *
     * @param id 主键id
     * @return com.carlos.system.menu.pojo.dto.RoleOperateDTO
     * @author yunjin
     * @date 2023-7-7 14:19:55
     */
    RoleOperateDTO getDtoById(Serializable id);

    /**
     * 分页列表
     *
     * @param param 分页参数
     * @author yunjin
     * @date 2023-7-7 14:19:55
     */
    Paging<RoleOperateVO> getPage(RoleOperatePageParam param);
}
